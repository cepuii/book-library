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
