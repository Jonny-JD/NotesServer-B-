INSERT INTO users (id, username, password, email, created_at)
VALUES (1, 'Dummy_user_#1', 'dummy_#1_pass', 'dummy_#1@email.com', '2025-01-01 00:00:00'),
       (2, 'Dummy_user_#2', 'dummy_#2_pass', 'dummy_#2@email.com', '2025-01-02 00:00:00'),
       (3, 'Dummy_user_#3', 'dummy_#3_pass', 'dummy_#3@email.com', '2025-01-03 00:00:00'),
       (4, 'Dummy_user_#4', 'dummy_#4_pass', 'dummy_#4@email.com', '2025-01-04 00:00:00'),
       (5, 'Dummy_user_#5', 'dummy_#5_pass', 'dummy_#5@email.com', '2025-01-05 00:00:00');

INSERT INTO notes (id, title, tag, content, author, created_at)
VALUES
    (1,  'dummy_title_#1_1',  'dummy_tag_#1_1',  'dummy_content_#1_1',  1, '2025-01-01 00:00:00'),
    (2,  'dummy_title_#2_1',  'dummy_tag_#1_1',  'dummy_content_#2_1',  1, '2025-01-01 00:00:01'),
    (3,  'dummy_title_#1_2',  'dummy_tag_#1_2',  'dummy_content_#1_2',  2, '2025-01-01 00:00:02'),
    (4,  'dummy_title_#2_2',  'dummy_tag_#2_2',  'dummy_content_#2_2',  2, '2025-01-01 00:00:02.500'),
    (5,  'dummy_title_#1_3',  'dummy_tag_#1_3',  'dummy_content_#1_3',  3, '2025-01-01 00:00:02.700'),
    (6,  'dummy_title_#2_3',  'dummy_tag_#2_3',  'dummy_content_#2_3',  3, '2025-01-01 00:00:02.900'),
    (7,  'dummy_title_#1_4',  'dummy_tag_#1_4',  'dummy_content_#1_4',  4, '2025-01-01 00:00:02.950'),
    (8,  'dummy_title_#1_5',  'dummy_tag_#1_5',  'dummy_content_#1_5',  5, '2025-01-01 00:00:02.980'),
    (9,  'dummy_title_#2_4',  'dummy_tag_#2_4',  'dummy_content_#2_4',  1, '2025-01-01 00:00:02.990'),
    (10, 'dummy_title_#1_6',  'dummy_tag_#1_6',  'dummy_content_#1_6',  2, '2025-01-01 00:00:02.999'),
    (11, 'dummy_title_#2_5',  'dummy_tag_#2_5',  'dummy_content_#2_5',  3, '2025-01-02 00:00:04'),
    (12, 'dummy_title_#1_7',  'dummy_tag_#1_7',  'dummy_content_#1_7',  4, '2025-01-02 00:00:05'),
    (13, 'dummy_title_#2_6',  'dummy_tag_#2_6',  'dummy_content_#2_6',  5, '2025-01-02 00:00:06'),
    (14, 'dummy_title_#1_8',  'dummy_tag_#1_8',  'dummy_content_#1_8',  1, '2025-01-02 00:00:07'),
    (15, 'dummy_title_#2_7',  'dummy_tag_#2_7',  'dummy_content_#2_7',  2, '2025-01-02 00:00:08');
