INSERT INTO rvga.archive_user (username, email, password, first_name, last_name, role)
VALUES
('jdoe', 'jdoe@example.com', 'password123', 'John', 'Doe', 'regular'),
('asmith', 'asmith@example.com', 'securePass456', 'Alice', 'Smith', 'moderator'),
('bwilliams', 'bwilliams@example.com', 'hash789!', NULL, NULL, 'admin');

INSERT INTO rvga.platform (name, manufacturer, release) 
VALUES
('PlayStation 2', 'Sony', '2000-03-04'),
('Xbox', 'Microsoft', '2001-11-15'),
('Dreamcast', 'Sega', '1999-11-27'),
('Sega Genesis', 'Sega', '1988-10-29'),
('Atari 2600', 'Atari', '1977-09-11');

INSERT INTO rvga.game (title, developer, publisher, platform_id, genre) 
VALUES
('Metal Gear Solid 2: Sons of Liberty', 'Konami', 'Konami', 1, 'Action-adventure'),
('Final Fantasy X', 'Square Enix', 'Square Enix', 1, 'Role-playing'),
('Metal Gear Solid 2: Sons of Liberty', 'Konami', 'Konami', 2, 'Action-adventure'),
('Halo: Combat Evolved', 'Bungie', 'Microsoft', 2, 'First-person shooter'),
('Dead or Alive 3', 'Team Ninja', 'Tecmo', 2, 'Fighting'),
('Sonic Adventure', 'Sega', 'Sega', 3, 'Platformer'),
('Crazy Taxi', 'Hitmaker', 'Sega', 3, 'Racing'),
('Sonic the Hedgehog', 'Sega', 'Sega', 4, 'Platformer'),
('Pac-Man', 'Namco', 'Atari', 5, 'Maze'),
('Pitfall!', 'Activision', 'Activision', 5, 'Platformer');

INSERT INTO rvga.game_version (id, game_id, release, notes) 
VALUES
('1.0', 1, '2001-11-18', 'Initial release'),
('1.1', 1, '2002-05-14', 'Expanded edition with new content'),
('1.0', 2, '2001-07-19', 'Initial release'),
('2.0', 2, '2002-12-18', 'International release with additional content'),
('1.0', 4, '2001-11-15', 'Initial release'),
('1.1', 4, '2002-06-27', 'Combat Evolved - Multiplayer edition'),
('1.0', 6, '1999-12-23', 'Initial release'),
('1.0', 7, '1999-12-28', 'Initial release'),
('1.0', 8, '1991-06-23', 'Initial release'),
('1.0', 9, '1980-06-01', 'Initial release'),
('1.0', 10, '1982-10-22', 'Initial release');

INSERT INTO rvga.emulator (name, developer, release, version) 
VALUES
('PCSX2', 'PCSX2 Team', '2002-03-23', '1.7.0'),
('RetroArch', 'Libretro', '2010-10-14', '1.9.0'),
('Dolphin', 'Dolphin Team', '2003-09-22', '5.0-17527'),
('xemu', 'xemu Team', '2017-12-19', '0.5.0');

INSERT INTO rvga.emulator_platform (emulator_id, platform_id) 
VALUES
(1, 1),
(2, 1),
(2, 2),
(2, 3),
(2, 4),
(2, 5),
(3, 3),
(4, 2);

INSERT INTO rvga.review (user_id, version_id, game_id, emulator_id, rating, comment) VALUES
(1, '1.0', 1, 1, 9, 'Excellent game with a compelling story and challenging gameplay.'),
(2, '1.0', 4, 4, 8, 'Great first-person shooter with immersive environments.'),
(3, '1.0', 6, 3, 10, 'A classic platformer with timeless appeal and fantastic music.'),
(1, '2.0', 2, NULL, 7, 'Still enjoyable, but feels a bit outdated compared to modern RPGs.'),
(2, '1.0', 7, 2, 6, 'Fun but repetitive gameplay with minor technical issues.'),
(3, '1.0', 8, 2, 9, 'A game that holds up well over time with smooth controls and emulator support.'),
(1, '1.0', 9, NULL, 8, 'Simple yet addictive gameplay with nostalgic charm.'),
(2, '1.0', 10, 2, 10, 'Iconic game that set the standard for the arcade genre, running smoothly on emulator.');



