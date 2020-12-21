package segura.taylor.controlador.interfaz.tienda;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;

public class ControladorTienda {
    public VBox contenedorPrincipal;

    public void initialize() {
        mostrarListasReproduccion();
    }

    private void limpiarPantalla() {
        contenedorPrincipal.getChildren().clear();
    }

    public void mostrarListasReproduccion() {
        limpiarPantalla();
        try {
            VBox root = FXMLLoader.load(getClass().getResource("../../../ui/ventanas/tienda/VistaListasReproduccion.fxml"));
            contenedorPrincipal.getChildren().add(root);
            //Expandir
            root.prefWidthProperty().bind(contenedorPrincipal.widthProperty());
            root.prefHeightProperty().bind(contenedorPrincipal.heightProperty());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void mostrarAlbumes() {
        limpiarPantalla();
        try {
            VBox root = FXMLLoader.load(getClass().getResource("../../../ui/ventanas/tienda/VistaAlbumes.fxml"));
            contenedorPrincipal.getChildren().add(root);
            //Expandir
            root.prefWidthProperty().bind(contenedorPrincipal.widthProperty());
            root.prefHeightProperty().bind(contenedorPrincipal.heightProperty());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void mostrarCanciones() {
        limpiarPantalla();
        try {
            VBox root = FXMLLoader.load(getClass().getResource("../../../ui/ventanas/tienda/VistaCanciones.fxml"));
            contenedorPrincipal.getChildren().add(root);
            //Expandir
            root.prefWidthProperty().bind(contenedorPrincipal.widthProperty());
            root.prefHeightProperty().bind(contenedorPrincipal.heightProperty());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
