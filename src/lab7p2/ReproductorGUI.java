/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab7p2;


import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 *
 * @author ljmc2
 */

public class ReproductorGUI extends JFrame {

    private JLabel lblImagen, lblTitulo, lblArtista, lblGenero, lblTiempo;
    private JSlider sldProgreso;
    private JButton playPauseBtn, stopBtn, abrirBtn;

    private String tituloActual = null;

    private Clip clip;
    private Timer timer;
    private boolean reproduciendo = false;
    private long posicionPausa = 0;

    public ReproductorGUI() {
        setTitle("Reproductor de Música");
        setSize(700, 650);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);
        getContentPane().setBackground(Color.BLACK);
        setLocationRelativeTo(null);

        abrirBtn = crearBoton("Abrir biblioteca");
        abrirBtn.setBounds(520, 20, 150, 30);
        abrirBtn.addActionListener(e -> abrirBiblioteca());
        add(abrirBtn);

        lblImagen = new JLabel("Sin portada", SwingConstants.CENTER);
        lblImagen.setForeground(Color.GRAY);
        lblImagen.setBorder(BorderFactory.createLineBorder(Color.GREEN));
        lblImagen.setBounds(200, 70, 300, 300);
        add(lblImagen);

        lblTitulo = crearLabelInfo("");
        lblTitulo.setBounds(200, 380, 300, 20);
        add(lblTitulo);

        lblArtista = crearLabelInfo("");
        lblArtista.setBounds(200, 405, 300, 20);
        add(lblArtista);

        lblGenero = crearLabelInfo("");
        lblGenero.setBounds(200, 430, 300, 20);
        add(lblGenero);

        lblTiempo = crearLabelInfo("00:00 / 00:00");
        lblTiempo.setBounds(200, 460, 300, 20);
        add(lblTiempo);

        sldProgreso = new JSlider();
        sldProgreso.setBounds(200, 490, 300, 20);
        sldProgreso.setForeground(new Color(0, 255, 0));
        sldProgreso.setBackground(Color.DARK_GRAY);
        sldProgreso.setValue(0);
        sldProgreso.setEnabled(false);
        add(sldProgreso);

        playPauseBtn = crearBoton("▶");
        playPauseBtn.setBounds(200, 520, 100, 40);
        playPauseBtn.addActionListener(e -> playPause());

        stopBtn = crearBoton("■");
        stopBtn.setBounds(400, 520, 100, 40);
        stopBtn.addActionListener(e -> stop());

        add(playPauseBtn);
        add(stopBtn);

        setVisible(true);
    }

    private JLabel crearLabelInfo(String texto) {
        JLabel lbl = new JLabel(texto, SwingConstants.CENTER);
        lbl.setForeground(Color.GREEN);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 14));
        return lbl;
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

    private void abrirBiblioteca() {
        Lista lista = Almacenamiento.cargarLista();
        new BibliotecaGUI(this, lista);
        this.setVisible(false);
    }

    public void seleccionarCancion(Cancion c) {
        tituloActual = c.getTitulo();
        try {
            if (clip != null) {
                clip.stop();
                clip.close();
            }
            File audio = new File(c.getRuta());
            AudioInputStream ais = AudioSystem.getAudioInputStream(audio);
            clip = AudioSystem.getClip();
            clip.open(ais);

            lblTitulo.setText(c.getTitulo());
            lblArtista.setText(c.getArtista());
            lblGenero.setText(c.getGenero());

            if (!c.getImagenPath().isEmpty()) {
                ImageIcon icon = new ImageIcon(new ImageIcon(c.getImagenPath())
                        .getImage().getScaledInstance(lblImagen.getWidth(), lblImagen.getHeight(), Image.SCALE_SMOOTH));
                lblImagen.setIcon(icon);
                lblImagen.setText("");
                lblImagen.setBorder(null);
            } else {
                lblImagen.setIcon(null);
                lblImagen.setText("Sin portada");
                lblImagen.setBorder(BorderFactory.createLineBorder(Color.GREEN));
            }

            int duracionSeg = (int) (clip.getMicrosecondLength() / 1_000_000);
            sldProgreso.setMaximum(duracionSeg);
            sldProgreso.setValue(0);

            //tiempo actual/total
            lblTiempo.setText("00:00 / " + formatTiempo(duracionSeg));

            posicionPausa = 0;
            reproduciendo = false;

            if (timer != null) timer.stop();
            timer = new Timer(500, e -> actualizarProgreso());

        } catch (Exception e) {
            e.getMessage();
        }
    }

    private void playPause() {
        if (clip == null) return;

        if (!reproduciendo) {
            clip.setMicrosecondPosition(posicionPausa);
            clip.start();
            reproduciendo = true;
            playPauseBtn.setText("⏸");
            timer.start();
        } else {
            posicionPausa = clip.getMicrosecondPosition();
            clip.stop();
            reproduciendo = false;
            playPauseBtn.setText("▶");
            timer.stop();
        }
    }

    public void stop() {
        if (clip == null) return;

        clip.stop();
        clip.setMicrosecondPosition(0);
        posicionPausa = 0;
        reproduciendo = false;
        playPauseBtn.setText("▶");
        sldProgreso.setValue(0);
        lblTiempo.setText("00:00 / " + formatTiempo(sldProgreso.getMaximum()));
        if (timer != null) timer.stop();
    }

    private void actualizarProgreso() {
        if (clip != null) {
            int seg = (int) (clip.getMicrosecondPosition() / 1_000_000);
            sldProgreso.setValue(seg);
            lblTiempo.setText(formatTiempo(seg) + " / " + formatTiempo(sldProgreso.getMaximum()));
            if (seg >= sldProgreso.getMaximum()) {
                stop();
            }
        }
    }

    private String formatTiempo(long seg) {
        long min = seg / 60;
        long s = seg % 60;
        return String.format("%02d:%02d", min, s);
    }

    public String getTituloActual() {
        return tituloActual;
    }

    public void reset() {
        tituloActual = null;
        if (clip != null) {
            clip.stop();
            clip.close();
            clip = null;
        }
        reproduciendo = false;
        posicionPausa = 0;

        lblTitulo.setText("");
        lblArtista.setText("");
        lblGenero.setText("");
        lblTiempo.setText("00:00 / 00:00");
        sldProgreso.setValue(0);

        lblImagen.setIcon(null);
        lblImagen.setText("Sin portada");
        lblImagen.setBorder(BorderFactory.createLineBorder(Color.GREEN));

        if (timer != null) timer.stop();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ReproductorGUI());
    }
}