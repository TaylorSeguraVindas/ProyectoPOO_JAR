package segura.taylor.controlador.interfaz.usuarios;

import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import segura.taylor.bl.entidades.Pais;
import segura.taylor.controlador.ControladorGeneral;
import segura.taylor.ui.dialogos.AlertDialog;
import segura.taylor.ui.dialogos.VentanaCambiarContrasenna;

import java.io.File;
import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ControladorRegistroCliente {
    public static Stage ventana;
    public static int idCliente;
    public static boolean modificando;
    public static String urlImagenPerfil;

    Stage window;

    private String recursoImagenPerfil = "";

    public TextField txtCorreo;
    public PasswordField txtContrasenna;
    public TextField txtNombre;
    public TextField txtApellidos;
    public TextField txtNombreUsuario;
    public DatePicker txtFechaNacimiento;
    public ComboBox txtPais;

    public Button btnSeleccionarImagen;
    public ImageView imagenPerfil;

    public Label lblTitulo;
    public Button btnRegistrarModificar;
    public Button btnCambiarContrasenna;

    public void initialize() {
        actualizarComboBoxPaises();
        if(modificando) {
            lblTitulo.setText("Modificar usuario");
            btnRegistrarModificar.setText("Modificar");
            btnRegistrarModificar.setOnAction(e -> modificarUsuario());

            recursoImagenPerfil = urlImagenPerfil;
            btnCambiarContrasenna.setVisible(true);
        } else {
            lblTitulo.setText("Registrar usuario");
            btnRegistrarModificar.setText("Registrar");
            btnRegistrarModificar.setOnAction(e -> registrarUsuario());
            btnCambiarContrasenna.setVisible(false);    //Ocultar botón para cambiar contraseña
        }
    }

    private boolean hayCamposVacios() {
        if(txtCorreo.getText().trim().equals("") ||
                txtContrasenna.getText().trim().equals("") ||
                txtNombre.getText().trim().equals("") ||
                txtApellidos.getText().trim().equals("") ||
                txtNombreUsuario.getText().trim().equals("") ||
                txtFechaNacimiento.getValue().toString().equals("") ||
                txtPais.getValue().toString().equals("")) {
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

        //Verificar edad
        LocalDate fechaNacimiento = txtFechaNacimiento.getValue();
        int edad = ControladorGeneral.instancia.calcularEdad(fechaNacimiento);

        if(edad < 18) {
            error = true;
            mensajeError = "Debe ser mayor de 18 años para usar la app";
        }

        if(error) {
            AlertDialog alertDialog = new AlertDialog();
            alertDialog.mostrar("Error", mensajeError);
        }

        return error;
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

    public void registrarUsuario() {
        if(hayCamposVacios()) return;
        if(errorEnCampos()) return;

        String correo = txtCorreo.getText();
        String contrasenna = txtContrasenna.getText();
        String nombre = txtNombre.getText();
        String apellidos = txtApellidos.getText();
        String nombreUsuario = txtNombreUsuario.getText();
        LocalDate fechaNacimiento = txtFechaNacimiento.getValue();
        int edad = ControladorGeneral.instancia.calcularEdad(fechaNacimiento);

        //Combo boxes
        String[] itemPais = txtPais.getValue().toString().split("-");
        int pais = Integer.parseInt(itemPais[0]);

        try {
            boolean resultado = ControladorGeneral.instancia.getGestor().crearUsuarioCliente(correo, contrasenna, nombre, apellidos, urlImagenPerfil, nombreUsuario, fechaNacimiento, edad, pais);
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

    private void modificarUsuario() {
        String correo = txtCorreo.getText();
        String nombre = txtNombre.getText();
        String apellidos = txtApellidos.getText();
        String nombreUsuario = txtNombreUsuario.getText();

        try {
            boolean resultado = ControladorGeneral.instancia.getGestor().modificarUsuario(idCliente, correo, nombreUsuario, urlImagenPerfil, nombre, apellidos);
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

    private void actualizarComboBoxPaises() {
        txtPais.getItems().clear();

        for (Pais pais : ControladorGeneral.instancia.getGestor().listarPaises()) {
            txtPais.getItems().add(pais.toComboBoxItem());
        }
    }

}
