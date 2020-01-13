DELETE FROM USER_ROLES;
DELETE FROM USER_VOTES;
DELETE FROM USERS;
DELETE FROM DISHES;
DELETE FROM RESTAURANTS;
DELETE FROM RESTAURANTS_MENU;
DELETE FROM VOTE_HISTORY;
DELETE FROM VOTE_HISTORY_MENU_OF_DAY;

INSERT INTO USERS (name, email, password)
VALUES ('User', 'user@yandex.ru', '{noop}password'),
       ('Admin', 'admin@gmail.com', '{noop}admin');

INSERT INTO USER_ROLES (role, user_id)
VALUES ('ROLE_USER', 1),
       ('ROLE_ADMIN', 2),
       ('ROLE_USER', 2);

insert into RESTAURANTS(ALL_PRICE_DISH, NAME, SCORE)
VALUES (200, 'Бонжур се ля ви', 15),
       (1200, 'Новый элитный ресторан 5 звезд', 555),
       (0, 'Столовая', 225);

INSERT INTO DISHES(NAME, PRICE, TODAY_MENU_DISH)
VALUES ('компот', 50, true),
       ('багет', 50, true),
       ('круасан', 50, true),
       ('краб', 350, true),
       ('икра заморская - баклажанная', 100, true),
       ('икра красная', 500, true),
       ('икра черная', 500, true),
       ('пирожок с мясом', 25, true),
       ('пирожок с черникой', 25, true),
       ('борщ', 50, true),
       ('новый модный обед', 200, true),
       ('блины', 50, false);

insert into RESTAURANTS_MENU(RESTAURANT_ID, MENU_ID)
VALUES (1, 2),
       (1, 3),
       (1, 5),
       (2, 6),
       (2, 7),
       (2, 11),
       (3, 1),
       (3, 8),
       (3, 9),
       (3, 10),
       (3, 4);

insert into VOTE_HISTORY(REST_SCORE, VOT_TIME, RESTAURANT_ID)
VALUES (27, '2019-02-03', 1),
       (33, '2019-02-03', 2),
       (19, '2019-02-03', 3),
       (19, '2019-02-04', 1),
       (19, '2019-02-04', 2),
       (19, '2019-02-04', 3),
       (15, '2019-02-05', 1),
       (44, '2019-02-05', 2),
       (2, '2019-02-05', 3);


insert into VOTE_HISTORY_MENU_OF_DAY(VOTE_HISTORY_ID, MENU_OF_DAY_ID)
VALUES (1, 2),
       (1, 3),
       (1, 4),
       (2, 11),
       (2, 12),
       (2, 6),
       (2, 7),
       (3, 1),
       (3, 8),
       (3, 9),
       (3, 10),
       (4, 3),
       (4, 4),
       (5, 6),
       (5, 7),
       (6, 1),
       (6, 8),
       (7, 2),
       (7, 3),
       (8, 11),
       (8, 12),
       (9, 9),
       (9, 10);
