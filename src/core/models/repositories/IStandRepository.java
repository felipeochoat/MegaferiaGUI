/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package core.models.repositories;

import core.models.Stand;
import java.util.ArrayList;

/**
 * 
 * @author martin 
 */

public interface IStandRepository {

    Stand findById(long id);

    boolean add(Stand stand);

    ArrayList<Stand> getAll();
}