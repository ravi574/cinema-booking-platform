CREATE TABLE movie (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       name VARCHAR(255),
                       language VARCHAR(50),
                       genre VARCHAR(50)
);

CREATE TABLE theatre (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         name VARCHAR(255),
                         city VARCHAR(100)
);

CREATE TABLE show (
                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                      movie_id BIGINT,
                      theatre_id BIGINT,
                      start_time DATETIME,
                      FOREIGN KEY (movie_id) REFERENCES movie(id),
                      FOREIGN KEY (theatre_id) REFERENCES theatre(id)
);

CREATE TABLE show_seat (
                           id BIGINT AUTO_INCREMENT PRIMARY KEY,
                           show_id BIGINT,
                           seat_number VARCHAR(10),
                           status VARCHAR(20),
                           FOREIGN KEY (show_id) REFERENCES show(id)
);

CREATE TABLE booking (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         user_id BIGINT,
                         show_id BIGINT,
                         total_amount DOUBLE,
                         status VARCHAR(20)
);

CREATE TABLE booking_seats (
                               booking_id BIGINT,
                               seat_id BIGINT,
                               PRIMARY KEY (booking_id, seat_id)
);

CREATE TABLE payment (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         booking_id BIGINT UNIQUE,
                         amount DOUBLE,
                         status VARCHAR(20),
                         transaction_id VARCHAR(255)
);