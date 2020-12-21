package segura.taylor.controlador.interfaz.admin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import segura.taylor.bl.entidades.Pais;
import segura.taylor.controlador.ControladorGeneral;
import segura.taylor.controlador.interfaz.pais.ControladorRegistroPais;
import segura.taylor.ui.dialogos.AlertDialog;
import segura.taylor.ui.dialogos.YesNoDialog;

import java.util.List;

public class ControladorPaisesAdmin {
    public TableView tblPaises;
    public VBox ventanaPrincipal;

    public void initialize() {
        inicializarTabla();
        mostrarDatos();
    }

    public void inicializarTabla() {
        //Nombre
        TableColumn<Pais, String> columnaNombre = new TableColumn("Nombre");
        columnaNombre.setMinWidth(100);
        columnaNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));

        //Descripcion
        TableColumn<Pais, String> columnaDescripcion = new TableColumn("Descripción");
        columnaDescripcion.setMinWidth(100);
        columnaDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));

        tblPaises.getColumns().addAll(columnaNombre, columnaDescripcion);

    }
    private void mostrarDatos() {
        tblPaises.getItems().clear();
        tblPaises.setItems(obtenerPaises());
    }

    public ObservableList<Pais> obtenerPaises() {
        List<Pais> Paises = ControladorGeneral.instancia.getGestor().listarPaises();

        ObservableList<Pais> PaisesFinal = FXCollections.observableArrayList();

        for(Pais Pais : Paises) {
            PaisesFinal.addAll(Pais);
        }

        return PaisesFinal;
    }

    public void agregarPais() {
        try {
            Stage ventanaRegistroPais = new Stage();
            //This locks previous window interacivity until this one is closed.
            ventanaRegistroPais.initModality(Modality.APPLICATION_MODAL);

            //Referencias para el controlador
            ControladorRegistroPais.ventana = ventanaRegistroPais;
            ControladorRegistroPais.modificando = false;

            VBox root = FXMLLoader.load(getClass().getResource("../../../ui/ventanas/VentanaRegistroPais.fxml"));
            Scene escena = new Scene(root, 580, 440);

            ventanaRegistroPais.setScene(escena);
            ventanaRegistroPais.setTitle("Registro de Pais");
            ventanaRegistroPais.setResizable(false);
            ventanaRegistroPais.showAndWait();

            mostrarDatos(); //Actualizar tabla
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void modificarPais() {
        try {
            //Referencias para el controlador
            Pais Paiseseleccionado = (Pais) tblPaises.getSelectionModel().getSelectedItem();

            if (Paiseseleccionado == null) {
                AlertDialog alertDialog = new AlertDialog();
                alertDialog.mostrar("Error", "No hay ningún Pais seleccionado");
                return;
            }

            Stage ventanaRegistroPais = new Stage();
            //This locks previous window interacivity until this one is closed.
            ventanaRegistroPais.initModality(Modality.APPLICATION_MODAL);

            ControladorRegistroPais.ventana = ventanaRegistroPais;
            ControladorRegistroPais.idPaisSeleccionado = Paiseseleccionado.getId();
            ControladorRegistroPais.modificando = true;

            VBox root = FXMLLoader.load(getClass().getResource("../../../ui/ventanas/VentanaRegistroPais.fxml"));

            //Referencia a los campos
            TextField txtNombre = (TextField) root.lookup("#txtNombre");
            TextArea txtDescripcion = (TextArea) root.lookup("#txtDescripcion");

            //Actualizar campos
            txtNombre.setText(Paiseseleccionado.getNombre());
            txtDescripcion.setText(Paiseseleccionado.getDescripcion());

            Scene escena = new Scene(root, 580, 440);

            ventanaRegistroPais.setScene(escena);
            ventanaRegistroPais.setTitle("Modificacion de Pais");
            ventanaRegistroPais.setResizable(false);
            ventanaRegistroPais.showAndWait();

            mostrarDatos(); //Actualizar tabla
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void eliminarPais() {
        YesNoDialog yesNoDialog = new YesNoDialog();
        boolean resultado = yesNoDialog.mostrar("Aviso", "Realmente quiere eliminar al Pais seleccionado?");

        if (resultado) {
            Pais Paiseseleccionado = (Pais) tblPaises.getSelectionModel().getSelectedItem();

            if (Paiseseleccionado == null) {
                AlertDialog alertDialog = new AlertDialog();
                alertDialog.mostrar("Error", "No hay ningún Pais seleccionado");
                return;
            }

            int idPais = Paiseseleccionado.getId();
            try {
                resultado = ControladorGeneral.instancia.getGestor().eliminarPais(idPais);
                if (resultado) {
                    AlertDialog alertDialog = new AlertDialog();
                    alertDialog.mostrar("Exito", "Pais eliminado correctamente");
                    mostrarDatos();
                } else {
                    AlertDialog alertDialog = new AlertDialog();
                    alertDialog.mostrar("Error", "No se pudo eliminar el Pais");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
