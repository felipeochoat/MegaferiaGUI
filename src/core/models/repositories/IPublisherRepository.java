/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package core.models.repositories;

import core.models.Publisher;
import java.util.ArrayList;

/**
 *
 * @author martin
 */
public interface IPublisherRepository {
    Publisher findByNit(String nit);

    boolean add(Publisher publisher);

    ArrayList<Publisher> getAll();
}
