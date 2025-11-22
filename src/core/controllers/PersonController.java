/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.controllers;

import core.models.repositories.IPersonRepository;
import core.models.repositories.IRepositoryProvider;
import core.models.repositories.RepositoryProvider;

/**
 *
 * @author famil
 */
public class PersonController {
    
    private static IRepositoryProvider repositoryProvider = RepositoryProvider.getProvider();
    private static IPersonRepository personRepository = repositoryProvider.getPersonRepository();

    public static void setRepositoryProvider(IRepositoryProvider customProvider) {
        if (customProvider != null) {
            repositoryProvider = customProvider;
            personRepository = customProvider.getPersonRepository();
        }
    }
    
}
