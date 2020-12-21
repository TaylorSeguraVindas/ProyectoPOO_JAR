package segura.taylor.controlador.interfaz.admin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import segura.taylor.bl.entidades.Compositor;
import segura.taylor.bl.entidades.Genero;
import segura.taylor.bl.entidades.Pais;
import segura.taylor.controlador.ControladorGeneral;
import segura.taylor.controlador.interfaz.compositor.ControladorRegistroCompositor;
import segura.taylor.ui.dialogos.AlertDialog;
import segura.taylor.ui.dialogos.YesNoDialog;

import java.util.List;
import java.util.Locale;

public class ControladorCompositoresAdmin {
    public TableView tblCompositores;
    public VBox ventanaPrincipal;

    public TextField txtBusqueda;


    public void initialize() {
        inicializarTabla();
        mostrarDatos();
    }

    public void inicializarTabla() {
        //Nombre
        TableColumn<Compositor, String> columnaNombre = new TableColumn("Nombre");
        columnaNombre.setMinWidth(100);
        columnaNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));

        //Apellidos
        TableColumn<Compositor, String> columnaApellidos = new TableColumn("Apellidos");
        columnaApellidos.setMinWidth(100);
        columnaApellidos.setCellValueFactory(new PropertyValueFactory<>("apellidos"));

        //FechaNacimiento
        TableColumn<Compositor, String> columnaFechaNacimiento = new TableColumn("Fecha Nacimiento");
        columnaFechaNacimiento.setMinWidth(100);
        columnaFechaNacimiento.setCellValueFactory(new PropertyValueFactory<>("fechaNacimiento"));

        //Edad
        TableColumn<Compositor, String> columnaEdad = new TableColumn("Edad");
        columnaEdad.setMinWidth(100);
        columnaEdad.setCellValueFactory(new PropertyValueFactory<>("edad"));

        //Pais
        TableColumn<Compositor, String> columnaPais = new TableColumn("Pais");
        columnaPais.setMinWidth(100);
        columnaPais.setCellValueFactory(new PropertyValueFactory<>("nombrePais"));

        //Genero
        TableColumn<Compositor, String> columnaGenero = new TableColumn("Genero");
        columnaGenero.setMinWidth(100);
        columnaGenero.setCellValueFactory(new PropertyValueFactory<>("nombreGenero"));

        tblCompositores.getColumns().addAll(columnaNombre, columnaApellidos, columnaFechaNacimiento, columnaEdad, columnaPais, columnaGenero);

    }
    private void mostrarDatos() {
        tblCompositores.getItems().clear();
        tblCompositores.setItems(obtenerCompositores(!txtBusqueda.getText().trim().equals("")));   //Usa filtro si el texto de búsqueda no está vacío
    }

    public ObservableList<Compositor> obtenerCompositores(boolean usandoFiltro) {
        List<Compositor> compositores = ControladorGeneral.instancia.getGestor().listarCompositores();

        ObservableList<Compositor> compositoresFinal = FXCollections.observableArrayList();

        for(Compositor compositor : compositores) {
            if(usandoFiltro) {
                if(compositorCoincideConBusqueda(compositor)) {
                    compositoresFinal.addAll(compositor);
                }
            } else {
                compositoresFinal.addAll(compositor);
            }
        }

        return compositoresFinal;
    }

    private boolean compositorCoincideConBusqueda(Compositor compositor) {
        String textoBusqueda = txtBusqueda.getText().trim().toUpperCase(Locale.ROOT);

        String nombreCompositor = compositor.getNombre().trim().toUpperCase(Locale.ROOT);
        if(nombreCompositor.equals(textoBusqueda) || nombreCompositor.contains(textoBusqueda)) {
            return true;
        }

        return false;
    }

    public void buscar() {
        mostrarDatos();
    }

    public void agregarCompositor() {
        try {
            Stage ventanaRegistroCompositor = new Stage();
            //This locks previous window interacivity until this one is closed.
            ventanaRegistroCompositor.initModality(Modality.APPLICATION_MODAL);

            //Referencias para el controlador
            ControladorRegistroCompositor.ventana = ventanaRegistroCompositor;
            ControladorRegistroCompositor.modificando = false;

            VBox root = FXMLLoader.load(getClass().getResource("../../../ui/ventanas/VentanaRegistroCompositor.fxml"));
            Scene escena = new Scene(root, 580, 440);

            ventanaRegistroCompositor.setScene(escena);
            ventanaRegistroCompositor.setTitle("Registro de Compositor");
            ventanaRegistroCompositor.setResizable(false);
            ventanaRegistroCompositor.showAndWait();

            mostrarDatos(); //Actualizar tabla
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void modificarCompositor() {
        try {
            //Referencias para el controlador
            Compositor compositorSeleccionado = (Compositor) tblCompositores.getSelectionModel().getSelectedItem();

            if (compositorSeleccionado == null) {
                AlertDialog alertDialog = new AlertDialog();
                alertDialog.mostrar("Error", "No hay ningún Compositor seleccionado");
                return;
            }

            Stage ventanaRegistroCompositor = new Stage();
            //This locks previous window interacivity until this one is closed.
            ventanaRegistroCompositor.initModality(Modality.APPLICATION_MODAL);

            ControladorRegistroCompositor.ventana = ventanaRegistroCompositor;
            ControladorRegistroCompositor.idCompositorSeleccionado = compositorSeleccionado.getId();
            ControladorRegistroCompositor.modificando = true;

            VBox root = FXMLLoader.load(getClass().getResource("../../../ui/ventanas/VentanaRegistroCompositor.fxml"));

            //Referencia a los campos
            TextField txtNombre = (TextField) root.lookup("#txtNombre");
            TextField txtApellidos = (TextField) root.lookup("#txtApellidos");
            ComboBox txtPais = (ComboBox) root.lookup("#txtPais");
            ComboBox txtGenero = (ComboBox) root.lookup("#txtGenero");
            DatePicker txtFechaNacimiento = (DatePicker) root.lookup("#txtFechaNacimiento");

            //Actualizar campos
            txtNombre.setText(compositorSeleccionado.getNombre());
            txtApellidos.setText(compositorSeleccionado.getApellidos());

            Pais paisCompositor = compositorSeleccionado.getPaisNacimiento();
            if(paisCompositor != null) {
                txtPais.setValue(paisCompositor.toComboBoxItem());
            }

            Genero generoCompositor = compositorSeleccionado.getGenero();
            if(generoCompositor != null) {
                txtGenero.setValue(generoCompositor.toComboBoxItem());
            }
            txtFechaNacimiento.setValue(compositorSeleccionado.getFechaNacimiento());

            //Desactivar campos inmodificables
            txtPais.setDisable(true);
            txtGenero.setDisable(true);

            Scene escena = new Scene(root, 580, 440);

            ventanaRegistroCompositor.setScene(escena);
            ventanaRegistroCompositor.setTitle("Modificacion de Compositor");
            ventanaRegistroCompositor.setResizable(false);
            ventanaRegistroCompositor.showAndWait();

            mostrarDatos(); //Actualizar tabla
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void eliminarCompositor() {
        YesNoDialog yesNoDialog = new YesNoDialog();
        boolean resultado = yesNoDialog.mostrar("Aviso", "Realmente quiere eliminar al Compositor seleccionado?");

        if (resultado) {
            Compositor compositorSeleccionado = (Compositor) tblCompositores.getSelectionModel().getSelectedItem();

            if (compositorSeleccionado == null) {
                AlertDialog alertDialog = new AlertDialog();
                alertDialog.mostrar("Error", "No hay ningún Compositor seleccionado");
                return;
            }
            
            int idCompositor = compositorSeleccionado.getId();
            try {
                resultado = ControladorGeneral.instancia.getGestor().eliminarCompositor(idCompositor);
                if (resultado) {
                    AlertDialog alertDialog = new AlertDialog();
                    alertDialog.mostrar("Exito", "Compositor eliminado correctamente");
                    mostrarDatos();
                } else {
                    AlertDialog alertDialog = new AlertDialog();
                    alertDialog.mostrar("Error", "No se pudo eliminar el Compositor");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
