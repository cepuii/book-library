INSERT INTO users (email, password)
VALUES ('ivan@email', 'qwerty');

INSERT INTO author
VALUES (DEFAULT, 'blinov'),
       (DEFAULT, 'romanchik');

INSERT INTO book (title, publication_id, date_publication, total)
VALUES ('java from epam', 2, '2021', 10),
       ('java methods v2', 1, '2015', 10);

INSERT INTO book_author
VALUES (1003, 1001),
       (1003, 1002),
       (1004, 1001);

