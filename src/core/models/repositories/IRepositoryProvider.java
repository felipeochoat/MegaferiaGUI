/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package core.models.repositories;

/**
 *
 * @author martin
 */

public interface IRepositoryProvider {
    IBookRepository getBookRepository();

    IPersonRepository getPersonRepository();

    IPublisherRepository getPublisherRepository();

    IStandRepository getStandRepository();
}