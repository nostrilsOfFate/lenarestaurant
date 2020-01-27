Разработка Spring/JPA Enterprise приложения (without frontend) c правами доступа на основе ролей с использованием наиболее популярных инструментов и технологий Java: Maven, Spring Boot, Security, JPA(Hibernate), REST(Jackson), datatables, jQuery + plugins, Lombok, Swagger Java 11 Stream and Time API и хранением в базе HSQLDB.

**Как запустить проект?**
Для запуска использовать **mvn spring-boot:run**

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
--header 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu

**Restaurants Controller**
Restaurants Controller
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
 1) получаем все рестораны:

 curl --location --request GET 'localhost:8080/rest/admin/restaurants' \
--header 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'

2) голосуем за первый с нашего пользователя первый раз
curl --location --request POST 'localhost:8080/rest/admin/restaurants/1/vote' \
--header 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'

3) получаем всех, смотрим что изменился score у ресторана с id=1

curl --location --request GET 'localhost:8080/rest/admin/restaurants' \
--header 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'

4) Пробуем проголосовать еще раз за тот же ресторан с одного аккаунта'

curl --location --request POST 'localhost:8080/rest/admin/restaurants/1/vote' \
--header 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'

5) Получаем всех - не меняется количество score
_curl --location --request GET 'localhost:8080/rest/admin/restaurants' \
--header 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'

6) голосуем за другой ресторан с этого же аккаунта
curl --location --request POST 'localhost:8080/rest/admin/restaurants/3/vote' \
--header 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'

7) Смотрим сумму - голос отнялся у рест-а с id=1,  прибавился к рест-у id=3

curl --location --request GET 'localhost:8080/rest/admin/restaurants' \
--header 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'

4) Update Rest

curl --location --request PUT 'localhost:8080/rest/admin/restaurants' \
--header 'Content-Type: application/json' \
--header 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu' \
--data-raw '{
  "allDishPrice": 0,
  "id": 1,
  "name": "string",
  "new": true,
  "score": 0
}'

5) GetbyName

curl --location --request GET 'localhost:8080/rest/admin/restaurants/by?name=KFC' \
--header 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'

6) Get

curl --location --request GET 'localhost:8080/rest/admin/restaurants/1' \
--header 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'

7) GetAll

curl --location --request GET 'localhost:8080/rest/admin/restaurants' \
--header 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'

8) Delete

curl --location --request DELETE 'localhost:8080/rest/admin/restaurants/1' \
--header 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'



**User Controller**
1) GetByMailUser-

curl --location --request GET 'localhost:8080/rest/admin/users/by?email=user%40yandex.ru' \
--header 'Content-Type: application/json' \
--header 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu' \
--data-raw ''

2) Get
curl --location --request GET 'localhost:8080/rest/admin/users/1' \
--header 'Content-Type: application/json' \
--header 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu' \
--data-raw ''

3) CreateWithLocation-

curl --location --request POST 'localhost:8080/rest/admin/users' \
--header 'Content-Type: application/json' \
--header 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu' \
--data-raw '{
  "email": "string@gmail.com",
  "id": null,
  "name": "werr",
  "new": true,
  "password": "string"
}'

4) GetAll

curl --location --request GET 'localhost:8080/rest/admin/users' \
--header 'Content-Type: application/json' \
--header 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu' \
--data-raw ''

5) Update

curl --location --request PUT 'localhost:8080/rest/admin/restaurants' \
--header 'Content-Type: application/json' \
--header 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu' \
--data-raw '{
  "email": "string",
  "enabled": true,
  "id": 3,
  "lastVoteDate": "string",
  "name": "Vova",
  "new": true,
  "password": "string",
  "registered": "string",
  "roles": [
    "ROLE_USER"
  ],
  "voted": true
}'
6) Delete

curl --location --request DELETE 'localhost:8080/rest/admin/users/1' \
--header 'Content-Type: application/json' \
--header 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu' \
--data-raw ''

7) Enable 
curl --location --request PATCH 'localhost:8080/rest/admin/users/1?enabled=true' \
--header 'Content-Type: application/json' \
--header 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu' \
--data-raw ''

**VoteHistoryController**
VoteHistoryController
1) Delete

curl --location --request DELETE 'localhost:8080/rest/admin/history/1' \
--header 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'

2) GetAll

curl --location --request GET 'localhost:8080/rest/admin/history' \
--header 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'

3) FindAllSorted

curl --location --request GET 'localhost:8080/rest/admin/history/sorted' \
--header 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'

4) Get

curl --location --request GET 'localhost:8080/rest/admin/history/1' \
--header 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'

5) FindAllByRestaurantId

curl --location --request GET 'localhost:8080/rest/admin/history/restaurant/1' \
--header 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'

6) FindAllSortedBetween

curl --location --request GET 'localhost:8080/rest/admin/history/sorted/between?endDate=2019-02-05&startDate=2019-02-03' \
--header 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'

7) FindAllSortedByScore

curl --location --request GET 'localhost:8080/rest/admin/history/sorted/15' \
--header 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'
