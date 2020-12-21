package segura.taylor.controlador.interfaz.genero;

import javafx.scene.control.*;
import javafx.stage.Stage;
import segura.taylor.controlador.ControladorGeneral;
import segura.taylor.ui.dialogos.AlertDialog;

public class ControladorRegistroGenero {
    public static int idGeneroSeleccionado;
    public static Stage ventana;
    public static boolean modificando;

    public TextField txtNombre;
    public TextArea txtDescripcion;

    public Button btnRegistrarModificar;
    public Label lblTitulo;

    public void initialize() {
        if(ControladorRegistroGenero.modificando) {
            lblTitulo.setText("Modificar Genero");
            btnRegistrarModificar.setText("Modificar");
            btnRegistrarModificar.setOnAction(e -> { modificarGenero(); });
        } else {
            lblTitulo.setText("Registrar Genero");
            btnRegistrarModificar.setText("Registrar");
            btnRegistrarModificar.setOnAction(e -> { registrarGenero(); });
        }
    }

    public void registrarGenero() {
        String nombre = txtNombre.getText();
        String descripcion = txtDescripcion.getText();

        try {
            boolean resultado = ControladorGeneral.instancia.getGestor().crearGenero(nombre, descripcion);
            if (resultado) {
                AlertDialog alertDialog = new AlertDialog();
                alertDialog.mostrar("Registro exitoso", "Genero registrado correctamente");
                ventana.close();
            } else {
                AlertDialog alertDialog = new AlertDialog();
                alertDialog.mostrar("Error", "No se pudo registrar el Genero");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void modificarGenero() {
        String nombre = txtNombre.getText();
        String descripcion = txtDescripcion.getText();

        try {
            boolean resultado = ControladorGeneral.instancia.getGestor().modificarGenero(idGeneroSeleccionado, nombre, descripcion);
            if (resultado) {
                AlertDialog alertDialog = new AlertDialog();
                alertDialog.mostrar("Modificacion exitosa", "Genero modificado correctamente");
                ventana.close();
            } else {
                AlertDialog alertDialog = new AlertDialog();
                alertDialog.mostrar("Error", "No se pudo modificar el Genero");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void cerrar() {
        ventana.close();
    }
}
