/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.controllers;

import core.controllers.utils.Response;
import core.controllers.utils.Status;
import core.models.Manager;
import core.models.Publisher;
import core.models.repositories.IPersonRepository;
import core.models.repositories.IPublisherRepository;
import core.models.repositories.IRepositoryProvider;
import core.models.repositories.RepositoryProvider;
import java.util.ArrayList;

/**
 *
 * @author famil
 */
public class PublisherController {
    
    private static IRepositoryProvider repositoryProvider = RepositoryProvider.getProvider();
    private static IPersonRepository personRepository = repositoryProvider.getPersonRepository();
    private static IPublisherRepository publisherRepository = repositoryProvider.getPublisherRepository();

    public static void setRepositoryProvider(IRepositoryProvider customProvider) {
        if (customProvider != null) {
            repositoryProvider = customProvider;
            personRepository = customProvider.getPersonRepository();
            publisherRepository = customProvider.getPublisherRepository();
        }
    }
    
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
    
    public static Response listaEditoriales() {

        try {
            ArrayList<Publisher> publishers = publisherRepository.getAll();
            ArrayList<Object[]> rows = new ArrayList<>();

            
            if (publishers == null || publishers.isEmpty()) {
                return new Response("No hay editoriales registradas.", Status.NOT_FOUND);
            }

            ArrayList<Publisher> publisherCopies = new ArrayList<>();
            try {
                for (Publisher publisher : publishers) {
                    publisherCopies.add(publisher.clone());
                }
            } catch (CloneNotSupportedException cloneException) {
                return new Response("Error al copiar editoriales.", Status.INTERNAL_SERVER_ERROR);
            }

            
            for (Publisher publisher : publisherCopies) {
                Object[] fila = new Object[5];
                fila[0] = publisher.getNit();
                fila[1] = publisher.getName();
                fila[2] = publisher.getAddress();
                fila[3] = publisher.getManager() != null ? publisher.getManager().getFullname() : "-";
                fila[4] = publisher.getStandQuantity();
                rows.add(fila);
            }

            
            for (int i = 0; i < rows.size() - 1; i++) {
                for (int j = i + 1; j < rows.size(); j++) {

                    String nit1 = rows.get(i)[0].toString();
                    String nit2 = rows.get(j)[0].toString();

                    if (nit1.compareTo(nit2) > 0) {
                        Object[] temp = rows.get(i);
                        rows.set(i, rows.get(j));
                        rows.set(j, temp);
                    }
                }
            }

            return new Response("Editoriales obtenidas correctamente.", Status.OK, rows);

        } catch (Exception e) {
            return new Response("Error inesperado al consultar editoriales.", Status.INTERNAL_SERVER_ERROR);
        }
    }
}
