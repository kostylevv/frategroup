CREATE TABLE IF NOT EXISTS rating (
    id INTEGER PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(10)
);

CREATE TABLE IF NOT EXISTS film (
    id INTEGER PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    description VARCHAR(200) NOT NULL,
    release_date DATE NOT NULL,
    duration INTEGER NOT NULL,
    rating_id INTEGER REFERENCES rating(id)
);

CREATE TABLE IF NOT EXISTS app_user (
    id INTEGER PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(50) NOT NULL,
    login VARCHAR(50) NOT NULL,
    name VARCHAR(50),
    birthday DATE NOT NULL
);

CREATE TABLE IF NOT EXISTS film_likes (
    id INTEGER PRIMARY KEY AUTO_INCREMENT,
    film_id INTEGER REFERENCES film(id),
    user_id INTEGER REFERENCES app_user(id)
);

CREATE TABLE IF NOT EXISTS genre (
    id INTEGER PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS film_genres (
    id INTEGER PRIMARY KEY AUTO_INCREMENT,
    film_id INTEGER REFERENCES film(id),
    genre_id INTEGER REFERENCES genre(id)
);

CREATE TABLE IF NOT EXISTS friends (
    id INTEGER PRIMARY KEY AUTO_INCREMENT,
    user_id INTEGER REFERENCES app_user(id),
    friend_id INTEGER REFERENCES app_user(id)
);
