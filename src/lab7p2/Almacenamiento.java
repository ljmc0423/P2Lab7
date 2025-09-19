/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab7p2;

import java.io.*;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

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
        if(!f.exists()) {
            Lista lista = new Lista();

            String mp3Path = "src/res/Devuélveme a mi chica.mp3";
            String portadaPath = "src/res/thumbnail.jpg";
            String wavPath = null;
            int duracion = 0;

            try {
                File wavFile = Mp3toWAV.convertMp3ToWav(new File(mp3Path));
                wavPath = wavFile.getAbsolutePath();

                //duración de canción en segundos
                try (AudioInputStream ais = AudioSystem.getAudioInputStream(wavFile)) {
                    AudioFormat format = ais.getFormat();
                    duracion = (int)(ais.getFrameLength() / format.getFrameRate());
                }

            } catch(Exception e){
                e.getMessage();
            }

            Cancion defaultSong = new Cancion("Devuélveme a mi chica","Hombres G",
                portadaPath,wavPath,"Rock en español",duracion);

            lista.agregar(defaultSong);
            guardarLista(lista);
            return lista;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))){
            return (Lista) ois.readObject();
        } catch(Exception e){
            e.getMessage();
            return new Lista();
        }
    }
}
