package segura.taylor.controlador.interfaz.cliente;

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
import segura.taylor.bl.entidades.Cancion;
import segura.taylor.bl.entidades.Compositor;
import segura.taylor.bl.entidades.Genero;
import segura.taylor.bl.enums.TipoCancion;
import segura.taylor.controlador.ControladorGeneral;
import segura.taylor.controlador.interfaz.cancion.ControladorRegistroCancion;
import segura.taylor.ui.dialogos.*;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class ControladorBibliotecaCliente {
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

        tblCanciones.getColumns().addAll(columnaNombre, columnaDuracion, columnaFechaLanzamiento, columnaArtista, columnaCompositor, columnaGenero, columnaPrecio);

    }
    private void mostrarDatos() {
        tblCanciones.getItems().clear();
        tblCanciones.setItems(obtenerCanciones(!txtBusqueda.getText().trim().equals("")));   //Usa filtro si el texto de búsqueda no está vacío
    }

    public ObservableList<Cancion> obtenerCanciones(boolean usandoFiltros) {
        ObservableList<Cancion> cancionesFinal = FXCollections.observableArrayList();

        try {
            List<Cancion> canciones = ControladorGeneral.instancia.getGestor().listarCancionesDeBibliotecaUsuario(ControladorGeneral.instancia.getIdUsuarioIngresado());

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
        } catch (Exception e) {
            e.printStackTrace();
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
        VentanaFiltroCancionesBiblioteca ventanaFiltros = new VentanaFiltroCancionesBiblioteca();
        ventanaFiltros.mostrar();
    }

    public void buscar() {
        //Actualizar lista
        mostrarDatos();
    }

    public void agregarCancionSeleccionadaARepo() {
        Cancion cancionSeleccionada = (Cancion) tblCanciones.getSelectionModel().getSelectedItem();

        if(cancionSeleccionada != null) {
            VentanaSeleccionarLista ventanaSeleccionarLista = new VentanaSeleccionarLista();
            int idLista = ventanaSeleccionarLista.mostrar();

            if(idLista != -1) {
                try {
                    boolean resultado = ControladorGeneral.instancia.getGestor().agregarCancionALista(idLista, cancionSeleccionada.getId());

                    if(resultado) {
                        AlertDialog alertDialog = new AlertDialog();
                        alertDialog.mostrar("Éxito", "Cancion agregada correctamente!");
                    } else {
                        AlertDialog alertDialog = new AlertDialog();
                        alertDialog.mostrar("Error", "No se pudo agregar la cancion");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
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
                //Si fue subida subida por el usuario se elimina del todo
                //Si fue comprada solo se quita de la biblioteca
                Optional<Cancion> cancion = ControladorGeneral.instancia.getGestor().buscarCancionPorId(idCancion);

                if(cancion.isPresent()) {
                    if(cancion.get().getTipoCancion().equals(TipoCancion.PARA_USUARIO)) {
                        resultado = ControladorGeneral.instancia.getGestor().eliminarCancion(idCancion);
                    } else if(cancion.get().getTipoCancion().equals(TipoCancion.PARA_TIENDA)) {
                        resultado = ControladorGeneral.instancia.getGestor().removerCancionDeBibliotecaUsuario(ControladorGeneral.instancia.getIdUsuarioIngresado(), idCancion);
                    }
                } else {
                    resultado = false;
                }

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
