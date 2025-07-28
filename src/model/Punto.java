package model;

public class Punto {
    private int fila;
    private int columna;

    public Punto(int fila, int columna) {
        this.fila = fila;
        this.columna = columna;
    }

    public int getFila() { return fila; }
    public int getColumna() { return columna; }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(!(o instanceof Punto)) return false;
        Punto p = (Punto) o;
        return fila == p.fila && columna == p.columna;
    }

    @Override
    public int hashCode() {
        return fila * 31 + columna;
    }
}
