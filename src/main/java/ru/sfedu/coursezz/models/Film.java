package ru.sfedu.coursezz.models;

import com.opencsv.bean.CsvBindByName;

/**
 * Class Film
 */
public class Film extends InfoRes {

    @CsvBindByName(required = true)
    private String country;

    @CsvBindByName(required = true)
    private String year;

    @CsvBindByName(required = true)
    private String producer;

    @CsvBindByName(required = true)
    private String format;

    public Film() {}

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    @Override
    public String toString() {
        return "Film{" +
                ", country='" + country +
                ", year='" + year +
                ", producer='" + producer +
                ", format='" + format + '\'' +
                '}';
    }
}
