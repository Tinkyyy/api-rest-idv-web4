USE quest_web;

INSERT INTO `users` (`username`, `lastname`, `firstname`, `email`, `level`, `password`, `role`, `creation_date`, `updated_date`)
VALUES  ('rinaz_s', 'Rinaz', 'Samir', 'samir@etna.io', '94', '$2a$11$tlcsW7m4If4N474SBecU9Oe0rx8UFVjaWd.eY1xzhqAUVngVi8dAO','ADMIN','2022-03-17 19:47:15','2022-03-17 19:47:15'),
        ('dummy_s', 'Sam', 'Dummy', 'dummy@etna.io', '1','$2a$11$tlcsW7m4If4N474SBecU9Oe0rx8UFVjaWd.eY1xzhqAUVngVi8dAO','USER','2022-03-17 19:47:15','2022-03-17 19:47:15');

INSERT INTO `category` (`name`,`description`, `creation_date`, `updated_date`)
VALUES  ('Name', 'Description', '2022-03-17 19:47:15', '2022-03-17 19:47:15');

INSERT INTO `lessons` (`name`,`title`, `thumbnail`, `content`, `author_id`, `category_id`, `creation_date`, `updated_date`)
VALUES  ('test', 'Title', 'Thumbnail', 'This is the content', '1', '1', '2022-03-17 19:47:15', '2022-03-17 19:47:15');
