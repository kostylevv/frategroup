INSERT INTO app_user(email, login, name, birthday)
VALUES ('test@mail', 'test-login', 'testname', '1995-04-28');

INSERT INTO app_user(email, login, name, birthday)
VALUES ('test2@mail', 'test2-login', 'testname2', '1995-04-29');

MERGE INTO mpa (id, name) VALUES
(1, 'G'),
(2, 'PG'),
(3, 'PG-13'),
(4, 'R'),
(5, 'NC-17');

MERGE INTO genre (id, name) VALUES
(1, 'Комедия'),
(2, 'Драма'),
(3, 'Мультфильм'),
(4, 'Триллер'),
(5, 'Документальный'),
(6, 'Боевик');

INSERT INTO film (name, description, release_date, duration, mpa_id)
VALUES ('test0', 'testDescription0', '1998-05-05', 100, 1);