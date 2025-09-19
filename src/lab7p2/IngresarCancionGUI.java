/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab7p2;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

/**
 *
 * @author ljmc2
 */

public class IngresarCancionGUI extends JFrame {
    private JTextField txtTitulo, txtArtista, txtDuracion, txtGenero;
    private JLabel lblPreviewImagen, lblArchivoMP3;
    private String rutaImagen, rutaAudio;//ruta wav tras conversión

    private Lista lista;
    private DefaultListModel<Cancion> modeloLista;
    private BibliotecaGUI biblioteca;

    public IngresarCancionGUI(BibliotecaGUI biblioteca, Lista lista, DefaultListModel<Cancion> modeloLista) {
        this.lista = lista;
        this.modeloLista = modeloLista;
        this.biblioteca = biblioteca;

        setTitle("Ingresar canción");
        setSize(500, 600);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().setBackground(Color.BLACK);
        setLayout(new BorderLayout(10, 10));

        JPanel panelCampos = new JPanel(new GridLayout(6, 2, 10, 10));
        panelCampos.setBackground(Color.BLACK);
        panelCampos.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panelCampos.add(crearLabel("Título:"));
        txtTitulo = crearTextField();
        panelCampos.add(txtTitulo);

        panelCampos.add(crearLabel("Artista:"));
        txtArtista = crearTextField();
        panelCampos.add(txtArtista);

        panelCampos.add(crearLabel("Género:"));
        txtGenero = crearTextField();
        panelCampos.add(txtGenero);

        JButton btnImagen = crearBoton("Subir Imagen");
        lblPreviewImagen = new JLabel("Sin imagen", SwingConstants.CENTER);
        lblPreviewImagen.setForeground(Color.GRAY);
        lblPreviewImagen.setBorder(BorderFactory.createLineBorder(Color.GREEN));
        panelCampos.add(btnImagen);
        panelCampos.add(lblPreviewImagen);
        btnImagen.addActionListener(e -> seleccionarImagen());

        JButton btnMP3 = crearBoton("Subir MP3");
        lblArchivoMP3 = new JLabel("Sin archivo", SwingConstants.LEFT);
        lblArchivoMP3.setForeground(Color.WHITE);
        panelCampos.add(btnMP3);
        panelCampos.add(lblArchivoMP3);
        btnMP3.addActionListener(e -> seleccionarAudio());

        add(panelCampos, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new GridLayout(1, 2, 10, 10));
        panelBotones.setBackground(Color.BLACK);
        panelBotones.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        JButton btnGuardar = crearBoton("Guardar");
        JButton btnCancelar = crearBoton("Cancelar");

        btnGuardar.addActionListener(e -> guardarCancion());
        btnCancelar.addActionListener(e -> cancelar());

        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);

        add(panelBotones, BorderLayout.SOUTH);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JLabel crearLabel(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setForeground(Color.GREEN);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 14));
        return lbl;
    }

    private JTextField crearTextField() {
        JTextField txt = new JTextField();
        txt.setBackground(Color.DARK_GRAY);
        txt.setForeground(Color.WHITE);
        txt.setCaretColor(Color.GREEN);
        return txt;
    }

    private JButton crearBoton(String texto) {
        JButton btn = new JButton(texto);
        btn.setBackground(new Color(0, 128, 0));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        return btn;
    }

    private void seleccionarImagen() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter("Imágenes", "jpg","png","jpeg"));
        if(chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION){
            File file = chooser.getSelectedFile();
            rutaImagen = file.getAbsolutePath();
            ImageIcon icon = new ImageIcon(rutaImagen);
            Image img = icon.getImage().getScaledInstance(100,100,Image.SCALE_SMOOTH);
            lblPreviewImagen.setIcon(new ImageIcon(img));
            lblPreviewImagen.setText("");
        }
    }

    private void seleccionarAudio() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter("Archivos MP3", "mp3"));
        if(chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION){
            File file = chooser.getSelectedFile();
            try {
                File wavFile = Mp3toWAV.convertMp3ToWav(file);
                rutaAudio = wavFile.getAbsolutePath();
                lblArchivoMP3.setText(file.getName());
            } catch(Exception e){
                JOptionPane.showMessageDialog(this,"Error al convertir audio: "+e.getMessage());
            }
        }
    }

    private void guardarCancion() {
        String titulo = txtTitulo.getText().trim();
        String artista = txtArtista.getText().trim();
        String genero = txtGenero.getText().trim();

        if (titulo.isEmpty() || artista.isEmpty() || genero.isEmpty() || rutaAudio == null) {
            JOptionPane.showMessageDialog(this, "Por favor llena todos los campos y selecciona un MP3.");
            return;
        }

        long duracionSeg = 0;
        try (AudioInputStream ais = AudioSystem.getAudioInputStream(new File(rutaAudio))) {
            AudioFormat format = ais.getFormat();
            duracionSeg = (long) (ais.getFrameLength() / format.getFrameRate());
        } catch (Exception e) {
            e.getMessage();
        }

        Cancion c = new Cancion(titulo, artista, rutaImagen, rutaAudio, genero, (int) duracionSeg);
        lista.agregar(c);
        modeloLista.addElement(c);

        Almacenamiento.guardarLista(lista);

        JOptionPane.showMessageDialog(this, "Canción agregada.");

        if (biblioteca != null) {
            biblioteca.setVisible(true);
        }

        this.dispose();
    }


    private void cancelar(){
        biblioteca.setVisible(true);
        this.dispose();
    }
}