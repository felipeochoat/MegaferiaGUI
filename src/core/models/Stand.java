/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.models;

import core.models.Publisher;
import java.util.ArrayList;

/**
 *
 * @author edangulo
 */
public class Stand {
    
    private long id;
    private double price;
    private ArrayList<Publisher> publishers;

    public Stand(long id, double price) {
        this.id = id;
        this.price = price;
        this.publishers = new ArrayList<>();
    }
    
    public void addPublisher(Publisher publisher) {
        this.publishers.add(publisher);
    }

    public long getId() {
        return id;
    }

    public double getPrice() {
        return price;
    }

    public ArrayList<Publisher> getPublishers() {
        return publishers;
    }
    
    public int getPublisherQuantity() {
        return this.publishers.size();
    }
    
    @Override
    public Stand clone () throws CloneNotSupportedException {
        Stand copy = new Stand(this.id, this.price);

        for (Publisher publisher : this.publishers) {
            Publisher publisherCopy;

            if (publisher.getManager() != null) {
                publisherCopy = new Publisher(
                        publisher.getNit(),
                        publisher.getName(),
                        publisher.getAddress(),
                        publisher.getManager().clone()
                );
            } else {
                publisherCopy = new Publisher(
                        publisher.getNit(),
                        publisher.getName(),
                        publisher.getAddress(),
                        null
                );
            }

            copy.addPublisher(publisherCopy);
        }

        return copy;
    }
    
}
