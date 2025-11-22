/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.controllers;

import core.controllers.utils.Response;
import core.controllers.utils.Status;
import core.models.Author;
import core.models.Manager;
import core.models.Narrator;
import core.models.repositories.IPersonRepository;
import core.models.repositories.IRepositoryProvider;
import core.models.repositories.RepositoryProvider;
import java.util.ArrayList;

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
    
    public static Response listaPersonas() {

        try {
            ArrayList<Author> authors = personRepository.getAuthors();
            ArrayList<Manager> managers = personRepository.getManagers();
            ArrayList<Narrator> narrators = personRepository.getNarrators();
            ArrayList<Object[]> rows = new ArrayList<>();

            
            if ((authors == null || authors.isEmpty())&& (managers == null || managers.isEmpty())&& (narrators == null || narrators.isEmpty())) {
                return new Response("No hay personas registradas.", Status.NOT_FOUND);
            }
            ArrayList<Author> authorCopies = new ArrayList<>();
            ArrayList<Manager> managerCopies = new ArrayList<>();
            ArrayList<Narrator> narratorCopies = new ArrayList<>();

            try {
                if (authors != null) {
                    for (Author author : authors) {
                        authorCopies.add(author.clone());
                    }
                }

                if (managers != null) {
                    for (Manager manager : managers) {
                        Manager copy = manager.clone();
                        if (manager.getPublisher() != null) {
                            copy.setPublisher(manager.getPublisher().clone());
                        }
                        managerCopies.add(copy);
                    }
                }

                if (narrators != null) {
                    for (Narrator narrator : narrators) {
                        narratorCopies.add(narrator.clone());
                    }
                }

            } catch (CloneNotSupportedException cloneException) {
                return new Response("Error al copiar personas.", Status.INTERNAL_SERVER_ERROR);
            }

           
            if (authors != null) {
                for (Author author : authorCopies) {
                    Object[] fila = new Object[5];
                    fila[0] = author.getId();
                    fila[1] = author.getFullname();
                    fila[2] = "Autor";
                    fila[3] = "-";
                    fila[4] = author.getBookQuantity();
                    rows.add(fila);
                }
            }

          
            if (managers != null) {
                for (Manager manager : managerCopies) {
                    Object[] fila = new Object[5];
                    fila[0] = manager.getId();
                    fila[1] = manager.getFullname();
                    fila[2] = "Gerente";
                    if (manager.getPublisher() != null) {
                        fila[3] = manager.getPublisher().getName();
                    } else {
                        fila[3] = "-";
                    }
                    fila[4] = 0; 
                    rows.add(fila);
                }
            }

   
            if (narrators != null) {
                for (Narrator narrator : narratorCopies) {
                    Object[] fila = new Object[5];
                    fila[0] = narrator.getId();
                    fila[1] = narrator.getFullname();
                    fila[2] = "Narrador";
                    fila[3] = "-";
                    fila[4] = narrator.getBookQuantity();
                    rows.add(fila);
                }
            }

       
            for (int i = 0; i < rows.size() - 1; i++) {
                for (int j = i + 1; j < rows.size(); j++) {

                    Object[] f1 = rows.get(i);
                    Object[] f2 = rows.get(j);

                    long id1 = (long) f1[0];  
                    long id2 = (long) f2[0];

                    if (id1 > id2) {
                        rows.set(i, f2);
                        rows.set(j, f1);
                    }
                }
            }

            return new Response("Personas obtenidas correctamente.", Status.OK, rows);

        } catch (Exception e) {
            return new Response("Error inesperado al consultar personas.", Status.INTERNAL_SERVER_ERROR);
        }
    }
    
}
