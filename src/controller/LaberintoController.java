package controller;

import java.io.FileWriter;
import java.io.IOException;
import model.*;
import view.LaberintoView;

public class LaberintoController {

    private Laberinto modelo;
    private LaberintoView vista;
    private BuscadorRuta buscador;

    public LaberintoController(Laberinto modelo, LaberintoView vista) {
        this.modelo = modelo;
        this.vista = vista;
        this.buscador = new BuscadorRuta(modelo);
        initView();
        initController();
    }

    private void initView() {
        vista.setSize(600, 600);
        vista.setLocationRelativeTo(null);
        vista.setVisible(true);
    }

    private void initController() {
        vista.getBtnResolver2Dir().addActionListener(e -> ejecutarBusqueda("rec2"));
        vista.getBtnResolver4Dir().addActionListener(e -> ejecutarBusqueda("rec4"));
        vista.getBtnResolverBFS().addActionListener(e -> ejecutarBusqueda("bfs"));
        vista.getBtnResolverDFS().addActionListener(e -> ejecutarBusqueda("dfs"));
      vista.getBtnGenerarLaberinto().addActionListener(e -> {
    int filas = Integer.parseInt(vista.getTxtFilas().getText());
    int columnas = Integer.parseInt(vista.getTxtColumnas().getText());
    Laberinto nuevo = new Laberinto(filas, columnas);
    vista.setLaberinto(nuevo); 
});



    }

    private void ejecutarBusqueda(String metodo) {
    modelo = vista.leerLaberintoDesdeUI();
    buscador.setLaberinto(modelo);  

    if (modelo.getInicio() == null || modelo.getDestino() == null) {
        vista.mostrarMensaje("Debe definir los puntos de inicio y destino.");
        return;
    }

    Ruta ruta = null;
    long inicio = System.nanoTime();

    switch(metodo) {
        case "rec2": ruta = buscador.buscarRecursivo2Dir(); break;
        case "rec4": ruta = buscador.buscarRecursivo4Dir(); break;
        case "bfs": ruta = buscador.buscarBFS(); break;
        case "dfs": ruta = buscador.buscarDFS(); break;
    }

    long fin = System.nanoTime();
    double tiempoMs = (fin - inicio) / 1_000_000.0;

    if (ruta != null) {
        vista.mostrarRuta(ruta);
        guardarTiempo(metodo, tiempoMs);
    } else {
        vista.mostrarMensaje("No se encontró ruta con " + metodo);
    }
}

    private void guardarTiempo(String metodo, double tiempo) {
    try (FileWriter fw = new FileWriter("tiempos.csv", true)) { 
        fw.write(metodo + "," + String.format("%.3f", tiempo) + "\n");
    } catch (IOException e) {
        vista.mostrarMensaje("Error guardando tiempos: " + e.getMessage());
    }
}

}
