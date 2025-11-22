/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.models;

import java.util.ArrayList;

/**
 *
 * @author edangulo
 */
public class Audiobook extends Book {
    
    private int duration;
    private Narrator narrador;

    public Audiobook(String title, ArrayList<Author> authors, String isbn, String genre, String format, double value, Publisher publisher, int duration, Narrator narrator) {
        super(title, authors, isbn, genre, format, value, publisher);
        this.duration = duration;
        this.narrador = narrator;
        
        this.narrador.addBook(this);
    }

    public int getDuration() {
        return duration;
    }

    public Narrator getNarrador() {
        return narrador;
    }
    
    
    @Override
    public Audiobook clone() throws CloneNotSupportedException {
        ArrayList<Author> authorCopies = new ArrayList<>();
        for (Author author : this.authors) {
            authorCopies.add(author.clone());
        }

        Publisher publisherCopy = this.publisher != null ? this.publisher.clone() : null;
        Narrator narratorCopy = this.narrador != null ? this.narrador.clone() : null;

        return new Audiobook(
                this.title,
                authorCopies,
                this.isbn,
                this.genre,
                this.format,
                this.value,
                publisherCopy,
                this.duration,
                narratorCopy
        );
    }
}
