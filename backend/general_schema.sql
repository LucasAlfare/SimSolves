CREATE TABLE Rooms (
    id SERIAL PRIMARY KEY,
    name TEXT UNIQUE,
    related_admin_id BIGINT REFERENCES Admins(id),
    minimum_time_required_to_join BIGINT,
    puzzle_category VARCHAR(255) DEFAULT 'RubiksCube',
    num_users INTEGER DEFAULT 1,
    duration BIGINT DEFAULT 86400, -- 86400 segundos = 1 dia
    chat_content TEXT DEFAULT ''
);

CREATE TABLE Users (
    id SERIAL PRIMARY KEY,
    username TEXT UNIQUE,
    email TEXT UNIQUE,
    hashed_password TEXT,
    personal_best_related_solve_id BIGINT REFERENCES Solves(id),
    current_room_id BIGINT REFERENCES Rooms(id)
);

CREATE TABLE Admins (
    id SERIAL PRIMARY KEY,
    related_user_id BIGINT REFERENCES Users(id)
);

CREATE TABLE Solves (
    id SERIAL PRIMARY KEY,
    related_user_id BIGINT REFERENCES Users(id),
    time BIGINT,
    scramble TEXT,
    penalty VARCHAR(255) DEFAULT 'Ok'
);