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
public class Cancion implements Serializable{
    private String titulo;;
    private String artista;
    private String imagenPath;
    private String ruta;
    private String genero;
    private int duracion;
    
    public Cancion(String titulo, String artista, String imagenPath, String ruta, String genero, int duracion){
        this.titulo=titulo;
        this.artista=artista;
        this.imagenPath=imagenPath;
        this.ruta=ruta;
        this.genero=genero;
        this.duracion=duracion;
    }
    
    @Override
    public String toString(){
        return titulo + " - " + artista;
    }
    
    public String getTitulo(){
        return titulo;
    }
    public String getArtista(){
        return artista;
    }
    public String getImagenPath(){
        return imagenPath;
    }

    public String getRuta() {
        return ruta;
    }

    public String getGenero() {
        return genero;
    }

    public int getDuracion() {
        return duracion;
    }
    
    
    
    
}
