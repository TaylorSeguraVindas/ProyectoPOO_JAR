package segura.taylor.controlador.interfaz.admin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import segura.taylor.bl.entidades.ListaReproduccion;
import segura.taylor.bl.entidades.Cancion;
import segura.taylor.controlador.ControladorGeneral;
import segura.taylor.controlador.interfaz.listaReproduccion.ControladorRegistroListaReproduccion;
import segura.taylor.ui.dialogos.AlertDialog;
import segura.taylor.ui.dialogos.VentanaSeleccionarCancion;
import segura.taylor.ui.dialogos.YesNoDialog;

import java.util.List;

public class ControladorListasReproduccionAdmin {
    private ListaReproduccion listaReproduccionSeleccionada;

    public TableView tblListasReproduccion;
    public TableView tblCanciones;

    public VBox ventanaPrincipal;

    public void initialize() {
        //Detectar click de la tabla ListaReproduccion para actualizar artistas y canciones
        tblListasReproduccion.setOnMouseClicked((MouseEvent event) -> {
            if(event.getButton().equals(MouseButton.PRIMARY)){
                listaReproduccionSeleccionada = (ListaReproduccion) tblListasReproduccion.getSelectionModel().getSelectedItem();
                actualizarInfoListaReproduccion();
            }
        });

        inicializarTablaListasReproduccion();
        inicializarTablaCanciones();
        mostrarDatos();
    }

    public void inicializarTablaListasReproduccion() {
        //Nombre
        TableColumn<ListaReproduccion, String> columnaNombre = new TableColumn("Nombre");
        columnaNombre.setMinWidth(100);
        columnaNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));

        //Fecha creación
        TableColumn<ListaReproduccion, String> columnaFechaCreacion = new TableColumn("Fecha creacion");
        columnaFechaCreacion.setMinWidth(100);
        columnaFechaCreacion.setCellValueFactory(new PropertyValueFactory<>("fechaCreacion"));

        //Calificación
        TableColumn<ListaReproduccion, String> columnaCalificacion = new TableColumn("Calificación");
        columnaCalificacion.setMinWidth(100);
        columnaCalificacion.setCellValueFactory(new PropertyValueFactory<>("calificacion"));

        //Imagen
        TableColumn<ListaReproduccion, String> columnaImagen = new TableColumn("Imagen");
        columnaImagen.setMinWidth(100);
        columnaImagen.setCellValueFactory(new PropertyValueFactory<>("imagen"));

        //Descripción
        TableColumn<ListaReproduccion, String> columnaDescripcion = new TableColumn("Descripción");
        columnaDescripcion.setMinWidth(100);
        columnaDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));

        tblListasReproduccion.getColumns().addAll(columnaNombre, columnaFechaCreacion, columnaCalificacion, columnaImagen, columnaDescripcion);

    }
    public void inicializarTablaCanciones() {
        //Recurso
        TableColumn<Cancion, String> columnaRecurso = new TableColumn("Recurso");
        columnaRecurso.setMinWidth(100);
        columnaRecurso.setCellValueFactory(new PropertyValueFactory<>("recurso"));

        //Nombre
        TableColumn<Cancion, String> columnaNombre = new TableColumn("Nombre");
        columnaNombre.setMinWidth(100);
        columnaNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));

        //Genero
        TableColumn<Cancion, String> columnaGenero = new TableColumn("Genero");
        columnaGenero.setMinWidth(100);
        columnaGenero.setCellValueFactory(new PropertyValueFactory<>("nombreGenero"));

        //Duración
        TableColumn<Cancion, String> columnaDuracion = new TableColumn("Duración");
        columnaDuracion.setMinWidth(100);
        columnaDuracion.setCellValueFactory(new PropertyValueFactory<>("duracion"));

        //Fecha lanzamiento
        TableColumn<Cancion, String> columnaFechaLanzamiento = new TableColumn("Fecha lanzamiento");
        columnaFechaLanzamiento.setMinWidth(100);
        columnaFechaLanzamiento.setCellValueFactory(new PropertyValueFactory<>("fechaLanzamiento"));


        tblCanciones.getColumns().addAll(columnaRecurso, columnaNombre, columnaGenero, columnaDuracion, columnaFechaLanzamiento);
    }

    private void mostrarDatos() {
        tblListasReproduccion.getItems().clear();
        tblListasReproduccion.setItems(obtenerListasReproduccion());
    }
    private void actualizarInfoListaReproduccion() {
        if(listaReproduccionSeleccionada == null) return;

        ObservableList<Cancion> canciones = FXCollections.observableArrayList();

        for (Cancion cancion : listaReproduccionSeleccionada.getCanciones()) {  //Obtener canciones
            canciones.add(cancion);
        }

        tblCanciones.setItems(canciones);
    }

    public ObservableList<ListaReproduccion> obtenerListasReproduccion() {
        List<ListaReproduccion> listasReproduccion = ControladorGeneral.instancia.getGestor().listarListasReproduccion();

        ObservableList<ListaReproduccion> listasReproduccionFinal = FXCollections.observableArrayList();

        for(ListaReproduccion listaReproduccion : listasReproduccion) {
            if(listaReproduccionSeleccionada != null) {
                if(listaReproduccionSeleccionada.getId() == listaReproduccion.getId()) {
                    listaReproduccionSeleccionada = listaReproduccion;  //Actualizar info de la lista seleccionada
                }
            }
            listasReproduccionFinal.addAll(listaReproduccion);
        }

        return listasReproduccionFinal;
    }

    //ListaReproduccion
    public void agregarListaReproduccion() {
        try {
            Stage ventanaRegistroListaReproduccion = new Stage();
            //This locks previous window interacivity until this one is closed.
            ventanaRegistroListaReproduccion.initModality(Modality.APPLICATION_MODAL);

            //Referencias para el controlador
            ControladorRegistroListaReproduccion.ventana = ventanaRegistroListaReproduccion;
            ControladorRegistroListaReproduccion.modificando = false;

            VBox root = FXMLLoader.load(getClass().getResource("../../../ui/ventanas/VentanaRegistroListaReproduccion.fxml"));
            Scene escena = new Scene(root, 720, 510);

            ventanaRegistroListaReproduccion.setScene(escena);
            ventanaRegistroListaReproduccion.setTitle("Registro de Lista de Reproduccion");
            ventanaRegistroListaReproduccion.setResizable(false);
            ventanaRegistroListaReproduccion.showAndWait();

            mostrarDatos(); //Actualizar tabla
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void modificarListaReproduccion() {
        try {
            //Referencias para el controlador
            ListaReproduccion ListaReproduccionSeleccionada = (ListaReproduccion) tblListasReproduccion.getSelectionModel().getSelectedItem();

            if (ListaReproduccionSeleccionada == null) {
                AlertDialog alertDialog = new AlertDialog();
                alertDialog.mostrar("Error", "No hay ningún Albun seleccionado");
                return;
            }

            Stage ventanaRegistroAlbun = new Stage();
            //This locks previous window interacivity until this one is closed.
            ventanaRegistroAlbun.initModality(Modality.APPLICATION_MODAL);

            ControladorRegistroListaReproduccion.ventana = ventanaRegistroAlbun;
            ControladorRegistroListaReproduccion.idListaReproduccionSeleccionada = ListaReproduccionSeleccionada.getId();
            ControladorRegistroListaReproduccion.modificando = true;

            VBox root = FXMLLoader.load(getClass().getResource("../../../ui/ventanas/VentanaRegistroListaReproduccion.fxml"));

            //Referencia a los campos
            TextField txtNombre = (TextField) root.lookup("#txtNombre");
            TextArea txtDescripcion = (TextArea) root.lookup("#txtDescripcion");
            ImageView imagenFondo = (ImageView) root.lookup("#imagenFondo");

            //Actualizar campos
            txtNombre.setText(ListaReproduccionSeleccionada.getNombre());
            txtDescripcion.setText(listaReproduccionSeleccionada.getDescripcion());
            if(!listaReproduccionSeleccionada.getImagen().equals("") && !listaReproduccionSeleccionada.getImagen().equals("null")) {
                ControladorRegistroListaReproduccion.urlImagenFondo = listaReproduccionSeleccionada.getImagen();
                imagenFondo.setImage(new Image(listaReproduccionSeleccionada.getImagen()));
            }

            Scene escena = new Scene(root, 710, 550);

            ventanaRegistroAlbun.setScene(escena);
            ventanaRegistroAlbun.setTitle("Modificacion de Lista de Reproduccion");
            ventanaRegistroAlbun.setResizable(false);
            ventanaRegistroAlbun.showAndWait();

            mostrarDatos(); //Actualizar tabla
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void eliminarListaReproduccion() {
        YesNoDialog yesNoDialog = new YesNoDialog();
        boolean resultado = yesNoDialog.mostrar("Aviso", "Realmente quiere eliminar la Lista de Reproducción seleccionada?");

        if (resultado) {
            ListaReproduccion ListaReproduccionSeleccionada = (ListaReproduccion) tblListasReproduccion.getSelectionModel().getSelectedItem();

            if (ListaReproduccionSeleccionada == null) {
                AlertDialog alertDialog = new AlertDialog();
                alertDialog.mostrar("Error", "No hay ninguna Lista de Reproduccion seleccionada");
                return;
            }

            int idAlbun = ListaReproduccionSeleccionada.getId();
            try {
                resultado = ControladorGeneral.instancia.getGestor().eliminarListaReproduccion(idAlbun);
                if (resultado) {
                    AlertDialog alertDialog = new AlertDialog();
                    alertDialog.mostrar("Exito", "Lista de Reproduccion eliminada correctamente");
                    mostrarDatos();
                } else {
                    AlertDialog alertDialog = new AlertDialog();
                    alertDialog.mostrar("Error", "No se pudo eliminar la Lista de Reproduccion");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //Canciones
    public void agregarCancion() {
        VentanaSeleccionarCancion ventanaSeleccionarCancion = new VentanaSeleccionarCancion();
        int idCancion = ventanaSeleccionarCancion.mostrar();

        if(idCancion != -1) {
            try {
                boolean resultado = ControladorGeneral.instancia.getGestor().agregarCancionALista(listaReproduccionSeleccionada.getId(), idCancion);
                if(resultado) {
                    AlertDialog alertDialog = new AlertDialog();
                    alertDialog.mostrar("Éxito", "Canción agregada correctamente!");
                    mostrarDatos();
                    actualizarInfoListaReproduccion();
                } else {
                    AlertDialog alertDialog = new AlertDialog();
                    alertDialog.mostrar("Error", "No se pudo agregar la canción");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
    public void removerCancion() {
        YesNoDialog yesNoDialog = new YesNoDialog();
        boolean resultado = yesNoDialog.mostrar("Aviso", "Realmente quiere remover la canción seleccionada?");

        if (resultado) {
            Cancion cancionSeleccionada = (Cancion) tblCanciones.getSelectionModel().getSelectedItem();

            try {
                resultado = ControladorGeneral.instancia.getGestor().removerCancionDeLista(listaReproduccionSeleccionada.getId(), cancionSeleccionada.getId());
                if (resultado) {
                    AlertDialog alertDialog = new AlertDialog();
                    alertDialog.mostrar("Exito", "Canción removida correctamente");
                    mostrarDatos();
                    actualizarInfoListaReproduccion();
                } else {
                    AlertDialog alertDialog = new AlertDialog();
                    alertDialog.mostrar("Error", "No se pudo remover la canción :(");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
