/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.controllers;

import core.controllers.utils.Response;
import core.controllers.utils.Status;
import core.models.Stand;

/**
 *
 * @author famil
 */
public class StandController {
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
}
