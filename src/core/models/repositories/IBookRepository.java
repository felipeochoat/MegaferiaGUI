/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.models.repositories;

import core.models.Book;
import java.util.ArrayList;

/**
 *
 * @author martin
 */
public interface IBookRepository {
    Book findByIsbn(String isbn);

    boolean add(Book book);

    ArrayList<Book> getAll();
}
