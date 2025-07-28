import model.Laberinto;
import view.LaberintoView;
import controller.LaberintoController;

public class Main {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            Laberinto lab = new Laberinto(5,5);
            LaberintoView view = new LaberintoView();
            LaberintoController controller = new LaberintoController(lab, view);
            view.cargarTiemposDesdeCSV();
        });
    }
}
