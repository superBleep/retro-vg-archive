CREATE SCHEMA IF NOT EXISTS rvga;

DROP TYPE IF EXISTS rvga.archive_role CASCADE;
DROP TABLE IF EXISTS rvga.archive_user CASCADE;
DROP TABLE IF EXISTS rvga.game_version CASCADE;
DROP TABLE IF EXISTS rvga.game CASCADE;
DROP TABLE IF EXISTS rvga.platform CASCADE;
DROP TABLE IF EXISTS rvga.emulator_platform CASCADE;
DROP TABLE IF EXISTS rvga.emulator CASCADE;
DROP TABLE IF EXISTS rvga.review CASCADE;

CREATE TYPE rvga.archive_role AS ENUM ('regular', 'moderator', 'admin');

CREATE TABLE rvga.archive_user (
	id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
	creation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	username VARCHAR(30) UNIQUE NOT NULL,
	email VARCHAR(70) UNIQUE NOT NULL,
	password VARCHAR(70) NOT NULL,
	first_name VARCHAR(50),
	last_name VARCHAR(50),
	role archive_role NOT NULL
);

CREATE TABLE rvga.platform (
	id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
	name VARCHAR(70) UNIQUE NOT NULL,
	manufacturer VARCHAR(70) NOT NULL,
	release DATE NOT NULL
);

CREATE TABLE rvga.game (
	id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
	title VARCHAR(100) NOT NULL,
	developer VARCHAR(70) NOT NULL,
	publisher VARCHAR(70) NOT NULL,
	platform_id BIGINT REFERENCES rvga.platform(id) NOT NULL,
	genre VARCHAR(50) NOT NULL
);

CREATE TABLE rvga.game_version (
	id VARCHAR(30),
	game_id BIGINT REFERENCES rvga.game(id),
	release DATE NOT NULL,
	notes TEXT NOT NULL,
	PRIMARY KEY (id, game_id)
);

CREATE TABLE rvga.emulator (
	id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
	name VARCHAR(70) UNIQUE NOT NULL,
	developer VARCHAR(70) NOT NULL,
	release DATE NOT NULL,
	version VARCHAR(30) NOT NULL
);

CREATE TABLE rvga.emulator_platform (
	emulator_id BIGINT REFERENCES rvga.emulator(id),
	platform_id BIGINT REFERENCES rvga.platform(id),
	PRIMARY KEY (emulator_id, platform_id)
);

CREATE TABLE rvga.review (
	id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
	user_id BIGINT REFERENCES rvga.archive_user(id) NOT NULL,
	version_id VARCHAR(30) NOT NULL,
	game_id BIGINT NOT NULL,
	creation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	emulator_id BIGINT REFERENCES rvga.emulator(id),
	rating SMALLINT NOT NULL,
	comment TEXT,
	FOREIGN KEY (version_id, game_id) REFERENCES rvga.game_version (id, game_id)
);