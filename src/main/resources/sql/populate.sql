INSERT INTO users (email, password, role_id)
VALUES ('ivan@email.com', 'qwerty1', 1);

INSERT INTO author
VALUES (DEFAULT, 'Blinov'),
       (DEFAULT, 'Romanchik');

INSERT INTO book (title, publication_id, date_publication, total)
VALUES ('Java from epam', 2, '2021', 10),
       ('Java methods v2', 1, '2015', 10);

INSERT INTO book_author
VALUES (1003, 1001),
       (1003, 1002),
       (1004, 1001);

SELECT book.id               b_id,
       book.title            b_title,
       a.name                a_name,
       pt.type               pt_tipe,
       book.date_publication b_date_pt,
       book.total            b_total
FROM book
         JOIN book_author ba ON book.id = ba.book_id
         JOIN author a ON a.id = ba.author_id
         JOIN publication_type pt ON pt.id = book.publication_id
WHERE book.title LIKE 'java m%';

SELECT book.id               b_id,
       book.title            b_title,
       a.id                  a_id,
       a.name                a_name,
       pt.type               pt_name,
       book.date_publication b_date,
       book.total            b_total
FROM book
         JOIN book_author ba ON book.id = ba.book_id
         JOIN author a ON a.id = ba.author_id
         JOIN publication_type pt ON pt.id = book.publication_id;

INSERT INTO book (title, publication_id, date_publication, total)
VALUES ('Java from epam v3', 2, '2024', 10),
       ('Some book', 1, '1006', 10),
       ('Some book 2', 1, '1007', 10),
       ('Some book 3', 1, '1008', 10),
       ('Some book 4', 1, '1009', 10),
       ('Some book 5', 1, '1010', 10),
       ('Some book 6', 1, '1011', 10);

INSERT INTO author
VALUES (DEFAULT, 'surname0'),
       (DEFAULT, 'surname1'),
       (DEFAULT, 'surname2'),
       (DEFAULT, 'surname3'),
       (DEFAULT, 'surname4'),
       (DEFAULT, 'surname5'),
       (DEFAULT, 'surname6'),
       (DEFAULT, 'surname7'),
       (DEFAULT, 'surname8');

INSERT INTO book_author
VALUES (1005, 1012),
       (1006, 1013),
       (1007, 1014),
       (1008, 1015),
       (1009, 1016),
       (1010, 1016),
       (1010, 1017),
       (1011, 1017),
       (1011, 1018);

SELECT *
FROM author
WHERE name = 'blinov';

INSERT INTO author (name)
VALUES ('testAuthor');

WITH val AS (SELECT id, "name" FROM author WHERE name = 'new author'),
     ins AS ( INSERT INTO author ("name") SELECT 'new author' WHERE NOT exists(SELECT 1 FROM val) RETURNING id)
SELECT id
FROM ins
UNION ALL
SELECT id
FROM val;

INSERT INTO users (email, password, role_id)
VALUES ('admin@admin.com', 'qwerty1', 3),
       ('librarian@email.com', 'qwerty1', 2);
