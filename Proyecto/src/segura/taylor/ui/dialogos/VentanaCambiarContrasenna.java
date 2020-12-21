package segura.taylor.ui.dialogos;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import segura.taylor.bl.entidades.Usuario;
import segura.taylor.controlador.ControladorGeneral;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VentanaCambiarContrasenna {
    TextField txtContrasennaActual;
    TextField txtContrasennaNueva;

    public void mostrar() {
        try {
            Stage window = new Stage();
            //This locks previous window interacivity until this one is closed.
            window.initModality(Modality.APPLICATION_MODAL);

            VBox root = FXMLLoader.load(getClass().getResource("../ventanas/VentanaCambiarContrasenna.fxml"));

            txtContrasennaActual = (TextField) root.lookup("#txtContrasennaActual");
            txtContrasennaNueva = (TextField) root.lookup("#txtContrasennaNueva");

            Button btnActualizar = (Button) root.lookup("#btnActualizar");
            btnActualizar.setOnAction(e -> {
                if(verificarCambio()) {
                    ControladorGeneral.instancia.getGestor().modificarContrasennaUsuario(ControladorGeneral.instancia.getIdUsuarioIngresado(), txtContrasennaNueva.getText());

                    segura.taylor.ui.dialogos.AlertDialog alertDialog = new segura.taylor.ui.dialogos.AlertDialog();
                    alertDialog.mostrar("Éxito", "Contraseña actualizada correctamente");

                    window.close();
                }
            });

            Button btnVolver = (Button) root.lookup("#btnVolver");
            btnVolver.setOnAction(e -> { window.close(); });

            Scene escena = new Scene(root, 380, 340);

            window.setScene(escena);
            window.setTitle("Cambiar contraseña");
            window.setResizable(false);
            window.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void mostrarForzado(String correoCliente) {
        try {
            Stage window = new Stage();
            //This locks previous window interacivity until this one is closed.
            window.initModality(Modality.APPLICATION_MODAL);

            VBox root = FXMLLoader.load(getClass().getResource("../ventanas/VentanaCambiarContrasenna.fxml"));

            txtContrasennaActual = (TextField) root.lookup("#txtContrasennaActual");

            //Aquí cambia
            Optional<Usuario> usuarioEncontrado = ControladorGeneral.instancia.getGestor().buscarUsuarioPorCorreo(correoCliente);

            if(usuarioEncontrado.isPresent()) { //Bloquear la contraseña actual
                txtContrasennaActual.setText(usuarioEncontrado.get().getContrasenna());
                txtContrasennaActual.setDisable(true);
            }

            txtContrasennaNueva = (TextField) root.lookup("#txtContrasennaNueva");

            Button btnActualizar = (Button) root.lookup("#btnActualizar");
            btnActualizar.setOnAction(e -> {
                if(verificarCambioForzado()) {
                    ControladorGeneral.instancia.getGestor().modificarContrasennaUsuario(usuarioEncontrado.get().getId(), txtContrasennaNueva.getText());

                    segura.taylor.ui.dialogos.AlertDialog alertDialog = new segura.taylor.ui.dialogos.AlertDialog();
                    alertDialog.mostrar("Éxito", "Contraseña actualizada correctamente");

                    window.close();
                }
            });

            Button btnVolver = (Button) root.lookup("#btnVolver");
            btnVolver.setOnAction(e -> { window.close(); });

            Scene escena = new Scene(root, 380, 340);

            window.setScene(escena);
            window.setTitle("Cambiar contraseña");
            window.setResizable(false);
            window.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean verificarCambio() {
        //Contraseña actual es correcta?
        if(!ControladorGeneral.instancia.getUsuarioIngresado().getContrasenna().equals(txtContrasennaActual.getText())) {
            segura.taylor.ui.dialogos.AlertDialog alertDialog = new segura.taylor.ui.dialogos.AlertDialog();
            alertDialog.mostrar("Error", "La contraseña actual ingresada es incorrecta");
            return false;
        }

        //Nueva contraseña es válida? Verificar contraseña (Entre 8 y 12 caracteres, como minimo: una letra minuscula, una letra mayuscula y un caracter especial)
        Pattern contrasennaPatt = Pattern.compile("^(?=.*[a-z])+(?=.*[A-Z])+(?=.*[0-9!_.@$!%*#?&])+[a-zA-Z0-9!_.@$!%*#?&]{8,12}$");
        Matcher matchContrasenna = contrasennaPatt.matcher(txtContrasennaNueva.getText());

        if(!matchContrasenna.matches()) {
            segura.taylor.ui.dialogos.AlertDialog alertDialog = new segura.taylor.ui.dialogos.AlertDialog();
            alertDialog.mostrar("Error", "Hay un problema con la contraseña, debe tener entre 8 y 12 caracteres, una letra minúscula, una mayúscula y un caracter especial");
            return false;
        }

        return true;
    }

    private boolean verificarCambioForzado() {
        //Nueva contraseña es válida? Verificar contraseña (Entre 8 y 12 caracteres, como minimo: una letra minuscula, una letra mayuscula y un caracter especial)
        Pattern contrasennaPatt = Pattern.compile("^(?=.*[a-z])+(?=.*[A-Z])+(?=.*[0-9!_.@$!%*#?&])+[a-zA-Z0-9!_.@$!%*#?&]{8,12}$");
        Matcher matchContrasenna = contrasennaPatt.matcher(txtContrasennaNueva.getText());

        if(!matchContrasenna.matches()) {
            segura.taylor.ui.dialogos.AlertDialog alertDialog = new AlertDialog();
            alertDialog.mostrar("Error", "Hay un problema con la contraseña, debe tener entre 8 y 12 caracteres, una letra minúscula, una mayúscula y un caracter especial");
            return false;
        }

        return true;
    }
}
