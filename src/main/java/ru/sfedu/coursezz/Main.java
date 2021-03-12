package ru.sfedu.coursezz;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.sfedu.coursezz.api.*;
import ru.sfedu.coursezz.models.*;
import ru.sfedu.coursezz.models.enums.ResultType;
import ru.sfedu.coursezz.utils.Constants;
import ru.sfedu.coursezz.utils.Result;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * The type Main.
 */
public class Main {
    public static final Logger log = LogManager.getLogger(Main.class);


    public static void main(String[] args) throws Exception {
        checkArgsCount(args);
        DataProvider dp = getDataProvider(args[0]);
        switch(args[1].toUpperCase(Locale.ROOT)) {
            case Constants.CREATE_BOOK: {
                Book book = new Book();
                Boolean append = Boolean.valueOf(args[2]);
                book.setId(Long.valueOf(args[3]));
                book.setBookAuthorName(args[4]);
                book.setNameOfBook(args[5]);
                book.setLink(args[6]);
                book.setReview(args[7]);
                book.setGenre(args[8]);
                book.setTimeReading(Long.valueOf(args[9]));
                List<Book> list = new ArrayList<>();
                list.add(book);
                Result<Book> res = dp.createBook(list, append);
                log.info(res.getMessage());
                log.info(res.getData());
                break;
            }
            case Constants.CREATE_FILM: {
                Film film = new Film();
                Boolean append = Boolean.valueOf(args[2]);
                film.setId(Long.valueOf(args[3]));
                film.setBookAuthorName(args[4]);
                film.setNameOfBook(args[5]);
                film.setCountry(args[6]);
                film.setYear(args[7]);
                film.setProducer(args[8]);
                film.setFormat(args[9]);
                List<Film> list = new ArrayList<>();
                list.add(film);
                Result<Film> res = dp.createFilm(list, append);
                log.info(res.getMessage());
                log.info(res.getData());
                break;
            }
            case Constants.CREATE_ARTICLE: {
                Article article = new Article();
                Boolean append = Boolean.valueOf(args[2]);
                article.setId(Long.valueOf(args[3]));
                article.setBookAuthorName(args[4]);
                article.setNameOfBook(args[5]);
                article.setTitle(args[6]);
                article.setContent(args[7]);
                List<Article> list = new ArrayList<>();
                list.add(article);
                Result<Article> res = dp.createArticle(list, append);
                log.info(res.getMessage());
                log.info(res.getData());
                break;
            }
            case Constants.CREATE_PLAN: {
                Plan plan = new Plan();
                Boolean append = Boolean.valueOf(args[2]);
                plan.setId(Long.valueOf(args[3]));
                plan.setName(args[4]);
                plan.setBook(dp.getBookById(Long.parseLong(args[5])).getData().get(0));
                plan.setStartDay(String.valueOf(args[6]));
                plan.setLastDay(String.valueOf(args[7]));
                plan.setStatus(Boolean.valueOf(args[8]));
                List<Plan> planList = new ArrayList<>();
                Result<Plan> res = dp.createPlan(planList, append);
                log.info(res.getMessage());
                log.info(res.getData());
                break;
            }
            case Constants.CREATE_CLIENT: {
                Client client = new Client();
                Boolean append = Boolean.valueOf(args[2]);
                client.setId(Long.valueOf(args[3]));
                client.setName(args[4]);
                client.setLogin(args[5]);
                client.setPassword(args[6]);
                client.setEmail(args[7]);
                List<Plan> planList = new ArrayList<>();
                if (args.length > 8) {
                    for (String arg : Arrays.copyOfRange(args, 8, args.length)) {
                        Result<Plan> res = dp.getPlanById(Long.parseLong(arg));
                        if (res.getResultType() == ResultType.COMPLETE) {
                            planList.addAll(res.getData());
                        }
                    }
                }
                client.setPlanList(planList);
                List<Client> clientList = new ArrayList<>();
                clientList.add(client);
                Result<Client> res = dp.createClient(clientList, append);
                log.info(res.getMessage());
                log.info(res.getData());
                break;
            }

            case Constants.GET_BOOKS: {
                Result<Book> res = dp.getBooks();
                log.info(res.getMessage());
                log.info(res.getData());
                break;
            }
            case Constants.GET_FILMS: {
                Result<Film> res = dp.getFilms();
                log.info(res.getMessage());
                log.info(res.getData());
                break;
            }
            case Constants.GET_ARTICLES: {
                Result<Article> res = dp.getArticles();
                log.info(res.getMessage());
                log.info(res.getData());
                break;
            }
            case Constants.GET_PLANS: {
                Result<Plan> res = dp.getPlans(Long.valueOf(args[2]));
                log.info(res.getMessage());
                log.info(res.getData());
                break;
            }
            case Constants.GET_CLIENTS: {
                Result<Client> res = dp.getClients();
                log.info(res.getMessage());
                log.info(res.getData());
                break;
            }
            case Constants.GET_BOOK_BY_ID: {
                Result<Book> res = dp.getBookById(Long.valueOf(args[2]));
                log.info(res.getMessage());
                log.info(res.getData());
                break;
            }
            case Constants.GET_FILM_BY_ID: {
                Result<Film> res = dp.getFilmById(Long.valueOf(args[2]));
                log.info(res.getMessage());
                log.info(res.getData());
                break;
            }
            case Constants.GET_ARTICLE_BY_ID: {
                Result<Article> res = dp.getArticleById(Long.valueOf(args[2]));
                log.info(res.getMessage());
                log.info(res.getData());

                break;
            }
            case Constants.GET_PLAN_BY_ID: {
                Result<Plan> res = dp.getPlanById(Long.valueOf(args[2]));
                log.info(res.getMessage());
                log.info(res.getData());

                break;
            }
            case Constants.GET_CLIENT_BY_ID: {
                Result<Client> res = dp.getClientById(Long.valueOf(args[2]));
                log.info(res.getMessage());
                log.info(res.getData());
                break;
            }
            case Constants.DELETE_BOOK: {
                Result<Book> res = dp.deleteBook(Long.valueOf(args[2]));
                log.info(res.getMessage());
                log.info(res.getData());
                break;
            }
            case Constants.DELETE_ARTICLE: {
                Result<Article> res = dp.deleteArticle(Long.valueOf(args[2]));
                log.info(res.getMessage());
                log.info(res.getData());
                break;
            }
            case Constants.DELETE_FILM: {
                Result<Film> res = dp.deleteFilm(Long.valueOf(args[2]));
                log.info(res.getMessage());
                log.info(res.getData());
                break;
            }
            case Constants.DELETE_PLAN: {
                Result<Plan> res = dp.deletePlan(Long.valueOf(args[2]));
                log.info(res.getMessage());
                log.info(res.getData());
                break;
            }
            case Constants.DELETE_CLIENT: {
                Result<Client> res = dp.deleteClient(Long.valueOf(args[2]));
                log.info(res.getMessage());
                log.info(res.getData());
                break;
            }
            case Constants.UPDATE_BOOK: {
                Book book = new Book();
                book.setId(Long.valueOf(args[1]));
                book.setBookAuthorName(args[2]);
                book.setNameOfBook(args[3]);
                book.setLink(args[4]);
                book.setReview(args[5]);
                book.setGenre(args[6]);
                book.setTimeReading(Long.valueOf(args[7]));
                Result<Book> res = dp.updateBook(book.getId(), book);
                log.info(res.getMessage());
                log.info(res.getData());
                break;
            }
            case Constants.UPDATE_FILM: {
                Film film = new Film();
                film.setId(Long.valueOf(args[2]));
                film.setBookAuthorName(args[3]);
                film.setNameOfBook(args[4]);
                film.setCountry(args[5]);
                film.setYear(args[6]);
                film.setProducer(args[7]);
                film.setFormat(args[8]);
                Result<Film> res = dp.updateFilm(film.getId(), film);
                log.info(res.getMessage());
                log.info(res.getData());
                break;
            }
            case Constants.UPDATE_ARTICLE: {
                Article article = new Article();
                article.setId(Long.valueOf(args[2]));
                article.setBookAuthorName(args[3]);
                article.setNameOfBook(args[4]);
                article.setTitle(args[5]);
                article.setContent(args[6]);
                Result<Article> res = dp.updateArticle(article.getId(), article);
                log.info(res.getMessage());
                log.info(res.getData());
                break;
            }
            case Constants.UPDATE_PLAN: {
                Plan plan = new Plan();
                plan.setId(Long.valueOf(args[2]));
                plan.setName(args[3]);
                plan.setBook(dp.getBookById(Long.parseLong(args[4])).getData().get(0));
                plan.setStartDay(String.valueOf(args[5]));
                plan.setLastDay(String.valueOf(args[6]));
                plan.setStatus(Boolean.valueOf(args[7]));
                Result<Plan> res = dp.updatePlan(plan.getId(), plan);
                log.info(res.getMessage());
                log.info(res.getData());
                break;
            }
            case Constants.UPDATE_CLIENT: {
                Client client = new Client();
                client.setId(Long.valueOf(args[2]));
                client.setName(args[3]);
                client.setLogin(args[4]);
                client.setPassword(args[5]);
                client.setEmail(args[6]);
                List<Plan> planList = new ArrayList<>();
                if (args.length > 7) {
                    for (String arg : Arrays.copyOfRange(args, 8, args.length)) {
                        Result<Plan> res = dp.getPlanById(Long.parseLong(arg));
                        if (res.getResultType() == ResultType.COMPLETE) {
                            planList.addAll(res.getData());
                        }
                    }
                }
                client.setPlanList(planList);
                Result<Client> res = dp.updateClient(client.getId(), client);
                log.info(res.getMessage());
                log.info(res.getData());
                break;
            }
            case Constants.CHANGE_PLAN_STATUS: {
                Long id = Long.valueOf(args[2]);
                Boolean status = Boolean.valueOf(args[3]);
                Result<?> res = dp.changePlanStatus(id, status);
                log.info(res.getMessage());
                log.info(res.getData());
                break;
            }
            case Constants.CALCULATE_TIME_PLAN: {
                Long id = Long.valueOf(args[2]);
                Result<?> res = dp.calculateTimePlan(id);
                log.info(res.getMessage());
                log.info(res.getData());
                break;
            }
            default: {
                log.error(Constants.INVALID_METHOD_NAME);
                System.exit(0);
            }
        }

    }


    private static DataProvider getDataProvider(String dpType) throws IOException {
        switch(dpType.toUpperCase(Locale.ROOT)) {
            case(Constants.CSV): {
                return new DataProviderCsv();
            }
            case(Constants.XML): {
                return new DataProviderXML();
            }
            case(Constants.DB): {
                return new DataProviderJdbc();
            }
            default: {
                log.error(Constants.INVALID_DATA_PROVIDER);
                System.exit(0);
            }
        }
        return null;
    }

    private static void checkArgsCount(String[] args) {
        if (args.length < 2) {
            log.error(Constants.FEW_ARGUMENTS);
            System.exit(0);
        }
    }
}

