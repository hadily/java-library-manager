package com.example.miniprojetjava.model;

import java.sql.Date;

public class BookPrints {
    private Book book;
    private Reader reader;
    private Date printDate;
    private Date returnDate;

    public BookPrints(Book book, Reader reader, Date printDate, Date returnDate) {
        this.book = book;
        this.reader = reader;
        this.printDate = printDate;
        this.returnDate = returnDate;
    }

    public Book getBook() {  return book; }

    public void setBook(Book book) { this.book = book;}

    public Reader getReader() { return reader; }

    public void setReader(Reader reader) { this.reader = reader; }

    public Date getPrintDate() {
        return printDate;
    }

    public void setPrintDate(Date printDate) {
        this.printDate = printDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    @Override
    public String toString() {
        return "BookPrints{" +
                "book=" + book +
                ", reader=" + reader +
                ", printDate=" + printDate +
                ", returnDate=" + returnDate +
                '}';
    }
}