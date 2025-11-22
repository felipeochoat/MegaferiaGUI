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
public class StandPurchaseController {

    private static ArrayList<String> selectedStands = new ArrayList<>();

    public static ArrayList<String> getSelectedStands() {
        return new ArrayList<>(selectedStands);
    }

    public static void clearSelection() {
        selectedStands.clear();
    }

    //MÉTODO PARA AÑADIR STANDS
    public static Response addStandToSelection(String standText) {

        if (standText == null || standText.trim().isEmpty()
                || standText.startsWith("Seleccione")) {
            return new Response("Debe seleccionar un stand válido.", 400);
        }

        if (selectedStands.contains(standText)) {
            return new Response("El stand ya fue agregado.", 400);
        }

        selectedStands.add(standText);
        return new Response("Stand agregado exitosamente.", 200, new ArrayList<>(selectedStands));
    }

    //MÉTODO PARA ELIMINAR STANDS
    public static Response removeStandFromSelection(String standText) {

        if (standText == null || standText.trim().isEmpty()
                || standText.startsWith("Seleccione")) {
            return new Response("Debe seleccionar un stand válido.", 400);
        }

        if (!selectedStands.contains(standText)) {
            return new Response("El stand no está en la lista.", 400);
        }

        selectedStands.remove(standText);

        return new Response("Stand eliminado correctamente.", 200, new ArrayList<>(selectedStands));
    }

}
