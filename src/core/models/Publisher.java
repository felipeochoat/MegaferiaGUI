/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.models;

import core.models.Manager;
import core.models.Book;
import java.util.ArrayList;

/**
 *
 * @author edangulo
 */
public class Publisher {
    
    private final String nit;
    private String name;
    private String address;
    private Manager manager;
    private ArrayList<Book> books;
    private ArrayList<Stand> stands;

    public Publisher(String nit, String name, String address, Manager manager) {
        this.nit = nit;
        this.name = name;
        this.address = address;
        this.manager = manager;
        this.books = new ArrayList<>();
        this.stands = new ArrayList<>();
        
        this.manager.setPublisher(this);
    }

    public String getNit() {
        return nit;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public Manager getManager() {
        return manager;
    }
    
    public int getStandQuantity() {
        return this.stands.size();
    }
    
    public void addBook(Book book) {
        this.books.add(book);
    }
    
    public void addStand(Stand stand) {
        this.stands.add(stand);
    }
    
    @Override
    public Publisher clone() throws CloneNotSupportedException {
        Manager managerCopy = this.manager != null ? this.manager.clone() : null;

        Publisher copy = new Publisher(this.nit, this.name, this.address, managerCopy);

        for (Stand stand : this.stands) {
            Stand standCopy = new Stand(stand.getId(), stand.getPrice());
            copy.addStand(standCopy);
        }

        return copy;
    }
    
}
