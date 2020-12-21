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
import segura.taylor.bl.entidades.Artista;
import segura.taylor.bl.entidades.Genero;
import segura.taylor.bl.entidades.Pais;
import segura.taylor.controlador.ControladorGeneral;
import segura.taylor.controlador.interfaz.artista.ControladorRegistroArtista;
import segura.taylor.ui.dialogos.AlertDialog;
import segura.taylor.ui.dialogos.VentanaFiltroArtistasAdmin;
import segura.taylor.ui.dialogos.YesNoDialog;

import java.util.List;
import java.util.Locale;

public class ControladorArtistasAdmin {
    public static boolean filtrandoPorNombre = true;
    public static boolean filtrandoPorAlias = false;
    public static boolean filtrandoPorNacionalidad = false;

    public TableView tblArtistas;
    public VBox ventanaPrincipal;

    public TextField txtBusqueda;

    public void initialize() {
        //Reiniciar filtros
        filtrandoPorNombre = true;
        filtrandoPorAlias = false;
        filtrandoPorNacionalidad = false;

        inicializarTabla();
        mostrarDatos();
    }

    public void inicializarTabla() {
        //Nombre
        TableColumn<Artista, String> columnaNombre = new TableColumn("Nombre");
        columnaNombre.setMinWidth(100);
        columnaNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));

        //Apellidos
        TableColumn<Artista, String> columnaApellidos = new TableColumn("Apellidos");
        columnaApellidos.setMinWidth(100);
        columnaApellidos.setCellValueFactory(new PropertyValueFactory<>("apellidos"));

        //NombreArtistico
        TableColumn<Artista, String> columnaNombreArtistico = new TableColumn("Nombre Artistico");
        columnaNombreArtistico.setMinWidth(100);
        columnaNombreArtistico.setCellValueFactory(new PropertyValueFactory<>("nombreArtistico"));

        //FechaNacimiento
        TableColumn<Artista, String> columnaFechaNacimiento = new TableColumn("Fecha Nacimiento");
        columnaFechaNacimiento.setMinWidth(100);
        columnaFechaNacimiento.setCellValueFactory(new PropertyValueFactory<>("fechaNacimiento"));

        //FechaDefuncion
        TableColumn<Artista, String> columnaFechaDefuncion = new TableColumn("Fecha Defuncion");
        columnaFechaDefuncion.setMinWidth(100);
        columnaFechaDefuncion.setCellValueFactory(new PropertyValueFactory<>("fechaDefuncion"));

        //Edad
        TableColumn<Artista, String> columnaEdad = new TableColumn("Edad");
        columnaEdad.setMinWidth(100);
        columnaEdad.setCellValueFactory(new PropertyValueFactory<>("edad"));

        //Genero
        TableColumn<Artista, String> columnaGenero = new TableColumn("Genero");
        columnaGenero.setMinWidth(100);
        columnaGenero.setCellValueFactory(new PropertyValueFactory<>("nombreGenero"));

        //Pais
        TableColumn<Artista, String> columnaPais = new TableColumn("Pais");
        columnaPais.setMinWidth(100);
        columnaPais.setCellValueFactory(new PropertyValueFactory<>("nombrePais"));

        //Descripcion
        TableColumn<Artista, String> columnaDescripcion = new TableColumn("Descripcion");
        columnaDescripcion.setMinWidth(100);
        columnaDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));

        tblArtistas.getColumns().addAll(columnaNombre, columnaApellidos, columnaNombreArtistico, columnaFechaNacimiento,columnaFechaDefuncion, columnaEdad, columnaGenero, columnaPais, columnaDescripcion);

    }
    private void mostrarDatos() {
        tblArtistas.getItems().clear();
        tblArtistas.setItems(obtenerArtistas(!txtBusqueda.getText().trim().equals("")));   //Usa filtro si el texto de búsqueda no está vacío
    }

    public ObservableList<Artista> obtenerArtistas(boolean usandoFiltro) {
        List<Artista> artistas = ControladorGeneral.instancia.getGestor().listarArtistas();

        ObservableList<Artista> artistasFinal = FXCollections.observableArrayList();

        for(Artista artista : artistas) {
            if(usandoFiltro) {
                if(artistaCoincideConBusqueda(artista)) {
                    artistasFinal.addAll(artista);
                }
            } else {
                artistasFinal.addAll(artista);
            }
        }

        return artistasFinal;
    }

    private boolean artistaCoincideConBusqueda(Artista artista) {
        String textoBusqueda = txtBusqueda.getText().trim().toUpperCase(Locale.ROOT);

        //NOMBRE
        if(filtrandoPorNombre) {
            String nombreArtista = artista.getNombre().trim().toUpperCase(Locale.ROOT);

            if(nombreArtista.equals(textoBusqueda) || nombreArtista.contains(textoBusqueda)) {
                return true;
            }
        }

        //ALIAS
        if(filtrandoPorAlias) {
            String nombreArtistico = artista.getNombreArtistico().trim().toUpperCase(Locale.ROOT);

            if(nombreArtistico.equals(textoBusqueda) || nombreArtistico.contains(textoBusqueda)) {
                return true;
            }
        }

        //NACIONALIDAD
        if(filtrandoPorNombre) {
            String paisArtista = artista.getNombrePais().trim().toUpperCase(Locale.ROOT);

            if(paisArtista.equals(textoBusqueda) || paisArtista.contains(textoBusqueda)) {
                return true;
            }
        }

        return false;
    }
    public void buscar() {
        mostrarDatos();
    }

    public void abrirFiltros() {
        VentanaFiltroArtistasAdmin ventanaFiltros = new VentanaFiltroArtistasAdmin();
        ventanaFiltros.mostrar();
    }

    public void agregarArtista() {
        try {
            Stage ventanaRegistroArtista = new Stage();
            //This locks previous window interacivity until this one is closed.
            ventanaRegistroArtista.initModality(Modality.APPLICATION_MODAL);

            //Referencias para el controlador
            ControladorRegistroArtista.ventana = ventanaRegistroArtista;
            ControladorRegistroArtista.modificando = false;

            VBox root = FXMLLoader.load(getClass().getResource("../../../ui/ventanas/VentanaRegistroArtista.fxml"));
            Scene escena = new Scene(root, 580, 440);

            ventanaRegistroArtista.setScene(escena);
            ventanaRegistroArtista.setTitle("Registro de artista");
            ventanaRegistroArtista.setResizable(false);
            ventanaRegistroArtista.showAndWait();

            mostrarDatos(); //Actualizar tabla
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void modificarArtista() {
        try {
            //Referencias para el controlador
            Artista artistaSeleccionado = (Artista) tblArtistas.getSelectionModel().getSelectedItem();

            if (artistaSeleccionado == null) {
                AlertDialog alertDialog = new AlertDialog();
                alertDialog.mostrar("Error", "No hay ningún artista seleccionado");
                return;
            }

            Stage ventanaRegistroArtista = new Stage();
            //This locks previous window interacivity until this one is closed.
            ventanaRegistroArtista.initModality(Modality.APPLICATION_MODAL);

            ControladorRegistroArtista.ventana = ventanaRegistroArtista;
            ControladorRegistroArtista.idArtistaSeleccionado = artistaSeleccionado.getId();
            ControladorRegistroArtista.modificando = true;

            VBox root = FXMLLoader.load(getClass().getResource("../../../ui/ventanas/VentanaRegistroArtista.fxml"));

            //Referencia a los campos
            TextField txtNombre = (TextField) root.lookup("#txtNombre");
            TextField txtApellidos = (TextField) root.lookup("#txtApellidos");
            TextField txtNombreArtistico = (TextField) root.lookup("#txtNombreArtistico");
            ComboBox txtGenero = (ComboBox) root.lookup("#txtGenero");
            ComboBox txtPais = (ComboBox) root.lookup("#txtPais");
            DatePicker txtFechaNacimiento = (DatePicker) root.lookup("#txtFechaNacimiento");
            DatePicker txtFechaDefuncion = (DatePicker) root.lookup("#txtFechaDefuncion");
            TextArea txtDescripcion = (TextArea) root.lookup("#txtDescripcion");

            //Actualizar campos
            txtNombre.setText(artistaSeleccionado.getNombre());
            txtApellidos.setText(artistaSeleccionado.getApellidos());
            txtNombreArtistico.setText(artistaSeleccionado.getNombreArtistico());

            Genero generoArtista = artistaSeleccionado.getGenero();
            if(generoArtista != null) {
                txtGenero.setValue(generoArtista.getNombre());
            }

            Pais paisArtista = artistaSeleccionado.getPaisNacimiento();
            if(paisArtista != null) {
                txtPais.setValue(paisArtista.getNombre());
            }

            txtFechaNacimiento.setValue(artistaSeleccionado.getFechaNacimiento());
            txtFechaDefuncion.setValue(artistaSeleccionado.getFechaDefuncion());
            txtDescripcion.setText(artistaSeleccionado.getDescripcion());

            //Desactivar campos inmodificables
            txtGenero.setDisable(true);
            txtPais.setDisable(true);
            txtFechaNacimiento.setDisable(true);

            Scene escena = new Scene(root, 580, 440);

            ventanaRegistroArtista.setScene(escena);
            ventanaRegistroArtista.setTitle("Modificación de artista");
            ventanaRegistroArtista.setResizable(false);
            ventanaRegistroArtista.showAndWait();

            mostrarDatos(); //Actualizar tabla
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void eliminarArtista() {
        YesNoDialog yesNoDialog = new YesNoDialog();
        boolean resultado = yesNoDialog.mostrar("Aviso", "Realmente quiere eliminar al artista seleccionado?");

        if (resultado) {
            Artista artistaSeleccionado = (Artista) tblArtistas.getSelectionModel().getSelectedItem();

            if (artistaSeleccionado == null) {
                AlertDialog alertDialog = new AlertDialog();
                alertDialog.mostrar("Error", "No hay ningún artista seleccionado");
                return;
            }

            int idArtista = artistaSeleccionado.getId();
            try {
                resultado = ControladorGeneral.instancia.getGestor().eliminarArtista(idArtista);
                if (resultado) {
                    AlertDialog alertDialog = new AlertDialog();
                    alertDialog.mostrar("Exito", "Artista eliminado correctamente");
                    mostrarDatos();
                } else {
                    AlertDialog alertDialog = new AlertDialog();
                    alertDialog.mostrar("Error", "No se pudo eliminar el artista");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
