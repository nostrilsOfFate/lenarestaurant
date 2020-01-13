drop table if exists dishes CASCADE;
drop table if exists restaurants CASCADE;
drop table if exists restaurants_menu CASCADE;
drop table if exists user_roles CASCADE;
drop table if exists user_votes CASCADE;
drop table if exists users CASCADE;
drop table if exists vote_history CASCADE;
drop table if exists vote_history_menu_of_day CASCADE;
create table dishes
(
    id              bigint generated by default as identity (start with 1),
    name            varchar(100)          not null,
    price           numeric(19, 2)        not null,
    today_menu_dish boolean default false not null,
    primary key (id)
);
create table restaurants
(
    id             bigint generated by default as identity (start with 1),
    all_price_dish numeric(19, 2) not null check (all_price_dish >= 0 AND all_price_dish <= 9223372036854775807),
    name           varchar(100)   not null,
    score          integer        not null,
    primary key (id)
);
create table restaurants_menu
(
    restaurant_id bigint not null,
    menu_id       bigint not null,
    primary key (restaurant_id, menu_id)
);
create table user_roles
(
    user_id bigint not null,
    role    varchar(255)
);
create table user_votes
(
    id            bigint generated by default as identity (start with 1),
    restaurant_id bigint,
    user_id       bigint,
    primary key (id)
);
create table users
(
    id             bigint generated by default as identity (start with 1),
    email          varchar(100)            not null,
    enabled        boolean   default true  not null,
    is_voted       boolean   default false not null,
    last_vote_date timestamp,
    name           varchar(100)            not null,
    password       varchar(100)            not null,
    registered     timestamp default now() not null,
    primary key (id)
);
create table vote_history
(
    id            bigint generated by default as identity (start with 1),
    rest_score    integer                 not null,
    vot_time      timestamp default now() not null,
    restaurant_id bigint,
    primary key (id)
);
create table vote_history_menu_of_day
(
    vote_history_id bigint not null,
    menu_of_day_id  bigint not null,
    primary key (vote_history_id, menu_of_day_id)
);
alter table restaurants
    add constraint restaurants_unique_name_idx unique (name);
alter table restaurants_menu
    add constraint UK_beq4rubxorc1f9xxgrm9efh3f unique (menu_id);
alter table users
    add constraint UK_6dotkott2kjsp8vw4d0m25fb7 unique (email);
alter table restaurants_menu
    add constraint FKqnmttpd1xhyjt472rmgndwhqp foreign key (menu_id) references dishes on DELETE CASCADE;
alter table restaurants_menu
    add constraint FKw7sfo072iun5audbbrk8nsfq foreign key (restaurant_id) references restaurants;
alter table user_roles
    add constraint FKhfh9dx7w3ubf1co1vdev94g3f foreign key (user_id) references users on delete cascade;
alter table vote_history
    add constraint FK76nhi2osvdups3o5h7i5ah3rd foreign key (restaurant_id) references restaurants on DELETE CASCADE;
alter table vote_history_menu_of_day
    add constraint FK835xr1vr8w3eqxf9roqrg0ijh foreign key (menu_of_day_id) references dishes on DELETE CASCADE;
alter table vote_history_menu_of_day
    add constraint FKh6by0usc6fsd4rb8ik1upfawc foreign key (vote_history_id) references vote_history on delete cascade