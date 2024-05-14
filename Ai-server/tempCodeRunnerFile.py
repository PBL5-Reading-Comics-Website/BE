import mysql.connector
import numpy as np
import pandas as pd
from flask import Flask, jsonify, request
from flask_cors import CORS
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.metrics.pairwise import cosine_similarity

app = Flask(__name__)
CORS(app)

db_config = {
    "host": "localhost",
    "user": "root",
    "password": "",
    "database": "comic_reading",
}


def get_manga_data():
    """Fetches manga data (excluding cover_image) for recommendations."""
    try:
        connection = mysql.connector.connect(**db_config)
        cursor = connection.cursor()
        query = """
            SELECT m.id, m.name, t.name AS tag, 
                   GREATEST(m.view_number, 1) AS views 
            FROM manga m
            LEFT JOIN tag t ON m.id = t.manga_id;
            """
        cursor.execute(query)
        data = cursor.fetchall()
        df = pd.DataFrame(data, columns=["id", "Name", "tag", "Views"])

        # Apply TF-IDF vectorization
        vectorizer = TfidfVectorizer()
        tag_matrix = vectorizer.fit_transform(df["tag"])
        tag_df = pd.DataFrame(
            tag_matrix.toarray(), columns=vectorizer.get_feature_names_out()
        )
        df = pd.concat([df, tag_df], axis=1)
        details_df = df.pivot_table(
            index=["id", "Name", "Views"],
            columns="tag",
            aggfunc=lambda x: 1,
            fill_value=0,
        ).reset_index()
        return details_df
    except mysql.connector.Error as err:
        print(f"Error: {err}")
        return None
    finally:
        if connection.is_connected():
            cursor.close()
            connection.close()


def get_manga_cover_images(manga_ids):
    """Fetches cover image URLs for a list of manga IDs."""
    try:
        connection = mysql.connector.connect(**db_config)
        cursor = connection.cursor()
        query = """
            SELECT id, cover_image
            FROM manga
            WHERE id IN ({});
            """.format(
            ",".join(["%s"] * len(manga_ids))
        )
        cursor.execute(query, manga_ids)
        image_data = cursor.fetchall()
        # Decode the URL from bytes to string
        return {row[0]: row[1].decode("utf-8") for row in image_data}
    except mysql.connector.Error as err:
        print(f"Error: {err}")
        return {}
    finally:
        if connection.is_connected():
            cursor.close()
            connection.close()


def get_recommendations(user_id, top_n=10):
    """Recommends mangas based on user's reading history and tag similarity."""
    details_df = get_manga_data()
    if details_df is None:
        print("Error fetching data from the database.")
        return []
    try:
        connection = mysql.connector.connect(**db_config)
        cursor = connection.cursor()
        query = """
            SELECT DISTINCT manga_id 
            FROM reading_history
            WHERE user_id = %s;
            """
        cursor.execute(query, (user_id,))
        reading_history_ids = [row[0] for row in cursor.fetchall()]
    except mysql.connector.Error as err:
        print(f"Error: {err}")
        return []
    finally:
        if connection.is_connected():
            cursor.close()
            connection.close()

    tag_columns = details_df.columns[3:]
    tag_df = details_df[tag_columns]

    if not reading_history_ids:
        # Recommend based on views if no reading history
        recommended_manga_indices = details_df["Views"].argsort()[::-1]
        recommended_mangas = details_df.iloc[recommended_manga_indices]
        return recommended_mangas[["id", "Name", "Views"]].head(top_n).values.tolist()
    else:
        # Create user profile
        user_profile = tag_df.loc[
            details_df[details_df["id"].isin(reading_history_ids)].index
        ].mean()
        user_profile_df = pd.DataFrame(
            [user_profile.tolist()], columns=user_profile.index
        )

        # Calculate cosine similarity
        similarity_scores = cosine_similarity(user_profile_df, tag_df)

        # Weight similarity scores by log-scaled views
        for i in range(len(details_df)):
            similarity_scores[0][i] *= np.log1p(details_df["Views"].iloc[i])

        # Get indices of similar mangas (excluding already read)
        similar_manga_indices = similarity_scores[0].argsort()[::-1]
        recommended_manga_ids = details_df["id"].iloc[similar_manga_indices].tolist()
        recommended_manga_ids = [
            manga_id
            for manga_id in recommended_manga_ids
            if manga_id not in reading_history_ids
        ]

        # Get recommended mangas with scores, names, and image URLs
        recommended_mangas = []
        image_urls = get_manga_cover_images(recommended_manga_ids)  # Fetch image URLs
        for manga_id in recommended_manga_ids[:top_n]:
            score = similarity_scores[0][
                details_df[details_df["id"] == manga_id].index[0]
            ]
            name = details_df[details_df["id"] == manga_id]["Name"].values[0]
            image_url = image_urls.get(manga_id, "")  # Get image URL from dictionary
            recommended_mangas.append([manga_id, name, score, image_url])
        return recommended_mangas


@app.route("/recommendations", methods=["GET"])
def get_user_recommendations():
    """Endpoint to get manga recommendations for a user."""
    user_id = request.args.get("userId")
    if not user_id:
        return jsonify({"error": "Missing userId parameter"}), 400
    try:
        user_id = int(user_id)
    except ValueError:
        return jsonify({"error": "Invalid userId parameter"}), 400
    recommendations = get_recommendations(user_id, top_n=10)

    # Format recommendations for JSON response
    formatted_recommendations = [
        {"id": manga_id, "name": name, "score": f"{score:.4f}", "imageUrl": image_url}
        for manga_id, name, score, image_url in recommendations
    ]
    print(formatted_recommendations)
    return jsonify({"recommendations": formatted_recommendations})


if __name__ == "__main__":
    app.run(debug=True)
