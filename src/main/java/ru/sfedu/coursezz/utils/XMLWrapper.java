package ru.sfedu.coursezz.utils;



import java.util.List;


public class XMLWrapper <T> {


    private List<T> list;

    public XMLWrapper() {};

    public XMLWrapper(List<T> list) {
        this.list = list;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
