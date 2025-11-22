/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.models;

import core.models.Author;
import core.models.Author;
import core.models.Book;
import java.util.ArrayList;

/**
 *
 * @author edangulo
 */
public class DigitalBook extends Book {
    
    private boolean hasHyperlink;
    private String hyperlink;

    public DigitalBook(String title, ArrayList<Author> authors, String isbn, String genre, String format, double value, Publisher publisher) {
        super(title, authors, isbn, genre, format, value, publisher);
        this.hasHyperlink = false;
        this.hyperlink = null;
    }
    
    public DigitalBook(String title, ArrayList<Author> authors, String isbn, String genre, String format, double value, Publisher publisher, String hyperlink) {
        super(title, authors, isbn, genre, format, value, publisher);
        this.hasHyperlink = true;
        this.hyperlink = hyperlink;
    }

    public boolean hasHyperlink() {
        return hasHyperlink;
    }
    
    public String getHyperlink() {
        return hyperlink;
    }
    
    @Override
    public DigitalBook clone() throws CloneNotSupportedException {
        ArrayList<Author> authorCopies = new ArrayList<>();
        for (Author author : this.authors) {
            authorCopies.add(author.clone());
        }

        Publisher publisherCopy = this.publisher != null ? this.publisher.clone() : null;

        if (this.hyperlink == null) {
            return new DigitalBook(
                    this.title,
                    authorCopies,
                    this.isbn,
                    this.genre,
                    this.format,
                    this.value,
                    publisherCopy
            );
        } else {
            return new DigitalBook(
                    this.title,
                    authorCopies,
                    this.isbn,
                    this.genre,
                    this.format,
                    this.value,
                    publisherCopy,
                    this.hyperlink
            );
        }
    }
    
}
