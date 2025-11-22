/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.controllers;

import core.controllers.utils.Response;
import core.controllers.utils.Status;
import core.models.Publisher;
import core.models.Stand;
import core.models.repositories.IPublisherRepository;
import core.models.repositories.IRepositoryProvider;
import core.models.repositories.IStandRepository;
import core.models.repositories.RepositoryProvider;
import java.util.ArrayList;

/**
 *
 * @author famil
 */
public class StandController {
    
    private static IRepositoryProvider repositoryProvider = RepositoryProvider.getProvider();
    private static IStandRepository standRepository = repositoryProvider.getStandRepository();
    private static IPublisherRepository publisherRepository = repositoryProvider.getPublisherRepository();

    public static void setRepositoryProvider(IRepositoryProvider customProvider) {
        if (customProvider != null) {
            repositoryProvider = customProvider;
            standRepository = customProvider.getStandRepository();
            publisherRepository = customProvider.getPublisherRepository();
        }
    }
    
    
    public static Response createStand(String id, String price) {
        try {
            long idLong;
            double priceDouble;

            
            if (id == null || id.trim().isEmpty()) {
                return new Response("El ID no puede estar vacío.", Status.BAD_REQUEST);
            }

            if (id.trim().length() > 15) {
                return new Response("El ID debe tener máximo 15 dígitos.", Status.BAD_REQUEST);
            }

            try {
                idLong = Long.parseLong(id.trim());

                if (idLong < 0) {
                    return new Response("El ID debe ser positivo.", Status.BAD_REQUEST);
                }

            } catch (NumberFormatException e) {
                return new Response("El ID debe ser numérico.", Status.BAD_REQUEST);
            }

            
            if (price == null || price.trim().isEmpty()) {
                return new Response("El precio no puede estar vacío.", Status.BAD_REQUEST);
            }

            try {
                priceDouble = Double.parseDouble(price.trim());

                if (priceDouble <= 0) {
                    return new Response("El precio debe ser mayor que 0.", Status.BAD_REQUEST);
                }

            } catch (NumberFormatException e) {
                return new Response("El precio debe ser numérico.", Status.BAD_REQUEST);
            }

            
            if (standRepository.findById(idLong) != null) {
                return new Response("Ya existe un stand con este ID.", Status.BAD_REQUEST);
            }

            
            Stand stand = new Stand(idLong, priceDouble);

            boolean added = standRepository.add(stand);
            if (!added) {
                return new Response("No se pudo guardar el stand.", Status.INTERNAL_SERVER_ERROR);
            }

            return new Response("Stand creado correctamente.", Status.CREATED, stand.clone());

        } catch (Exception ex) {
            return new Response("Error inesperado.", Status.INTERNAL_SERVER_ERROR);
        }
    }
    
    
    public static Response buyStands(ArrayList<String> standTexts, ArrayList<String> editorialTexts) {

        try {

            if (standTexts == null || standTexts.isEmpty()) {
                return new Response("Debe seleccionar al menos un stand.", Status.BAD_REQUEST);
            }

            if (editorialTexts == null || editorialTexts.isEmpty()) {
                return new Response("Debe seleccionar al menos una editorial.", Status.BAD_REQUEST);
            }

            ArrayList<Stand> stands = new ArrayList<>();
            ArrayList<Publisher> publishers = new ArrayList<>();

            
            ArrayList<Long> usedStandIds = new ArrayList<>();

            for (String s : standTexts) {
                String idStr = s.split("-")[0].trim();

                long id;
                try {
                    id = Long.parseLong(idStr);
                } catch (Exception e) {
                    return new Response("ID de stand inválido: " + idStr, Status.BAD_REQUEST);
                }

                if (usedStandIds.contains(id)) {
                    return new Response("No puede repetir el mismo stand.", Status.BAD_REQUEST);
                }
                usedStandIds.add(id);

                Stand stand = standRepository.findById(id);
                if (stand == null) {
                    return new Response("El stand con ID " + id + " no existe.", Status.BAD_REQUEST);
                }

                stands.add(stand);
            }

            
            stands.sort((a, b) -> Long.compare(a.getId(), b.getId()));

            
            ArrayList<String> usedNits = new ArrayList<>();

            for (String e : editorialTexts) {

                int ini = e.indexOf("(");
                int fin = e.indexOf(")");

                if (ini == -1 || fin == -1) {
                    return new Response("Formato inválido para editorial: " + e, Status.BAD_REQUEST);
                }

                String nit = e.substring(ini + 1, fin).trim();

                if (usedNits.contains(nit)) {
                    return new Response("No puede repetir la misma editorial.", Status.BAD_REQUEST);
                }
                usedNits.add(nit);

                Publisher pub = publisherRepository.findByNit(nit);
                if (pub == null) {
                    return new Response("La editorial con NIT " + nit + " no existe.", Status.BAD_REQUEST);
                }

                publishers.add(pub);
            }

            publishers.sort((a, b) -> a.getNit().compareTo(b.getNit()));

            for (Stand stand : stands) {
                for (Publisher pub : publishers) {
                    stand.addPublisher(pub);
                    pub.addStand(stand);
                }
            }

            return new Response("Compra realizada exitosamente.", Status.OK);

        } catch (Exception ex) {
            return new Response("Error inesperado.", Status.INTERNAL_SERVER_ERROR);
        }
    }

    public static Response listaStands() {

        try {
            ArrayList<Stand> stands = standRepository.getAll();
            ArrayList<Object[]> rows = new ArrayList<>();

            
            if (stands == null || stands.isEmpty()) {
                return new Response("No hay stands registrados.", Status.NOT_FOUND);
            }

            ArrayList<Stand> standCopies = new ArrayList<>();
            try {
                for (Stand stand : stands) {
                    standCopies.add(stand.clone());
                }
            } catch (CloneNotSupportedException cloneException) {
                return new Response("Error al copiar stands.", Status.INTERNAL_SERVER_ERROR);
            }

            
            for (Stand stand : standCopies) {

                String publishers = "";

                if (stand.getPublisherQuantity() > 0) {

                    publishers = stand.getPublishers().get(0).getName();

                    for (int i = 1; i < stand.getPublisherQuantity(); i++) {
                        publishers += ", " + stand.getPublishers().get(i).getName();
                    }
                }

                Object[] fila = new Object[4];
                fila[0] = stand.getId();
                fila[1] = stand.getPrice();
                fila[2] = stand.getPublisherQuantity() > 0 ? "Si" : "No";
                fila[3] = publishers;
                rows.add(fila);
            }

            
            for (int i = 0; i < rows.size() - 1; i++) {
                for (int j = i + 1; j < rows.size(); j++) {

                    int id1 = (int) rows.get(i)[0];
                    int id2 = (int) rows.get(j)[0];

                    if (id1 > id2) {
                        Object[] temp = rows.get(i);
                        rows.set(i, rows.get(j));
                        rows.set(j, temp);
                    }
                }
            }

            return new Response("Stands obtenidos correctamente.", Status.OK, rows);

        } catch (Exception e) {
            return new Response("Error inesperado al consultar stands.", Status.INTERNAL_SERVER_ERROR);
        }
    }
}
