/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab7p2;

import javax.swing.*;
import java.awt.*;

/**
 *
 * @author ljmc2
 */
public class BibliotecaGUI extends JFrame {
    private Lista lista;
    private ReproductorGUI reproductor;
    private DefaultListModel<Cancion> modeloLista;
    private JList<Cancion> listaVisual;

    public BibliotecaGUI(ReproductorGUI reproductor, Lista lista) {
        this.reproductor = reproductor;
        this.lista = lista;

        setTitle("Tu Biblioteca");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(700, 500);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.BLACK);
        setLayout(new BorderLayout());

        JLabel titulo = new JLabel("Tu Biblioteca", SwingConstants.CENTER);
        titulo.setForeground(Color.GREEN);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        add(titulo, BorderLayout.NORTH);

        // Modelo
        modeloLista = new DefaultListModel<>();
        Nodo tmp = lista.getInicio();
        while(tmp != null){
            modeloLista.addElement(tmp.cancion);
            tmp = tmp.siguiente;
        }

        // JList con renderer personalizado
        listaVisual = new JList<>(modeloLista);
        listaVisual.setCellRenderer(new ListCellRenderer<Cancion>() {
            @Override
            public Component getListCellRendererComponent(JList<? extends Cancion> list, Cancion c, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                JPanel panel = new JPanel(new BorderLayout(10, 0));
                panel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
                panel.setBackground(isSelected ? Color.DARK_GRAY.darker() : Color.DARK_GRAY);

                JLabel lblImg = new JLabel();
                if(c.getImagenPath() != null && !c.getImagenPath().isEmpty()){
                    ImageIcon icon = new ImageIcon(new ImageIcon(c.getImagenPath())
                            .getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH));
                    lblImg.setIcon(icon);
                } else {
                    lblImg.setText("Sin portada");
                    lblImg.setForeground(Color.GRAY);
                    lblImg.setHorizontalAlignment(SwingConstants.CENTER);
                    lblImg.setPreferredSize(new Dimension(60,60));
                    lblImg.setBorder(BorderFactory.createLineBorder(Color.GREEN));
                }
                panel.add(lblImg, BorderLayout.WEST);

                JPanel info = new JPanel(new GridLayout(4,1));
                info.setBackground(isSelected ? Color.DARK_GRAY.darker() : Color.DARK_GRAY);
                JLabel lblTitulo = new JLabel("Título: " + c.getTitulo());
                lblTitulo.setForeground(Color.GREEN);
                JLabel lblArtista = new JLabel("Artista: " + c.getArtista());
                lblArtista.setForeground(Color.GREEN);
                JLabel lblGenero = new JLabel("Género: " + c.getGenero());
                lblGenero.setForeground(Color.GREEN);
                JLabel lblDuracion = new JLabel("Duración: " + c.getDuracion() + " seg");
                lblDuracion.setForeground(Color.GREEN);
                info.add(lblTitulo);
                info.add(lblArtista);
                info.add(lblGenero);
                info.add(lblDuracion);

                panel.add(info, BorderLayout.CENTER);
                return panel;
            }
        });

        listaVisual.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaVisual.setBackground(Color.DARK_GRAY);
        listaVisual.setForeground(Color.GREEN);

        JScrollPane scroll = new JScrollPane(listaVisual);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel();
        panelBotones.setBackground(Color.BLACK);
        panelBotones.setLayout(new FlowLayout());

        JButton agregarBtn = crearBoton("Agregar");
        JButton eliminarBtn = crearBoton("Eliminar");
        JButton reproducirBtn = crearBoton("Reproducir");
        JButton regresarBtn = crearBoton("Regresar");

        agregarBtn.addActionListener(e -> agregarCancion());
        eliminarBtn.addActionListener(e -> eliminarCancion());
        reproducirBtn.addActionListener(e -> reproducirCancion());
        regresarBtn.addActionListener(e -> regresar());

        panelBotones.add(agregarBtn);
        panelBotones.add(eliminarBtn);
        panelBotones.add(reproducirBtn);
        panelBotones.add(regresarBtn);

        add(panelBotones, BorderLayout.SOUTH);

        setVisible(true);
    }

    private JButton crearBoton(String texto){
        JButton btn = new JButton(texto);
        btn.setBackground(new Color(0,128,0));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(120,40));
        return btn;
    }

    private void agregarCancion(){
        new IngresarCancionGUI(this, lista, modeloLista);
        
    }

    private void eliminarCancion(){
    int idx = listaVisual.getSelectedIndex();
    if(idx != -1){
        Cancion c = modeloLista.getElementAt(idx);

        // Detener si se está reproduciendo usando solo el título
        if(reproductor.getTituloActual() != null &&
            reproductor.getTituloActual().equals(c.getTitulo())) {
            reproductor.stop();
            reproductor.reset();
        }

        lista.eliminar(c.getTitulo());
        modeloLista.remove(idx);
        Almacenamiento.guardarLista(lista);
    } else {
        JOptionPane.showMessageDialog(this,"Selecciona una canción para eliminar.");
    }
}

    private void reproducirCancion(){
        int idx = listaVisual.getSelectedIndex();
        if(idx != -1){
            Cancion c = modeloLista.getElementAt(idx);
            reproductor.setVisible(true);
            reproductor.seleccionarCancion(c);
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this,"Selecciona una canción para reproducir.");
        }
    }

    private void regresar(){
        reproductor.setVisible(true);
        this.dispose();
    }
}