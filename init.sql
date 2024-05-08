create database comic_reading;
use comic_reading;

CREATE TABLE role (
    id INT AUTO_INCREMENT NOT NULL,
    name VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE manga (
    id INT AUTO_INCREMENT NOT NULL,
    name VARCHAR(255) NOT NULL,
    cover_image LONGBLOB NOT NULL,
    publishing_company VARCHAR(255) NOT NULL DEFAULT 'Unknown',
    author VARCHAR(255) NOT NULL DEFAULT '',
    artist VARCHAR(255) NOT NULL DEFAULT '',
    description LONGTEXT NOT NULL,
    status VARCHAR(255) NOT NULL DEFAULT 'ONGOING',
    reading_status VARCHAR(255) NOT NULL DEFAULT '',
    view_number INT NOT NULL DEFAULT 0,
    favourite_number INT NOT NULL DEFAULT 0,
    comment_number INT NOT NULL DEFAULT 0,
    publish_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    update_user_id INT,
    PRIMARY KEY (id)
);

CREATE TABLE chapter (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name TEXT NOT NULL,
    number INT NOT NULL,
    comment_number INT NOT NULL DEFAULT 0,
    publish_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    manga_id INT NOT NULL,
    FOREIGN KEY (manga_id)
        REFERENCES manga (id)
);

CREATE TABLE image (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name varchar(255),
    image_url LONGTEXT,
    image_id varchar(255),
    chapter_id INT NOT NULL,
    FOREIGN KEY (chapter_id)
        REFERENCES chapter (id)
);

CREATE TABLE tag (
    id INT AUTO_INCREMENT NOT NULL,
    name VARCHAR(255) NOT NULL,
    manga_id INT NOT NULL,
    FOREIGN KEY (manga_id)
        REFERENCES manga (id),
    PRIMARY KEY (id)
);

-- create table user (
-- 	id int auto_increment not null,
--     name varchar(255) not null,
--     nickname varchar(255) not null,
--     avatar longblob,
--     date_of_birth date not null,
--     gender boolean not null,
--     email varchar(255) not null,
--     registration_date date not null,
--     primary key(id)
-- );

CREATE TABLE user (
    id INT AUTO_INCREMENT NOT NULL,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(255),
    avatar LONGBLOB,
    date_of_birth DATE,
    gender BOOLEAN,
    email VARCHAR(255) UNIQUE,
    registration_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    role_id INT NOT NULL DEFAULT 2,
    FOREIGN KEY (role_id)
        REFERENCES role (id),
    PRIMARY KEY (id)
);

CREATE TABLE reading_history (
    id INT AUTO_INCREMENT NOT NULL,
    end_at DATETIME NOT NULL,
    user_id INT NOT NULL,
    manga_id INT NOT NULL,
    chapter_id INT NOT NULL,
    FOREIGN KEY (user_id)
        REFERENCES user (id),
    FOREIGN KEY (manga_id)
        REFERENCES manga (id),
    FOREIGN KEY (chapter_id)
        REFERENCES chapter (id),
    PRIMARY KEY (id)
);

CREATE TABLE comment (
    id INT AUTO_INCREMENT NOT NULL,
    content LONGTEXT NOT NULL,
    create_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    reply_id INT NULL,
    user_id INT NOT NULL,
    manga_id INT NOT NULL,
    FOREIGN KEY (user_id)
        REFERENCES user (id),
    FOREIGN KEY (manga_id)
        REFERENCES manga (id),
    PRIMARY KEY (id)
);

CREATE TABLE report (
    id INT AUTO_INCREMENT NOT NULL,
    content LONGTEXT,
    status VARCHAR(255) NOT NULL DEFAULT 'PENDING',
    manga_id INT NOT NULL,
    comment_id INT NOT NULL,
    FOREIGN KEY (manga_id)
        REFERENCES manga (id),
    FOREIGN KEY (comment_id)
        REFERENCES comment (id),
    PRIMARY KEY (id)
);

CREATE TABLE following (
    id INT AUTO_INCREMENT NOT NULL,
    user_id INT NOT NULL,
    manga_id INT NOT NULL,
    FOREIGN KEY (user_id)
        REFERENCES user (id),
    FOREIGN KEY (manga_id)
        REFERENCES manga (id),
    PRIMARY KEY (id)
);
  
INSERT INTO comic_reading.`role` (name) VALUES ('ROLE_ADMIN');
INSERT INTO comic_reading.`role` (name) VALUES ('ROLE_USER');
INSERT INTO comic_reading.`role` (name) VALUES ('ROLE_POSTER');

DELIMITER //
CREATE PROCEDURE InsertNewManga(IN mangaName VARCHAR(255), IN author VARCHAR(255), IN artist VARCHAR(255), IN publishing_company VARCHAR(255), IN description TEXT, IN cover_image VARCHAR(255), IN user_id int)
BEGIN
    -- Check if the manga already exists
    DECLARE existingMangaId INT;
SELECT 
    id
INTO existingMangaId FROM
    manga
WHERE
    name = mangaName;

    -- Set default value for user_id if it is NULL
    IF user_id IS NULL THEN
        SET user_id = 1;
    END IF;

    IF existingMangaId IS NULL THEN
        -- Insert the new manga
        INSERT INTO manga (name, author, artist, publishing_company, description, cover_image, update_user_id)
        VALUES (mangaName, author, artist, publishing_company, description, cover_image, user_id);
    ELSE
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'A manga with this name already exists.';
    END IF;
END //
DELIMITER;

DELIMITER //
CREATE PROCEDURE InsertNewChapter(IN mangaName VARCHAR(255), IN chapterName VARCHAR(255))
BEGIN
    DECLARE mangaId INT;
    DECLARE maxNumber INT;

    -- Get the manga_id
    SELECT id INTO mangaId FROM manga WHERE name = mangaName;

    -- Get the max chapter number
    SELECT COALESCE(MAX(number), 0) INTO maxNumber FROM chapter WHERE manga_id = mangaId;

    -- Insert the new chapter
    INSERT INTO chapter (manga_id, name, number, update_at) 
    VALUES (mangaId, chapterName, maxNumber + 1, NOW());
END //
DELIMITER ;

DELIMITER //
CREATE PROCEDURE InsertChapterImage(IN chapterId INT, IN imageName VARCHAR(255), IN imageId VARCHAR(255), IN chapterName VARCHAR(255), IN mangaName VARCHAR(255))
BEGIN
    DECLARE imageExists TINYINT;

    -- Check if the image name already exists
    SELECT CASE WHEN COUNT(*) > 0 THEN 1 ELSE 0 END INTO imageExists
    FROM image
    WHERE name = imageName;

    IF NOT imageExists THEN
        -- Generate the image URL based on the image name, chapter name, and manga name
        SET @imageUrl = CONCAT('https://res.cloudinary.com/dpkxkkrnl/image/upload/v1714697856/van_chuong_viet/', REPLACE(mangaName, ' ', '%20'), '/', REPLACE(chapterName, ' ', '%20'), '/', REPLACE(imageName, ' ', '%20'), '.jpg');

        -- Insert the new image for the chapter
        INSERT INTO image (name, image_url, image_id, chapter_id)
        VALUES (imageName, @imageUrl, imageId, chapterId);
    END IF;
END //
DELIMITER ;

DELIMITER //
CREATE PROCEDURE AddMultipleTags(IN tagNames VARCHAR(255), IN mangaId INT)
BEGIN
    DECLARE idx INT DEFAULT 0;
    DECLARE tagName VARCHAR(255);
    WHILE idx < JSON_LENGTH(tagNames) DO
        SET tagName = JSON_UNQUOTE(JSON_EXTRACT(tagNames, CONCAT('$[', idx, ']')));
        INSERT INTO tag (name, manga_id) VALUES (tagName, mangaId);
        SET idx = idx + 1;
    END WHILE;
END //
DELIMITER ;

CALL InsertNewManga('BE BLUES Ao ni Nare', 'Tanaka Yasuki', 'Tanaka Yasuki', 'A Company', 'A story about a boy who dreams to become a professional football player.', 'https://res.cloudinary.com/dpkxkkrnl/image/upload/v1714683288/van_chuong_viet/BE%20BLUES%20%7EAo%20ni%20Nare%7E/cover_a6dejj.png', 1);
CALL InsertNewManga('Arakure Ojousama wa Monmon Shiteiru', 'Tanaka Yasuki', 'Artist Name', 'A Company', 'A story about a boy who dreams to become a professional football player.', 'https://res.cloudinary.com/dpkxkkrnl/image/upload/v1715082710/van_chuong_viet/Arakure%20Ojousama%20wa%20Monmon%20Shiteiru/cover_vi3srv.jpg', 1);

CALL AddMultipleTags('["Sports", "Drama", "Slice of Life"]', 1);

CALL InsertNewChapter('BE BLUES Ao ni Nare', 'Vol. 1 Ch. 1');
CALL InsertNewChapter('BE BLUES Ao ni Nare', 'Vol. 1 Ch. 2');

CALL InsertChapterImage(1, '00_m0kuuz', 'Image ID', 'Vol. 1 Ch. 1', 'BE BLUES Ao ni Nare');
CALL InsertChapterImage(1, '01_lt8uan', 'Image ID', 'Vol. 1 Ch. 1', 'BE BLUES Ao ni Nare');
CALL InsertChapterImage(1, '02_fppavu', 'Image ID', 'Vol. 1 Ch. 1', 'BE BLUES Ao ni Nare');
CALL InsertChapterImage(1, '03_ytwdzu', 'Image ID', 'Vol. 1 Ch. 1', 'BE BLUES Ao ni Nare');
CALL InsertChapterImage(1, '04_an4abq', 'Image ID', 'Vol. 1 Ch. 1', 'BE BLUES Ao ni Nare');
CALL InsertChapterImage(1, '05_p2yeiv', 'Image ID', 'Vol. 1 Ch. 1', 'BE BLUES Ao ni Nare');
CALL InsertChapterImage(1, '06_nuslh8', 'Image ID', 'Vol. 1 Ch. 1', 'BE BLUES Ao ni Nare');
CALL InsertChapterImage(1, '07_xzt39d', 'Image ID', 'Vol. 1 Ch. 1', 'BE BLUES Ao ni Nare');
CALL InsertChapterImage(1, '08_xwpop3', 'Image ID', 'Vol. 1 Ch. 1', 'BE BLUES Ao ni Nare');
CALL InsertChapterImage(1, '09_cckzwo', 'Image ID', 'Vol. 1 Ch. 1', 'BE BLUES Ao ni Nare');
CALL InsertChapterImage(1, '10_jzn0mp', 'Image ID', 'Vol. 1 Ch. 1', 'BE BLUES Ao ni Nare');
CALL InsertChapterImage(1, '11_trsw8y', 'Image ID', 'Vol. 1 Ch. 1', 'BE BLUES Ao ni Nare');
CALL InsertChapterImage(1, '12_g3vgy7', 'Image ID', 'Vol. 1 Ch. 1', 'BE BLUES Ao ni Nare');

CALL InsertChapterImage(2, '01_h9afk4', 'Image ID', 'Vol. 1 Ch. 2', 'BE BLUES Ao ni Nare');
CALL InsertChapterImage(2, '02_movz4f', 'Image ID', 'Vol. 1 Ch. 2', 'BE BLUES Ao ni Nare');
CALL InsertChapterImage(2, '05_ozq4d3', 'Image ID', 'Vol. 1 Ch. 2', 'BE BLUES Ao ni Nare');
CALL InsertChapterImage(2, '06_aqdcbk', 'Image ID', 'Vol. 1 Ch. 2', 'BE BLUES Ao ni Nare');
CALL InsertChapterImage(2, '07_mjbgtp', 'Image ID', 'Vol. 1 Ch. 2', 'BE BLUES Ao ni Nare');
CALL InsertChapterImage(2, '08_m2zpn1', 'Image ID', 'Vol. 1 Ch. 2', 'BE BLUES Ao ni Nare');
CALL InsertChapterImage(2, '10_oedkfj', 'Image ID', 'Vol. 1 Ch. 2', 'BE BLUES Ao ni Nare');
CALL InsertChapterImage(2, '11_mb5nkh', 'Image ID', 'Vol. 1 Ch. 2', 'BE BLUES Ao ni Nare');