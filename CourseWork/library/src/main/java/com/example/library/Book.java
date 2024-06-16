package com.example.library;

public class Book {
    private String isbn;
    private String title;
    private String category;
    private String author;
    private String publisher;
    private String price;
    private int quantity;
    private boolean is_recommend;
    private String description;
    private byte[] bookimage;

    public Book(String isbn, String title, String category, String author, String publisher, String price, int quantity, boolean is_recommend, String description, byte[] bookimage) {
        this.isbn = isbn;
        this.title = title;
        this.category = category;
        this.author = author;
        this.publisher = publisher;
        this.price = price;
        this.quantity = quantity;
        this.is_recommend = is_recommend;
        this.description = description;
        this.bookimage = bookimage;
    }

    public String getIsbn() { return isbn; }

    public void setIsbn(String isbn) { this.isbn = isbn; }

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public String getCategory() { return category; }

    public void setCategory(String category) { this.category = category; }

    public String getAuthor() { return author; }

    public void setAuthor(String author) { this.author = author; }

    public String getPublisher() { return publisher; }

    public void setPublisher(String publisher) { this.publisher = publisher; }

    public String getPrice() { return price; }

    public void setPrice(String price) { this.price = price; }

    public int getQuantity() { return quantity; }

    public void setQuantity(int quantity) { this.quantity = quantity; }

    public boolean isIs_recommend() { return is_recommend; }

    public void setIs_recommend(boolean is_recommend) { this.is_recommend = is_recommend; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public byte[] getBookimage() { return bookimage; }

    public void setBookimage(byte[] bookimage) { this.bookimage = bookimage; }
}
