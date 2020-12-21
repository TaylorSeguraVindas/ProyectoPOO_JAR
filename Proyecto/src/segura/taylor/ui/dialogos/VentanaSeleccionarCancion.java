package segura.taylor.ui.dialogos;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class VentanaSeleccionarCancion {
    private int idCancion = -1;

    public int mostrar() {
        try {
            Stage window = new Stage();
            //This locks previous window interacivity until this one is closed.
            window.initModality(Modality.APPLICATION_MODAL);

            VBox root = FXMLLoader.load(getClass().getResource("../ventanas/VentanaSeleccionarCancion.fxml"));

            ComboBox txtCancion = (ComboBox) root.lookup("#txtCancion");
            Button btnAceptar = (Button) root.lookup("#btnAceptar");
            btnAceptar.setOnAction(e -> {
                String[] valorCancion = txtCancion.getValue().toString().split("-");
                idCancion = Integer.parseInt(valorCancion[0]);
                window.close();
            });

            Button btnVolver = (Button) root.lookup("#btnVolver");
            btnVolver.setOnAction(e -> { window.close(); });

            Scene escena = new Scene(root, 380, 280);

            window.setScene(escena);
            window.setTitle("Seleccione una canción");
            window.setResizable(false);
            window.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return idCancion;
    }
}
