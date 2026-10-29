CREATE TABLE category (
    id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    CONSTRAINT uk_category_name UNIQUE (name)
);

CREATE TABLE author (
    id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE book (
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    isbn         VARCHAR(13)    NOT NULL,
    title        VARCHAR(255)   NOT NULL,
    description  VARCHAR(4000),
    price        DECIMAL(10,2)  NOT NULL,
    published_on DATE,
    stock        INT            NOT NULL DEFAULT 0,
    version      BIGINT         NOT NULL DEFAULT 0,
    category_id  BIGINT,
    CONSTRAINT uk_book_isbn      UNIQUE (isbn),
    CONSTRAINT ck_book_stock     CHECK (stock >= 0),
    CONSTRAINT ck_book_price     CHECK (price >= 0),
    CONSTRAINT fk_book_category  FOREIGN KEY (category_id) REFERENCES category (id)
);

CREATE TABLE book_author (
    book_id   BIGINT NOT NULL,
    author_id BIGINT NOT NULL,
    PRIMARY KEY (book_id, author_id),
    CONSTRAINT fk_book_author_book   FOREIGN KEY (book_id)   REFERENCES book (id),
    CONSTRAINT fk_book_author_author FOREIGN KEY (author_id) REFERENCES author (id)
);