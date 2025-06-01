INSERT INTO users (id, username, password, email, created_at)
VALUES (1, 'Dummy_user_1', 'dummy_1_pass', 'dummy_1@email.com', '2025-01-01 00:00:00'),
       (2, 'Dummy_user_2', 'dummy_2_pass', 'dummy_2@email.com', '2025-01-02 00:00:00'),
       (3, 'Dummy_user_3', 'dummy_3_pass', 'dummy_3@email.com', '2025-01-03 00:00:00'),
       (4, 'Dummy_user_4', 'dummy_4_pass', 'dummy_4@email.com', '2025-01-04 00:00:00'),
       (5, 'Dummy_user_5', 'dummy_5_pass', 'dummy_5@email.com', '2025-01-05 00:00:00');

INSERT INTO notes (id, title, tag, content, author, created_at)
VALUES
    ('11111111-1111-1111-1111-111111111111', 'dummy_title_1_1', 'dummy_tag_1_1', 'dummy_content_1_1', 1, '2025-01-01 00:00:00'),
    ('22222222-2222-2222-2222-222222222222', 'dummy_title_2_1', 'dummy_tag_1_1', 'dummy_content_2_1', 1, '2025-01-01 00:00:01'),
    ('33333333-3333-3333-3333-333333333333', 'dummy_title_1_2', 'dummy_tag_1_2', 'dummy_content_1_2', 2, '2025-01-01 00:00:02'),
    ('44444444-4444-4444-4444-444444444444', 'dummy_title_2_2', 'dummy_tag_2_2', 'dummy_content_2_2', 2, '2025-01-01 00:00:02.500'),
    ('55555555-5555-5555-5555-555555555555', 'dummy_title_1_3', 'dummy_tag_1_3', 'dummy_content_1_3', 3, '2025-01-01 00:00:02.700'),
    ('66666666-6666-6666-6666-666666666666', 'dummy_title_2_3', 'dummy_tag_2_3', 'dummy_content_2_3', 3, '2025-01-01 00:00:02.900'),
    ('77777777-7777-7777-7777-777777777777', 'dummy_title_1_4', 'dummy_tag_1_4', 'dummy_content_1_4', 4, '2025-01-01 00:00:02.950'),
    ('88888888-8888-8888-8888-888888888888', 'dummy_title_1_5', 'dummy_tag_1_5', 'dummy_content_1_5', 5, '2025-01-01 00:00:02.980'),
    ('99999999-9999-9999-9999-999999999999', 'dummy_title_2_4', 'dummy_tag_2_4', 'dummy_content_2_4', 1, '2025-01-01 00:00:02.990'),
    ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'dummy_title_1_6', 'dummy_tag_1_6', 'dummy_content_1_6', 2, '2025-01-01 00:00:02.999'),
    ('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 'dummy_title_2_5', 'dummy_tag_2_5', 'dummy_content_2_5', 3, '2025-01-02 00:00:04'),
    ('cccccccc-cccc-cccc-cccc-cccccccccccc', 'dummy_title_1_7', 'dummy_tag_1_7', 'dummy_content_1_7', 4, '2025-01-02 00:00:05'),
    ('dddddddd-dddd-dddd-dddd-dddddddddddd', 'dummy_title_2_6', 'dummy_tag_2_6', 'dummy_content_2_6', 5, '2025-01-02 00:00:06'),
    ('eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee', 'dummy_title_1_8', 'dummy_tag_1_8', 'dummy_content_1_8', 1, '2025-01-02 00:00:07'),
    ('ffffffff-ffff-ffff-ffff-ffffffffffff', 'dummy_title_2_7', 'dummy_tag_2_7', 'dummy_content_2_7', 2, '2025-01-02 00:00:08');
