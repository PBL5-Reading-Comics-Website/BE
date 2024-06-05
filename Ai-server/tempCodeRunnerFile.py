if not reading_history_ids:
        # Recommend based on views if no reading history
        recommended_manga_indices = details_df["Views"].argsort()[::-1]
        recommended_mangas = details_df.iloc[recommended_manga_indices]

        # Get cover image URLs for recommended mangas
        image_urls = get_manga_cover_images(recommended_mangas["id"].tolist())

        # Ensure at least 10 recommendations
        num_mangas = len(recommended_mangas)
        if num_mangas < top_n:
            # Repeat the top mangas if there are fewer than 10
            top_mangas = recommended_mangas[["id", "Name", "Views"]].head(top_n - num_mangas)
            recommended_mangas = pd.concat([recommended_mangas, top_mangas]).reset_index(drop=True)

        # Return recommendations with image URLs
        return [
            [manga_id, name, views, image_urls.get(manga_id, "")]
            for manga_id, name, views in recommended_mangas[["id", "Name", "Views"]].head(top_n).values.tolist()
        ]