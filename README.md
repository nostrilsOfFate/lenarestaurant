Разработка Spring/JPA Enterprise приложения (without frontend) c правами доступа на основе ролей с использованием наиболее популярных инструментов и технологий Java: Maven, Spring Boot, Security, JPA(Hibernate), REST(Jackson), datatables, jQuery + plugins, Lombok, Swagger Java 11 Stream and Time API и хранением в базе HSQLDB.

**Просмотр функционала и методов:**
http://localhost:8080/swagger-ui.html#/
Для входа 
User: 'user@yandex.ru' пароль: 'password' (без кавычек)
Admin: 'admin@gmail.com' пароль: 'admin'  (без кавычек)

Design and implement a REST API using Hibernate / Spring / SpringMVC (or Spring-Boot) without frontend.

**The task is:**
vouting
Build a voting system for deciding where to have lunch.
enabled
2 types of users: admin and regular users
Admin can input a restaurant and it's lunch menu of the day (2-5 items usually, just a dish name and price)
Menu changes each day (admins do the updates)
Users can vote on which restaurant they want to have lunch at
Only one vote counted per user
If user votes again the same day:
If it is before 11:00 we asume that he changed his mind.
If it is after 11:00 then it is too late, vote can't be changed
Each restaurant provides new menu each day.

As a result, provide a link to github repository.

It should contain the code and README.md with API documentation and curl commands to get data for voting and vote.


**Описание проекта:** 
Реализован функционал голосование юзеров за рестораны, с учетом данного условия снятия голосов и невозможности проголосовать за один и тот же ресторан повторно.

**DishController**
1) GetAll

curl --location --request GET 'localhost:8080/rest/admin/dishes' \
--header 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'

2) Create with Location

curl --location --request POST 'localhost:8080/rest/admin/dishes/' \
--header 'Content-Type: application/json' \
--header 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu' \
--data-raw '{
  "name": "hinkaly",
  "new": true,
  "price": 1200,
  "todayMenuDish": true
}'

3) Get

curl --location --request GET 'localhost:8080/rest/admin/dishes/2' \
--header 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'

4) isTodayMenuDish



5) Update

curl --location --request PUT 'localhost:8080/rest/admin/dishes/' \
--header 'Content-Type: application/json' \
--header 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu' \
--data-raw '{
  "id": 2,
  "name": "Lavash",
  "new": true,
  "price": 30,
  "todayMenuDish": true
}'

6) Delete

curl --location --request DELETE 'localhost:8080/rest/admin/dishes/1' \
--header 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'

**Restaurants Controller**
1) CreateWithLocation

curl --location --request POST 'localhost:8080/rest/admin/restaurants' \
--header 'Content-Type: application/json' \
--header 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu' \
--data-raw '{
  "allDishPrice": 0,
  "id": 0,
  "name": "KFC *****",
  "new": true,
  "score": 0
}'

2) FindAll Sorted By Name

curl --location --request GET 'localhost:8080/rest/admin/restaurants/byname' \
--header 'Content-Type: application/json' \
--header 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu' \
--data-raw ''

3) voteForRestaurant



4) Update Rest

5) GetbyName

6) Get

7) GetAll

8) Delete



**User Controller**
1) GetByMailUser-
2) Get
3) CreateWithLocation-
4) GetNotFound
5) GetAll
6) Update
7) Delete
8) Enable 

**VoteHistoryController**
1) Delete
2) GetAll
3) GetNotFound
4) FindAllSorted
5) Get
7) FindAllByRestaurantId
8) FindAllSortedBetween
9) FindAllSortedByScore