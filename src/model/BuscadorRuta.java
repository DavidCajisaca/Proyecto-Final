package model;

import java.util.*;

public class BuscadorRuta {

    private Laberinto laberinto;

    public BuscadorRuta(Laberinto laberinto) {
        this.laberinto = laberinto;
    }
    public void setLaberinto(Laberinto laberinto) {
        this.laberinto = laberinto;
    }

    public Ruta buscarRecursivo2Dir() {
        List<Punto> ruta = new ArrayList<>();
        if(buscar2Dir(laberinto.getInicio().getFila(), laberinto.getInicio().getColumna(), ruta)) {
            return new Ruta(ruta);
        }
        return null;
    }

    private boolean buscar2Dir(int fila, int col, List<Punto> ruta) {
        if(fila < 0 || col < 0 || fila >= laberinto.getFilas() || col >= laberinto.getColumnas()) return false;
        if(!laberinto.getCelda(fila, col).esTransitable()) return false;
        Punto actual = new Punto(fila, col);
        if(ruta.contains(actual)) return false; 
        ruta.add(actual);

        if(actual.equals(laberinto.getDestino())) return true;

        if(buscar2Dir(fila, col+1, ruta)) return true;
        if(buscar2Dir(fila+1, col, ruta)) return true;

        ruta.remove(ruta.size()-1); 
        return false;
    }

    public Ruta buscarRecursivo4Dir() {
        List<Punto> ruta = new ArrayList<>();
        if(buscar4Dir(laberinto.getInicio().getFila(), laberinto.getInicio().getColumna(), ruta)) {
            return new Ruta(ruta);
        }
        return null;
    }

    private boolean buscar4Dir(int fila, int col, List<Punto> ruta) {
        if(fila < 0 || col < 0 || fila >= laberinto.getFilas() || col >= laberinto.getColumnas()) return false;
        if(!laberinto.getCelda(fila, col).esTransitable()) return false;
        Punto actual = new Punto(fila, col);
        if(ruta.contains(actual)) return false;
        ruta.add(actual);

        if(actual.equals(laberinto.getDestino())) return true;

        // arriba
        if(buscar4Dir(fila-1, col, ruta)) return true;
        // derecha
        if(buscar4Dir(fila, col+1, ruta)) return true;
        // abajo
        if(buscar4Dir(fila+1, col, ruta)) return true;
        // izquierda
        if(buscar4Dir(fila, col-1, ruta)) return true;

        ruta.remove(ruta.size()-1);
        return false;
    }

    public Ruta buscarBFS() {
        int filas = laberinto.getFilas();
        int columnas = laberinto.getColumnas();

        boolean[][] visitado = new boolean[filas][columnas];
        Map<Punto, Punto> padres = new HashMap<>();

        Queue<Punto> cola = new LinkedList<>();
        Punto inicio = laberinto.getInicio();
        Punto destino = laberinto.getDestino();

        cola.add(inicio);
        visitado[inicio.getFila()][inicio.getColumna()] = true;

        while(!cola.isEmpty()) {
            Punto actual = cola.poll();
            if(actual.equals(destino)) {
                // reconstruir ruta
                List<Punto> ruta = new ArrayList<>();
                Punto p = actual;
                while(p != null) {
                    ruta.add(p);
                    p = padres.get(p);
                }
                Collections.reverse(ruta);
                return new Ruta(ruta);
            }

            int[][] movimientos = {{-1,0},{1,0},{0,-1},{0,1}};
            for(int[] m : movimientos) {
                int f = actual.getFila() + m[0];
                int c = actual.getColumna() + m[1];
                if(f >= 0 && c >= 0 && f < filas && c < columnas) {
                    if(!visitado[f][c] && laberinto.getCelda(f, c).esTransitable()) {
                        Punto vecino = new Punto(f, c);
                        cola.add(vecino);
                        visitado[f][c] = true;
                        padres.put(vecino, actual);
                    }
                }
            }
        }

        return null; 
    }

    public Ruta buscarDFS() {
        int filas = laberinto.getFilas();
        int columnas = laberinto.getColumnas();
        boolean[][] visitado = new boolean[filas][columnas];
        List<Punto> ruta = new ArrayList<>();

        if(buscarDFSRec(laberinto.getInicio(), visitado, ruta)) {
            return new Ruta(ruta);
        }
        return null;
    }

    private boolean buscarDFSRec(Punto actual, boolean[][] visitado, List<Punto> ruta) {
        int fila = actual.getFila();
        int col = actual.getColumna();

        if(fila < 0 || col < 0 || fila >= laberinto.getFilas() || col >= laberinto.getColumnas()) return false;
        if(!laberinto.getCelda(fila, col).esTransitable()) return false;
        if(visitado[fila][col]) return false;

        visitado[fila][col] = true;
        ruta.add(actual);

        if(actual.equals(laberinto.getDestino())) return true;

        int[][] movimientos = {{-1,0},{1,0},{0,-1},{0,1}};

        for(int[] m : movimientos) {
            int nf = fila + m[0];
            int nc = col + m[1];
            if(buscarDFSRec(new Punto(nf,nc), visitado, ruta)) return true;
        }

        ruta.remove(ruta.size()-1);
        return false;
    }
}
