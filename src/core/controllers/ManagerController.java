/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.controllers;

import core.controllers.utils.Response;
import core.controllers.utils.Status;
import core.models.Manager;
import core.models.repositories.IPersonRepository;
import core.models.repositories.IRepositoryProvider;
import core.models.repositories.RepositoryProvider;

/**
 *
 * @author famil
 */
public class ManagerController {
    
    private static IRepositoryProvider repositoryProvider = RepositoryProvider.getProvider();
    private static IPersonRepository personRepository = repositoryProvider.getPersonRepository();

    public static void setRepositoryProvider(IRepositoryProvider customProvider) {
        if (customProvider != null) {
            repositoryProvider = customProvider;
            personRepository = customProvider.getPersonRepository();
        }
    }
    
    public static Response createManager(String id, String firstName, String lastName) {

        try {

           
            if (id == null || id.trim().isEmpty()) {
                return new Response("El ID no puede estar vacío.", Status.BAD_REQUEST);
            }

            if (id.trim().length() > 15) {
                return new Response("El ID debe tener máximo 15 dígitos.", Status.BAD_REQUEST);
            }

            long idLong;
            try {
                idLong = Long.parseLong(id.trim());

                if (idLong < 0) {
                    return new Response("El ID debe ser positivo.", Status.BAD_REQUEST);
                }

            } catch (NumberFormatException ex) {
                return new Response("El ID debe ser numérico.", Status.BAD_REQUEST);
            }

            
            if (firstName == null || firstName.trim().isEmpty()) {
                return new Response("El nombre no puede estar vacío.", Status.BAD_REQUEST);
            }

            
            if (lastName == null || lastName.trim().isEmpty()) {
                return new Response("El apellido no puede estar vacío.", Status.BAD_REQUEST);
            }

            
            if (personRepository.findById(idLong) != null) {
                return new Response("Otra persona ya tiene este ID.", Status.BAD_REQUEST);
            }

            
            Manager manager = new Manager(idLong, firstName, lastName);

            boolean added = personRepository.add(manager);

            if (!added) {
                return new Response("No se pudo guardar el gerente.", Status.INTERNAL_SERVER_ERROR);
            }

            return new Response("Gerente creado correctamente.",
                    Status.CREATED,
                    manager.clone());

        } catch (Exception ex) {
            return new Response("Error inesperado.", Status.INTERNAL_SERVER_ERROR);
        }
    }
}
