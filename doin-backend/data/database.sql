CREATE TABLE Users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL, -- Store hashed passwords
    created_at DATETIME NOT NULL DEFAULT CURRENT_DATE(),
    profile_picture_id INTEGER,
    security_question VARCHAR(255),
    FOREIGN KEY (profile_picture_id) REFERENCES Pictures(id)
);

CREATE TABLE Friendships (
    id INT AUTO_INCREMENT PRIMARY KEY,
    sender_id INTEGER NOT NULL,
    receiver_id INTEGER NOT NULL,
    type VARCHAR(50) CHECK (type IN('close', 'friend', 'removed'))
    status VARCHAR(50) CHECK (status IN ('pending', 'accepted', 'declined', 'removed')),
    created_at DATETIME NOT NULL DEFAULT CURRENT_DATE(),
    executed_at DATETIME,
    FOREIGN KEY (sender_id) REFERENCES Users(id),
    FOREIGN KEY (receiver_id) REFERENCES Users(id)
);

CREATE TABLE Events (
    id INT AUTO_INCREMENT PRIMARY KEY,
    event_type VARCHAR(100) NOT NULL,
    visibility VARCHAR(100) NOT NULL,
    creator_id INTEGER NOT NULL,
    location VARCHAR(255),
    time DATETIME NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_DATE(),
    description TEXT,
    FOREIGN KEY (creator_id) REFERENCES Users(id)
);

CREATE TABLE Event_Joiners (
    event_id INTEGER NOT NULL,
    user_id INTEGER NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_DATE(),
    PRIMARY KEY (event_id, user_id),
    FOREIGN KEY (event_id) REFERENCES Events(id),
    FOREIGN KEY (user_id) REFERENCES Users(id)
);

CREATE TABLE Event_Image (
    event_id INTEGER NOT NULL,
    picture_id INTEGER NOT NULL,
    PRIMARY KEY (event_id, picture_id),
    FOREIGN KEY (event_id) REFERENCES Events(id),
    FOREIGN KEY (picture_id) REFERENCES Pictures(id)
);

CREATE TABLE Pictures (
    id INT AUTO_INCREMENT PRIMARY KEY,
    image BLOB NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_DATE(),
);

CREATE TABLE Security_Questions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    answer VARCHAR(255) NOT NULL, -- Store hashed answer
    question VARCHAR(255) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES Users(id)
);
