
CREATE DATABASE IF NOT EXISTS booknova_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE booknova_db;

CREATE USER 'booknova_user'@'localhost' IDENTIFIED BY 'booknova!123';
GRANT ALL PRIVILEGES ON novabook_db.* TO 'novabook_user'@'localhost';
FLUSH PRIVILEGES;

CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(120) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    phone VARCHAR(30) NOT NULL,
    role ENUM('USER','ADMIN') NOT NULL DEFAULT 'USER',
    access_level ENUM('READ_ONLY','READ_WRITE', 'MANAGE') NOT NULL DEFAULT 'READ_WRITE',
    active BOOLEAN NOT NULL DEFAULT TRUE,
    deleted BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS book (
    id INT AUTO_INCREMENT PRIMARY KEY,
    isbn VARCHAR(20) NOT NULL UNIQUE,
    title VARCHAR(100) NOT NULL,
    author VARCHAR(100) NOT NULL,
    stock INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS member (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT UNIQUE,
    name VARCHAR(100) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    deleted BOOLEAN NOT NULL DEFAULT FALSE,
    role ENUM('REGULAR','PREMIUM') NOT NULL DEFAULT 'REGULAR',
    access_level ENUM('READ_ONLY','READ_WRITE', 'MANAGE') NOT NULL DEFAULT 'READ_WRITE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS loan (
    id INT AUTO_INCREMENT PRIMARY KEY,
    member_id INT,
    book_id INT,
    date_loaned DATE,
    date_due DATE,
    returned BOOLEAN,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (member_id) REFERENCES member(id),
    FOREIGN KEY (book_id) REFERENCES book(id)
);

CREATE TABLE IF NOT EXISTS membership_request (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    user_name VARCHAR(100) NOT NULL,
    user_email VARCHAR(120) NOT NULL,
    status ENUM('PENDING', 'APPROVED', 'REJECTED') NOT NULL DEFAULT 'PENDING',
    request_reason TEXT,
    approved_by_user_id INT,
    requested_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    processed_at TIMESTAMP NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (approved_by_user_id) REFERENCES users(id)
);


-- Add user_id column to member table
-- Execute this script in MySQL Workbench or command line

USE novabook;

-- Add user_id column to member table
ALTER TABLE member 
ADD COLUMN user_id INT UNIQUE AFTER id;

-- Add foreign key constraint
ALTER TABLE member
ADD CONSTRAINT fk_member_user 
FOREIGN KEY (user_id) REFERENCES users(id);

-- Verify the change
DESCRIBE member;

-- If you have existing members that need to be linked to users,
-- you can update them manually. Example:
-- UPDATE member SET user_id = 1 WHERE id = 1;

SELECT 'Column user_id added successfully to member table!' AS Result;