package model;

public class Laberinto {
    private Celda[][] matriz;
    private int filas, columnas;
    private Punto inicio, destino;

    public Laberinto(int filas, int columnas) {
        this.filas = filas;
        this.columnas = columnas;
        matriz = new Celda[filas][columnas];
        for(int i=0; i<filas; i++)
            for(int j=0; j<columnas; j++)
                matriz[i][j] = new Celda(true);
    }

    public void setCelda(int fila, int columna, boolean transitable) {
        matriz[fila][columna].setTransitable(transitable);
    }

    public Celda getCelda(int fila, int columna) {
        return matriz[fila][columna];
    }
public Celda[][] getCeldas() {
    return matriz;
}

    public int getFilas() { return filas; }
    public int getColumnas() { return columnas; }

    public void setInicio(Punto inicio) {
        this.inicio = inicio;
    }

    public void setDestino(Punto destino) {
        this.destino = destino;
    }

    public Punto getInicio() { return inicio; }
    public Punto getDestino() { return destino; }
}
