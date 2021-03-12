Параметр для environment.properties - -DenvironmentPath
Параметр для логгера - log4j2.configurationFile

Аргументы для добавления записей:

createArticle - Создание Статьи Bool append, Long id, String bookAuthorName, String nameOfBook, String title, String content
createBook - Создание Книги Bool append, Long id, String bookAuthorName, String nameOfBook, String link, String review, String genre, Long timeReading
createFilm - Создание Адаптации Bool append, Long id, String bookAuthorName, String nameOfBook, String country, String year, String producer, String format
createPlan - Создание Плана Bool append, Long id, String name, Long bookId, String startDay, String lastDay, Bool status
createClient - Создание Клиента Bool append, Long id, String name, String login, String password, String email, Long planIds (Может быть несколько)



Получение записей:

getArticles
getBooks
getFilms
getPlans
getClients

Обновление записей

updateArticle - Обнолвение Статьи Long id, String bookAuthorName, String nameOfBook, String title, String content
updateBook - Обновление Книги Long id, String bookAuthorName, String nameOfBook, String link, String review, String genre, Long timeReading
updateFilm - Обновление Адаптации Long id, String bookAuthorName, String nameOfBook, String country, String year, String producer, String format
updatePlan - Обновление Плана Long id, String name, Long bookId, String startDay, String lastDay, Bool status
updateClient - Обновление Клиента Long id, String name, String login, String password, String email, Long planIds (Может быть несколько)


Удаление записей

Необходимо указать только Long id

deleteAdmin
deleteManager
deleteSpeaker
deleteChannel
deleteZone
deleteEvent

Изменение статуса плана

changePlanStatus Long id, Boolean Status

Расчет времени чтения в день
calculateTimePlan Long id


Csv
XML
DB

Примеры запуска:
java -jar -DenvironmentPath=./environment.properties -Dlog4j2.configurationFile=./log4j2.properties ./coursezz.jar db getArticles
java -jar -DenvironmentPath=./environment.properties -Dlog4j2.configurationFile=./log4j2.properties ./coursezz.jar db createArticle true 8 "Author" "Book" "Title" "Content"
java -jar -DenvironmentPath=./environment.properties -Dlog4j2.configurationFile=./log4j2.properties ./coursezz.jar db deleteArticle 8
java -jar -DenvironmentPath=./environment.properties -Dlog4j2.configurationFile=./log4j2.properties ./coursezz.jar db updateArticle 8 "Author New" "Book" "Title New" "Content"
