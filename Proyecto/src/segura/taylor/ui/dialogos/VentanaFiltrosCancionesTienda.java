package segura.taylor.ui.dialogos;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import segura.taylor.controlador.interfaz.tienda.ControladorVistaCanciones;

public class VentanaFiltrosCancionesTienda {
    public CheckBox chbFiltroNombre;
    public CheckBox chbFiltroArtista;
    public CheckBox chbFiltroGenero;

    public void mostrar() {
        try {
            Stage window = new Stage();
            //This locks previous window interacivity until this one is closed.
            window.initModality(Modality.APPLICATION_MODAL);

            VBox root = FXMLLoader.load(getClass().getResource("../ventanas/tienda/VentanaFiltroCanciones.fxml"));

            chbFiltroNombre = (CheckBox) root.lookup("#chbFiltroNombre");
            chbFiltroNombre.setOnAction(e -> cambioFiltroNombre());

            chbFiltroArtista = (CheckBox) root.lookup("#chbFiltroArtista");
            chbFiltroArtista.setOnAction(e -> cambioFiltroArtista());

            chbFiltroGenero = (CheckBox) root.lookup("#chbFiltroGenero");
            chbFiltroGenero.setOnAction(e -> cambioFiltroGenero());

            //Actualizar campos
            chbFiltroNombre.setSelected(ControladorVistaCanciones.filtrandoPorNombre);
            chbFiltroArtista.setSelected(ControladorVistaCanciones.filtrandoPorArtista);
            chbFiltroGenero.setSelected(ControladorVistaCanciones.filtrandoPorGenero);

            Button btnVolver = (Button) root.lookup("#btnVolver");
            btnVolver.setOnAction(e -> { window.close(); });

            Scene escena = new Scene(root, 380, 280);

            window.setScene(escena);
            window.setTitle("Filtros cancion");
            window.setResizable(false);
            window.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cambioFiltroNombre() {
        ControladorVistaCanciones.filtrandoPorNombre = chbFiltroNombre.isSelected();
    }

    public void cambioFiltroArtista() {
        ControladorVistaCanciones.filtrandoPorArtista = chbFiltroArtista.isSelected();
    }

    public void cambioFiltroGenero() {
        ControladorVistaCanciones.filtrandoPorGenero = chbFiltroGenero.isSelected();
    }
}
