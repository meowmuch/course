package ru.sfedu.coursezz.utils.Generatos;

import ru.sfedu.coursezz.api.DataProviderJdbc;
import ru.sfedu.coursezz.models.*;
import ru.sfedu.coursezz.utils.Data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class GeneratorJdbc {

    public static DataProviderJdbc instance;


    public static List<Book> generateBooks(int count) {
        List<Book> bookList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Book book = new Book();
            book.setId(generateLong());
            book.setBookAuthorName(Data.bookAuthorName[i]);
            book.setNameOfBook(Data.nameOfBook[i]);
            book.setLink(Data.link[i]);
            book.setReview(Data.review[i]);
            book.setGenre(Data.genre[i]);
            book.setTimeReading(Data.timeReading[i]);
            bookList.add(book);
        }
        return bookList;
    }

    public static List<Film> generateFilms(int count) {
        List<Film> filmList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Film film = new Film();
            film.setId(generateLong());
            film.setBookAuthorName(Data.bookAuthorName[i]);
            film.setNameOfBook(Data.nameOfBook[i]);
            film.setCountry(Data.country[i]);
            film.setYear(Data.year[i]);
            film.setProducer(Data.producer[i]);
            film.setFormat(Data.format[i]);
            filmList.add(film);
        }
        return filmList;
    }

    public static List<Article> generateArticles(int count) {
        List<Article> articleList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Article article = new Article();
            article.setId(generateLong());
            article.setBookAuthorName(Data.bookAuthorName[i]);
            article.setNameOfBook(Data.nameOfBook[i]);
            article.setTitle(Data.title[i]);
            article.setContent(Data.content[i]);
            articleList.add(article);
        }
        return articleList;
    }

    public static List<Client> generateClients(int count, int planCount) {
        List<Client> clientList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Client client = new Client();
            client.setId(generateLong());
            client.setName(Data.name[i]);
            client.setLogin(Data.login[i]);
            client.setPassword(Data.password[i]);
            client.setEmail(Data.email[i]);
            client.setPlanList(generatePlans(planCount));
            clientList.add(client);
        }
        return clientList;
    }


    public static List<Plan> generatePlans(int count) {
        List<Plan> planList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Plan plan = new Plan();
            plan.setId(generateLong());
            plan.setName(Data.planName[i]);
            Book book = generateBooks(1).get(0);
            plan.setBook(book);
            plan.setStartDay(Data.startDay[i]);
            plan.setLastDay(Data.lastDay[i]);
            plan.setStatus(generateBoolean());
            planList.add(plan);
        }
        return planList;

    }



    private static int generateInt() {
        return new Random().nextInt(10000000);
    }

    private static Double generateDouble() {
        return Double.valueOf(generateInt());
    }

    public static Long generateLong() {
        return Long.valueOf(generateInt());
    }

    private static boolean generateBoolean() {
        return new Random().nextInt(100) % 2 == 1;
    }


}