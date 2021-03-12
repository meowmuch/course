package ru.sfedu.coursezz.models;

import com.opencsv.bean.CsvBindByName;

/**
 * Class Article
 */
public class Article extends InfoRes {

    @CsvBindByName(required = true)
    private String title;

    @CsvBindByName(required = true)
    private String content;

    public Article() {}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Article{" +
                "title=" + title +
                ", content='" + content + '\'' +
                '}';
    }

}
