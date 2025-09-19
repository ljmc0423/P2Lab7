/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab7p2;

import java.io.*;

/**
 *
 * @author ljmc2
 */
public class Almacenamiento {
    private static final String FILE_PATH = "biblioteca.dat";

    public static void guardarLista(Lista lista){
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))){
            oos.writeObject(lista);
        } catch(Exception e){
            e.getMessage();
        }
    }

    public static Lista cargarLista(){
        File f = new File(FILE_PATH);
        if(!f.exists()) return new Lista();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))){
            return (Lista) ois.readObject();
        } catch(Exception e){
            e.getMessage();
            return new Lista();
        }
    }
}
