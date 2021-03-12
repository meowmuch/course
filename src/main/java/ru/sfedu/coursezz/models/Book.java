package ru.sfedu.coursezz.models;


import com.opencsv.bean.CsvBindByName;

/**
 * Class Book
 */
public class Book extends InfoRes{

    @CsvBindByName(required = true)
    private String link;

    @CsvBindByName(required = true)
    private String review;

    @CsvBindByName(required = true)
    private String genre;

    @CsvBindByName(required = true)
    private Long timeReading;




    public Book () {}
    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public Long getTimeReading() {
        return timeReading;
    }

    public void setTimeReading(Long timeReading) {
        this.timeReading = timeReading;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }


    @Override
    public String toString() {
        return "Book{" +
                "link=" + link +
                ", review='" + review +
                ", timeReading='" + timeReading +
                ", genre='" + genre + '\'' +
                '}';
    }
}