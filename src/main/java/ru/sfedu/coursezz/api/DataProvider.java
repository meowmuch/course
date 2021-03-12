package ru.sfedu.coursezz.api;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import ru.sfedu.coursezz.models.*;
import ru.sfedu.coursezz.utils.Result;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * The interface Data provider.
 */

public interface DataProvider {

    /**
     * Creates Book Record in DataSource
     * @param list list of records
     * @param append flag which show, do we need to add records to the end of list
     * @return the result
     * @throws Exception
     */
    public Result<Book> createBook(List<Book> list, boolean append) throws Exception;


    /**
     * Creates Film Record in DataSource
     * @param list list of records
     * @param append flag which show, do we need to add records to the end of list
     * @return the result
     * @throws Exception
     */
    public Result<Film> createFilm(List<Film> list, boolean append) throws Exception;


    /**
     * Creates Article Record in DataSource
     * @param list list of records
     * @param append flag which show, do we need to add records to the end of list
     * @return the result
     * @throws Exception
     */
    public Result<Article> createArticle(List<Article> list, boolean append) throws Exception;


    /**
     * Creates Plan Record in DataSource
     * @param list list of records
     * @param append flag which show, do we need to add records to the end of list
     * @return the result
     * @throws Exception
     */
    public Result<Plan> createPlan(List<Plan> list, boolean append) throws Exception;


    /**
     * Creates Client Record in DataSource
     * @param list list of records
     * @param append flag which show, do we need to add records to the end of list
     * @return the result
     * @throws Exception
     */
    public Result<Client> createClient(List<Client> list, boolean append) throws Exception;

    /**
     *
     * Retrieves collection of Book
     * @return the books
     * @throws Exception
     */
    public Result<Book> getBooks() throws Exception;

    /**
     * Retrieves collection of Film
     * @return the films
     * @throws Exception
     */
    public Result<Film> getFilms() throws Exception;

    /**
     * Retrieves collection of Article
     * @return the articles
     * @throws Exception
     */
    public Result<Article> getArticles() throws Exception;

    /**
     * Retrieves collection of Client Plans
     * @param id client id
     * @return the client plans
     * @throws Exception
     */
    Result<Plan> getPlans(Long id) throws Exception;

    /**
     * Retrieves collection of Client
     * @return the clients
     * @throws Exception
     */
    public Result<Client> getClients() throws Exception;

    /**
     * Retrieve Book with given id
     * @param id the id
     * @return the book by id
     * @throws Exception
     */
    public Result<Book> getBookById(Long id) throws Exception;

    /**
     * Retrieve Film with given id
     * @param id the id
     * @return the film by id
     * @throws Exception
     */
    public Result<Film> getFilmById(Long id) throws Exception;

    /**
     * Retrieve Article with given id
     * @param id the id
     * @return the article by id
     * @throws Exception
     */
    public Result<Article> getArticleById(Long id) throws Exception;

    /**
     * Retrieve Plan with given id
     * @param id the id
     * @return the plan by id
     * @throws Exception
     */
    public Result<Plan> getPlanById(Long id) throws Exception;

    /**
     * Retrieve Client with given id
     * @param id the id
     * @return the client by id
     * @throws Exception
     */
    public Result<Client> getClientById(Long id) throws Exception;

    /**
     * Delete Book result.
     * @param id the id
     * @return the result
     * @throws Exception
     */
    public Result<Book> deleteBook(Long id) throws Exception;

    /**
     * Delete Film result.
     * @param id the id
     * @return the result
     * @throws Exception
     */
    public Result<Film> deleteFilm(Long id) throws Exception;

    /**
     * Delete Article result.
     * @param id the id
     * @return the result
     * @throws Exception
     */
    public Result<Article> deleteArticle(Long id) throws Exception;

    /**
     * Delete Plan result.
     * @param id the id
     * @return the result
     * @throws Exception
     */
    public Result<Plan> deletePlan(Long id) throws Exception;

    /**
     * Delete Client result.
     * @param id the id
     * @return the result
     * @throws Exception
     */
    public Result<Client> deleteClient(Long id) throws Exception;

    /**
     * Updates Book Record
     * @param id the id
     * @param newBook new record
     * @return the result
     * @throws Exception
     */
    public Result<Book> updateBook(Long id, Book newBook) throws Exception;

    /**
     * Updates Film Record
     * @param id the id
     * @param newFilm new record
     * @return the result
     * @throws Exception
     */
    public Result<Film> updateFilm(Long id, Film newFilm) throws Exception;

    /**
     * Updates Article Record
     * @param id the id
     * @param newArticle new record
     * @return the result
     * @throws Exception
     */
    public Result<Article> updateArticle(Long id, Article newArticle) throws Exception;

    /**
     * Updates Plan Record
     * @param id the id
     * @param newPlan new record
     * @return the result
     * @throws Exception
     */
    public Result<Plan> updatePlan(Long id, Plan newPlan) throws Exception;

    /**
     * Updates Client Record
     * @param id the id
     * @param newClient new record
     * @return the result
     * @throws Exception
     */
    public Result<Client> updateClient(Long id, Client newClient) throws Exception;


    /**
     *  Change Plan status
     * @param id Plan id which you want to change status
     * @param status new status
     * @return the result
     * @throws IOException
     */
    public Result<Plan> changePlanStatus(Long id, boolean status) throws IOException, NoSuchMethodException, InvocationTargetException, CsvRequiredFieldEmptyException, InstantiationException, IllegalAccessException, CsvDataTypeMismatchException, Exception;


    /**
     * Calculate reading time for Plan
     * @param id Plan id which you want to calculate time
     * @return the result
     * @throws Exception
     */
    Result<Double> calculateTimePlan(Long id) throws Exception;

    /**
     * Generic method which inserts records of any class
     * @param listRecord list of records
     * @param append flag which show, do we need to add records to the end of list
     * @param cn Class Name
     * @param <T> Generic type (any Class or type)
     * @return the result
     * @throws Exception
     */
    public <T> Result<T> insertRecord(List <T> listRecord, boolean append, Class<T> cn) throws Exception;

    /**
     * Generic method which gives you records from any DataSource
     * @param cn Class Name
     * @param <T> Generic Type (any Class or type)
     * @return the result
     * @throws Exception
     */
    public <T> Result<T> getRecords (Class cn) throws Exception;

    /**
     * Generic method which gives you record from any DataSource with given id
     * @param id the id
     * @param cn Class Name
     * @param <T> Generic Type (any Class or type)
     * @return the result
     * @throws Exception
     */
    public <T> Result getRecordById(Long id, Class<T> cn) throws Exception;

    /**
     * Generic method which delete record from any DataSource
     * @param id the id
     * @param cn Class Name
     * @param <T> Generic Type (any Class or type)
     * @return the result
     * @throws Exception
     */
    public <T> Result deleteRecord(Long id, Class<T> cn) throws Exception;

    /**
     * Generic Method which updates any record from any DataSource
     * @param id the id
     * @param record new record
     * @param <T>  Generic Type (any Class or type)
     * @return the result
     * @throws Exception
     */
    public <T> Result updateRecord(Long id, T record) throws Exception;
}