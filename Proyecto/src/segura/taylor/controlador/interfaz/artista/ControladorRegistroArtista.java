package segura.taylor.controlador.interfaz.artista;

import javafx.scene.control.*;
import javafx.stage.Stage;
import segura.taylor.bl.entidades.Genero;
import segura.taylor.bl.entidades.Pais;
import segura.taylor.controlador.ControladorGeneral;
import segura.taylor.ui.dialogos.AlertDialog;

import java.time.LocalDate;

public class ControladorRegistroArtista {
    public static int idArtistaSeleccionado;
    public static Stage ventana;
    public static boolean modificando;

    public TextField txtNombre;
    public TextField txtApellidos;
    public TextField txtNombreArtistico;
    public ComboBox txtGenero;
    public ComboBox txtPais;
    public DatePicker txtFechaNacimiento;
    public DatePicker txtFechaDefuncion;
    public TextArea txtDescripcion;

    public Button btnRegistrarModificar;
    public Label lblTitulo;

    public void initialize() {
        if(ControladorRegistroArtista.modificando) {
            lblTitulo.setText("Modificar artista");
            btnRegistrarModificar.setText("Modificar");
            btnRegistrarModificar.setOnAction(e -> { modificarArtista(); });
        } else {
            lblTitulo.setText("Registrar artista");
            btnRegistrarModificar.setText("Registrar");
            btnRegistrarModificar.setOnAction(e -> { registrarArtista(); });
        }

        actualizarComboBoxPaises();
        actualizarComboBoxGeneros();
    }

    public void registrarArtista() {
        String nombre = txtNombre.getText();
        String apellidos = txtApellidos.getText();
        String nombreArtistico = txtNombreArtistico.getText();
        LocalDate fechaNacimiento = txtFechaNacimiento.getValue();
        LocalDate fechaDefuncion = txtFechaDefuncion.getValue();

        //Combo boxes
        String[] itemPais = txtPais.getValue().toString().split("-");
        int paisNacimiento = Integer.parseInt(itemPais[0]);

        String[] itemGenero = txtGenero.getValue().toString().split("-");
        int genero = Integer.parseInt(itemGenero[0]);

        int edad = ControladorGeneral.instancia.calcularEdad(fechaNacimiento);
        String descripcion = txtDescripcion.getText();

        try {
            boolean resultado = ControladorGeneral.instancia.getGestor().crearArtista(nombre, apellidos, nombreArtistico, fechaNacimiento, fechaDefuncion, paisNacimiento, genero, edad, descripcion);
            if (resultado) {
                AlertDialog alertDialog = new AlertDialog();
                alertDialog.mostrar("Registro exitoso", "Artista registrado correctamente");
                ventana.close();
            } else {
                AlertDialog alertDialog = new AlertDialog();
                alertDialog.mostrar("Error", "No se pudo registrar el artista");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void modificarArtista() {
        String nombre = txtNombre.getText();
        String apellidos = txtApellidos.getText();
        String nombreArtistico = txtNombreArtistico.getText();
        LocalDate fechaDefuncion = txtFechaDefuncion.getValue();
        String descripcion = txtDescripcion.getText();

        try {
            boolean resultado = ControladorGeneral.instancia.getGestor().modificarArtista(idArtistaSeleccionado, nombre, apellidos, nombreArtistico, fechaDefuncion, descripcion);
            if (resultado) {
                AlertDialog alertDialog = new AlertDialog();
                alertDialog.mostrar("Modificacion exitosa", "Artista modificado correctamente");
                ventana.close();
            } else {
                AlertDialog alertDialog = new AlertDialog();
                alertDialog.mostrar("Error", "No se pudo modificar el artista");
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

    private void limpiarCampos() {
        txtNombre.setText("");
        txtApellidos.setText("");
        txtNombreArtistico.setText("");
        txtGenero.setValue("");
        txtPais.setValue("");
        txtFechaNacimiento.setValue(null);
        txtFechaDefuncion.setValue(null);
        txtDescripcion.setText("");
    }
}
