package ru.sfedu.coursezz.api;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.sfedu.coursezz.utils.Constants;
import ru.sfedu.coursezz.models.*;
import ru.sfedu.coursezz.models.enums.ResultType;
import ru.sfedu.coursezz.utils.ConfigurationUtil;
import ru.sfedu.coursezz.utils.Result;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DataProviderJdbc implements DataProvider {

    private final String DB_USER = ConfigurationUtil.getConfigurationEntry(Constants.DB_USER);
    private final String DB_PASSWORD = ConfigurationUtil.getConfigurationEntry(Constants.DB_PASSWORD);

    private final String DB_PROTOCOL = ConfigurationUtil.getConfigurationEntry(Constants.DB_PROTOCOL);
    private final String PATH_TO_DB = ConfigurationUtil.getConfigurationEntry(Constants.PATH_TO_DB);
    private final String DB_NAME = ConfigurationUtil.getConfigurationEntry(Constants.DB_NAME);

    private final String PATH_TO_SCHEMA = ConfigurationUtil.getConfigurationEntry(Constants.PATH_TO_SQL_SCHEMA);

    private Connection connection;
    private Statement statement;

    private final Logger log = LogManager.getLogger(DataProviderJdbc.class);

    public DataProviderJdbc() throws IOException {}


    private void initConnection() throws SQLException {
        connection = DriverManager.getConnection(DB_PROTOCOL + PATH_TO_DB + DB_NAME, DB_USER, DB_PASSWORD);
        statement = connection.createStatement();
    }


    private void initDataSource() throws SQLException {
        log.debug(Constants.LOG_CREATING_FILE);
        File file = new File(PATH_TO_SCHEMA);
        if (file.exists()) {
            try {
                FileInputStream fis = new FileInputStream(file);
                byte[] data = new byte[(int) file.length()];
                fis.read(data);
                fis.close();
                String query = new String(data, StandardCharsets.UTF_8);
                initConnection();
                log.debug(query);
                statement.executeUpdate(query);
            } catch (IOException e) {
                log.error(e);
            } catch (SQLException e) {
                log.error(e);
            }
        }
        initConnection();
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
        Result<Plan> res = insertRecord(list, append, Plan.class);
        if (res.getResultType() == ResultType.FAIL) {
            return res;
        }
        for (Plan plan : list) {
            insertRecord(Collections.singletonList(plan.getBook()), true, Book.class);
            setParent(Plan.class, Book.class, plan.getBook().getId(), plan.getId());
        }
        return res;
    }

    @Override
    public Result<Client> createClient(List<Client> list, boolean append) throws Exception {
        Result<Client> res = insertRecord(list, append, Client.class);
        if (res.getResultType() == ResultType.FAIL) {
            return res;
        }
        for (Client client : list) {
            List<Plan> planList = client.getPlanList();
            insertRecord(planList, true, Plan.class);
            List<Long> planIds = getIds(planList);
            for (Long id : planIds) {
                setParent( Plan.class, Client.class, id, client.getId());
            }
        }
        return res;
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
    public Result<Client> getClients() throws Exception {
        Result<Client> res = getRecords(Client.class);
        if (res.getResultType() == ResultType.FAIL) {
            return res;
        }
        List<Client> clientList = res.getData();
        clientList.stream().forEach(client -> {
            List<Plan> planList = new ArrayList<>();
            Result<Plan> planRes = getByParent(client.getId(), Plan.class, Client.class);
            if (planRes.getResultType() == ResultType.COMPLETE) {
                planList = planRes.getData();
            }
            client.setPlanList(planList);
        });
        return res;
    }

    @Override
    public Result<Plan> getPlans( Long id) throws Exception {
        Result<Client> res = getClientById(id);
        Client client = res.getData().get(0);
        List plan = client.getPlanList();
        return new Result<>(plan, ResultType.COMPLETE, Constants.GET_PLANS);
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
    public Result<Client> deleteClient(Long id) throws Exception {
        return deleteRecord(id, Client.class);
    }

    @Override
    public Result<Plan> deletePlan(Long id) throws Exception {
        return deleteRecord(id, Plan.class);
    }

    @Override
    public Result<Book> getBookById(Long id) {
        return getRecordById(id, Book.class);
    }

    @Override
    public Result<Film> getFilmById(Long id) {
        return getRecordById(id, Film.class);
    }

    @Override
    public Result<Article> getArticleById(Long id) {
        return getRecordById(id, Article.class);
    }

    @Override
    public Result<Client> getClientById(Long id) {
        return getRecordById(id, Client.class);
    }

    @Override
    public Result<Plan> getPlanById(Long id) {
        return getRecordById(id, Plan.class);
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
    public Result<Client> updateClient(Long id, Client client) throws Exception {
        return updateRecord(id, client);
    }

    @Override
    public Result<Plan> updatePlan(Long id, Plan plan) throws Exception {
        return updateRecord(id, plan);
    }

    @Override
    public Result<Plan> changePlanStatus(Long id, boolean status) throws Exception {
        try {
            initDataSource();
            Result<Plan> res = getRecordById(id, Plan.class);
            if (res.getResultType() == ResultType.FAIL) {
                return res;
            }
            log.info(res.getData());
            statement.executeUpdate(String.format(Constants.SQL_CHANGE_STATUS, Plan.class.getSimpleName(), status, id));
            close();
            return new Result<>(null, ResultType.COMPLETE, Constants.STATUS_CHANGED);
        }
        catch (SQLException e) {
            log.error(e);
            return new Result<>(null, ResultType.FAIL, Constants.SQL_FAIL);
        }
    }

    @Override
    public Result<Double> calculateTimePlan(Long id) throws Exception {
        try {
            Result<Plan> res = getPlanById(id);
            Plan plan = res.getData().get(0);
            Long plan1 = plan.getBook().getId();
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



    @Override
    public <T> Result<T> insertRecord(List<T> listRecord, boolean append, Class<T> cn) throws Exception {
        initDataSource();
        if (!append) {
            flushTable(cn);
        }
        return add(listRecord, cn);
    }

    @Override
    public <T> Result<T> getRecords(Class cn) throws Exception {
        initDataSource();
        Result<T> res = get(cn);
        if (res.getResultType() == ResultType.FAIL) {
            return res;
        }
        List<T> listRecords = res.getData();
        if (listRecords.size() == 0) {
            return new Result<>(listRecords, ResultType.FAIL, Constants.LIST_EMPTY);
        }
        return res;
    }

    @Override
    public <T> Result<T> getRecordById(Long id, Class<T> cn) {
        try {
            initDataSource();
            ResultSet rs = statement.executeQuery(String.format(Constants.DB_BY_ID, cn.getSimpleName(), id));
            List<T> res = getDataFormResultSet(rs, cn);
            if (res.size() > 0) {
                return new Result<>(res, ResultType.COMPLETE, Constants.FOUND_ELEMENT);
            }
            return new Result<>(null, ResultType.FAIL, Constants.NOT_FOUND);
        } catch (Exception e) {
            log.error(e);
            return new Result<>(null, ResultType.FAIL, Constants.SQL_FAIL);
        }
    }

    @Override
    public <T> Result<T> deleteRecord(Long id, Class<T> cn) throws Exception {
        initDataSource();
        int rowCount = statement.executeUpdate(
                String.format(
                        Constants.DELETE_BY_ID,
                        cn.getSimpleName(),
                        id));
        close();
        log.debug(rowCount);
        if (rowCount > 0) {
            return new Result<>(null, ResultType.COMPLETE, Constants.DELETED_SUCCESSFULLY);
        }
        return new Result<>(null, ResultType.FAIL, Constants.NOT_FOUND);
    }

    @Override
    public <T> Result<T> updateRecord(Long id, T record) throws Exception {
        initDataSource();
        int rowCount = statement.executeUpdate(
                String.format(
                        Constants.UPDATE,
                        record.getClass().getSimpleName(),
                        String.join(Constants.PARAM_DELIMITER, getSqlUpdateString(record)), id));
        close();
        log.debug(rowCount);
        if (rowCount > 0) {
            return new Result<>(null, ResultType.COMPLETE, Constants.UPDATED_SUCCESSFULLY);
        }
        return new Result<>(null, ResultType.FAIL, Constants.NOT_FOUND);
    }


    /**
     * Parse record to a String which can be used in INSERT query
     * @param record which will be parsed
     * @param <T> Generic Type
     * @return Array with column names and its values
     * @throws InvocationTargetException in case if there is no id getter for given class
     * @throws IllegalAccessException in case if method can't invoke method on given object
     */
    public <T> String[] getSqlString (T record) throws InvocationTargetException, IllegalAccessException {
        Class<?> cn = record.getClass();
        List<Method> methods = Arrays.stream(cn.getMethods())
                .filter(method -> method.getName().matches(Constants.GET_METHODS_PATTERN))
                .filter(method -> !method.getName().equals(Constants.GET_CLASS))
                .collect(Collectors.toList());
        log.debug(methods);
        List<String> columns = new ArrayList<>();
        List<String> values = new ArrayList<>();
        for (Method method : methods) {
            if (Constants.PRIMITIVE_CLASSES.contains(method.getReturnType())) {
                columns.add(method.getName().replace(Constants.GET, Constants.EMPTY_STRING).toLowerCase(Locale.ROOT));
                if (method.getReturnType() == String.class) {
                    values.add(String.format(Constants.SQL_STRING,method.invoke(record)));
                } else {
                    values.add(String.valueOf(method.invoke(record)));

                }
            }
        }
        log.debug(columns);
        log.debug(values);
        return new String[]{String.join(Constants.PARAM_DELIMITER, columns), String.join(Constants.PARAM_DELIMITER, values)};
    }

    /**
     * Parse record into string for UPDATE query
     * @param record which will be parsed
     * @param <T> Generic Type
     * @return List of Strings
     * @throws InvocationTargetException in case if there is no id getter for given class
     * @throws IllegalAccessException in case if method can't invoke method on given object
     */
    private <T> List<String> getSqlUpdateString (T record) throws InvocationTargetException, IllegalAccessException {
        String[] data = getSqlString(record);
        List<String> columns = Arrays.asList(data[0].split(Constants.PARAM_DELIMITER));
        List<String> values = Arrays.asList(data[1].split(Constants.PARAM_DELIMITER));
        List<String> res = IntStream
                .range(0, columns.size())
                .mapToObj(index -> columns.get(index) + Constants.SINGLE_EQUAL + values.get(index))
                .collect(Collectors.toList());
        return res;
    }

    /**
     * Generic method which only inserting records
     * @param listRecord list to write
     * @param cn Class Name
     * @param <T> Generic Type
     * @return structure with status, data(if not error) and message
     * @throws SQLException
     */
    private <T> Result<T> add(List<T> listRecord, Class<?> cn) throws SQLException {
        initDataSource();
        try {
            List<String> query = new ArrayList<>();
            for (T record : listRecord) {
                String[] data = getSqlString(record);
                log.debug(String.format(Constants.DB_INSERT,cn.getSimpleName().toUpperCase(Locale.ROOT),data[0], data[1]));
                query.add(
                        String.format(
                                Constants.DB_INSERT,
                                cn.getSimpleName().toUpperCase(Locale.ROOT),
                                data[0], data[1]));
            }
            log.info(String.join(Constants.EMPTY_STRING, query));
            statement.executeUpdate(String.join(Constants.EMPTY_STRING, query));
            return new Result<>(listRecord, ResultType.COMPLETE, Constants.INSERTED_SUCCESSFULLY);
        } catch( Exception e) {
            log.error(e);
            return new Result<>(null, ResultType.FAIL, Constants.SQL_FAIL);
        }
        finally {
            close();
        }
    }

    /**
     * Generic method which only read from DataSource
     * @param cn Class Name
     * @param <T> Generic Type
     * @return structure with status, data(if not error) and message
     * @throws SQLException
     */
    private <T> Result<T> get(Class<?> cn) throws SQLException {
        initDataSource();
        try {
            ResultSet rs = statement.executeQuery(String.format(Constants.DB_SELECT, cn.getSimpleName()));
            List<T> res = getDataFormResultSet(rs, cn);
            log.info(res);
            return new Result<T>(res, ResultType.COMPLETE, Constants.RECORDS_FOUND);
        }
        catch (SQLException e) {
            log.error(e);
            return new Result<>(null, ResultType.FAIL, Constants.SQL_FAIL);
        }
        finally {
            close();
        }
    }

    /**
     * Close current connection
     */
    private void close() throws SQLException {
        connection.close();
        statement.close();
    }

    /**
     * Links two records
     * @param cn class which related to child table name
     * @param parentCn class which related to parent table name
     * @param parentId id
     * @param id id of child
     * @param <T> Generic Type
     * @throws SQLException
     */
    private <T> void setParent(Class<?> cn, Class<?> parentCn, Long parentId, Long id)  throws SQLException {
        initDataSource();
        try {
            log.info(String.format(Constants.CHANGE_PARENT_ID, cn.getSimpleName(), parentCn.getSimpleName(), parentId, id));
            statement.executeUpdate(String.format(Constants.CHANGE_PARENT_ID, cn.getSimpleName(), parentCn.getSimpleName(), parentId, id));
        }
        catch (SQLException e) {
            log.error(e);
        }
        finally {
            close();
        }
    }

    /**
     * Get all child by Foreign key
     * @param parentId foreign key
     * @param cn table name from where you want to get records
     * @param parentCn table name from where to find key
     * @param <T> Generic Type
     * @return the result
     */
    private <T> Result<T> getByParent(Long parentId, Class<?> cn, Class<?> parentCn){
        try {
            initDataSource();
            log.info(String.format(Constants.GET_BY_PARENT_ID, cn.getSimpleName(), parentCn.getSimpleName(), parentId));
            ResultSet rs = statement.executeQuery(String.format(Constants.GET_BY_PARENT_ID, cn.getSimpleName(), parentCn.getSimpleName(), parentId));
            List<T> res = getDataFormResultSet(rs, cn);
            return new Result<>(res, ResultType.COMPLETE, Constants.RECORDS_FOUND);
        } catch(Exception e) {
            log.error(e);
            return new Result<>(null, ResultType.FAIL, Constants.SQL_FAIL);
        }
    }

    /**
     * Remove all data from table
     * @param cn table name
     * @throws SQLException
     */
    public void flushTable(Class<?> cn) throws SQLException {
        initDataSource();
        log.info(Constants.FLUSH_FILE + cn.getSimpleName().toUpperCase(Locale.ROOT));
        statement.executeUpdate(String.format(Constants.DELETE_ALL, cn.getSimpleName().toUpperCase(Locale.ROOT)));
        close();
    }

    /**
     *
     * @param rs raw data
     * @param cn instance of the class we need
     * @param <T> Generic Type
     * @param <T> Generic Type
     * @throws SQLException
     */
    private <T> List<T> getDataFormResultSet(ResultSet rs, Class cn) throws SQLException {
        List<T> res = new ArrayList<>();
        if (cn == Book.class) {
            res = (List<T>) getBookFromResultSet(rs);
        }
        if (cn == Film.class) {
            res = (List<T>) getFilmFromResultSet(rs);
        }
        if (cn == Article.class) {
            res = (List<T>) getArticleFromResultSet(rs);
        }
        if (cn == Plan.class) {
            res = (List<T>) getPlanFromResultSet(rs);
        }
        if (cn == Client.class) {
            res = (List<T>) getClientFromResultSet(rs);
        }
        return res;
    }

    /**
     * Get id
     * @param list list records
     * @param <T> Generic Type
     * @return the result
     */
    private <T> List<Long> getIds(List<T> list) {
        List<Long> res = new ArrayList<>();
        Class<?> cn;
        if (list != null && list.size() > 0) {
            cn = list.get(0).getClass();
        } else {
            return new ArrayList<>();
        }
        try {
            Method getter = cn.getMethod(Constants.METHOD_GET_ID);
            for (T el: list) {
                res.add((Long) getter.invoke(el));
            }
        }
        catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            log.error(e);
            log.info(Constants.NO_METHOD);
        }
        return res;
    }

    /**
     * Get Book from Result Set
     * @param rs
     * @return the result
     * @throws SQLException
     */
    private List<Book> getBookFromResultSet(ResultSet rs) throws SQLException {
        List<Book> res = new ArrayList<>();
        while (rs.next()) {
            Book book = new Book();
            book.setId(rs.getLong(1));
            book.setBookAuthorName(rs.getString(2));
            book.setNameOfBook(rs.getString(3));
            book.setLink(rs.getString(4));
            book.setReview(rs.getString(5));
            book.setGenre(rs.getString(6));
            book.setTimeReading(rs.getLong(7));
            res.add(book);
        }
        return res;
    }
    /**
     * Get Film from Result Set
     * @param rs
     * @return the result
     * @throws SQLException
     */
    private List<Film> getFilmFromResultSet(ResultSet rs) throws SQLException {
        List<Film> res = new ArrayList<>();
        while (rs.next()) {
            Film film = new Film();
            film.setId(rs.getLong(1));
            film.setBookAuthorName(rs.getString(2));
            film.setNameOfBook(rs.getString(3));
            film.setCountry(rs.getString(4));
            film.setYear(rs.getString(5));
            film.setProducer(rs.getString(6));
            film.setFormat(rs.getString(7));
            res.add(film);
        }
        return res;

    }

    /**
     * Get Article from Result Set
     * @param rs
     * @return the result
     * @throws SQLException
     */
    private List<Article> getArticleFromResultSet(ResultSet rs) throws SQLException {
        List<Article> res = new ArrayList<>();
        while (rs.next()) {
            Article article = new Article();
            article.setId(rs.getLong(1));
            article.setBookAuthorName(rs.getString(2));
            article.setNameOfBook(rs.getString(3));
            article.setTitle(rs.getString(4));
            article.setContent(rs.getString(5));
            res.add(article);
        }
        return res;
    }
    /**
     * Get Plan from Result Set
     * @param rs
     * @return the result
     * @throws SQLException
     */
    private List<Plan> getPlanFromResultSet(ResultSet rs) throws SQLException {
        List<Plan> res = new ArrayList<>();
        while (rs.next()) {
            Plan plan = new Plan();
            plan.setId(rs.getLong(1));
            plan.setName(rs.getString(2));
            plan.setBook(new Book());
            plan.getBook().setId(rs.getLong(3));
            plan.setStartDay(rs.getString(4));
            plan.setLastDay(rs.getString(5));
            plan.setStatus(rs.getBoolean(7));
            res.add(plan);
        }
        return res;
    }
    /**
     * Get lient from Result Set
     * @param rs
     * @return the result
     * @throws SQLException
     */
    private List<Client> getClientFromResultSet(ResultSet rs) throws SQLException {
        List<Client> res = new ArrayList<>();
        while (rs.next()) {
            Client client = new Client();
            client.setId(rs.getLong(1));
            client.setName(rs.getString(2));
            client.setLogin(rs.getString(3));
            client.setPassword(rs.getString(4));
            client.setEmail(rs.getString(5));
            res.add(client);
        }
        return res;
    }

}
