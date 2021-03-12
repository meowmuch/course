package ru.sfedu.coursezz.utils;

import ru.sfedu.coursezz.models.enums.ResultType;

import java.util.List;

public class Result<T> {
    private List<T> data;

    private T data1;

    public void setData(List<T> data) {
        this.data = data;
    }

    public void setResultType(ResultType resultType) {
        this.resultType = resultType;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private ResultType resultType;
    private String message;

    public List<T> getData() {
        return data;
    }

    public ResultType getResultType() {
        return resultType;
    }

    public String getMessage() {
        return message;
    }

    public Result(List<T> data, ResultType resultType, String message) {
        this.data = data;
        this.resultType = resultType;
        this.message = message;
    }

    public Result(T data1, ResultType resultType, String message) {
        this.data1 = data1;
        this.resultType = resultType;
        this.message = message;
    }

    @Override
    public String toString() {
        return "Result{" +
                "resultType=" + resultType +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
