INSERT INTO users (email, password, role_id)
VALUES ('ivan@email.com',
        '$argon2id$v=19$m=1048576,t=4,p=8$eUDJSTxGL9M4gXSbjX2Fmw$isGrGa+erC6lGq4ZyIGWkvVWloFLy/dWc0940QSZKzg', 1);

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

INSERT INTO author (id, name)
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
       (1006, 1014),
       (1006, 1015),
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
VALUES ('admin@admin.com',
        '$argon2id$v=19$m=1048576,t=4,p=8$eUDJSTxGL9M4gXSbjX2Fmw$isGrGa+erC6lGq4ZyIGWkvVWloFLy/dWc0940QSZKzg', 3),
       ('librarian@email.com',
        '$argon2id$v=19$m=1048576,t=4,p=8$eUDJSTxGL9M4gXSbjX2Fmw$isGrGa+erC6lGq4ZyIGWkvVWloFLy/dWc0940QSZKzg', 2);

INSERT INTO book (title, publication_id, date_publication, total)
VALUES ('Python for everyone', 3, '2021', 15),
       ('Data Science with R', 1, '2020', 20),
       ('Web Development with React', 2, '2019', 5),
       ('Advanced Machine Learning', 3, '2018', 25),
       ('DevOps with Docker', 1, '2017', 30),
       ('Database Design and Implementation', 2, '2016', 10),
       ('Agile Project Management', 3, '2015', 20),
       ('Big Data Analytics with Hadoop', 1, '2014', 15),
       ('Cloud Computing with AWS', 2, '2013', 25),
       ('JavaScript for Beginners', 3, '2012', 10),
       ('Advanced CSS and Sass', 1, '2011', 15),
       ('Building RESTful APIs', 2, '2010', 20),
       ('Mobile Development with Flutter', 3, '2009', 5),
       ('Machine Learning with TensorFlow', 1, '2008', 25),
       ('Building Scalable Applications', 2, '2007', 30),
       ('Full Stack Web Development', 3, '2006', 10),
       ('Data Structures and Algorithms', 1, '2005', 20),
       ('Software Design Patterns', 2, '2004', 15),
       ('Artificial Intelligence Fundamentals', 3, '2003', 25),
       ('Cryptography and Network Security', 1, '2002', 30),
       ('Compiler Design', 2, '2001', 10),
       ('Operating System Design', 3, '2000', 20),
       ('Object-Oriented Programming', 1, '1999', 15),
       ('Functional Programming with Haskell', 2, '1998', 25),
       ('Computer Networking Fundamentals', 3, '1997', 30),
       ('Advanced Database Systems', 1, '1996', 10),
       ('Embedded Systems Design', 2, '1995', 20),
       ('Game Development with Unity', 3, '1994', 5),
       ('Parallel Computing', 1, '1993', 25),
       ('Computer Architecture and Organization', 2, '1992', 30),
       ('Programming Languages', 3, '1991', 10),
       ('User Experience Design', 1, '1990', 20),
       ('Virtual Reality Development', 2, '1989', 15),
       ('Augmented Reality Development', 3, '1988', 25),
       ('Human-Computer Interaction', 1, '1987', 30),
       ('Web Security Fundamentals', 2, '1986', 10);

INSERT INTO book_author
VALUES (1025, 1012),
       (1026, 1013),
       (1027, 1014),
       (1028, 1015),
       (1029, 1016),
       (1030, 1016),
       (1030, 1017),
       (1031, 1018),
       (1032, 1012),
       (1033, 1013),
       (1034, 1014),
       (1035, 1015),
       (1036, 1016),
       (1037, 1017),
       (1038, 1018),
       (1039, 1012),
       (1040, 1013),
       (1041, 1014),
       (1042, 1015),
       (1043, 1016),
       (1044, 1017),
       (1044, 1019),
       (1045, 1018),
       (1045, 1017),
       (1046, 1020),
       (1047, 1012),
       (1048, 1013),
       (1049, 1014),
       (1050, 1015),
       (1051, 1016),
       (1052, 1017),
       (1053, 1018),
       (1054, 1019),
       (1055, 1017),
       (1056, 1020),
       (1057, 1013),
       (1058, 1012),
       (1059, 1014),
       (1060, 1015);

