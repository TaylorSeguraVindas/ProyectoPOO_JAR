package segura.taylor.ui.dialogos;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class VentanaContrasennaTemporal {
    private TextField txtCorreo;

    public void mostrar() {
        try {
            Stage window = new Stage();
            //This locks previous window interacivity until this one is closed.
            window.initModality(Modality.APPLICATION_MODAL);

            VBox root = FXMLLoader.load(getClass().getResource("../ventanas/VentanaContrasennaTemporal.fxml"));

            Button btnEnviar = (Button) root.lookup("#btnEnviar");
            txtCorreo = (TextField) root.lookup("#txtCorreo");

            btnEnviar.setOnAction(e -> {
                //Verificar correo
                segura.taylor.ui.dialogos.VentanaVerificarCorreo ventanaVerificarCorreo = new VentanaVerificarCorreo();
                boolean correoVerificado = ventanaVerificarCorreo.mostrar();

                if(correoVerificado) {
                    //Cambiar contraseña
                    segura.taylor.ui.dialogos.VentanaCambiarContrasenna ventanaCambiarContrasenna = new VentanaCambiarContrasenna();
                    ventanaCambiarContrasenna.mostrarForzado(txtCorreo.getText());
                }
                window.close();
            });

            Button btnVolver = (Button) root.lookup("#btnVolver");
            btnVolver.setOnAction(e -> { window.close(); });

            Scene escena = new Scene(root, 380, 400);

            window.setScene(escena);
            window.setTitle("Olvidé mi contraseña");
            window.setResizable(false);
            window.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
