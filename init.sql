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
    description LONGTEXT NULL,
    status VARCHAR(255) NOT NULL DEFAULT 'ONGOING',
    reading_status VARCHAR(255) DEFAULT 'READING' NOT NULL,
    view_number INT DEFAULT 0,
    favourite_number INT DEFAULT 0,
    comment_number INT DEFAULT 0,
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
  
INSERT INTO comic_reading.`role` (name) VALUES ('ADMIN');
INSERT INTO comic_reading.`role` (name) VALUES ('USER');
INSERT INTO comic_reading.`role` (name) VALUES ('POSTER');

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
        SET @imageUrl = CONCAT('https://res.cloudinary.com/dpkxkkrnl/image/upload/v1715082722/van_chuong_viet/', REPLACE(mangaName, ' ', '%20'), '/', REPLACE(chapterName, ' ', '%20'), '/', REPLACE(imageName, ' ', '%20'), '.jpg');

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

CALL InsertNewManga('BE BLUES ~Ao ni Nare~', 'Tanaka Yasuki', 'Tanaka Yasuki', 'A Company', 'A story about a boy who dreams to become a professional football player.', 'https://res.cloudinary.com/dpkxkkrnl/image/upload/v1714683288/van_chuong_viet/BE%20BLUES%20%7EAo%20ni%20Nare%7E/cover_a6dejj.png', 1);
CALL InsertNewManga('Arakure Ojousama wa Monmon Shiteiru', 'Kinoshita Yuichi', 'Kinoshita Yuichi', 'A Company', 'Meet Tsubaki Kuchinashi, the Manhater Prince. Beautiful and popular, Tsubaks granddaughter - suffice it to say, but any student or teacher who even looks at her the wrong way could be expelled at a moment iron grip on the school. Naturally, a challenge to her rule bothers Kuchinashi, so she decides to get rid of him as soon as possible. Her plan? Get him in a honey trap. However, that easier said than done, as she starts finding hidden aspects of herself that she didn want to acknowledge, either to herself or to Tadashi.  \n  \nWill she prevail before her conscience eats her up from her own salacious taunts? Will Tadashi play into her hands? What manner of a person will he prove himself to be when facing her taunts?', 'https://res.cloudinary.com/dpkxkkrnl/image/upload/v1715082710/van_chuong_viet/Arakure%20Ojousama%20wa%20Monmon%20Shiteiru/cover_vi3srv.jpg', 1);
CALL InsertNewManga('Bishu no Daichi Fukushu ~Fukushuu no tame ni Kao wo Suteta Onna~', 'Fujimori Harumi', 'Fujimori Harumi', 'B Company', 'A woman who was robbed of her family by bullying is reborn as another person, and she devotes herself to revenge from hell! , Because of her ugly appearance, she was badly bullied at a girls school. The leader of bullying is Ayako Takashimazu, the daughter of a large company. Hana, who was accused of theft by her, was forced to leave school...!?', 'https://res.cloudinary.com/dpkxkkrnl/image/upload/v1715082932/van_chuong_viet/Bishu%20no%20Daichi%20Fukushu%20%7EFukushuu%20no%20tame%20ni%20Kao%20wo%20Suteta%20Onna%7E/cover_mvtwiw.jpg', 1);
CALL InsertNewManga('Boushoku no Berserk Ore Dake Level to Iu Gainen o Toppa Suru', 'Isshiki Ichika', 'Isshiki Ichika', 'B Company', 'In this world, there are two types of people. Those Who have powerful', 'https://res.cloudinary.com/dpkxkkrnl/image/upload/v1715093132/van_chuong_viet/Boushoku%20no%20Berserk%20Ore%20Dake%20Level%20to%20Iu%20Gainen%20o%20Toppa%20Suru/cover_ufyaya.jpg', 1);
CALL InsertNewManga('Choujin X', 'Ishida Sui', 'Ishida Sui', 'A Company', 'When an airplane goes down in a plume of black smoke near their city, best friends Tokio and Azuma join the volunteer search and rescue team.', 'https://res.cloudinary.com/dpkxkkrnl/image/upload/v1714683297/van_chuong_viet/Choujin%20X/cover_dan9nn.jpg', 1);
CALL InsertNewManga('Classroom of the Elite - 2nd Year Arc', 'Kinugasa Shougo', 'Shia Sasane', 'A Company', 'The second spring at the Advanced Nurturing High School is welcoming Ayanokoji and Class D. The special exams arent only what awaits them, but the unique freshmen as well.', 'https://res.cloudinary.com/dpkxkkrnl/image/upload/v1715094064/van_chuong_viet/Classroom%20of%20the%20Elite%20-%202nd%20Year%20Arc/cover_ft01yo.jpg', 1);
CALL InsertNewManga('Dame Ningen no Aishikata', 'Iwaba Junki', 'Iwaba Junki', 'A Company', 'No talent, no money, no education. A worthless person, bottom of the barrel.\"\n\"But... just seeing you like that makes me feel so happy.\"\nShinba, an unemployed young man, is what someone could call a useless person.\nFor some reason, such a man is dating a cool and beautiful woman named Hizumi.', 'https://res.cloudinary.com/dpkxkkrnl/image/upload/v1715094941/van_chuong_viet/Dame%20Ningen%20no%20Aishikata/cover_jcpwp2.jpg', 1);
CALL InsertNewManga('Dragon''s Son Emerged', 'Dii', 'Dii', 'C Company', 'No talent, no money, no education. A worthless person, bottom of the barrel.\"\n\"But... just seeing you like that makes me feel so happy.\"\nShinba, an unemployed young man, is what someone could call a useless person.\nFor some reason, such a man is dating a cool and beautiful woman named Hizumi.', 'https://res.cloudinary.com/dpkxkkrnl/image/upload/v1715095120/van_chuong_viet/Dragon%27s%20Son%20Emerged/cover_cwpxzc.jpg', 1);
CALL InsertNewManga('Fate/Grand Order', 'TYPE-MOON', 'Kawaguchi Takeshi', 'C Company', 'No talent, no money, no education. A worthless person, bottom of the barrel.\"\n\"But... just seeing you like that makes me feel so happy.\"\nShinba, an unemployed young man, is what someone could call a useless person.\nFor some reason, such a man is dating a cool and beautiful woman named Hizumi.', 'https://res.cloudinary.com/dpkxkkrnl/image/upload/v1715098242/van_chuong_viet/FateGrand%20Order/cover_avldjr.jpg', 1);
CALL InsertNewManga('FPS de Shoshinsha Bokotte Real Fight ni Hattenshita Kekka w', 'Medatarou', 'Medatarou', 'B Company', 'No talent, no money, no education. A worthless person, bottom of the barrel.\"\n\"But... just seeing you like that makes me feel so happy.\"\nShinba, an unemployed young man, is what someone could call a useless person.\nFor some reason, such a man is dating a cool and beautiful woman named Hizumi.', 'https://res.cloudinary.com/dpkxkkrnl/image/upload/v1715098426/van_chuong_viet/FPS%20de%20Shoshinsha%20Bokotte%20Real%20Fight%20ni%20Hattenshita%20Kekka%20w/cover_ss0hjl.jpg', 1);
CALL InsertNewManga('Half Succubus wo Hirotta', 'Zurikishi', 'Zurikishi', 'B Company', 'No talent, no money, no education. A worthless person, bottom of the barrel.\"\n\"But... just seeing you like that makes me feel so happy.\"\nShinba, an unemployed young man, is what someone could call a useless person.\nFor some reason, such a man is dating a cool and beautiful woman named Hizumi.', 'https://res.cloudinary.com/dpkxkkrnl/image/upload/v1714683308/van_chuong_viet/Half%20Succubus%20wo%20Hirotta/cover_b8ygur.jpg', 1);
CALL InsertNewManga('Hazure Skill Soine ga Kakuseishi, Boku ga Saikyou Harem wo Kizuku Made', 'Marusei', 'Denji Houniki', 'B Company', 'No talent, no money, no education. A worthless person, bottom of the barrel.\"\n\"But... just seeing you like that makes me feel so happy.\"\nShinba, an unemployed young man, is what someone could call a useless person.\nFor some reason, such a man is dating a cool and beautiful woman named Hizumi.', 'https://res.cloudinary.com/dpkxkkrnl/image/upload/v1715098537/van_chuong_viet/Hazure%20Skill%20Soine%20ga%20Kakuseishi%2C%20Boku%20ga%20Saikyou%20Harem%20wo%20Kizuku%20Made/cover_igqckw.jpg', 1);

CALL AddMultipleTags('["Sports", "Drama", "Slice of Life"]', 1);
CALL AddMultipleTags('["Romance", "Comedy", "Slice of Life"]', 2);
CALL AddMultipleTags('["Drama", "Horror", "Mystery"]', 3);
CALL AddMultipleTags('["Action", "Romance", "Adventure", "Drama", "Fantasy"]', 4);
CALL AddMultipleTags('["Thriller", "Sci-Fi", "Action", "Psychological", "Comedy", "Drama", "Slice of Life", "Mystery"]', 5);
CALL AddMultipleTags('["Psychological", "Romance", "Comedy", "Drama"]', 6);
CALL AddMultipleTags('["Psychological", "Romance"]', 7);
CALL AddMultipleTags('["Action", "Drama", "Mystery"]', 8);
CALL AddMultipleTags('["Sci-Fi", "Historical", "Action", "Comedy", "Crime", "Magical Girls", "Adventure", "Drama", "Fantasy", "Tragedy"]', 9);
CALL AddMultipleTags('["Comedy"]', 10);
CALL AddMultipleTags('["Comedy", "Slice of Life"]', 11);
CALL AddMultipleTags('["Romance", "Comedy", "Fantasy"]', 12);

CALL InsertNewChapter('BE BLUES ~Ao ni Nare~', 'Vol. 1 Ch. 1');
CALL InsertNewChapter('BE BLUES ~Ao ni Nare~', 'Vol. 1 Ch. 2');
CALL InsertNewChapter('Arakure Ojousama wa Monmon Shiteiru', 'Vol. 1 Ch. 1');
CALL InsertNewChapter('Arakure Ojousama wa Monmon Shiteiru', 'Vol. 1 Ch. 2');
CALL InsertNewChapter('Arakure Ojousama wa Monmon Shiteiru', 'Vol. 1 Ch. 3');
CALL InsertNewChapter('Bishu no Daichi Fukushu ~Fukushuu no tame ni Kao wo Suteta Onna~', 'Vol. 1 Ch. 1');

CALL InsertChapterImage(1, '00_m0kuuz', '00_m0kuuz', 'Vol. 1 Ch. 1', 'BE BLUES ~Ao ni Nare~');
CALL InsertChapterImage(1, '01_lt8uan', '01_lt8uan', 'Vol. 1 Ch. 1', 'BE BLUES ~Ao ni Nare~');
CALL InsertChapterImage(1, '02_fppavu', 'Image ID', 'Vol. 1 Ch. 1', 'BE BLUES ~Ao ni Nare~');
CALL InsertChapterImage(1, '03_ytwdzu', 'Image ID', 'Vol. 1 Ch. 1', 'BE BLUES ~Ao ni Nare~');
CALL InsertChapterImage(1, '04_an4abq', 'Image ID', 'Vol. 1 Ch. 1', 'BE BLUES ~Ao ni Nare~');
CALL InsertChapterImage(1, '05_p2yeiv', 'Image ID', 'Vol. 1 Ch. 1', 'BE BLUES ~Ao ni Nare~');
CALL InsertChapterImage(1, '06_nuslh8', 'Image ID', 'Vol. 1 Ch. 1', 'BE BLUES ~Ao ni Nare~');
CALL InsertChapterImage(1, '07_xzt39d', 'Image ID', 'Vol. 1 Ch. 1', 'BE BLUES ~Ao ni Nare~');
CALL InsertChapterImage(1, '08_xwpop3', 'Image ID', 'Vol. 1 Ch. 1', 'BE BLUES ~Ao ni Nare~');
CALL InsertChapterImage(1, '09_cckzwo', 'Image ID', 'Vol. 1 Ch. 1', 'BE BLUES ~Ao ni Nare~');
CALL InsertChapterImage(1, '10_jzn0mp', 'Image ID', 'Vol. 1 Ch. 1', 'BE BLUES ~Ao ni Nare~');
CALL InsertChapterImage(1, '11_trsw8y', 'Image ID', 'Vol. 1 Ch. 1', 'BE BLUES ~Ao ni Nare~');
CALL InsertChapterImage(1, '12_g3vgy7', 'Image ID', 'Vol. 1 Ch. 1', 'BE BLUES ~Ao ni Nare~');

CALL InsertChapterImage(2, '01_h9afk4', 'Image ID', 'Vol. 1 Ch. 2', 'BE BLUES ~Ao ni Nare~');
CALL InsertChapterImage(2, '02_movz4f', 'Image ID', 'Vol. 1 Ch. 2', 'BE BLUES ~Ao ni Nare~');
CALL InsertChapterImage(2, '05_ozq4d3', 'Image ID', 'Vol. 1 Ch. 2', 'BE BLUES ~Ao ni Nare~');
CALL InsertChapterImage(2, '06_aqdcbk', 'Image ID', 'Vol. 1 Ch. 2', 'BE BLUES ~Ao ni Nare~');
CALL InsertChapterImage(2, '07_mjbgtp', 'Image ID', 'Vol. 1 Ch. 2', 'BE BLUES ~Ao ni Nare~');
CALL InsertChapterImage(2, '08_m2zpn1', 'Image ID', 'Vol. 1 Ch. 2', 'BE BLUES ~Ao ni Nare~');
CALL InsertChapterImage(2, '10_oedkfj', 'Image ID', 'Vol. 1 Ch. 2', 'BE BLUES ~Ao ni Nare~');
CALL InsertChapterImage(2, '11_mb5nkh', 'Image ID', 'Vol. 1 Ch. 2', 'BE BLUES ~Ao ni Nare~');

CALL InsertChapterImage(3, '00_l0dnc7', 'Image ID', 'Vol. 1 Ch. 1', 'Arakure Ojousama wa Monmon Shiteiru');
CALL InsertChapterImage(3, '01_ib7jau', 'Image ID', 'Vol. 1 Ch. 1', 'Arakure Ojousama wa Monmon Shiteiru');
CALL InsertChapterImage(3, '02_a2lhze', 'Image ID', 'Vol. 1 Ch. 1', 'Arakure Ojousama wa Monmon Shiteiru');
CALL InsertChapterImage(3, '03_s8n8ia', 'Image ID', 'Vol. 1 Ch. 1', 'Arakure Ojousama wa Monmon Shiteiru');
CALL InsertChapterImage(3, '04_anhedq', 'Image ID', 'Vol. 1 Ch. 1', 'Arakure Ojousama wa Monmon Shiteiru');
CALL InsertChapterImage(3, '05_dc9ptp', 'Image ID', 'Vol. 1 Ch. 1', 'Arakure Ojousama wa Monmon Shiteiru');
CALL InsertChapterImage(3, '06_p1waxz', 'Image ID', 'Vol. 1 Ch. 1', 'Arakure Ojousama wa Monmon Shiteiru');
CALL InsertChapterImage(3, '07_ruiknu', 'Image ID', 'Vol. 1 Ch. 1', 'Arakure Ojousama wa Monmon Shiteiru');
CALL InsertChapterImage(3, '08_lygoxk', 'Image ID', 'Vol. 1 Ch. 1', 'Arakure Ojousama wa Monmon Shiteiru');
CALL InsertChapterImage(3, '09_ofrca8', 'Image ID', 'Vol. 1 Ch. 1', 'Arakure Ojousama wa Monmon Shiteiru');
CALL InsertChapterImage(3, '10_dnjq1n', 'Image ID', 'Vol. 1 Ch. 1', 'Arakure Ojousama wa Monmon Shiteiru');
CALL InsertChapterImage(3, '11_jf77cl', 'Image ID', 'Vol. 1 Ch. 1', 'Arakure Ojousama wa Monmon Shiteiru');
CALL InsertChapterImage(3, '12_zfw2fc', 'Image ID', 'Vol. 1 Ch. 1', 'Arakure Ojousama wa Monmon Shiteiru');
CALL InsertChapterImage(3, '13_ote9nn', 'Image ID', 'Vol. 1 Ch. 1', 'Arakure Ojousama wa Monmon Shiteiru');
CALL InsertChapterImage(3, '14_qucwtt', 'Image ID', 'Vol. 1 Ch. 1', 'Arakure Ojousama wa Monmon Shiteiru');
CALL InsertChapterImage(3, '15_v7zvyp', 'Image ID', 'Vol. 1 Ch. 1', 'Arakure Ojousama wa Monmon Shiteiru');
CALL InsertChapterImage(3, '16_rl0quv', 'Image ID', 'Vol. 1 Ch. 1', 'Arakure Ojousama wa Monmon Shiteiru');
CALL InsertChapterImage(3, '17_p7mlgp', 'Image ID', 'Vol. 1 Ch. 1', 'Arakure Ojousama wa Monmon Shiteiru');
CALL InsertChapterImage(3, '18_zqvvlu', 'Image ID', 'Vol. 1 Ch. 1', 'Arakure Ojousama wa Monmon Shiteiru');
CALL InsertChapterImage(3, '19_zckrgi', 'Image ID', 'Vol. 1 Ch. 1', 'Arakure Ojousama wa Monmon Shiteiru');
CALL InsertChapterImage(3, '20_rdqecr', 'Image ID', 'Vol. 1 Ch. 1', 'Arakure Ojousama wa Monmon Shiteiru');
CALL InsertChapterImage(3, '21_awana2', 'Image ID', 'Vol. 1 Ch. 1', 'Arakure Ojousama wa Monmon Shiteiru');
CALL InsertChapterImage(3, '22_pyqgez', 'Image ID', 'Vol. 1 Ch. 1', 'Arakure Ojousama wa Monmon Shiteiru');
CALL InsertChapterImage(3, '23_lnekyv', 'Image ID', 'Vol. 1 Ch. 1', 'Arakure Ojousama wa Monmon Shiteiru');
CALL InsertChapterImage(3, '24_czmuv7', 'Image ID', 'Vol. 1 Ch. 1', 'Arakure Ojousama wa Monmon Shiteiru');
CALL InsertChapterImage(3, '25_bapkih', 'Image ID', 'Vol. 1 Ch. 1', 'Arakure Ojousama wa Monmon Shiteiru');

CALL InsertChapterImage(4, '00_ywocwz', 'Image ID', 'Vol. 1 Ch. 2', 'Arakure Ojousama wa Monmon Shiteiru');
CALL InsertChapterImage(4, '01_a1fncu', 'Image ID', 'Vol. 1 Ch. 2', 'Arakure Ojousama wa Monmon Shiteiru');
CALL InsertChapterImage(4, '02_ruqoaa', 'Image ID', 'Vol. 1 Ch. 2', 'Arakure Ojousama wa Monmon Shiteiru');
CALL InsertChapterImage(4, '03_sb5sb6', 'Image ID', 'Vol. 1 Ch. 2', 'Arakure Ojousama wa Monmon Shiteiru');
CALL InsertChapterImage(4, '04_xkvfmm', 'Image ID', 'Vol. 1 Ch. 2', 'Arakure Ojousama wa Monmon Shiteiru');
CALL InsertChapterImage(4, '05_zmwdah', 'Image ID', 'Vol. 1 Ch. 2', 'Arakure Ojousama wa Monmon Shiteiru');
CALL InsertChapterImage(4, '06_fqz2up', 'Image ID', 'Vol. 1 Ch. 2', 'Arakure Ojousama wa Monmon Shiteiru');
CALL InsertChapterImage(4, '07_wwz1wg', 'Image ID', 'Vol. 1 Ch. 2', 'Arakure Ojousama wa Monmon Shiteiru');
CALL InsertChapterImage(4, '08_mdi8cr', 'Image ID', 'Vol. 1 Ch. 2', 'Arakure Ojousama wa Monmon Shiteiru');
CALL InsertChapterImage(4, '09_hchbj5', 'Image ID', 'Vol. 1 Ch. 2', 'Arakure Ojousama wa Monmon Shiteiru');
CALL InsertChapterImage(4, '10_uohchj', 'Image ID', 'Vol. 1 Ch. 2', 'Arakure Ojousama wa Monmon Shiteiru');
CALL InsertChapterImage(4, '11_ufafke', 'Image ID', 'Vol. 1 Ch. 2', 'Arakure Ojousama wa Monmon Shiteiru');
CALL InsertChapterImage(4, '12_zvriyg', 'Image ID', 'Vol. 1 Ch. 2', 'Arakure Ojousama wa Monmon Shiteiru');
CALL InsertChapterImage(4, '13_ecqa5u', 'Image ID', 'Vol. 1 Ch. 2', 'Arakure Ojousama wa Monmon Shiteiru');
CALL InsertChapterImage(4, '14_qqm2me', 'Image ID', 'Vol. 1 Ch. 2', 'Arakure Ojousama wa Monmon Shiteiru');
CALL InsertChapterImage(4, '15_vn45vr', 'Image ID', 'Vol. 1 Ch. 2', 'Arakure Ojousama wa Monmon Shiteiru');
CALL InsertChapterImage(4, '16_bscbbe', 'Image ID', 'Vol. 1 Ch. 2', 'Arakure Ojousama wa Monmon Shiteiru');
CALL InsertChapterImage(4, '17_zgrp6y', 'Image ID', 'Vol. 1 Ch. 2', 'Arakure Ojousama wa Monmon Shiteiru');
CALL InsertChapterImage(4, '18_fqcmqg', 'Image ID', 'Vol. 1 Ch. 2', 'Arakure Ojousama wa Monmon Shiteiru');
CALL InsertChapterImage(4, '19_yrblkj', 'Image ID', 'Vol. 1 Ch. 2', 'Arakure Ojousama wa Monmon Shiteiru');
CALL InsertChapterImage(4, '20_wsddaf', 'Image ID', 'Vol. 1 Ch. 2', 'Arakure Ojousama wa Monmon Shiteiru');
CALL InsertChapterImage(4, '21_vyljxz', 'Image ID', 'Vol. 1 Ch. 2', 'Arakure Ojousama wa Monmon Shiteiru');
CALL InsertChapterImage(4, '22_bfwgff', 'Image ID', 'Vol. 1 Ch. 2', 'Arakure Ojousama wa Monmon Shiteiru');
CALL InsertChapterImage(4, '23_rqwrpc', 'Image ID', 'Vol. 1 Ch. 2', 'Arakure Ojousama wa Monmon Shiteiru');

CALL InsertChapterImage(5, '00_klpv0l', 'Image ID', 'Vol. 1 Ch. 3', 'Arakure Ojousama wa Monmon Shiteiru');
CALL InsertChapterImage(5, '01_jgb4jy', 'Image ID', 'Vol. 1 Ch. 3', 'Arakure Ojousama wa Monmon Shiteiru');
CALL InsertChapterImage(5, '02_qhhmzz', 'Image ID', 'Vol. 1 Ch. 3', 'Arakure Ojousama wa Monmon Shiteiru');
CALL InsertChapterImage(5, '03_vrvqjp', 'Image ID', 'Vol. 1 Ch. 3', 'Arakure Ojousama wa Monmon Shiteiru');
CALL InsertChapterImage(5, '04_fstg1i', 'Image ID', 'Vol. 1 Ch. 3', 'Arakure Ojousama wa Monmon Shiteiru');
CALL InsertChapterImage(5, '05_odcw6t', 'Image ID', 'Vol. 1 Ch. 3', 'Arakure Ojousama wa Monmon Shiteiru');
CALL InsertChapterImage(5, '06_ijbiwg', 'Image ID', 'Vol. 1 Ch. 3', 'Arakure Ojousama wa Monmon Shiteiru');
CALL InsertChapterImage(5, '07_hsmb8i', 'Image ID', 'Vol. 1 Ch. 3', 'Arakure Ojousama wa Monmon Shiteiru');
CALL InsertChapterImage(5, '08_hqnp9a', 'Image ID', 'Vol. 1 Ch. 3', 'Arakure Ojousama wa Monmon Shiteiru');
CALL InsertChapterImage(5, '09_dcwlvx', 'Image ID', 'Vol. 1 Ch. 3', 'Arakure Ojousama wa Monmon Shiteiru');
CALL InsertChapterImage(5, '10_j8ud39', 'Image ID', 'Vol. 1 Ch. 3', 'Arakure Ojousama wa Monmon Shiteiru');
CALL InsertChapterImage(5, '11_oqc0ep', 'Image ID', 'Vol. 1 Ch. 3', 'Arakure Ojousama wa Monmon Shiteiru');
CALL InsertChapterImage(5, '12_uhwlb7', 'Image ID', 'Vol. 1 Ch. 3', 'Arakure Ojousama wa Monmon Shiteiru');
CALL InsertChapterImage(5, '13_oynnl2', 'Image ID', 'Vol. 1 Ch. 3', 'Arakure Ojousama wa Monmon Shiteiru');
CALL InsertChapterImage(5, '14_eqys2i', 'Image ID', 'Vol. 1 Ch. 3', 'Arakure Ojousama wa Monmon Shiteiru');
CALL InsertChapterImage(5, '15_uvef1z', 'Image ID', 'Vol. 1 Ch. 3', 'Arakure Ojousama wa Monmon Shiteiru');
CALL InsertChapterImage(5, '16_kektsb', 'Image ID', 'Vol. 1 Ch. 3', 'Arakure Ojousama wa Monmon Shiteiru');
CALL InsertChapterImage(5, '17_ifhpmh', 'Image ID', 'Vol. 1 Ch. 3', 'Arakure Ojousama wa Monmon Shiteiru');
CALL InsertChapterImage(5, '18_e6jp3i', 'Image ID', 'Vol. 1 Ch. 3', 'Arakure Ojousama wa Monmon Shiteiru');
CALL InsertChapterImage(5, '19_uukyju', 'Image ID', 'Vol. 1 Ch. 3', 'Arakure Ojousama wa Monmon Shiteiru');
CALL InsertChapterImage(5, '20_rknpoh', 'Image ID', 'Vol. 1 Ch. 3', 'Arakure Ojousama wa Monmon Shiteiru');
CALL InsertChapterImage(5, '21_xc4lzs', 'Image ID', 'Vol. 1 Ch. 3', 'Arakure Ojousama wa Monmon Shiteiru');
CALL InsertChapterImage(5, '22_uucpzj', 'Image ID', 'Vol. 1 Ch. 3', 'Arakure Ojousama wa Monmon Shiteiru');

CALL InsertChapterImage(6, '00_yzu64j', 'Image ID', 'Vol. 1 Ch. 1', 'Bishu no Daichi Fukushu ~Fukushuu no tame ni Kao wo Suteta Onna~');
CALL InsertChapterImage(6, '01_hjbvke', 'Image ID', 'Vol. 1 Ch. 1', 'Bishu no Daichi Fukushu ~Fukushuu no tame ni Kao wo Suteta Onna~');
CALL InsertChapterImage(6, '02_l6julb', 'Image ID', 'Vol. 1 Ch. 1', 'Bishu no Daichi Fukushu ~Fukushuu no tame ni Kao wo Suteta Onna~');
CALL InsertChapterImage(6, '03_dlwtxw', 'Image ID', 'Vol. 1 Ch. 1', 'Bishu no Daichi Fukushu ~Fukushuu no tame ni Kao wo Suteta Onna~');
CALL InsertChapterImage(6, '04_jg3wfb', 'Image ID', 'Vol. 1 Ch. 1', 'Bishu no Daichi Fukushu ~Fukushuu no tame ni Kao wo Suteta Onna~');
CALL InsertChapterImage(6, '05_l9cude', 'Image ID', 'Vol. 1 Ch. 1', 'Bishu no Daichi Fukushu ~Fukushuu no tame ni Kao wo Suteta Onna~');
CALL InsertChapterImage(6, '06_vynemw', 'Image ID', 'Vol. 1 Ch. 1', 'Bishu no Daichi Fukushu ~Fukushuu no tame ni Kao wo Suteta Onna~');
CALL InsertChapterImage(6, '07_mua9e5', 'Image ID', 'Vol. 1 Ch. 1', 'Bishu no Daichi Fukushu ~Fukushuu no tame ni Kao wo Suteta Onna~');
CALL InsertChapterImage(6, '08_yqwdtd', 'Image ID', 'Vol. 1 Ch. 1', 'Bishu no Daichi Fukushu ~Fukushuu no tame ni Kao wo Suteta Onna~');
CALL InsertChapterImage(6, '09_w9su4m', 'Image ID', 'Vol. 1 Ch. 1', 'Bishu no Daichi Fukushu ~Fukushuu no tame ni Kao wo Suteta Onna~');
CALL InsertChapterImage(6, '10_cnokzr', 'Image ID', 'Vol. 1 Ch. 1', 'Bishu no Daichi Fukushu ~Fukushuu no tame ni Kao wo Suteta Onna~');
CALL InsertChapterImage(6, '11_wl5diy', 'Image ID', 'Vol. 1 Ch. 1', 'Bishu no Daichi Fukushu ~Fukushuu no tame ni Kao wo Suteta Onna~');
CALL InsertChapterImage(6, '12_ilw1xt', 'Image ID', 'Vol. 1 Ch. 1', 'Bishu no Daichi Fukushu ~Fukushuu no tame ni Kao wo Suteta Onna~');
CALL InsertChapterImage(6, '13_ant7yo', 'Image ID', 'Vol. 1 Ch. 1', 'Bishu no Daichi Fukushu ~Fukushuu no tame ni Kao wo Suteta Onna~');
CALL InsertChapterImage(6, '14_vo7jjw', 'Image ID', 'Vol. 1 Ch. 1', 'Bishu no Daichi Fukushu ~Fukushuu no tame ni Kao wo Suteta Onna~');
CALL InsertChapterImage(6, '15_gtm98k', 'Image ID', 'Vol. 1 Ch. 1', 'Bishu no Daichi Fukushu ~Fukushuu no tame ni Kao wo Suteta Onna~');
CALL InsertChapterImage(6, '16_flij5i', 'Image ID', 'Vol. 1 Ch. 1', 'Bishu no Daichi Fukushu ~Fukushuu no tame ni Kao wo Suteta Onna~');
CALL InsertChapterImage(6, '17_xtcyrz', 'Image ID', 'Vol. 1 Ch. 1', 'Bishu no Daichi Fukushu ~Fukushuu no tame ni Kao wo Suteta Onna~');
CALL InsertChapterImage(6, '18_uazock', 'Image ID', 'Vol. 1 Ch. 1', 'Bishu no Daichi Fukushu ~Fukushuu no tame ni Kao wo Suteta Onna~');
CALL InsertChapterImage(6, '19_xq9ldh', 'Image ID', 'Vol. 1 Ch. 1', 'Bishu no Daichi Fukushu ~Fukushuu no tame ni Kao wo Suteta Onna~');
CALL InsertChapterImage(6, '20_f1fffh', 'Image ID', 'Vol. 1 Ch. 1', 'Bishu no Daichi Fukushu ~Fukushuu no tame ni Kao wo Suteta Onna~');
CALL InsertChapterImage(6, '21_hr6ta3', 'Image ID', 'Vol. 1 Ch. 1', 'Bishu no Daichi Fukushu ~Fukushuu no tame ni Kao wo Suteta Onna~');
CALL InsertChapterImage(6, '22_fc6bi5', 'Image ID', 'Vol. 1 Ch. 1', 'Bishu no Daichi Fukushu ~Fukushuu no tame ni Kao wo Suteta Onna~');
CALL InsertChapterImage(6, '23_aancsx', 'Image ID', 'Vol. 1 Ch. 1', 'Bishu no Daichi Fukushu ~Fukushuu no tame ni Kao wo Suteta Onna~');
CALL InsertChapterImage(6, '24_svmknf', 'Image ID', 'Vol. 1 Ch. 1', 'Bishu no Daichi Fukushu ~Fukushuu no tame ni Kao wo Suteta Onna~');
CALL InsertChapterImage(6, '25_l5ojhf', 'Image ID', 'Vol. 1 Ch. 1', 'Bishu no Daichi Fukushu ~Fukushuu no tame ni Kao wo Suteta Onna~');
CALL InsertChapterImage(6, '26_fa7pka', 'Image ID', 'Vol. 1 Ch. 1', 'Bishu no Daichi Fukushu ~Fukushuu no tame ni Kao wo Suteta Onna~');
CALL InsertChapterImage(6, '27_ovpcai', 'Image ID', 'Vol. 1 Ch. 1', 'Bishu no Daichi Fukushu ~Fukushuu no tame ni Kao wo Suteta Onna~');
CALL InsertChapterImage(6, '28_dcpfyx', 'Image ID', 'Vol. 1 Ch. 1', 'Bishu no Daichi Fukushu ~Fukushuu no tame ni Kao wo Suteta Onna~');
CALL InsertChapterImage(6, '29_r1qfpm', 'Image ID', 'Vol. 1 Ch. 1', 'Bishu no Daichi Fukushu ~Fukushuu no tame ni Kao wo Suteta Onna~');
CALL InsertChapterImage(6, '30_yudzwk', 'Image ID', 'Vol. 1 Ch. 1', 'Bishu no Daichi Fukushu ~Fukushuu no tame ni Kao wo Suteta Onna~');
CALL InsertChapterImage(6, '31_eqrxdb', 'Image ID', 'Vol. 1 Ch. 1', 'Bishu no Daichi Fukushu ~Fukushuu no tame ni Kao wo Suteta Onna~');
CALL InsertChapterImage(6, '32_lxvkwn', 'Image ID', 'Vol. 1 Ch. 1', 'Bishu no Daichi Fukushu ~Fukushuu no tame ni Kao wo Suteta Onna~');
CALL InsertChapterImage(6, '33_hugl6i', 'Image ID', 'Vol. 1 Ch. 1', 'Bishu no Daichi Fukushu ~Fukushuu no tame ni Kao wo Suteta Onna~');
CALL InsertChapterImage(6, '34_mnwvxy', 'Image ID', 'Vol. 1 Ch. 1', 'Bishu no Daichi Fukushu ~Fukushuu no tame ni Kao wo Suteta Onna~');
CALL InsertChapterImage(6, '35_eyonsr', 'Image ID', 'Vol. 1 Ch. 1', 'Bishu no Daichi Fukushu ~Fukushuu no tame ni Kao wo Suteta Onna~');
CALL InsertChapterImage(6, '36_cx6lyd', 'Image ID', 'Vol. 1 Ch. 1', 'Bishu no Daichi Fukushu ~Fukushuu no tame ni Kao wo Suteta Onna~');
CALL InsertChapterImage(6, '37_we4izm', 'Image ID', 'Vol. 1 Ch. 1', 'Bishu no Daichi Fukushu ~Fukushuu no tame ni Kao wo Suteta Onna~');
CALL InsertChapterImage(6, '38_dsmz63', 'Image ID', 'Vol. 1 Ch. 1', 'Bishu no Daichi Fukushu ~Fukushuu no tame ni Kao wo Suteta Onna~');
CALL InsertChapterImage(6, '39_n0yk0e', 'Image ID', 'Vol. 1 Ch. 1', 'Bishu no Daichi Fukushu ~Fukushuu no tame ni Kao wo Suteta Onna~');
CALL InsertChapterImage(6, '40_o6daiy', 'Image ID', 'Vol. 1 Ch. 1', 'Bishu no Daichi Fukushu ~Fukushuu no tame ni Kao wo Suteta Onna~');
CALL InsertChapterImage(6, '41_cqzywb', 'Image ID', 'Vol. 1 Ch. 1', 'Bishu no Daichi Fukushu ~Fukushuu no tame ni Kao wo Suteta Onna~');
CALL InsertChapterImage(6, '42_ldv6fd', 'Image ID', 'Vol. 1 Ch. 1', 'Bishu no Daichi Fukushu ~Fukushuu no tame ni Kao wo Suteta Onna~');
CALL InsertChapterImage(6, '43_geh5es', 'Image ID', 'Vol. 1 Ch. 1', 'Bishu no Daichi Fukushu ~Fukushuu no tame ni Kao wo Suteta Onna~');
CALL InsertChapterImage(6, '44_xmhjsr', 'Image ID', 'Vol. 1 Ch. 1', 'Bishu no Daichi Fukushu ~Fukushuu no tame ni Kao wo Suteta Onna~');
CALL InsertChapterImage(6, '45_vinmld', 'Image ID', 'Vol. 1 Ch. 1', 'Bishu no Daichi Fukushu ~Fukushuu no tame ni Kao wo Suteta Onna~');
CALL InsertChapterImage(6, '46_em1u8d', 'Image ID', 'Vol. 1 Ch. 1', 'Bishu no Daichi Fukushu ~Fukushuu no tame ni Kao wo Suteta Onna~');
