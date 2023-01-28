DROP TABLE IF EXISTS user_role CASCADE;
DROP TABLE IF EXISTS publication_type CASCADE;
DROP TABLE IF EXISTS loan_status CASCADE;
DROP TABLE IF EXISTS loan;
DROP TABLE IF EXISTS book_author;
DROP TABLE IF EXISTS author;
DROP TABLE IF EXISTS book;
DROP TABLE IF EXISTS users;
DROP SEQUENCE IF EXISTS global_seq;

CREATE SEQUENCE global_seq START WITH 1000;

CREATE TABLE publication_type
(
    id   INTEGER UNIQUE NOT NULL PRIMARY KEY,
    type VARCHAR UNIQUE NOT NULL
);

CREATE TABLE book
(
    id               BIGINT DEFAULT nextval('global_seq') PRIMARY KEY,
    title            VARCHAR UNIQUE NOT NULL,
    publication_id   INTEGER        NOT NULL,
    date_publication INTEGER        NOT NULL,
    total            INTEGER        NOT NULL,

    FOREIGN KEY (publication_id) REFERENCES publication_type (id) ON DELETE CASCADE
);


CREATE TABLE author
(
    id   BIGINT DEFAULT nextval('global_seq') PRIMARY KEY,
    name VARCHAR UNIQUE NOT NULL
);

CREATE TABLE book_author
(
    book_id   INTEGER NOT NULL,
    author_id INTEGER NOT NULL,

    CONSTRAINT unique_pair_idx UNIQUE (book_id, author_id),
    FOREIGN KEY (book_id) REFERENCES book (id) ON DELETE CASCADE,
    FOREIGN KEY (author_id) REFERENCES author (id) ON DELETE CASCADE
);

CREATE TABLE user_role
(
    id   INTEGER PRIMARY KEY NOT NULL,
    role VARCHAR UNIQUE      NOT NULL
);

CREATE TABLE users
(
    id         BIGINT    DEFAULT nextval('global_seq') PRIMARY KEY,
    email      VARCHAR                 NOT NULL,
    password   VARCHAR                 NOT NULL,
    registered TIMESTAMP DEFAULT now() NOT NULL,
    blocked    BOOLEAN   DEFAULT false NOT NULL,
    role_id    BIGINT    DEFAULT 0     NOT NULL,

    FOREIGN KEY (role_id) REFERENCES user_role (id) ON DELETE CASCADE
);

CREATE UNIQUE INDEX users_unique_email_idx ON users (email);

CREATE TABLE loan_status
(
    id     INTEGER PRIMARY KEY,
    status VARCHAR NOT NULL
);

CREATE TABLE loan
(
    id         BIGINT  DEFAULT nextval('global_seq') PRIMARY KEY,
    user_id    INTEGER               NOT NULL,
    book_id    INTEGER               NOT NULL,
    start_time DATE    DEFAULT now() NOT NULL,
    duration   INTEGER DEFAULT ('0') NOT NULL,
    status_id  INTEGER               NOT NULL,
    fine       INTEGER DEFAULT 0     NOT NULL,

    CONSTRAINT unique_indexs_user_book_id UNIQUE (user_id, book_id),
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (book_id) REFERENCES book (id),
    FOREIGN KEY (status_id) REFERENCES loan_status (id)
);


INSERT INTO user_role
VALUES (0, 'UNREGISTER'),
       (1, 'READER'),
       (2, 'LIBRARIAN'),
       (3, 'ADMIN');

INSERT INTO publication_type
VALUES (0, 'BOOK'),
       (1, 'JOURNAL'),
       (2, 'ARTICLE'),
       (3, 'NEWSPAPER');

INSERT INTO loan_status
VALUES (0, 'RAW'),
       (1, 'COMPLETE'),
       (2, 'OVERDUE');