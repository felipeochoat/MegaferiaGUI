/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.models.repositories;

import core.models.Stand;
import core.models.storage.Storage;
import java.util.ArrayList;

/**
 *
 * @author martin
 */

public class StandRepository implements IStandRepository {

    private final Storage storage = Storage.getInstance();

    @Override
    public Stand findById(long id) {
        for (Stand s : storage.getStands()) {
            if (s.getId() == id) {
                return s;
            }
        }
        return null;
    }

    @Override
    public boolean add(Stand stand) {
        if (findById(stand.getId()) != null) {
            return false;
        }
        storage.getStands().add(stand);
        return true;
    }

    @Override
    public ArrayList<Stand> getAll() {
        return new ArrayList<>(storage.getStands());
    }
}