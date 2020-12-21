package segura.taylor.controlador.interfaz.compositor;

import javafx.scene.control.*;
import javafx.stage.Stage;
import segura.taylor.bl.entidades.Genero;
import segura.taylor.bl.entidades.Pais;
import segura.taylor.controlador.ControladorGeneral;
import segura.taylor.ui.dialogos.AlertDialog;

import java.time.LocalDate;

public class ControladorRegistroCompositor {
    public static int idCompositorSeleccionado;
    public static Stage ventana;
    public static boolean modificando;

    public TextField txtNombre;
    public TextField txtApellidos;
    public ComboBox txtPais;
    public ComboBox txtGenero;
    public DatePicker txtFechaNacimiento;

    public Button btnRegistrarModificar;
    public Label lblTitulo;

    public void initialize() {
        if(ControladorRegistroCompositor.modificando) {
            lblTitulo.setText("Modificar compositor");
            btnRegistrarModificar.setText("Modificar");
            btnRegistrarModificar.setOnAction(e -> { modificarCompositor(); });
        } else {
            lblTitulo.setText("Registrar compositor");
            btnRegistrarModificar.setText("Registrar");
            btnRegistrarModificar.setOnAction(e -> { registrarCompositor(); });
        }

        actualizarComboBoxPaises();
        actualizarComboBoxGeneros();
    }

    public void registrarCompositor() {
        String nombre = txtNombre.getText();
        String apellidos = txtApellidos.getText();
        LocalDate fechaNacimiento = txtFechaNacimiento.getValue();

        //Combo boxes
        String[] itemPais = txtPais.getValue().toString().split("-");
        int paisNacimiento = Integer.parseInt(itemPais[0]);

        String[] itemGenero = txtGenero.getValue().toString().split("-");
        int genero = Integer.parseInt(itemGenero[0]);

        int edad = ControladorGeneral.instancia.calcularEdad(fechaNacimiento);

        try {
            boolean resultado = ControladorGeneral.instancia.getGestor().crearCompositor(nombre, apellidos, paisNacimiento, genero, fechaNacimiento, edad);
            if (resultado) {
                AlertDialog alertDialog = new AlertDialog();
                alertDialog.mostrar("Registro exitoso", "Compositor registrado correctamente");
                ventana.close();
            } else {
                AlertDialog alertDialog = new AlertDialog();
                alertDialog.mostrar("Error", "No se pudo registrar el Compositor");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void modificarCompositor() {
        String nombre = txtNombre.getText();
        String apellidos = txtApellidos.getText();

        try {
            boolean resultado = ControladorGeneral.instancia.getGestor().modificarCompositor(idCompositorSeleccionado, nombre, apellidos);
            if (resultado) {
                AlertDialog alertDialog = new AlertDialog();
                alertDialog.mostrar("Modificacion exitosa", "Compositor modificado correctamente");
                ventana.close();
            } else {
                AlertDialog alertDialog = new AlertDialog();
                alertDialog.mostrar("Error", "No se pudo modificar el Compositor");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void cerrar() {
        ventana.close();
    }

    private void actualizarComboBoxPaises() {
        txtPais.getItems().clear();

        for (Pais pais : ControladorGeneral.instancia.getGestor().listarPaises()) {
            txtPais.getItems().add(pais.toComboBoxItem());
        }
    }

    private void actualizarComboBoxGeneros() {
        txtGenero.getItems().clear();

        for (Genero genero : ControladorGeneral.instancia.getGestor().listarGeneros()) {
            txtGenero.getItems().add(genero.toComboBoxItem());
        }
    }
}
