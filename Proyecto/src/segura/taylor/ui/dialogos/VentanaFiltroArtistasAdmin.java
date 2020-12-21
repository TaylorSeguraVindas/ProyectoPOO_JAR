package segura.taylor.ui.dialogos;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import segura.taylor.controlador.interfaz.admin.ControladorArtistasAdmin;

public class VentanaFiltroArtistasAdmin {
    public CheckBox chbFiltroNombre;
    public CheckBox chbFiltroAlias;
    public CheckBox chbFiltroNacionalidad;

    public void mostrar() {
        try {
            Stage window = new Stage();
            //This locks previous window interacivity until this one is closed.
            window.initModality(Modality.APPLICATION_MODAL);

            VBox root = FXMLLoader.load(getClass().getResource("../ventanas/admin/VentanaFiltroArtistas.fxml"));

            chbFiltroNombre = (CheckBox) root.lookup("#chbFiltroNombre");
            chbFiltroNombre.setOnAction(e -> cambioFiltroNombre());

            chbFiltroAlias = (CheckBox) root.lookup("#chbFiltroAlias");
            chbFiltroAlias.setOnAction(e -> cambioFiltroAlias());

            chbFiltroNacionalidad = (CheckBox) root.lookup("#chbFiltroNacionalidad");
            chbFiltroNacionalidad.setOnAction(e -> cambioFiltroNacionalidad());

            //Actualizar campos
            chbFiltroNombre.setSelected(ControladorArtistasAdmin.filtrandoPorNombre);
            chbFiltroAlias.setSelected(ControladorArtistasAdmin.filtrandoPorAlias);
            chbFiltroNacionalidad.setSelected(ControladorArtistasAdmin.filtrandoPorNacionalidad);

            Button btnVolver = (Button) root.lookup("#btnVolver");
            btnVolver.setOnAction(e -> { window.close(); });

            Scene escena = new Scene(root, 380, 280);

            window.setScene(escena);
            window.setTitle("Filtros artistas");
            window.setResizable(false);
            window.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cambioFiltroNombre() {
        ControladorArtistasAdmin.filtrandoPorNombre = chbFiltroNombre.isSelected();
    }

    public void cambioFiltroAlias() {
        ControladorArtistasAdmin.filtrandoPorAlias = chbFiltroAlias.isSelected();
    }

    public void cambioFiltroNacionalidad() {
        ControladorArtistasAdmin.filtrandoPorNacionalidad = chbFiltroNacionalidad.isSelected();
    }

}
