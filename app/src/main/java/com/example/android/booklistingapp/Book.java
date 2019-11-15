package com.example.android.booklistingapp;

public class Book {

    // Book Author
    private String mAuthor;

    // Book title
    private String mTitle;



    /*
     * Create a new Book object.
     *
     * @param bookAuthor is the Book's Author
     * @param bookTitle is the Book's title
     * */

    public Book(String bookAuthor, String bookTitle)
    {
        mAuthor = bookAuthor;
        mTitle = bookTitle;
    }

    public Book(String bookTitle)
    {
        mTitle = bookTitle;
    }

    /**
     * Get the book author
     */
    public String getAuthor() {
        return mAuthor;
    }

    /**
     * Get the book title
     */
    public String getTitle() {return mTitle;}



}
