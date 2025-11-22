/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.models.repositories;

import core.models.Book;
import core.models.storage.Storage;
import java.util.ArrayList;

/**
 *
 * @author martin
 */
public class BookRepository implements IBookRepository {

    private final Storage storage = Storage.getInstance();

    @Override
    public Book findByIsbn(String isbn) {
        for (Book b : storage.getBooks()) {
            if (b.getIsbn().equals(isbn)) return b;
        }
        return null;
    }

    @Override
    public boolean add(Book book) {
        if (findByIsbn(book.getIsbn()) != null) return false;
        storage.getBooks().add(book);
        return true;
    }

    @Override
    public ArrayList<Book> getAll() {
        return new ArrayList<>(storage.getBooks());
    }
}