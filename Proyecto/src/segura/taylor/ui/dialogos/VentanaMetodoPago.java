package segura.taylor.ui.dialogos;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class VentanaMetodoPago {
    private boolean metodoCorrecto = false;

    private TextField txtNumeroTarjeta;
    private TextField txtMes;
    private TextField txtAnno;
    private TextField txtCSV;

    public boolean mostrar() {
        try {
            Stage window = new Stage();
            //This locks previous window interacivity until this one is closed.
            window.initModality(Modality.APPLICATION_MODAL);

            VBox root = FXMLLoader.load(getClass().getResource("../ventanas/cliente/VentanaMetodoPago.fxml"));

            txtNumeroTarjeta = (TextField) root.lookup("#txtNumeroTarjeta");
            txtMes = (TextField) root.lookup("#txtMes");
            txtAnno = (TextField) root.lookup("#txtAnno");
            txtCSV = (TextField) root.lookup("#txtCSV");

            Button btnAceptar = (Button) root.lookup("#btnAceptar");
            btnAceptar.setOnAction(e -> {
                if(!errorEnCampos()) {
                    metodoCorrecto = true;
                    window.close();
                }
            });

            Button btnVolver = (Button) root.lookup("#btnVolver");
            btnVolver.setOnAction(e -> { window.close(); });

            Scene escena = new Scene(root, 380, 280);

            window.setScene(escena);
            window.setTitle("Ingreso método de pago");
            window.setResizable(false);
            window.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return metodoCorrecto;
    }

    private boolean errorEnCampos() {
        boolean error;

        //Info tarjeta
        if(txtNumeroTarjeta.getText().length() == 16 &&
                (txtCSV.getText().length() == 3 || txtCSV.getText().length() == 4)) {
            error = false;
        } else {
            segura.taylor.ui.dialogos.AlertDialog alertDialog = new segura.taylor.ui.dialogos.AlertDialog();
            alertDialog.mostrar("Error", "Hay un problema con el numero o CSV");
            error = true;
        }

        try {   //Mes
            Integer.parseInt(txtMes.getText());
        } catch (Exception e) {
            segura.taylor.ui.dialogos.AlertDialog alertDialog = new segura.taylor.ui.dialogos.AlertDialog();
            alertDialog.mostrar("Error", "Hay un problema con el mes");
            error = true;
        }

        try {   //Año
            Integer.parseInt(txtAnno.getText());
        } catch (Exception e) {
            segura.taylor.ui.dialogos.AlertDialog alertDialog = new AlertDialog();
            alertDialog.mostrar("Error", "Hay un problema con el año");
            error = true;
        }

        return error;
    }
}
