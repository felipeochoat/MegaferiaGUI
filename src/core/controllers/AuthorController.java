/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.controllers;

import core.controllers.utils.Response;
import core.controllers.utils.Status;
import core.models.Author;

/**
 *
 * @author famil
 */
public class AuthorController {
    public static Response createAuthor(String id, String firstName, String lastName) {
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

            
            Author author = new Author(idLong, firstName, lastName);
            boolean added = personRepository.add(author);

            if (!added) {
                return new Response("No se pudo guardar el autor.", Status.INTERNAL_SERVER_ERROR);
            }

            return new Response("Autor creado correctamente.", Status.CREATED, author.clone());

        } catch (Exception ex) {
            return new Response("Error inesperado.", Status.INTERNAL_SERVER_ERROR);
        }
    }
}
