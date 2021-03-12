package ru.sfedu.coursezz.utils.Converters;

import com.opencsv.bean.AbstractBeanField;
import ru.sfedu.coursezz.models.Book;


public class BookConverter extends AbstractBeanField {


    private String fieldsDelimiter = "__";

    @Override
    protected Object convert(String value) {
        Book book = new Book();
        String[] parsedArgs = value.split(fieldsDelimiter);
        book.setId(Long.parseLong(parsedArgs[0]));
        book.setBookAuthorName(parsedArgs[1]);
        book.setNameOfBook(parsedArgs[2]);
        book.setTimeReading(Long.parseLong(parsedArgs[3]));
        return book;
    }

    @Override
    public String convertToWrite(Object value) {
        Book book = (Book) value;
        return String.format("%d" + fieldsDelimiter +
                             "%s" + fieldsDelimiter +
                             "%s" + fieldsDelimiter +
                             "%d",
                             book.getId(),
                             book.getBookAuthorName(),
                             book.getNameOfBook(),
                             book.getTimeReading());
    }

}


