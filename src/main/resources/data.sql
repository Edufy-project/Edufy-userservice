INSERT INTO users (username, password, preferred_genres, total_play_count, roles) VALUES ('joey', '$2a$10$8y1x2Q/7G3R4v9Lz7K1hOe5PbE9NwQx2Z5pXcV1hL6E9zQv7P9A1G', 'Hip-Hop', 0, 'ADMIN');
INSERT INTO users (username, password, preferred_genres, total_play_count, roles) VALUES ('ross', '$2a$10$G7pN4wL6k9V1hOe5Qx2Z5pXcV1hL6E9zQv7P9A1G', 'RnB', 0, 'USER');
INSERT INTO users (username, password, preferred_genres, total_play_count, roles) VALUES ('rachel', '$2a$10$F4kC1n3pH5vG9eR2uE.l1YkTjQ7Z5oW3xL6rT9aF3dX1B8Q2e', 'Rock', 0, 'USER');
INSERT INTO users (username, password, preferred_genres, total_play_count, roles) VALUES ('chandler', '$2a$10$D2f1pQ7sX3zL6wV.u3RkJcG0P1T5vB7qE9mH2xF4L8O3w6', 'Disco', 0, 'USER');

INSERT INTO user_media_history (user_id, media_id, media_type) VALUES (1, 1, 'music');
INSERT INTO user_media_history (user_id, media_id, media_type) VALUES (1, 2, 'music');
INSERT INTO user_media_history (user_id, media_id, media_type) VALUES (1, 3, 'music');
INSERT INTO user_media_history (user_id, media_id, media_type) VALUES (1, 4, 'music');
INSERT INTO user_media_history (user_id, media_id, media_type) VALUES (1, 5, 'music');
INSERT INTO user_media_history (user_id, media_id, media_type) VALUES (1, 6, 'music');
INSERT INTO user_media_history (user_id, media_id, media_type) VALUES (1, 7, 'music');
INSERT INTO user_media_history (user_id, media_id, media_type) VALUES (1, 8, 'music');
INSERT INTO user_media_history (user_id, media_id, media_type) VALUES (1, 14, 'music');
INSERT INTO user_media_history (user_id, media_id, media_type) VALUES (1, 17, 'music');
INSERT INTO user_media_history (user_id, media_id, media_type) VALUES (1, 22, 'music');
INSERT INTO user_media_history (user_id, media_id, media_type) VALUES (1, 19, 'music');

INSERT INTO user_media_history (user_id, media_id, media_type) VALUES (2, 36, 'music');
INSERT INTO user_media_history (user_id, media_id, media_type) VALUES (2, 29, 'music');
INSERT INTO user_media_history (user_id, media_id, media_type) VALUES (2, 33, 'music');

INSERT INTO user_media_history (user_id, media_id, media_type) VALUES (3, 28, 'music');
INSERT INTO user_media_history (user_id, media_id, media_type) VALUES (3, 29, 'music');
INSERT INTO user_media_history (user_id, media_id, media_type) VALUES (3, 4, 'music');
INSERT INTO user_media_history (user_id, media_id, media_type) VALUES (3, 3, 'music');

INSERT INTO user_feedback (user_id, media_id, media_type, feedback_type) VALUES (2, 36, 'music', 'THUMBS_DOWN');
INSERT INTO user_feedback (user_id, media_id, media_type, feedback_type) VALUES (2, 33, 'music', 'THUMBS_DOWN');
INSERT INTO user_feedback (user_id, media_id, media_type, feedback_type) VALUES (2, 5, 'music', 'THUMBS_DOWN');