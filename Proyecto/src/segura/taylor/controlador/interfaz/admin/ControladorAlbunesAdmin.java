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
import segura.taylor.bl.entidades.*;
import segura.taylor.controlador.ControladorGeneral;
import segura.taylor.controlador.interfaz.album.ControladorRegistroAlbum;
import segura.taylor.ui.dialogos.AlertDialog;
import segura.taylor.ui.dialogos.VentanaSeleccionarArtista;
import segura.taylor.ui.dialogos.VentanaSeleccionarCancion;
import segura.taylor.ui.dialogos.YesNoDialog;

import java.util.List;

public class ControladorAlbunesAdmin {
    private Album albumSeleccionado;

    public TableView tblAlbunes;
    public TableView tblArtistas;
    public TableView tblCanciones;

    public VBox ventanaPrincipal;

    public void initialize() {
        //Detectar click de la tabla album para actualizar artistas y canciones
        tblAlbunes.setOnMouseClicked((MouseEvent event) -> {
            if(event.getButton().equals(MouseButton.PRIMARY)){
                albumSeleccionado = (Album) tblAlbunes.getSelectionModel().getSelectedItem();
                actualizarInfoAlbum();
            }
        });

        inicializarTablaAlbunes();
        inicializarTablaArtistas();
        inicializarTablaCanciones();
        mostrarDatos();
    }

    public void inicializarTablaAlbunes() {
        //Nombre
        TableColumn<Album, String> columnaNombre = new TableColumn("Nombre");
        columnaNombre.setMinWidth(100);
        columnaNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));

        //Fecha creación
        TableColumn<Album, String> columnaFechaCreacion = new TableColumn("Fecha lanzamiento");
        columnaFechaCreacion.setMinWidth(100);
        columnaFechaCreacion.setCellValueFactory(new PropertyValueFactory<>("fechaLanzamiento"));

        //Fecha lanzamiento
        TableColumn<Album, String> columnaFechaLanzamiento = new TableColumn("Fecha lanzamiento");
        columnaFechaLanzamiento.setMinWidth(100);
        columnaFechaLanzamiento.setCellValueFactory(new PropertyValueFactory<>("fechaLanzamiento"));

        tblAlbunes.getColumns().addAll(columnaNombre, columnaFechaCreacion, columnaFechaLanzamiento);

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
    public void inicializarTablaArtistas() {
        //NombreArtistico
        TableColumn<Artista, String> columnaNombreArtistico = new TableColumn("Nombre Artistico");
        columnaNombreArtistico.setMinWidth(100);
        columnaNombreArtistico.setCellValueFactory(new PropertyValueFactory<>("nombreArtistico"));

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

        tblArtistas.getColumns().addAll(columnaNombreArtistico, columnaGenero, columnaPais, columnaDescripcion);
    }

    private void mostrarDatos() {
        tblAlbunes.getItems().clear();
        tblAlbunes.setItems(obtenerAlbunes());
    }
    private void actualizarInfoAlbum() {
        if(albumSeleccionado == null) return;

        ObservableList<Artista> artistas = FXCollections.observableArrayList();
        ObservableList<Cancion> canciones = FXCollections.observableArrayList();

        for (Artista artista : albumSeleccionado.getArtistas()) {   //Obtener artistas
            artistas.add(artista);
        }

        for (Cancion cancion : albumSeleccionado.getCanciones()) {  //Obtener canciones
            canciones.add(cancion);
        }

        tblArtistas.setItems(artistas);
        tblCanciones.setItems(canciones);
    }

    public ObservableList<Album> obtenerAlbunes() {
        List<Album> albunes = ControladorGeneral.instancia.getGestor().listarAlbunes();

        ObservableList<Album> albunesFinal = FXCollections.observableArrayList();

        for(Album album : albunes) {
            if(albumSeleccionado != null) {
                if(albumSeleccionado.getId() == album.getId()) {
                    albumSeleccionado = album;  //Actualizar info del album seleccionado.
                }
            }
            albunesFinal.add(album);
        }

        return albunesFinal;
    }

    //Album
    public void agregarAlbum() {
        try {
            Stage ventanaRegistroAlbum = new Stage();
            //This locks previous window interacivity until this one is closed.
            ventanaRegistroAlbum.initModality(Modality.APPLICATION_MODAL);

            //Referencias para el controlador
            ControladorRegistroAlbum.ventana = ventanaRegistroAlbum;
            ControladorRegistroAlbum.modificando = false;

            VBox root = FXMLLoader.load(getClass().getResource("../../../ui/ventanas/VentanaRegistroAlbum.fxml"));
            Scene escena = new Scene(root, 720, 510);

            ventanaRegistroAlbum.setScene(escena);
            ventanaRegistroAlbum.setTitle("Registro de Album");
            ventanaRegistroAlbum.setResizable(false);
            ventanaRegistroAlbum.showAndWait();

            mostrarDatos(); //Actualizar tabla
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void modificarAlbum() {
        try {
            //Referencias para el controlador
            Album albumSeleccionado = (Album) tblAlbunes.getSelectionModel().getSelectedItem();

            if (albumSeleccionado == null) {
                AlertDialog alertDialog = new AlertDialog();
                alertDialog.mostrar("Error", "No hay ningún Albun seleccionado");
                return;
            }

            Stage ventanaRegistroAlbun = new Stage();
            //This locks previous window interacivity until this one is closed.
            ventanaRegistroAlbun.initModality(Modality.APPLICATION_MODAL);

            ControladorRegistroAlbum.ventana = ventanaRegistroAlbun;
            ControladorRegistroAlbum.idAlbumSeleccionado = albumSeleccionado.getId();
            ControladorRegistroAlbum.modificando = true;

            VBox root = FXMLLoader.load(getClass().getResource("../../../ui/ventanas/VentanaRegistroAlbum.fxml"));

            //Referencia a los campos
            TextField txtNombre = (TextField) root.lookup("#txtNombre");
            DatePicker txtFechaLanzamiento = (DatePicker) root.lookup("#txtFechaLanzamiento");
            ImageView imgFondo = (ImageView) root.lookup("#imagenFondo");

            //Actualizar campos
            txtNombre.setText(albumSeleccionado.getNombre());
            txtFechaLanzamiento.setValue(albumSeleccionado.getFechaLanzamiento());
            System.out.println("Imagen: " + albumSeleccionado.getImagen());

            if(!albumSeleccionado.getImagen().equals("") && !albumSeleccionado.getImagen().equals("null")) {
                ControladorRegistroAlbum.urlImagenFondo = albumSeleccionado.getImagen();
                imgFondo.setImage(new Image(albumSeleccionado.getImagen()));
            }

            //Desactivar campos inmodificables
            txtFechaLanzamiento.setDisable(true);

            Scene escena = new Scene(root, 580, 440);

            ventanaRegistroAlbun.setScene(escena);
            ventanaRegistroAlbun.setTitle("Modificacion de Album");
            ventanaRegistroAlbun.setResizable(false);
            ventanaRegistroAlbun.showAndWait();

            mostrarDatos(); //Actualizar tabla
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void eliminarAlbum() {
        YesNoDialog yesNoDialog = new YesNoDialog();
        boolean resultado = yesNoDialog.mostrar("Aviso", "Realmente quiere eliminar al Albun seleccionado?");

        if (resultado) {
            Album AlbumSeleccionado = (Album) tblAlbunes.getSelectionModel().getSelectedItem();

            if (AlbumSeleccionado == null) {
                AlertDialog alertDialog = new AlertDialog();
                alertDialog.mostrar("Error", "No hay ningún Album seleccionado");
                return;
            }

            int idAlbun = AlbumSeleccionado.getId();
            try {
                resultado = ControladorGeneral.instancia.getGestor().eliminarAlbum(idAlbun);
                if (resultado) {
                    AlertDialog alertDialog = new AlertDialog();
                    alertDialog.mostrar("Exito", "Album eliminado correctamente");
                    mostrarDatos();
                } else {
                    AlertDialog alertDialog = new AlertDialog();
                    alertDialog.mostrar("Error", "No se pudo eliminar el Album");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //Artistas
    public void agregarArtista() {
        VentanaSeleccionarArtista ventanaSeleccionarArtista = new VentanaSeleccionarArtista();
        int idArtista = ventanaSeleccionarArtista.mostrar();

        if(idArtista != -1) {
            try {
                boolean resultado = ControladorGeneral.instancia.getGestor().agregarArtistaEnAlbum(albumSeleccionado.getId(), idArtista);
                if(resultado) {
                    AlertDialog alertDialog = new AlertDialog();
                    alertDialog.mostrar("Éxito", "Artista agregado correctamente!");
                    mostrarDatos();
                    actualizarInfoAlbum();
                } else {
                    AlertDialog alertDialog = new AlertDialog();
                    alertDialog.mostrar("Error", "No se pudo agregar el artista");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
    public void removerArtista() {
        YesNoDialog yesNoDialog = new YesNoDialog();
        boolean resultado = yesNoDialog.mostrar("Aviso", "Realmente quiere eliminar al artista seleccionado?");

        if (resultado) {
            Artista artistaSeleccionado = (Artista) tblArtistas.getSelectionModel().getSelectedItem();

            try {
                resultado = ControladorGeneral.instancia.getGestor().removerArtistaDeAlbum(albumSeleccionado.getId(), artistaSeleccionado.getId());
                if (resultado) {
                    AlertDialog alertDialog = new AlertDialog();
                    alertDialog.mostrar("Exito", "Artista removido correctamente");
                    mostrarDatos();
                    actualizarInfoAlbum();
                } else {
                    AlertDialog alertDialog = new AlertDialog();
                    alertDialog.mostrar("Error", "No se pudo remover el artista");
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
                boolean resultado = ControladorGeneral.instancia.getGestor().agregarCancionEnAlbum(albumSeleccionado.getId(), idCancion);
                if(resultado) {
                    AlertDialog alertDialog = new AlertDialog();
                    alertDialog.mostrar("Éxito", "Canción agregada correctamente!");
                    mostrarDatos();
                    actualizarInfoAlbum();
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
        boolean resultado = yesNoDialog.mostrar("Aviso", "Realmente quiere eliminar la canción seleccionada?");

        if (resultado) {
            Cancion cancionSeleccionada = (Cancion) tblCanciones.getSelectionModel().getSelectedItem();

            try {
                resultado = ControladorGeneral.instancia.getGestor().removerCancionDeAlbum(albumSeleccionado.getId(), cancionSeleccionada.getId());
                if (resultado) {
                    AlertDialog alertDialog = new AlertDialog();
                    alertDialog.mostrar("Exito", "Canción removida correctamente");
                    mostrarDatos();
                    actualizarInfoAlbum();
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
