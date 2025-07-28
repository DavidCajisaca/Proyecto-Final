package view;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.swing.*;
import model.Laberinto;
import model.Punto;
import model.Ruta;

public class LaberintoView extends JFrame {

    private JPanel panelControles;
    private JButton btnGenerarLaberinto;
    private JButton btnResolver2Dir;
    private JButton btnResolver4Dir;
    private JButton btnResolverBFS;
    private JButton btnResolverDFS;
    private JTextField txtFilas;
    private JTextField txtColumnas;
    private JTextField txtInicioFila, txtInicioColumna;
    private JTextField txtDestinoFila, txtDestinoColumna;
    private JPanel gridPanel;
    private JCheckBox[][] checkboxes;
    private Laberinto laberinto;

    private JTextArea textAreaRuta;
    private JTable tablaTiempos;

    public LaberintoView() {
        setTitle("Laberinto MVC");

        panelControles = new JPanel();
        panelControles.setLayout(new GridLayout(3, 4, 5, 5));

        panelControles.add(new JLabel("Filas:"));
        txtFilas = new JTextField("5");
        panelControles.add(txtFilas);

        panelControles.add(new JLabel("Columnas:"));
        txtColumnas = new JTextField("5");
        panelControles.add(txtColumnas);

        panelControles.add(new JLabel("Inicio (fila,col):"));
        txtInicioFila = new JTextField("0");
        txtInicioColumna = new JTextField("0");
        panelControles.add(txtInicioFila);
        panelControles.add(txtInicioColumna);

        panelControles.add(new JLabel("Destino (fila,col):"));
        txtDestinoFila = new JTextField("4");
        txtDestinoColumna = new JTextField("4");
        panelControles.add(txtDestinoFila);
        panelControles.add(txtDestinoColumna);

        btnGenerarLaberinto = new JButton("Generar Laberinto");
        btnResolver2Dir = new JButton("Resolver Rec 2 Dir");
        btnResolver4Dir = new JButton("Resolver Rec 4 Dir");
        btnResolverBFS = new JButton("Resolver BFS");
        btnResolverDFS = new JButton("Resolver DFS");

        JPanel botonesPanel = new JPanel();
        botonesPanel.add(btnGenerarLaberinto);
        botonesPanel.add(btnResolver2Dir);
        botonesPanel.add(btnResolver4Dir);
        botonesPanel.add(btnResolverBFS);
        botonesPanel.add(btnResolverDFS);

        textAreaRuta = new JTextArea(10, 30);
        textAreaRuta.setEditable(false);

        gridPanel = new JPanel();

        tablaTiempos = new JTable(new String[][] {}, new String[] {"Método", "Tiempo (ms)"});

        JScrollPane scrollRuta = new JScrollPane(textAreaRuta);
        JScrollPane scrollTabla = new JScrollPane(tablaTiempos);

        Container cp = getContentPane();
        cp.setLayout(new BorderLayout());
        cp.add(panelControles, BorderLayout.NORTH);
        cp.add(gridPanel, BorderLayout.CENTER);
        cp.add(botonesPanel, BorderLayout.SOUTH);
        cp.add(scrollRuta, BorderLayout.EAST);
        cp.add(scrollTabla, BorderLayout.WEST);

        generarGrid(5, 5);
    }

    public void generarGrid(int filas, int columnas) {
        gridPanel.removeAll();
        gridPanel.setLayout(new GridLayout(filas, columnas));
        checkboxes = new JCheckBox[filas][columnas];
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                JCheckBox cb = new JCheckBox();
                cb.setSelected(true);
                checkboxes[i][j] = cb;
                gridPanel.add(cb);
            }
        }
        gridPanel.revalidate();
        gridPanel.repaint();
    }

    public Laberinto leerLaberintoDesdeUI() {
        int filas = Integer.parseInt(txtFilas.getText());
        int columnas = Integer.parseInt(txtColumnas.getText());

        Laberinto lab = new Laberinto(filas, columnas);

        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                boolean transitable = checkboxes[i][j].isSelected();
                lab.setCelda(i, j, transitable);
            }
        }

        int inicioFila = Integer.parseInt(txtInicioFila.getText());
        int inicioCol = Integer.parseInt(txtInicioColumna.getText());
        int destinoFila = Integer.parseInt(txtDestinoFila.getText());
        int destinoCol = Integer.parseInt(txtDestinoColumna.getText());

        lab.setInicio(new Punto(inicioFila, inicioCol));
        lab.setDestino(new Punto(destinoFila, destinoCol));

        this.laberinto = lab;
        return lab;
    }

    public void setLaberinto(Laberinto laberinto) {
        this.laberinto = laberinto;

        gridPanel.removeAll();
        gridPanel.setLayout(new GridLayout(laberinto.getFilas(), laberinto.getColumnas()));

        checkboxes = new JCheckBox[laberinto.getFilas()][laberinto.getColumnas()];

        for (int i = 0; i < laberinto.getFilas(); i++) {
            for (int j = 0; j < laberinto.getColumnas(); j++) {
                JCheckBox cb = new JCheckBox();
                cb.setSelected(laberinto.getCeldas()[i][j].esTransitable());
                checkboxes[i][j] = cb;
                gridPanel.add(cb);
            }
        }

        gridPanel.revalidate();
        gridPanel.repaint();
    }

    public void mostrarRuta(Ruta ruta) {
        StringBuilder sb = new StringBuilder();
        sb.append("Ruta encontrada:\n");
        for (Punto p : ruta.getPuntos()) {
            sb.append("(").append(p.getFila()).append(",").append(p.getColumna()).append(") ");
        }
        textAreaRuta.setText(sb.toString());

        for (int i = 0; i < laberinto.getFilas(); i++)
            for (int j = 0; j < laberinto.getColumnas(); j++)
                checkboxes[i][j].setBackground(null);

        for (Punto p : ruta.getPuntos())
            checkboxes[p.getFila()][p.getColumna()].setBackground(Color.CYAN);
    }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }

    public void cargarTiemposDesdeCSV() {
        List<String[]> filas = new ArrayList<>();
        try (Scanner sc = new Scanner(new File("tiempos.csv"))) {
            while (sc.hasNextLine()) {
                String linea = sc.nextLine();
                String[] partes = linea.split(",");
                if (partes.length == 2)
                    filas.add(partes);
            }
        } catch (IOException e) {
        }
        String[][] datos = filas.toArray(new String[0][]);
        String[] columnas = {"Método", "Tiempo (ms)"};
        tablaTiempos.setModel(new javax.swing.table.DefaultTableModel(datos, columnas));
    }

    public JButton getBtnResolver2Dir() { return btnResolver2Dir; }
    public JButton getBtnResolver4Dir() { return btnResolver4Dir; }
    public JButton getBtnResolverBFS() { return btnResolverBFS; }
    public JButton getBtnResolverDFS() { return btnResolverDFS; }
    public JButton getBtnGenerarLaberinto() { return btnGenerarLaberinto; }

    public JTextField getTxtFilas() { return txtFilas; }
    public JTextField getTxtColumnas() { return txtColumnas; }
    public JTextField getTxtInicioFila() { return txtInicioFila; }
    public JTextField getTxtInicioColumna() { return txtInicioColumna; }
    public JTextField getTxtDestinoFila() { return txtDestinoFila; }
    public JTextField getTxtDestinoColumna() { return txtDestinoColumna; }
}
