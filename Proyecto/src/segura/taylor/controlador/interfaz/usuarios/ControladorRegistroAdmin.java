package segura.taylor.controlador.interfaz.usuarios;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import segura.taylor.controlador.ControladorGeneral;
import segura.taylor.ui.dialogos.AlertDialog;
import segura.taylor.ui.dialogos.VentanaCambiarContrasenna;

import java.io.File;
import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ControladorRegistroAdmin {
    public static Stage ventana;
    public static int idAdmin;
    public static boolean modificando;
    public static String urlImagenPerfil;

    Stage window;

    private String recursoImagenPerfil = "";

    public Label lblTitulo;

    public TextField txtCorreo;
    public PasswordField txtContrasenna;
    public TextField txtNombre;
    public TextField txtApellidos;
    public TextField txtNombreUsuario;

    public ImageView imagenPerfil;

    public Button btnRegistrarModificar;
    public Button btnCambiarContrasenna;

    public void initialize() {
        if(modificando) {
            lblTitulo.setText("Modificar admin");

            btnCambiarContrasenna.setVisible(true);

            btnRegistrarModificar.setText("Modificar");
            btnRegistrarModificar.setOnAction(e -> modificarUsuario());

            recursoImagenPerfil = urlImagenPerfil;
        } else {
            lblTitulo.setText("Registrar admin");

            btnCambiarContrasenna.setVisible(false);    //Ocultar botón para cambiar contraseña

            btnRegistrarModificar.setText("Registrar");
            btnRegistrarModificar.setOnAction(e -> registrarUsuario());
        }
    }

    public void seleccionarImagen() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccione una imagen de perfil");
        fileChooser.setInitialDirectory(new File("C:/dev/"));

        File selectedFile = fileChooser.showOpenDialog(window);

        if(selectedFile != null) {
            recursoImagenPerfil = selectedFile.toURI().toString();
            urlImagenPerfil = recursoImagenPerfil;
            imagenPerfil.setImage(new Image(recursoImagenPerfil));
        }
    }

    private boolean hayCamposVacios() {
        if(txtCorreo.getText().trim().equals("") ||
                txtContrasenna.getText().trim().equals("") ||
                txtNombre.getText().trim().equals("") ||
                txtApellidos.getText().trim().equals("") ||
                txtNombreUsuario.getText().trim().equals("")) {
            AlertDialog alertDialog = new AlertDialog();
            alertDialog.mostrar("Error", "No se puede realizar el registro, hay campos vacíos");
            return true;
        }

        return false;
    }

    private boolean errorEnCampos() {
        boolean error = false;
        String mensajeError = "";

        //Verificar correo (aaa@aaa) + (*.com, .*.org, *.ac.cr)
        Pattern contrasennaPattCom = Pattern.compile("^[a-zA-Z0-9.]+\\@[a-zA-Z0-9]+.com$");
        Pattern contrasennaPattOrg = Pattern.compile("^[a-zA-Z0-9.]+\\@[a-zA-Z0-9]+.org$");
        Pattern contrasennaPattAcCr = Pattern.compile("^[a-zA-Z0-9.]+\\@[a-zA-Z0-9]+.ac.cr$");

        Matcher matchCorreoCom = contrasennaPattCom.matcher(txtCorreo.getText());
        Matcher matchCorreoOrg = contrasennaPattOrg.matcher(txtCorreo.getText());
        Matcher matchCorreoAcCr = contrasennaPattAcCr.matcher(txtCorreo.getText());

        if(!(matchCorreoCom.matches() || matchCorreoOrg.matches() || matchCorreoAcCr.matches())) {
            error = true;
            mensajeError = "Hay un problema con el correo, verifique que sea válido";
        }

        //Verificar contraseña (Entre 8 y 12 caracteres, como minimo: una letra minuscula, una letra mayuscula y un caracter especial)
        Pattern contrasennaPatt = Pattern.compile("^(?=.*[a-z])+(?=.*[A-Z])+(?=.*[0-9!_.@$!%*#?&])+[a-zA-Z0-9!_.@$!%*#?&]{8,12}$");
        Matcher matchContrasenna = contrasennaPatt.matcher(txtContrasenna.getText());

        if(!matchContrasenna.matches()) {
            error = true;
            mensajeError = "Hay un problema con la contraseña, debe tener entre 8 y 12 caracteres, una letra minúscula, una mayúscula y un caracter especial";
        }

        if(error) {
            AlertDialog alertDialog = new AlertDialog();
            alertDialog.mostrar("Error", mensajeError);
        }

        return error;
    }

    public void registrarUsuario() {
        if(hayCamposVacios()) return;
        if(errorEnCampos()) return;
        
        String correo = txtCorreo.getText();
        String contrasenna = txtContrasenna.getText();
        String nombre = txtNombre.getText();
        String apellidos = txtApellidos.getText();
        String nombreUsuario = txtNombreUsuario.getText();
        LocalDate fechaCreacion = LocalDate.now();

        try {
            boolean resultado = ControladorGeneral.instancia.getGestor().crearUsuarioAdmin(correo, contrasenna, nombre, apellidos, recursoImagenPerfil, nombreUsuario, fechaCreacion);
            if (resultado) {
                AlertDialog alertDialog = new AlertDialog();
                alertDialog.mostrar("Registro exitoso", "Usuario registrado correctamente");
                volver();
            } else {
                AlertDialog alertDialog = new AlertDialog();
                alertDialog.mostrar("Error", "No se pudo registrar el usuario");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void modificarUsuario() {
        String correo = txtCorreo.getText();
        String nombre = txtNombre.getText();
        String apellidos = txtApellidos.getText();
        String nombreUsuario = txtNombreUsuario.getText();

        try {
            boolean resultado = ControladorGeneral.instancia.getGestor().modificarUsuario(idAdmin, correo, nombreUsuario, urlImagenPerfil, nombre, apellidos);
            if (resultado) {
                AlertDialog alertDialog = new AlertDialog();
                alertDialog.mostrar("Modificación exitosa", "Usuario modificado correctamente");
                volver();
            } else {
                AlertDialog alertDialog = new AlertDialog();
                alertDialog.mostrar("Error", "No se pudo modificar el usuario");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cambiarContrasenna() {
        VentanaCambiarContrasenna ventanaCambiarContrasenna = new VentanaCambiarContrasenna();
        ventanaCambiarContrasenna.mostrar();
    }

    public void volver() {
        if(modificando) {
            ventana.close();
        } else {
            ControladorGeneral.instancia.menuIniciarSesion();
        }
    }
}
