DELETE FROM meals;
DELETE FROM user_roles;
DELETE FROM users;

ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password, calories_per_day) VALUES
  ('User', 'user@yandex.ru', '{noop}password', 2005),
  ('Admin', 'admin@gmail.com', '{noop}admin', 1900);

INSERT INTO user_roles (role, user_id) VALUES
  ('ROLE_USER', 100000),
  ('ROLE_ADMIN', 100001),
  ('ROLE_USER', 100001);


INSERT INTO meals (user_id, description, calories, datetime) VALUES
  (100000, 'Завтрак', 500, '2015-05-30 09:01:00'),
  (100000, 'Обед', 1000, '2015-05-30 13:01:00'),
  (100000, 'Ужин', 500, '2015-05-30 20:01:00'),
  (100000, 'Завтрак', 500, '2015-05-31 09:01:00'),
  (100000, 'Обед', 1000, '2015-05-31 13:01:00'),
  (100000, 'Полдник', 100, '2015-05-31 17:01:00'),
  (100000, 'Ужин', 500, '2015-05-31 20:01:00'),
  (100001, 'Завтрак', 500, '2015-05-20 09:01:00'),
  (100001, 'Обед', 1000, '2015-05-20 13:01:00'),
  (100001, 'Ужин', 500, '2015-05-20 20:01:00');