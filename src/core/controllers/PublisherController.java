/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.controllers;

import core.controllers.utils.Response;
import core.controllers.utils.Status;
import core.models.Manager;
import core.models.Publisher;

/**
 *
 * @author famil
 */
public class PublisherController {
    public static Response createPublisher(String nit, String name, String address, String managerIdStr) {
        try {

            if (nit.trim().isEmpty()) {
                return new Response("NIT no puede estar vacio", Status.BAD_REQUEST);
            }
            if (name.trim().isEmpty()) {
                return new Response("nombre no puede estar vacio", Status.BAD_REQUEST);
            }
            if (address.trim().isEmpty()) {
                return new Response("La dirección no puede estar vacio", Status.BAD_REQUEST);
            }
            if (managerIdStr.equals("Seleccione uno...")) {
                return new Response("Tiene que elegir un gerente", Status.BAD_REQUEST);
            }
            
            
            if (!nit.matches("\\d{3}\\.\\d{3}\\.\\d{3}-\\d")) {
                return new Response("Formato de NIT incorrecto. Formato esperado XXX.XXX.XXX-X", Status.BAD_REQUEST);
            }

            
            long managerId = Long.parseLong(managerIdStr.trim());
            Manager manager = (Manager) personRepository.findById(managerId);   // <-- CAMBIO

            if (manager == null) {
                return new Response("El manager no existe", Status.BAD_REQUEST);
            }

            
            if (publisherRepository.findByNit(nit) != null) {   // <-- CAMBIO
                return new Response("Una editorial ya contiene este NIT", Status.BAD_REQUEST);
            }

            
            Publisher publisher = new Publisher(nit, name, address, manager);

            boolean added = publisherRepository.add(publisher);  // <-- CAMBIO

            if (!added) {
                return new Response("La editorial no se pudo guardar", Status.INTERNAL_SERVER_ERROR);
            }

            return new Response("Editorial creada con exito", Status.CREATED, publisher.clone());

        } catch (Exception ex) {
            return new Response("Error inesperado", Status.INTERNAL_SERVER_ERROR);
        }
    }
}
