package ru.sfedu.coursezz.api;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import ru.sfedu.coursezz.utils.Constants;
import ru.sfedu.coursezz.models.*;
import ru.sfedu.coursezz.models.enums.ResultType;
import ru.sfedu.coursezz.utils.*;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DataProviderXML implements DataProvider{

    private final String PATH = ConfigurationUtil.getConfigurationEntry(Constants.PATH_TO_XML);

    private final String FILE_EXTENSION = Constants.XML_FILE_EXTENSION;

    private Logger log = LogManager.getLogger(DataProviderXML.class);

    public DataProviderXML() throws IOException {
    }


    public void initDataSource(String path) throws IOException {
        File file = new File(path);
        if (!file.exists()) {
            Path dirPath = Paths.get(PATH);
            Files.createDirectories(dirPath);
            file.createNewFile();
        }
    }

    @Override
    public Result<Book> createBook(List<Book> list, boolean append) throws Exception {
        return insertRecord(list, append, Book.class);
    }

    @Override
    public Result<Film> createFilm(List<Film> list, boolean append) throws Exception {
        return insertRecord(list, append, Film.class);
    }

    @Override
    public Result<Article> createArticle(List<Article> list, boolean append) throws Exception {
        return insertRecord(list, append, Article.class);
    }

    @Override
    public Result<Plan> createPlan(List<Plan> list, boolean append) throws Exception {
        return insertRecord(list, append, Plan.class);
    }

    @Override
    public Result<Client> createClient(List<Client> list, boolean append) throws Exception {
        return insertRecord(list, append, Client.class);
    }

    @Override
    public Result<Book> getBooks() throws Exception {
        return getRecords(Book.class);
    }

    @Override
    public Result<Film> getFilms() throws Exception {
        return getRecords(Film.class);
    }

    @Override
    public Result<Article> getArticles() throws Exception {
        return getRecords(Article.class);
    }

    @Override
    public Result<Plan> getPlans(Long id) throws Exception {
        Result<Client> res = getClientById(id);
        Client client = res.getData().get(0);
        List plan = client.getPlanList();
        return new Result<>(plan, ResultType.COMPLETE, Constants.GET_PLANS);
    }


    @Override
    public Result<Client> getClients() throws Exception {
        return getRecords(Client.class);
    }

    @Override
    public Result<Book> getBookById(Long id) throws Exception {
        return getRecordById(id, Book.class);
    }

    @Override
    public Result<Film> getFilmById(Long id) throws Exception {
        return getRecordById(id, Film.class);
    }

    @Override
    public Result<Article> getArticleById(Long id) throws Exception {
        return getRecordById(id, Article.class);
    }

    @Override
    public Result<Plan> getPlanById(Long id) throws Exception {
        return getRecordById(id, Plan.class);
    }

    @Override
    public Result<Client> getClientById(Long id) throws Exception {
        return getRecordById(id, Client.class);
    }

    @Override
    public Result<Book> deleteBook(Long id) throws Exception {
        return deleteRecord(id, Book.class);
    }

    @Override
    public Result<Film> deleteFilm(Long id) throws Exception {
        return deleteRecord(id, Film.class);
    }

    @Override
    public Result<Article> deleteArticle(Long id) throws Exception {
        return deleteRecord(id, Article.class);
    }

    @Override
    public Result<Plan> deletePlan(Long id) throws Exception {
        return deleteRecord(id, Plan.class);
    }

    @Override
    public Result<Client> deleteClient(Long id) throws Exception {
        return deleteRecord(id, Client.class);
    }

    @Override
    public Result<Book> updateBook(Long id, Book book) throws Exception {
        return updateRecord(id, book);
    }

    @Override
    public Result<Film> updateFilm(Long id, Film film) throws Exception {
        return updateRecord(id, film);
    }

    @Override
    public Result<Article> updateArticle(Long id, Article article) throws Exception {
        return updateRecord(id, article);
    }

    @Override
    public Result<Plan> updatePlan(Long id, Plan plan) throws Exception {
        return updateRecord(id, plan);
    }

    @Override
    public Result<Client> updateClient(Long id, Client client) throws Exception {
        return updateRecord(id, client);
    }

    @Override
    public Result<Plan> changePlanStatus(Long id, boolean status) throws Exception {
        Result<Plan> res = getPlanById(id);
        if (res.getResultType() == ResultType.FAIL) {
            return res;
        }
        Plan plan = res.getData().get(0);
        plan.setStatus(status);

        return new Result(Collections.singletonList(plan), ResultType.COMPLETE, Constants.STATUS_CHANGED);
    }


    @Override
    public Result<Double> calculateTimePlan(Long id) throws Exception {
        try {
            Result<Plan> res = getPlanById(id);
            Plan plan = res.getData().get(0);
            Long plan1 = plan.getBook().getTimeReading();
            SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT, Locale.ENGLISH);
            Date firstDate = sdf.parse(String.valueOf(plan.getStartDay()));
            log.debug(firstDate);
            Date secondDate = sdf.parse(String.valueOf(plan.getLastDay()));
            log.debug(secondDate);
            Long diffInMillies = Math.abs(secondDate.getTime() - firstDate.getTime());
            log.debug(diffInMillies);
            Long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
            log.debug(diff);
            Double readTime = (double) plan1 / (double) diff;
            log.debug(readTime);
            log.info(Constants.PLAN_TIME);
            return new Result<>(readTime, ResultType.COMPLETE, Constants.PLAN_TIME);
        } catch (ParseException e) {
            log.error(e);
            return new Result<>(null, ResultType.FAIL, Constants.PLAN_TIME);
        }
    }

    /**
     * return path to csv file
     * @param cn used for filename
     * @return path
     */
    public String getPath(Class cn) throws IOException {
        return PATH + cn.getSimpleName().toLowerCase() + ConfigurationUtil.getConfigurationEntry(FILE_EXTENSION);
    }

    @Override
    public <T> Result<T> insertRecord(List<T> listRecord, boolean append, Class<T> cn) throws Exception {
        if (append) {
            Result<T> res = get(cn);
            List<T> oldRecords = res.getData();
            if (res.getResultType() == ResultType.FAIL) {
                oldRecords = new ArrayList<>();
            }
            if (hasDuplicates(listRecord, oldRecords)) {
                return new Result<T>((T) null, ResultType.FAIL, Constants.ALREADY_EXIST);
            }
            listRecord = Stream
                    .concat(oldRecords.stream(), listRecord.stream())
                    .collect(Collectors.toList());
        }
        XMLWrapper<T> wrapper = new XMLWrapper<>(listRecord);
        
        log.info(Constants.LOG_RECORDS + wrapper.getList().toString());
        return add(wrapper, cn);
    }

    @Override
    public <T> Result<T> getRecords(Class cn) {
        Result<T> res = get(cn);
        if (res.getResultType() == ResultType.FAIL) {
            return res;
        } else if (res.getData().size() == 0) {
            return new Result<T>(res.getData(), ResultType.FAIL, Constants.LIST_EMPTY);
        }
        return res;
    }

    @Override
    public <T> Result<T> getRecordById(Long id, Class<T> cn) {
        Result res = getRecords(cn);
        if (res.getResultType() == ResultType.FAIL) {
            log.info(res.getMessage());
            return res;
        }
        log.info(res.getData());
        return getById(res.getData(), id, cn);
    }

    @Override
    public <T> Result<T> deleteRecord(Long id, Class<T> cn) throws Exception {
        Result<T> res;
        res = getRecordById(id, cn);
        if (res.getResultType() == ResultType.FAIL) {
            return res;
        }
        T recordToDelete = res.getData().get(0);
        res = getRecords(cn);
        if (res.getResultType() == ResultType.FAIL) {
            return res;
        }
        List<T> list = res.getData();
        list.remove(recordToDelete);
        res = insertRecord(res.getData(), false, cn);
        if (res.getResultType() == ResultType.FAIL) {
            return res;
        }
        return new Result<>(res.getData(), ResultType.COMPLETE, Constants.DELETED_SUCCESSFULLY);
    }

    @Override
    public <T> Result<T> updateRecord(Long id, T record) throws Exception {
        Class<T> cn = (Class<T>) record.getClass();
        Result<T> res = this.deleteRecord(id, cn);
        if (res.getResultType() == ResultType.FAIL) {
            return res;
        }
        List<T> list = res.getData();
        list.add(record);
        return insertRecord(list, false, cn);
    }

    /**
     * Get by id
     * @param records records
     * @param id the id
     * @param cn Class Name
     * @param <T> Generic Type
     * @return the result
     */
    private <T> Result<T> getById(List<T> records, Long id, Class<T> cn) {
        try {
            Method getter = cn.getMethod(Constants.METHOD_GET_ID);


            for (T element : records) {
                Long recordId = (Long) getter.invoke(element);
                if (recordId.equals(id)) {
                    return new Result<>(Collections.singletonList(element), ResultType.COMPLETE, Constants.FOUND_ELEMENT);
                }
            }
            return new Result<>(null, ResultType.FAIL, Constants.NOT_FOUND);


        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            log.error(e);
            return new Result<>(null, ResultType.FAIL, Constants.NO_METHOD);
        }
    }

    /**
     * Duplicates
     * @param newRecords new Records
     * @param oldRecords old Records
     * @param <T> Generic Type
     * @return the result
     * @throws IllegalAccessException
     */
    private <T> Boolean hasDuplicates(List<T> newRecords, List<T> oldRecords) throws IllegalAccessException {
        Class<?> cn = newRecords.get(0).getClass();
        log.debug(cn);
        try {
            Method idGetter = cn.getMethod(Constants.METHOD_GET_ID);
            List<Long> newIds = new ArrayList<>();
            for (T el: newRecords) {
                newIds.add((Long) idGetter.invoke(el));
            }
            List<Long> oldIds = new ArrayList<>();
            log.debug(oldRecords.toString());
            for (T el: oldRecords) {
                oldIds.add((Long) idGetter.invoke(el));
            }
            return oldIds
                    .stream()
                    .anyMatch(newIds::contains);
        }
        catch (NoSuchMethodException | InvocationTargetException e) {
            log.error(e);
            log.info(Constants.NO_METHOD);
            return false;
        }
    }

    /**
     * Generic method which only inserting records
     * @param wrapper wrapper
     * @param cn Class Name
     * @param <T> Generic Type
     * @return the result
     * @throws SQLException if file already in use
     */
    private <T> Result<T> add(XMLWrapper<T> wrapper, Class<?> cn) {
        try {
            String path = getPath(cn);
            initDataSource(path);
            FileWriter fw = new FileWriter(path);
            Serializer serializer = new Persister();
            log.debug(Constants.LOG_WRITER_INIT);
            serializer.write(wrapper, fw);
            fw.close();
            return new Result<T>(wrapper.getList(), ResultType.COMPLETE, Constants.INSERTED_SUCCESSFULLY);
        } catch (Exception e) {
            log.error(e);
            return new Result<T>((T) null, ResultType.FAIL, Constants.BAD_FILE);
        }
    }


    /**
     * Generic method which only read from DataSource
     * @param cn Class Name
     * @param f Datasource reader
     * @param <T> Generic Type
     * @return the result
     * @throws IOException
     */
    private <T> Result<T> get(Class<T> cn) {
        try {
            String path = getPath(cn);
            initDataSource(path);
            FileReader reader = new FileReader(path);
            Serializer serializer = new Persister();
            log.debug(Constants.LOG_READER_INIT);
            XMLWrapper<T> result = serializer.read(XMLWrapper.class, reader);
            reader.close();
            return new Result<>(result.getList(), ResultType.COMPLETE, Constants.RECORDS_FOUND);
        }
        catch (Exception e) {
            log.error(e);
            return new Result<>(null, ResultType.FAIL, Constants.BAD_FILE);
        }
    }
}