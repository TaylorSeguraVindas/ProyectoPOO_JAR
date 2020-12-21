package segura.taylor.controlador.interfaz.listaReproduccion;

import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import segura.taylor.controlador.ControladorGeneral;
import segura.taylor.ui.dialogos.AlertDialog;

import java.io.File;
import java.time.LocalDate;

public class ControladorRegistroListaReproduccion {
    public static int idListaReproduccionSeleccionada;
    public static Stage ventana;
    public static boolean modificando;
    public static String urlImagenFondo;

    public TextField txtNombre;
    public TextArea txtDescripcion;

    public Button btnRegistrarModificar;
    public Button btnSeleccionarImagen;

    public Label lblTitulo;

    public ImageView imagenFondo;
    private String recursoImagenFondo = "";

    public void initialize() {
        if(ControladorRegistroListaReproduccion.modificando) {
            lblTitulo.setText("Modificar ListaReproduccion");
            btnRegistrarModificar.setText("Modificar");
            btnRegistrarModificar.setOnAction(e -> { modificarListaReproduccion(); });
        } else {
            lblTitulo.setText("Registrar ListaReproduccion");
            btnRegistrarModificar.setText("Registrar");
            btnRegistrarModificar.setOnAction(e -> { registrarListaReproduccion(); });
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

    public void registrarListaReproduccion() {
        String nombre = txtNombre.getText();
        LocalDate fechaCreacion = LocalDate.now();
        String descripcion = txtDescripcion.getText();

        try {
            int idListaRegistrada = ControladorGeneral.instancia.getGestor().crearListaReproduccion(nombre, fechaCreacion, recursoImagenFondo, descripcion);
            if (idListaRegistrada != -1) {
                //Si la lista fue creada por un usuario corriente automáticamente se agrega a su biblioteca
                if(!ControladorGeneral.instancia.usuarioIngresadoEsAdmin()) {
                    ControladorGeneral.instancia.getGestor().agregarListaReproduccionABibliotecaUsuario(ControladorGeneral.instancia.getIdUsuarioIngresado(), idListaRegistrada);
                }

                AlertDialog alertDialog = new AlertDialog();
                alertDialog.mostrar("Registro exitoso", "Lista de Reproduccion registrada correctamente");
                ventana.close();
            } else {
                AlertDialog alertDialog = new AlertDialog();
                alertDialog.mostrar("Error", "No se pudo registrar la Lista de Reproduccion");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void modificarListaReproduccion() {
        String nombre = txtNombre.getText();
        String descripcion = txtDescripcion.getText();

        try {
            boolean resultado = ControladorGeneral.instancia.getGestor().modificarListaReproduccion(idListaReproduccionSeleccionada, nombre, urlImagenFondo, descripcion);
            if (resultado) {
                AlertDialog alertDialog = new AlertDialog();
                alertDialog.mostrar("Modificacion exitosa", "Lista de Reproduccion modificada correctamente");
                ventana.close();
            } else {
                AlertDialog alertDialog = new AlertDialog();
                alertDialog.mostrar("Error", "No se pudo modificar la Lista de Reproduccion");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void cerrar() {
        ventana.close();
    }
}
