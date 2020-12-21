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
import segura.taylor.bl.entidades.*;
import segura.taylor.controlador.ControladorGeneral;
import segura.taylor.controlador.interfaz.cancion.ControladorRegistroCancion;
import segura.taylor.ui.dialogos.AlertDialog;
import segura.taylor.ui.dialogos.VentanaFiltroCancionesAdmin;
import segura.taylor.ui.dialogos.YesNoDialog;

import java.util.List;
import java.util.Locale;

public class ControladorCancionesAdmin {
    public static boolean filtrandoPorNombre = true;
    public static boolean filtrandoPorArtista = false;
    public static boolean filtrandoPorGenero = false;

    public TableView tblCanciones;
    public VBox ventanaPrincipal;
    public TextField txtBusqueda;

    public void initialize() {
        //Reiniciar filtros
        filtrandoPorNombre = true;
        filtrandoPorArtista = false;
        filtrandoPorGenero = false;

        inicializarTabla();
        mostrarDatos();
    }

    public void inicializarTabla() {
        //Recurso
        TableColumn<Cancion, String> columnaRecurso = new TableColumn("Recurso");
        columnaRecurso.setMinWidth(100);
        columnaRecurso.setCellValueFactory(new PropertyValueFactory<>("recurso"));

        //Nombre
        TableColumn<Cancion, String> columnaNombre = new TableColumn("Nombre");
        columnaNombre.setMinWidth(100);
        columnaNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));

        //Duración
        TableColumn<Cancion, String> columnaDuracion = new TableColumn("Duración");
        columnaDuracion.setMinWidth(100);
        columnaDuracion.setCellValueFactory(new PropertyValueFactory<>("duracion"));

        //Fecha lanzamiento
        TableColumn<Cancion, String> columnaFechaLanzamiento = new TableColumn("Fecha lanzamiento");
        columnaFechaLanzamiento.setMinWidth(100);
        columnaFechaLanzamiento.setCellValueFactory(new PropertyValueFactory<>("fechaLanzamiento"));

        //Artista
        TableColumn<Cancion, String> columnaArtista = new TableColumn("Artista");
        columnaArtista.setMinWidth(100);
        columnaArtista.setCellValueFactory(new PropertyValueFactory<>("nombreArtista"));

        //Compositor
        TableColumn<Cancion, String> columnaCompositor = new TableColumn("Compositor");
        columnaCompositor.setMinWidth(100);
        columnaCompositor.setCellValueFactory(new PropertyValueFactory<>("nombreCompositor"));

        //Genero
        TableColumn<Cancion, String> columnaGenero = new TableColumn("Genero");
        columnaGenero.setMinWidth(100);
        columnaGenero.setCellValueFactory(new PropertyValueFactory<>("nombreGenero"));

        //Precio
        TableColumn<Cancion, String> columnaPrecio = new TableColumn("Precio");
        columnaPrecio.setMinWidth(100);
        columnaPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));

        tblCanciones.getColumns().addAll(columnaRecurso, columnaNombre, columnaDuracion, columnaFechaLanzamiento, columnaArtista, columnaCompositor, columnaGenero, columnaPrecio);

    }
    private void mostrarDatos() {
        tblCanciones.getItems().clear();
        tblCanciones.setItems(obtenerCanciones(!txtBusqueda.getText().trim().equals("")));   //Usa filtro si el texto de búsqueda no está vacío
    }

    public ObservableList<Cancion> obtenerCanciones(boolean usandoFiltros) {
        List<Cancion> canciones = ControladorGeneral.instancia.getGestor().listarCanciones();

        ObservableList<Cancion> cancionesFinal = FXCollections.observableArrayList();

        for(Cancion cancion : canciones) {
            if(usandoFiltros) {
                if(cancionCoincideConBusqueda(cancion)) {
                    cancionesFinal.addAll(cancion);
                }
            } else {
                cancionesFinal.addAll(cancion);
            }
        }

        return cancionesFinal;
    }

    private boolean cancionCoincideConBusqueda(Cancion cancion) {
        String textoBusqueda = txtBusqueda.getText().trim().toUpperCase(Locale.ROOT);

        //NOMBRE
        if(filtrandoPorNombre) {
            String nombreCancion = cancion.getNombre().trim().toUpperCase(Locale.ROOT);
            if(nombreCancion.equals(textoBusqueda) || nombreCancion.contains(textoBusqueda)) {
                return true;
            }
        }

        //GENERO
        if(filtrandoPorGenero) {
            String generoCancion = cancion.getGenero().getNombre().trim().toUpperCase(Locale.ROOT);
            if(generoCancion.equals(textoBusqueda) || generoCancion.contains(textoBusqueda)) {
                return true;
            }
        }

        //ARTISTA
        if (filtrandoPorArtista) {
            String artistaCancion = cancion.getArtista().getNombreArtistico().trim().toUpperCase(Locale.ROOT);
            if(artistaCancion.equals(textoBusqueda) || artistaCancion.contains(textoBusqueda)) {
                return true;
            }
        }

        return false;
    }

    public void abrirFiltros() {
        //Mostrar filtros
        VentanaFiltroCancionesAdmin ventanaFiltros = new VentanaFiltroCancionesAdmin();
        ventanaFiltros.mostrar();
    }

    public void buscar() {
        //Actualizar lista
        mostrarDatos();
    }

    public void reproducirCancion() {
        Cancion cancionSeleccionada = (Cancion) tblCanciones.getSelectionModel().getSelectedItem();

        if(cancionSeleccionada != null) {
            ControladorGeneral.instancia.cargarCancion(cancionSeleccionada.getRecurso());
            ControladorGeneral.instancia.reproducirCancion();
        }
    }

    public void agregarCancion() {
        try {
            Stage ventanaRegistroCancion = new Stage();
            //This locks previous window interacivity until this one is closed.
            ventanaRegistroCancion.initModality(Modality.APPLICATION_MODAL);

            //Referencias para el controlador
            ControladorRegistroCancion.ventana = ventanaRegistroCancion;
            ControladorRegistroCancion.modificando = false;

            VBox root = FXMLLoader.load(getClass().getResource("../../../ui/ventanas/VentanaRegistroCancion.fxml"));
            Scene escena = new Scene(root, 660, 400);

            ventanaRegistroCancion.setScene(escena);
            ventanaRegistroCancion.setTitle("Registro de Cancion");
            ventanaRegistroCancion.setResizable(false);
            ventanaRegistroCancion.showAndWait();

            mostrarDatos(); //Actualizar tabla
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void modificarCancion() {
        try {
            //Referencias para el controlador
            Cancion cancionSeleccionada = (Cancion) tblCanciones.getSelectionModel().getSelectedItem();

            if (cancionSeleccionada == null) {
                AlertDialog alertDialog = new AlertDialog();
                alertDialog.mostrar("Error", "No hay ninguna Cancion seleccionada");
                return;
            }

            Stage ventanaRegistroCancion = new Stage();
            //This locks previous window interacivity until this one is closed.
            ventanaRegistroCancion.initModality(Modality.APPLICATION_MODAL);

            ControladorRegistroCancion.ventana = ventanaRegistroCancion;
            ControladorRegistroCancion.idCancionSeleccionada = cancionSeleccionada.getId();
            ControladorRegistroCancion.modificando = true;

            VBox root = FXMLLoader.load(getClass().getResource("../../../ui/ventanas/VentanaRegistroCancion.fxml"));

            //Referencia a los campos
            TextField txtRecurso = (TextField) root.lookup("#txtRecurso");
            TextField txtNombre = (TextField) root.lookup("#txtNombre");
            TextField txtDuracion = (TextField) root.lookup("#txtDuracion");
            DatePicker txtFechaLanzamiento = (DatePicker) root.lookup("#txtFechaLanzamiento");

            ComboBox txtArtista = (ComboBox) root.lookup("#txtArtista");
            ComboBox txtCompositor = (ComboBox) root.lookup("#txtCompositor");
            ComboBox txtGenero = (ComboBox) root.lookup("#txtGenero");

            TextField txtPrecio = (TextField) root.lookup("#txtPrecio");

            //Actualizar campos
            txtRecurso.setText(cancionSeleccionada.getRecurso());
            txtNombre.setText(cancionSeleccionada.getNombre());
            txtDuracion.setText(String.valueOf(cancionSeleccionada.getDuracion()));
            txtFechaLanzamiento.setValue(cancionSeleccionada.getFechaLanzamiento());

            Artista artistaCancion = cancionSeleccionada.getArtista();
            if(artistaCancion != null) {
                txtArtista.setValue(artistaCancion.toComboBoxItem());
            }

            Compositor compositorCancion = cancionSeleccionada.getCompositor();
            if(compositorCancion != null) {
                txtCompositor.setValue(compositorCancion.toComboBoxItem());
            }

            Genero generoCancion = cancionSeleccionada.getGenero();
            if(generoCancion != null) {
                txtGenero.setValue(generoCancion.toComboBoxItem());
            }

            txtPrecio.setText(String.valueOf(cancionSeleccionada.getPrecio()));

            //Desactivar campos inmodificables

            Scene escena = new Scene(root, 580, 440);

            ventanaRegistroCancion.setScene(escena);
            ventanaRegistroCancion.setTitle("Modificacion de Cancion");
            ventanaRegistroCancion.setResizable(false);
            ventanaRegistroCancion.showAndWait();

            mostrarDatos(); //Actualizar tabla
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void eliminarCancion() {
        YesNoDialog yesNoDialog = new YesNoDialog();
        boolean resultado = yesNoDialog.mostrar("Aviso", "Realmente quiere eliminar la Cancion seleccionada?");

        if (resultado) {
            Cancion CancionSeleccionado = (Cancion) tblCanciones.getSelectionModel().getSelectedItem();

            if (CancionSeleccionado == null) {
                AlertDialog alertDialog = new AlertDialog();
                alertDialog.mostrar("Error", "No hay ninguna Cancion seleccionada");
                return;
            }

            int idCancion = CancionSeleccionado.getId();
            try {
                resultado = ControladorGeneral.instancia.getGestor().eliminarCancion(idCancion);
                if (resultado) {
                    AlertDialog alertDialog = new AlertDialog();
                    alertDialog.mostrar("Exito", "Cancion eliminada correctamente");
                    mostrarDatos();
                } else {
                    AlertDialog alertDialog = new AlertDialog();
                    alertDialog.mostrar("Error", "No se pudo eliminar la Cancion");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
