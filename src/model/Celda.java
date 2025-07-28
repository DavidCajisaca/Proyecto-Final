package model;

public class Celda {
    private boolean transitable;

    public Celda(boolean transitable) {
        this.transitable = transitable;
    }

    public boolean esTransitable() {
        return transitable;
    }

    public void setTransitable(boolean transitable) {
        this.transitable = transitable;
    }
    public int getValor() {
    return transitable ? 1 : 0;
}

}
