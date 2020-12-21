package segura.taylor.controlador.interfaz.admin;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import segura.taylor.bl.entidades.Admin;
import segura.taylor.controlador.ControladorGeneral;
import segura.taylor.controlador.interfaz.usuarios.ControladorRegistroAdmin;
import segura.taylor.ui.dialogos.AlertDialog;


public class ControladorVentanaPrincipalAdmin {
    public VBox contenedorPrincipal;
    public Label lblNombreCancion;
    public Label lblNombreArtista;

    private void limpiarPantalla() {
        contenedorPrincipal.getChildren().clear();
    }

    public void initialize() {
        ControladorGeneral.instancia.refVentanaPrincipalAdmin = this;
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
    public void mostrarUsuarios() {
        limpiarPantalla();
        try {
            VBox root = FXMLLoader.load(getClass().getResource("../../../ui/ventanas/admin/UsuariosAdmin.fxml"));
            contenedorPrincipal.getChildren().add(root);
            //Expandir
            root.prefWidthProperty().bind(contenedorPrincipal.widthProperty());
            root.prefHeightProperty().bind(contenedorPrincipal.heightProperty());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void mostrarAlbunes() {
        limpiarPantalla();
        try {
            VBox root = FXMLLoader.load(getClass().getResource("../../../ui/ventanas/admin/AlbunesAdmin.fxml"));
            contenedorPrincipal.getChildren().add(root);
            //Expandir
            root.prefWidthProperty().bind(contenedorPrincipal.widthProperty());
            root.prefHeightProperty().bind(contenedorPrincipal.heightProperty());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void mostrarArtistas() {
        limpiarPantalla();
        try {
            VBox root = FXMLLoader.load(getClass().getResource("../../../ui/ventanas/admin/ArtistasAdmin.fxml"));
            contenedorPrincipal.getChildren().add(root);
            //Expandir
            root.prefWidthProperty().bind(contenedorPrincipal.widthProperty());
            root.prefHeightProperty().bind(contenedorPrincipal.heightProperty());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void mostrarCompositores() {
        limpiarPantalla();
        try {
            VBox root = FXMLLoader.load(getClass().getResource("../../../ui/ventanas/admin/CompositoresAdmin.fxml"));
            contenedorPrincipal.getChildren().add(root);
            //Expandir
            root.prefWidthProperty().bind(contenedorPrincipal.widthProperty());
            root.prefHeightProperty().bind(contenedorPrincipal.heightProperty());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void mostrarGeneros() {
        limpiarPantalla();
        try {
            VBox root = FXMLLoader.load(getClass().getResource("../../../ui/ventanas/admin/GenerosAdmin.fxml"));
            contenedorPrincipal.getChildren().add(root);
            //Expandir
            root.prefWidthProperty().bind(contenedorPrincipal.widthProperty());
            root.prefHeightProperty().bind(contenedorPrincipal.heightProperty());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void mostrarCanciones() {
        limpiarPantalla();
        try {
            VBox root = FXMLLoader.load(getClass().getResource("../../../ui/ventanas/admin/CancionesAdmin.fxml"));
            contenedorPrincipal.getChildren().add(root);
            //Expandir
            root.prefWidthProperty().bind(contenedorPrincipal.widthProperty());
            root.prefHeightProperty().bind(contenedorPrincipal.heightProperty());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void mostrarListasReproduccion() {
        limpiarPantalla();
        try {
            VBox root = FXMLLoader.load(getClass().getResource("../../../ui/ventanas/admin/ListasReproduccionAdmin.fxml"));
            contenedorPrincipal.getChildren().add(root);
            //Expandir
            root.prefWidthProperty().bind(contenedorPrincipal.widthProperty());
            root.prefHeightProperty().bind(contenedorPrincipal.heightProperty());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void mostrarPaises() {
        limpiarPantalla();
        try {
            VBox root = FXMLLoader.load(getClass().getResource("../../../ui/ventanas/admin/PaisesAdmin.fxml"));
            contenedorPrincipal.getChildren().add(root);
            //Expandir
            root.prefWidthProperty().bind(contenedorPrincipal.widthProperty());
            root.prefHeightProperty().bind(contenedorPrincipal.heightProperty());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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

    public void modificarAdmin() {
        if(!ControladorGeneral.instancia.usuarioIngresadoEsAdmin()) return;

        try {
            //Referencias para el controlador
            Admin usuarioIngresado = (Admin) ControladorGeneral.instancia.getUsuarioIngresado();

            if (usuarioIngresado == null) {
                AlertDialog alertDialog = new AlertDialog();
                alertDialog.mostrar("Error", "No se pudo verificar el usuario ingresado");
                return;
            }

            Stage ventanaRegistroAdmin = new Stage();
            //This locks previous window interacivity until this one is closed.
            ventanaRegistroAdmin.initModality(Modality.APPLICATION_MODAL);

            ControladorRegistroAdmin.ventana = ventanaRegistroAdmin;
            ControladorRegistroAdmin.idAdmin = usuarioIngresado.getId();
            ControladorRegistroAdmin.modificando = true;

            VBox root = FXMLLoader.load(getClass().getResource("../../../ui/ventanas/VentanaRegistroAdmin.fxml"));

            //Referencia a los campos
            TextField txtCorreo = (TextField) root.lookup("#txtCorreo");
            PasswordField txtContrasenna = (PasswordField) root.lookup("#txtContrasenna");
            TextField txtNombre = (TextField) root.lookup("#txtNombre");
            TextField txtApellidos = (TextField) root.lookup("#txtApellidos");
            TextField txtNombreUsuario = (TextField) root.lookup("#txtNombreUsuario");
            ImageView imagenPerfil = (ImageView) root.lookup("#imagenPerfil");

            //Actualizar campos
            txtCorreo.setText(usuarioIngresado.getCorreo());
            txtContrasenna.setText(usuarioIngresado.getContrasenna());
            txtNombre.setText(usuarioIngresado.getNombre());
            txtApellidos.setText(usuarioIngresado.getApellidos());
            txtNombreUsuario.setText(usuarioIngresado.getNombreUsuario());

            ControladorRegistroAdmin.urlImagenPerfil = "";

            if(usuarioIngresado.getImagenPerfil() != null) {
                if(!usuarioIngresado.getImagenPerfil().equals("") && !usuarioIngresado.getImagenPerfil().equals("null")) {
                    ControladorRegistroAdmin.urlImagenPerfil = usuarioIngresado.getImagenPerfil();
                    System.out.println("Imagen: " + usuarioIngresado.getImagenPerfil());

                    imagenPerfil.setImage(new Image(usuarioIngresado.getImagenPerfil()));
                }
            }

            //Desactivar campos inmodificables
            txtContrasenna.setDisable(true);

            Scene escena = new Scene(root, 580, 440);

            ventanaRegistroAdmin.setScene(escena);
            ventanaRegistroAdmin.setTitle("Modificación de admin");
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
