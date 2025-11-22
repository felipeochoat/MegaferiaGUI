/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.models.repositories;

import core.models.Author;
import core.models.Manager;
import core.models.Narrator;
import core.models.Person;
import core.models.storage.Storage;
import java.util.ArrayList;

/**
 *
 * @author martin
 */
public class PersonRepository implements IPersonRepository{
    private final Storage storage = Storage.getInstance();

    @Override
    public ArrayList<Author> getAuthors() {
        return new ArrayList<>(storage.getAuthors());
    }

    @Override
    public ArrayList<Manager> getManagers() {
        return new ArrayList<>(storage.getManagers());
    }

    @Override
    public ArrayList<Narrator> getNarrators() {
        return new ArrayList<>(storage.getNarrators());
    }

    @Override
    public Person findById(long id) {

        for (Author a : storage.getAuthors()) {
            if (a.getId() == id) return a;
        }

        for (Manager m : storage.getManagers()) {
            if (m.getId() == id) return m;
        }

        for (Narrator n : storage.getNarrators()) {
            if (n.getId() == id) return n;
        }

        return null;
    }

    @Override
    public boolean add(Person person) {

        if (findById(person.getId()) != null) return false;

        if (person instanceof Author) storage.getAuthors().add((Author) person);
        else if (person instanceof Manager) storage.getManagers().add((Manager) person);
        else if (person instanceof Narrator) storage.getNarrators().add((Narrator) person);

        return true;
    }
}
