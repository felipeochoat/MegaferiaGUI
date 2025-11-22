/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.models;

import core.models.Book;
import core.models.Author;
import java.util.ArrayList;

/**
 *
 * @author edangulo
 */
public class PrintedBook extends Book {
    
    private int pages;
    private int copies;

    public PrintedBook(String title, ArrayList<Author> authors, String isbn, String genre, String format, double value, Publisher publisher, int pages, int copies) {
        super(title, authors, isbn, genre, format, value, publisher);
        this.pages = pages;
        this.copies = copies;
    }

    public int getPages() {
        return pages;
    }

    public int getCopies() {
        return copies;
    }
    
    @Override
    public PrintedBook clone() throws CloneNotSupportedException {
        ArrayList<Author> authorCopies = new ArrayList<>();
        for (Author author : this.authors) {
            authorCopies.add(author.clone());
        }

        Publisher publisherCopy = this.publisher != null ? this.publisher.clone() : null;

        return new PrintedBook(
                this.title,
                authorCopies,
                this.isbn,
                this.genre,
                this.format,
                this.value,
                publisherCopy,
                this.pages,
                this.copies
        );
    }
    
}
