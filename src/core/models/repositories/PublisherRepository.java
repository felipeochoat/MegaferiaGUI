/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.models.repositories;

import core.models.Publisher;
import core.models.storage.Storage;
import java.util.ArrayList;

/**
 *
 * @author martin
 */
public class PublisherRepository implements IPublisherRepository {

    private final Storage storage = Storage.getInstance();

    @Override
    public Publisher findByNit(String nit) {
        for (Publisher p : storage.getPublishers()) {
            if (p.getNit().equals(nit)) return p;
        }
        return null;
    }

    @Override
    public boolean add(Publisher publisher) {
        if (findByNit(publisher.getNit()) != null) return false;
        storage.getPublishers().add(publisher);
        return true;
    }

    @Override
    public ArrayList<Publisher> getAll() {
        return new ArrayList<>(storage.getPublishers());
    }
}