package com.codeup.booknova.ui.model;

import javafx.beans.property.*;

/**
 * JavaFX Property wrapper for Book entity to use in TableViews
 */
public class BookTableModel {
    private final IntegerProperty id;
    private final StringProperty title;
    private final StringProperty author;
    private final StringProperty isbn;
    private final StringProperty genre;
    private final IntegerProperty totalCopies;
    private final IntegerProperty availableCopies;
    private final BooleanProperty available;
    
    public BookTableModel() {
        this.id = new SimpleIntegerProperty();
        this.title = new SimpleStringProperty();
        this.author = new SimpleStringProperty();
        this.isbn = new SimpleStringProperty();
        this.genre = new SimpleStringProperty();
        this.totalCopies = new SimpleIntegerProperty();
        this.availableCopies = new SimpleIntegerProperty();
        this.available = new SimpleBooleanProperty();
    }
    
    public BookTableModel(Integer id, String title, String author, String isbn, 
                         String genre, Integer totalCopies, Integer availableCopies) {
        this();
        this.id.set(id != null ? id : 0);
        this.title.set(title != null ? title : "");
        this.author.set(author != null ? author : "");
        this.isbn.set(isbn != null ? isbn : "");
        this.genre.set(genre != null ? genre : "");
        this.totalCopies.set(totalCopies != null ? totalCopies : 0);
        this.availableCopies.set(availableCopies != null ? availableCopies : 0);
        this.available.set(availableCopies != null && availableCopies > 0);
    }
    
    // Property getters
    public IntegerProperty idProperty() { return id; }
    public StringProperty titleProperty() { return title; }
    public StringProperty authorProperty() { return author; }
    public StringProperty isbnProperty() { return isbn; }
    public StringProperty genreProperty() { return genre; }
    public IntegerProperty totalCopiesProperty() { return totalCopies; }
    public IntegerProperty availableCopiesProperty() { return availableCopies; }
    public BooleanProperty availableProperty() { return available; }
    
    // Value getters
    public Integer getId() { return id.get(); }
    public String getTitle() { return title.get(); }
    public String getAuthor() { return author.get(); }
    public String getIsbn() { return isbn.get(); }
    public String getGenre() { return genre.get(); }
    public Integer getTotalCopies() { return totalCopies.get(); }
    public Integer getAvailableCopies() { return availableCopies.get(); }
    public Boolean getAvailable() { return available.get(); }
    
    // Display method for tables
    public String getDisplayText() {
        return String.format("%s - %s (%s disponibles)", 
                           title.get(), author.get(), availableCopies.get());
    }
}