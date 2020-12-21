package segura.taylor.ui.dialogos;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class VentanaSeleccionarArtista {
    private int idArtista = -1;

    public int mostrar() {
        try {
            Stage window = new Stage();
            //This locks previous window interacivity until this one is closed.
            window.initModality(Modality.APPLICATION_MODAL);

            VBox root = FXMLLoader.load(getClass().getResource("../ventanas/VentanaSeleccionarArtista.fxml"));

            ComboBox txtArtista = (ComboBox) root.lookup("#txtArtista");
            Button btnAceptar = (Button) root.lookup("#btnAceptar");
            btnAceptar.setOnAction(e -> {
                String[] valorCancion = txtArtista.getValue().toString().split("-");
                idArtista = Integer.parseInt(valorCancion[0]);
                window.close();
            });

            Button btnVolver = (Button) root.lookup("#btnVolver");
            btnVolver.setOnAction(e -> { window.close(); });

            Scene escena = new Scene(root, 380, 280);

            window.setScene(escena);
            window.setTitle("Seleccione un artista");
            window.setResizable(false);
            window.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return idArtista;
    }
}
