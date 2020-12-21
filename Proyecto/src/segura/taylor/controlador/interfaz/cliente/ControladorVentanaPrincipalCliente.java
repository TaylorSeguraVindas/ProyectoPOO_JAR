package segura.taylor.controlador.interfaz.cliente;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import segura.taylor.bl.entidades.Cliente;
import segura.taylor.bl.entidades.ListaReproduccion;
import segura.taylor.bl.enums.TipoListaReproduccion;
import segura.taylor.controlador.ControladorGeneral;
import segura.taylor.controlador.interfaz.listaReproduccion.ControladorRegistroListaReproduccion;
import segura.taylor.controlador.interfaz.tienda.ControladorInfoListaReproduccion;
import segura.taylor.controlador.interfaz.usuarios.ControladorRegistroCliente;
import segura.taylor.ui.dialogos.AlertDialog;
import segura.taylor.ui.dialogos.YesNoDialog;

import java.util.List;

public class ControladorVentanaPrincipalCliente {
    public VBox contenedorPrincipal;
    public ListView listListaReproduccion;

    public Label lblNombreCancion;
    public Label lblNombreArtista;

    private void limpiarPantalla() {
        contenedorPrincipal.getChildren().clear();
    }

    public void initialize() {
        ControladorGeneral.instancia.refVentanaPrincipalCliente = this;
        listListaReproduccion.setOnMouseClicked(new EventHandler<MouseEvent>() {    //Evento doble click
            @Override
            public void handle(MouseEvent click) {
                if (click.getClickCount() == 2) {
                    ControladorInfoListaReproduccion.idListaSeleccionada = ControladorGeneral.instancia.getGestor().obtenerIdListaReproduccion(listListaReproduccion.getSelectionModel().getSelectedItem().toString());
                    mostrarInfoListaReproduccion();
                }
            }
        });

        actualizarListasReproduccionUsuario();
    }

    //GENERAL LISTAS REPRODUCCION
    public void actualizarListasReproduccionUsuario() {
        listListaReproduccion.getItems().clear();   //Limpiar

        List<ListaReproduccion> listas = ControladorGeneral.instancia.getGestor().getBibliotecaUsuarioIngresado().getListasDeReproduccion();

        for (ListaReproduccion lista : listas) {
            listListaReproduccion.getItems().add(lista.getNombre());
        }
    }

    public void crearListaReproduccion() {
        //Agregar a biblioteca y mostrar en el menu
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

            actualizarListasReproduccionUsuario(); //Actualizar tabla
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void modificarListaReproduccion() {
        try {
            //Referencias para el controlador
            if(listListaReproduccion.getSelectionModel().getSelectedItem() == null) return;

            int idListaSeleccionada = ControladorGeneral.instancia.getGestor().obtenerIdListaReproduccion(listListaReproduccion.getSelectionModel().getSelectedItem().toString());
            ListaReproduccion listaReproduccionSeleccionada = ControladorGeneral.instancia.getGestor().buscarListaReproduccionPorId(idListaSeleccionada).get();

            if (listaReproduccionSeleccionada == null) {
                AlertDialog alertDialog = new AlertDialog();
                alertDialog.mostrar("Error", "No se encontró la lista");
                return;
            }

            //El usuario solo puede modificar listas que haya creado
            if(listaReproduccionSeleccionada.getTipoLista().equals(TipoListaReproduccion.PARA_TIENDA)) {
                AlertDialog alertDialog = new AlertDialog();
                alertDialog.mostrar("Error", "No tiene permisos para modificar esta lista");
                return;
            }

            Stage ventanaRegistroAlbun = new Stage();
            //This locks previous window interacivity until this one is closed.
            ventanaRegistroAlbun.initModality(Modality.APPLICATION_MODAL);

            ControladorRegistroListaReproduccion.ventana = ventanaRegistroAlbun;
            ControladorRegistroListaReproduccion.idListaReproduccionSeleccionada = listaReproduccionSeleccionada.getId();
            ControladorRegistroListaReproduccion.modificando = true;

            VBox root = FXMLLoader.load(getClass().getResource("../../../ui/ventanas/VentanaRegistroListaReproduccion.fxml"));

            //Referencia a los campos
            TextField txtNombre = (TextField) root.lookup("#txtNombre");
            TextArea txtDescripcion = (TextArea) root.lookup("#txtDescripcion");
            ImageView imagenFondo = (ImageView) root.lookup("#imagenFondo");

            //Actualizar campos
            txtNombre.setText(listaReproduccionSeleccionada.getNombre());
            txtDescripcion.setText(listaReproduccionSeleccionada.getDescripcion());
            if(!listaReproduccionSeleccionada.getImagen().equals("")) {
                ControladorRegistroListaReproduccion.urlImagenFondo = listaReproduccionSeleccionada.getImagen();
                imagenFondo.setImage(new Image(listaReproduccionSeleccionada.getImagen()));
            }

            Scene escena = new Scene(root, 710, 550);

            ventanaRegistroAlbun.setScene(escena);
            ventanaRegistroAlbun.setTitle("Modificacion de Lista de Reproduccion");
            ventanaRegistroAlbun.setResizable(false);
            ventanaRegistroAlbun.showAndWait();

            actualizarListasReproduccionUsuario();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void removerListaReproduccion() {
        YesNoDialog yesNoDialog = new YesNoDialog();
        boolean decision = yesNoDialog.mostrar("Aviso", "Está realmente seguro que desea remover la lista seleccionada");

        if(!decision) return;


        //remover de biblioteca y actualizar el menu
        if(listListaReproduccion.getSelectionModel().getSelectedItem() == null) return;

        int idListaSeleccionada = ControladorGeneral.instancia.getGestor().obtenerIdListaReproduccion(listListaReproduccion.getSelectionModel().getSelectedItem().toString());

        if(idListaSeleccionada != -1) {
            try {
                boolean resultado = ControladorGeneral.instancia.getGestor().removerListaReproduccionDeBibliotecaUsuario(ControladorGeneral.instancia.getIdUsuarioIngresado(), idListaSeleccionada);

                if(resultado) {
                    ListaReproduccion listaRemovida = ControladorGeneral.instancia.getGestor().buscarListaReproduccionPorId(idListaSeleccionada).get();

                    //Si la lista fue creada por el usuario se elimina del todo
                    if(listaRemovida.getTipoLista().equals(TipoListaReproduccion.PARA_USUARIO)) {
                        resultado = ControladorGeneral.instancia.getGestor().eliminarListaReproduccion(idListaSeleccionada);
                    }

                    if(resultado) {
                        AlertDialog alertDialog = new AlertDialog();
                        alertDialog.mostrar("Éxito!", "Lista removida correctamente");
                        actualizarListasReproduccionUsuario();
                        return;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        AlertDialog alertDialog = new AlertDialog();
        alertDialog.mostrar("Error", "Ocurrió un problema al intentar remover la lista");
    }

    //MUSICA
    public void onPausaReproducirPressed() {
        ControladorGeneral.instancia.alternarEstadoCancion();
    }
    public void onSiguienteCancionPressed() {
        ControladorGeneral.instancia.siguienteCancion();
    }
    public void onCancionAnteriorPressed() {
        ControladorGeneral.instancia.cancionAnterior();
    }
    public void actualizarInfoCancion(String nombreCancion, String nombreArtista) {
        lblNombreCancion.setText(nombreCancion);
        lblNombreArtista.setText(nombreArtista);
    }

    //MENUS
    public void mostrarTienda() {
        limpiarPantalla();
        try {
            VBox root = FXMLLoader.load(getClass().getResource("../../../ui/ventanas/tienda/VentanaTienda.fxml"));
            contenedorPrincipal.getChildren().add(root);
            //Expandir
            root.prefWidthProperty().bind(contenedorPrincipal.widthProperty());
            root.prefHeightProperty().bind(contenedorPrincipal.heightProperty());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void mostrarInfoListaReproduccion() {
        limpiarPantalla();
        try {
            ControladorInfoListaReproduccion.desdeTienda = false;

            VBox root = FXMLLoader.load(getClass().getResource("../../../ui/ventanas/tienda/VentanaInfoListaReproduccion.fxml"));
            contenedorPrincipal.getChildren().add(root);
            //Expandir
            root.prefWidthProperty().bind(contenedorPrincipal.widthProperty());
            root.prefHeightProperty().bind(contenedorPrincipal.heightProperty());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void mostrarInfoAlbum() {
        limpiarPantalla();
        try {
            VBox root = FXMLLoader.load(getClass().getResource("../../../ui/ventanas/tienda/VentanaInfoAlbum.fxml"));
            contenedorPrincipal.getChildren().add(root);
            //Expandir
            root.prefWidthProperty().bind(contenedorPrincipal.widthProperty());
            root.prefHeightProperty().bind(contenedorPrincipal.heightProperty());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void mostrarBiblioteca() {
        limpiarPantalla();
        try {
            VBox root = FXMLLoader.load(getClass().getResource("../../../ui/ventanas/cliente/BibliotecaCliente.fxml"));
            contenedorPrincipal.getChildren().add(root);
            //Expandir
            root.prefWidthProperty().bind(contenedorPrincipal.widthProperty());
            root.prefHeightProperty().bind(contenedorPrincipal.heightProperty());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void modificarUsuario() {
        //Ventana para modificar usuario
        if(ControladorGeneral.instancia.usuarioIngresadoEsAdmin()) return;

        try {
            //Referencias para el controlador
            Cliente usuarioIngresado = (Cliente) ControladorGeneral.instancia.getUsuarioIngresado();

            if (usuarioIngresado == null) {
                AlertDialog alertDialog = new AlertDialog();
                alertDialog.mostrar("Error", "No se pudo verificar el usuario ingresado");
                return;
            }


            Stage ventanaRegistroAdmin = new Stage();
            //This locks previous window interacivity until this one is closed.
            ventanaRegistroAdmin.initModality(Modality.APPLICATION_MODAL);

            ControladorRegistroCliente.ventana = ventanaRegistroAdmin;
            ControladorRegistroCliente.idCliente = usuarioIngresado.getId();
            ControladorRegistroCliente.modificando = true;

            VBox root = FXMLLoader.load(getClass().getResource("../../../ui/ventanas/VentanaRegistroCliente.fxml"));

            //Referencia a los campos
            TextField txtCorreo = (TextField) root.lookup("#txtCorreo");
            PasswordField txtContrasenna = (PasswordField) root.lookup("#txtContrasenna");
            TextField txtNombre = (TextField) root.lookup("#txtNombre");
            TextField txtApellidos = (TextField) root.lookup("#txtApellidos");
            TextField txtNombreUsuario = (TextField) root.lookup("#txtNombreUsuario");
            ImageView imagenPerfil = (ImageView) root.lookup("#imagenPerfil");
            DatePicker txtFechaNacimiento = (DatePicker) root.lookup("#txtFechaNacimiento");
            ComboBox txtPais = (ComboBox) root.lookup("#txtPais");

            //Actualizar campos
            txtCorreo.setText(usuarioIngresado.getCorreo());
            txtContrasenna.setText(usuarioIngresado.getContrasenna());
            txtNombre.setText(usuarioIngresado.getNombre());
            txtApellidos.setText(usuarioIngresado.getApellidos());
            txtNombreUsuario.setText(usuarioIngresado.getNombreUsuario());
            txtFechaNacimiento.setValue(usuarioIngresado.getFechaNacimiento());
            txtPais.setValue(usuarioIngresado.getPais().toComboBoxItem());

            ControladorRegistroCliente.urlImagenPerfil = "";

            if(!usuarioIngresado.getImagenPerfil().equals("") && !usuarioIngresado.getImagenPerfil().equals("null")) {
                ControladorRegistroCliente.urlImagenPerfil = usuarioIngresado.getImagenPerfil();
                try {
                    imagenPerfil.setImage(new Image(usuarioIngresado.getImagenPerfil()));
                } catch (Exception e) {
                    System.out.println("Imagen invalida");
                }
            }

            //Desactivar campos inmodificables
            txtContrasenna.setDisable(true);
            txtFechaNacimiento.setDisable(true);
            txtPais.setDisable(true);

            Scene escena = new Scene(root, 580, 440);

            ventanaRegistroAdmin.setScene(escena);
            ventanaRegistroAdmin.setTitle("Modificación de cliente");
            ventanaRegistroAdmin.setResizable(false);
            ventanaRegistroAdmin.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cerrarSesion() {
        ControladorGeneral.instancia.menuIniciarSesion();
    }

    public void mostrarInfoCancion() {
        limpiarPantalla();
        try {
            VBox root = FXMLLoader.load(getClass().getResource("../../../ui/ventanas/tienda/VentanaInfoCancion.fxml"));
            contenedorPrincipal.getChildren().add(root);
            //Expandir
            root.prefWidthProperty().bind(contenedorPrincipal.widthProperty());
            root.prefHeightProperty().bind(contenedorPrincipal.heightProperty());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
