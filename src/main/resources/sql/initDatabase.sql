DROP TABLE IF EXISTS user_role;
DROP TABLE IF EXISTS loan;
DROP TABLE IF EXISTS book_author;
DROP TABLE IF EXISTS author;
DROP TABLE IF EXISTS book;
DROP TABLE IF EXISTS "user";
DROP SEQUENCE IF EXISTS global_seq;

CREATE SEQUENCE global_seq START WITH 100;

CREATE TABLE book
(
    id               INTEGER PRIMARY KEY DEFAULT nextval('global_seq'),
    title            VARCHAR                         NOT NULL,
    publication      VARCHAR                         NOT NULL,
    date_publication TIMESTAMP                       NOT NULL,
    no_total         INTEGER                         NOT NULL,
    no_actual        INTEGER             default '0' NOT NULL
);

CREATE UNIQUE INDEX books_unique_title_idx ON book (title);

CREATE TABLE author
(
    id     INTEGER PRIMARY KEY DEFAULT nextval('global_seq'),
    author VARCHAR NOT NULL
);

CREATE TABLE book_author
(
    book_id   INTEGER,
    author_id INTEGER,
    FOREIGN KEY (book_id) REFERENCES book (id) ON DELETE CASCADE,
    FOREIGN KEY (author_id) REFERENCES author (id) ON DELETE CASCADE,
    PRIMARY KEY (book_id, author_id)
);

CREATE TABLE "user"
(
    id         INTEGER PRIMARY KEY DEFAULT nextval('global_seq'),
    name       VARCHAR                           NOT NULL,
    email      VARCHAR                           NOT NULL,
    password   VARCHAR                           NOT NULL,
    registered TIMESTAMP           DEFAULT now() NOT NULL,
    blocked    BOOLEAN             DEFAULT false NOT NULL,
    fine       INTEGER             DEFAULT 0     NOT NULL
);

CREATE UNIQUE INDEX users_unique_email_idx ON "user" (email);

CREATE TABLE user_role
(
    user_id INTEGER NOT NULL,
    role    VARCHAR NOT NULL,
    CONSTRAINT user_role_idx UNIQUE (user_id, role),
    FOREIGN KEY (user_id) REFERENCES "user" (id) ON DELETE CASCADE
);


CREATE TABLE loan
(
    id         SERIAL PRIMARY KEY,
    user_id    INTEGER                 NOT NULL,
    book_id    INTEGER                 NOT NULL,
    start_time DATE    DEFAULT now()   NOT NULL,
    duration   INTEGER DEFAULT ('0')   NOT NULL,
    status     VARCHAR DEFAULT ('RAW') NOT NULL,
    FOREIGN KEY (user_id) REFERENCES "user" (id),
    FOREIGN KEY (book_id) REFERENCES book (id)
);
