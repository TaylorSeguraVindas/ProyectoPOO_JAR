package segura.taylor.controlador.interfaz.pais;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import segura.taylor.controlador.ControladorGeneral;
import segura.taylor.ui.dialogos.AlertDialog;

public class ControladorRegistroPais {
    public static int idPaisSeleccionado;
    public static Stage ventana;
    public static boolean modificando;

    public TextField txtNombre;
    public TextArea txtDescripcion;

    public Button btnRegistrarModificar;
    public Label lblTitulo;

    public void initialize() {
        if(ControladorRegistroPais.modificando) {
            lblTitulo.setText("Modificar Pais");
            btnRegistrarModificar.setText("Modificar");
            btnRegistrarModificar.setOnAction(e -> { modificarPais(); });
        } else {
            lblTitulo.setText("Registrar Pais");
            btnRegistrarModificar.setText("Registrar");
            btnRegistrarModificar.setOnAction(e -> { registrarPais(); });
        }
    }

    public void registrarPais() {
        String nombre = txtNombre.getText();
        String descripcion = txtDescripcion.getText();

        try {
            boolean resultado = ControladorGeneral.instancia.getGestor().crearPais(nombre, descripcion);
            if (resultado) {
                AlertDialog alertDialog = new AlertDialog();
                alertDialog.mostrar("Registro exitoso", "Pais registrado correctamente");
                ventana.close();
            } else {
                AlertDialog alertDialog = new AlertDialog();
                alertDialog.mostrar("Error", "No se pudo registrar el Pais");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void modificarPais() {
        String nombre = txtNombre.getText();
        String descripcion = txtDescripcion.getText();

        try {
            boolean resultado = ControladorGeneral.instancia.getGestor().modificarPais(idPaisSeleccionado, nombre, descripcion);
            if (resultado) {
                AlertDialog alertDialog = new AlertDialog();
                alertDialog.mostrar("Modificacion exitosa", "Pais modificado correctamente");
                ventana.close();
            } else {
                AlertDialog alertDialog = new AlertDialog();
                alertDialog.mostrar("Error", "No se pudo modificar el Pais");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void cerrar() {
        ventana.close();
    }
}
