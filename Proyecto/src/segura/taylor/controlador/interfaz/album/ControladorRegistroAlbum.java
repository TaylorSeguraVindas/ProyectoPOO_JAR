package segura.taylor.controlador.interfaz.album;

import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import segura.taylor.controlador.ControladorGeneral;
import segura.taylor.ui.dialogos.AlertDialog;

import java.io.File;
import java.time.LocalDate;

public class ControladorRegistroAlbum {
    public static int idAlbumSeleccionado;
    public static String urlImagenFondo;
    public static Stage ventana;
    public static boolean modificando;

    public TextField txtNombre;
    public DatePicker txtFechaLanzamiento;

    public Button btnRegistrarModificar;
    public Button btnSeleccionarImagen;

    public Label lblTitulo;

    public ImageView imagenFondo;
    public String recursoImagenFondo = "";

    public void initialize() {
        if(ControladorRegistroAlbum.modificando) {
            lblTitulo.setText("Modificar Album");
            btnRegistrarModificar.setText("Modificar");
            btnRegistrarModificar.setOnAction(e -> { modificarAlbum(); });
        } else {
            lblTitulo.setText("Registrar Album");
            btnRegistrarModificar.setText("Registrar");
            btnRegistrarModificar.setOnAction(e -> { registrarAlbum(); });
        }
    }

    public void registrarAlbum() {
        String nombre = txtNombre.getText();
        LocalDate fechaCreacion = LocalDate.now();
        LocalDate fechaLanzamiento = txtFechaLanzamiento.getValue();

        try {
            boolean resultado = ControladorGeneral.instancia.getGestor().crearAlbum(nombre, fechaCreacion, fechaLanzamiento, recursoImagenFondo);
            if (resultado) {
                AlertDialog alertDialog = new AlertDialog();
                alertDialog.mostrar("Registro exitoso", "Album registrado correctamente");
                ventana.close();
            } else {
                AlertDialog alertDialog = new AlertDialog();
                alertDialog.mostrar("Error", "No se pudo registrar el Album");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void seleccionarImagen() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccione una imagen de fondo");
        fileChooser.setInitialDirectory(new File("C:/dev/"));

        File selectedFile = fileChooser.showOpenDialog(ventana);

        if(selectedFile != null) {
            recursoImagenFondo = selectedFile.toURI().toString();
            urlImagenFondo = recursoImagenFondo;
            imagenFondo.setImage(new Image(recursoImagenFondo));
        }
    }

    public void modificarAlbum() {
        String nombre = txtNombre.getText();

        try {
            boolean resultado = ControladorGeneral.instancia.getGestor().modificarAlbum(idAlbumSeleccionado, nombre, urlImagenFondo);
            if (resultado) {
                AlertDialog alertDialog = new AlertDialog();
                alertDialog.mostrar("Modificacion exitosa", "Album modificado correctamente");
                ventana.close();
            } else {
                AlertDialog alertDialog = new AlertDialog();
                alertDialog.mostrar("Error", "No se pudo modificar el Album");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void cerrar() {
        ventana.close();
    }
}
