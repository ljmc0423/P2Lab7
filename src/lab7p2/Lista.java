/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab7p2;

import java.io.Serializable;

/**
 *
 * @author ljmc2
 */
public class Lista implements Serializable{
    Nodo inicio;
    
    public void agregar(Cancion cancion){
        Nodo nuevo=new Nodo(cancion);
        if(inicio==null){
            inicio=nuevo;
        }else{
            Nodo tmp=inicio;
            while(tmp.siguiente!=null){
                tmp=tmp.siguiente;
            }
            tmp.siguiente=nuevo;
        }
    }
    
    public void eliminar(String nombreCancion) {
        if (inicio == null){
            return;
        }

        if (inicio.cancion.getTitulo().equals(nombreCancion)) {
            inicio = inicio.siguiente;
            return;
        }

        Nodo actual = inicio;
        while (actual.siguiente != null) {
            if (actual.siguiente.cancion.getTitulo().equals(nombreCancion)) {
                actual.siguiente = actual.siguiente.siguiente;
                return;
            }
            actual = actual.siguiente;
        }
    }
    
    public Nodo getInicio(){
        return inicio;
    }
    
}
