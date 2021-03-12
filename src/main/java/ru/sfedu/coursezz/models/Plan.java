package ru.sfedu.coursezz.models;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;
import ru.sfedu.coursezz.utils.Converters.BookConverter;

import java.util.Objects;

/**
 * Class Plan
 */
public class Plan {

    @CsvBindByName(required = true)
    private Long id;

    @CsvBindByName(required = true)
    private String name;

    @CsvCustomBindByName(required = true, converter = BookConverter.class)
    private Book book;

    @CsvBindByName(required = true)
    private String startDay;

    @CsvBindByName(required = true)
    private String lastDay;

    @CsvBindByName(required = true)
    private Boolean status;

    public Plan() {}

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Book getBook() { return book; }

    public void setBook(Book book) {
        this.book = book;
    }

    public String getStartDay() {
        return startDay;
    }

    public void setStartDay(String startDay) {
        this.startDay = startDay;
    }

    public String getLastDay() { return lastDay; }

    public void setLastDay(String lastDay) {
        this.lastDay = lastDay;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Plan plan = (Plan) o;
        return
                Objects.equals(book, plan.book);
    }

    @Override
    public int hashCode() {
        return Objects.hash(book);
    }

    @Override
    public String toString() {
        return "Plan{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", book='" + book +
                ", startDay=" + startDay +
                ", lastDay=" + lastDay +
                ", status=" + status +
                '}';
    }



}
