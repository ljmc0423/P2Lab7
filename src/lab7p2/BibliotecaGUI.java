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
    private Cancion cancionSeleccionada = null;//cancion marcada por select

    public BibliotecaGUI(ReproductorGUI reproductor, Lista lista) {
        this.reproductor = reproductor;
        this.lista = lista;

        setTitle("Tu Biblioteca");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(700, 500);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.BLACK);
        setLayout(new BorderLayout());

        modeloLista = new DefaultListModel<>();
        Nodo tmp = lista.getInicio();
        while(tmp != null){
            modeloLista.addElement(tmp.cancion);
            tmp = tmp.siguiente;
        }

        //lista de canciones
        listaVisual = new JList<>(modeloLista);
        listaVisual.setCellRenderer(new ListCellRenderer<Cancion>() {
            @Override
            public Component getListCellRendererComponent(JList<? extends Cancion> list, Cancion c, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                JPanel panel = new JPanel(new BorderLayout(10, 0));
                panel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

                //fondo segun estado de cancion(cliqueada/seleccionada)
                if(c.equals(cancionSeleccionada)){
                    panel.setBackground(new Color(255, 255, 255, 100)); // blanco semitransparente
                } else if(isSelected){
                    panel.setBackground(Color.DARK_GRAY.brighter());
                } else {
                    panel.setBackground(Color.DARK_GRAY);
                }

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
                info.setBackground(panel.getBackground()); // mismo fondo
                JLabel lblTitulo = new JLabel("Título: " + c.getTitulo());
                lblTitulo.setForeground(Color.GREEN);
                JLabel lblArtista = new JLabel("Artista: " + c.getArtista());
                lblArtista.setForeground(Color.GREEN);
                JLabel lblGenero = new JLabel("Género: " + c.getGenero());
                lblGenero.setForeground(Color.GREEN);
                JLabel lblDuracion = new JLabel("Duración: " + formatearDuracion(c.getDuracion()));
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
        JButton selectBtn = crearBoton("Select"); // NUEVO botón

        agregarBtn.addActionListener(e -> agregarCancion());
        eliminarBtn.addActionListener(e -> eliminarCancion());
        reproducirBtn.addActionListener(e -> reproducirCancion());
        regresarBtn.addActionListener(e -> regresar());
        selectBtn.addActionListener(e -> {
            int idx = listaVisual.getSelectedIndex();
            if(idx != -1){
                cancionSeleccionada = modeloLista.getElementAt(idx);
                listaVisual.repaint();
            } else {
                JOptionPane.showMessageDialog(this, "Selecciona primero una canción para marcarla.");
            }
        });

        panelBotones.add(agregarBtn);
        panelBotones.add(eliminarBtn);
        panelBotones.add(reproducirBtn);
        panelBotones.add(regresarBtn);
        panelBotones.add(selectBtn);

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
        if(cancionSeleccionada != null){
            if(reproductor.getTituloActual() != null &&
               reproductor.getTituloActual().equals(cancionSeleccionada.getTitulo())) {
                reproductor.stop();
                reproductor.reset();
            }

            lista.eliminar(cancionSeleccionada.getTitulo());
            modeloLista.removeElement(cancionSeleccionada);
            cancionSeleccionada = null; // limpiar selección
            Almacenamiento.guardarLista(lista);
        } else {
            JOptionPane.showMessageDialog(this,"Debes seleccionar una canción con 'Select'.");
        }
    }

    private void reproducirCancion(){
        if(cancionSeleccionada != null){
            reproductor.setVisible(true);
            reproductor.seleccionarCancion(cancionSeleccionada);
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this,"Debes seleccionar una canción con 'Select'.");
        }
    }

    private void regresar(){
        reproductor.setVisible(true);
        this.dispose();
    }

    //formato 0:00 en vez de segundos
    private String formatearDuracion(int segundos) {
        int min = segundos / 60;
        int seg = segundos % 60;
        return String.format("%d:%02d", min, seg);
    }
}
