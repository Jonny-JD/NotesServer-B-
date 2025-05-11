INSERT INTO users (id, username, password, email, created_at)
VALUES (1, 'Dummy_user_#1', 'dummy_#1_pass', 'dummy_#1@email.com', DATE(NOW())),
       (2, 'Dummy_user_#2', 'dummy_#2_pass', 'dummy_#2@email.com', DATE(NOW()) + 1),
       (3, 'Dummy_user_#3', 'dummy_#3_pass', 'dummy_#3@email.com', DATE(NOW()) + 2),
       (4, 'Dummy_user_#4', 'dummy_#4_pass', 'dummy_#4@email.com', DATE(NOW()) + 3),
       (5, 'Dummy_user_#5', 'dummy_#5_pass', 'dummy_#5@email.com', DATE(NOW()) + 4);

INSERT INTO notes (id, title, tag, content, author, created_at)
VALUES (1, 'dummy_title_#1_1', 'dummy_tag_#1_1', 'dummy_content_#1_1', 1, DATE(now())),
       (2, 'dummy_title_#2_1', 'dummy_tag_#1_1', 'dummy_content_#2_1', 1, DATE(now()) + 1),
       (3, 'dummy_title_#1_2', 'dummy_tag_#1_2', 'dummy_content_#1_2', 2, DATE(now()) + 2),
       (4, 'dummy_title_#2_2', 'dummy_tag_#2_2', 'dummy_content_#2_2', 2, DATE(now()) + 3),
       (5, 'dummy_title_#1_3', 'dummy_tag_#1_3', 'dummy_content_#1_3', 3, DATE(now()) + 4),
       (6, 'dummy_title_#2_3', 'dummy_tag_#2_3', 'dummy_content_#2_3', 3, DATE(now()) + 5),
       (7, 'dummy_title_#1_4', 'dummy_tag_#1_4', 'dummy_content_#1_4', 4, DATE(now()) + 5),
       (8, 'dummy_title_#1_5', 'dummy_tag_#1_5', 'dummy_content_#1_5', 5, DATE(now()) + 6)