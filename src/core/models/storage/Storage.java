/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.models.storage;

import core.models.Author;
import core.models.Book;
import core.models.Manager;
import core.models.Narrator;
import core.models.Person;
import core.models.Publisher;
import core.models.Stand;
import java.util.ArrayList;

/**
 *
 * @author martin
 */
public class Storage {

    //Singleton
    private static Storage instance = null;

    private ArrayList<Stand> stands;
    private ArrayList<Author> authors;
    private ArrayList<Manager> managers;
    private ArrayList<Narrator> narrators;
    private ArrayList<Publisher> publishers;
    private ArrayList<Book> books;

    private Storage() {
        stands = new ArrayList<>();
        authors = new ArrayList<>();
        managers = new ArrayList<>();
        narrators = new ArrayList<>();
        publishers = new ArrayList<>();
        books = new ArrayList<>();
    }

    public static Storage getInstance() {
        if (instance == null) {
            instance = new Storage();
        }
        return instance;
    }

    public ArrayList<Person> getPersons() {
        ArrayList<Person> all = new ArrayList<>();
        all.addAll(authors);
        all.addAll(managers);
        all.addAll(narrators);
        return all;
    }

    public ArrayList<Author> getAuthors() {
        return authors;
    }

    public ArrayList<Narrator> getNarrators() {
        return narrators;
    }

    public ArrayList<Manager> getManagers() {
        return managers;
    }

    public ArrayList<Publisher> getPublishers() {
        return publishers;
    }

    public ArrayList<Stand> getStands() {
        return stands;
    }

    public ArrayList<Book> getBooks() {
        return books;
    }
}
