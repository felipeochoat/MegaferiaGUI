/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package core.models.repositories;

import core.models.Person;

/**
 *
 * @author martin
 */
public interface IPersonRepository {
    
    Person findById(long id);

    boolean add(Person person);

    java.util.ArrayList<core.models.Author> getAuthors();

    java.util.ArrayList<core.models.Manager> getManagers();

    java.util.ArrayList<core.models.Narrator> getNarrators();
}
