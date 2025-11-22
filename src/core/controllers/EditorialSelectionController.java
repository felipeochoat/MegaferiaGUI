/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.controllers;

import core.controllers.utils.Response;
import java.util.ArrayList;

/**
 *
 * @author famil
 */
public class EditorialSelectionController {

    private static ArrayList<String> selectedEditorials = new ArrayList<>();

    public static ArrayList<String> getSelectedEditorials() {
        return new ArrayList<>(selectedEditorials);
    }

    public static void clear() {
        selectedEditorials.clear();
    }

   
    public static Response addEditorial(String editorialText) {

        if (editorialText == null || editorialText.trim().isEmpty()
                || editorialText.startsWith("Seleccione")) {
            return new Response("Debe seleccionar una editorial válida.", 400);
        }

        if (selectedEditorials.contains(editorialText)) {
            return new Response("Esta editorial ya fue agregada.", 400);
        }

        selectedEditorials.add(editorialText);
        return new Response("Editorial agregada exitosamente.", 200,
                new ArrayList<>(selectedEditorials));
    }

   
    public static Response removeEditorial(String editorialText) {

        if (editorialText == null || editorialText.trim().isEmpty()
                || editorialText.startsWith("Seleccione")) {
            return new Response("Debe seleccionar una editorial válida.", 400);
        }

        if (!selectedEditorials.contains(editorialText)) {
            return new Response("La editorial no está en la lista.", 400);
        }

        selectedEditorials.remove(editorialText);
        return new Response("Editorial eliminada correctamente.", 200,
                new ArrayList<>(selectedEditorials));
    }

}
