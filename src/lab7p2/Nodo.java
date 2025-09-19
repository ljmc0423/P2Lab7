/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package lab7p2;

import java.io.Serializable;

/**
 *
 * @author ljmc2
 */
public class Nodo implements Serializable{

    Cancion cancion;
    Nodo siguiente;
    public Nodo(Cancion cancion){
        this.cancion=cancion;
        this.siguiente=null;
    }
    
}
