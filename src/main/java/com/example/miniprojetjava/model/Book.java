package com.example.miniprojetjava.model;

public class Book {
    private int bookId;
    private String title;
    private Author author;
    private String bookCover;
    private String description;
    private String category;
    private String picture;
    private int CIN;
    private int isbnCode;


    public Book(int bookId, int isbnCode, String title, String bookCover, String description, String authorFirstName, String authorLastName, String category) {
        this.bookId = bookId;
        this.title = title;
        this.bookCover = bookCover;
        this.description = description;
        this.author = new Author(authorFirstName, authorLastName);
        this.category = category;
        this.isbnCode = isbnCode;
    }

    public int getIsbnCode() { return isbnCode; }

    public void setIsbnCode(int isbnCode) { this.isbnCode = isbnCode; }

    public int getBookId() { return bookId; }

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public Author getAuthor() { return author; }

    public void setAuthor(Author author) { this.author = author; }

    public String getBookCover() { return bookCover; }

    public void setBookCover(String bookCover) { this.bookCover = bookCover; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public String getCategory() { return category; }

    public void setCategory(String category) { this.category = category; }

    @Override
    public String toString() {
        return "Book{" +
                " bookId='" + bookId +'\'' +
                ", title='" + title + '\'' +
                ", author=" + author +
                ", bookCover='" + bookCover + '\'' +
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                '}';
    }
}
