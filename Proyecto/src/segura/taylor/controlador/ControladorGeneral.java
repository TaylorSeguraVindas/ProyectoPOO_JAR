package segura.taylor.controlador;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.Media;
import javafx.stage.Stage;
import segura.taylor.bl.entidades.*;
import segura.taylor.bl.gestor.Gestor;

import segura.taylor.controlador.interfaz.admin.ControladorVentanaPrincipalAdmin;
import segura.taylor.controlador.interfaz.cliente.ControladorVentanaPrincipalCliente;
import segura.taylor.ui.dialogos.*;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class ControladorGeneral {
    //Referencia estatica
    public static ControladorGeneral instancia;
    public static ControladorVentanaPrincipalAdmin refVentanaPrincipalAdmin;
    public static ControladorVentanaPrincipalCliente refVentanaPrincipalCliente;

    //BL
    private Gestor gestor = new Gestor();

    //UI
    private Stage window;

    //Reproductor
    private Cancion cancionActual = null;
    private RepositorioCanciones repoCancionesActual;   //La lista que siempre va a estar sonando
    private MediaPlayer mediaPlayer;
    private double volumen;
    private boolean pausado = true;

    //Propiedades
    public Gestor getGestor() {
        return gestor;
    }

    public Usuario getUsuarioIngresado() {
        return gestor.getUsuarioIngresado();
    }
    public int getIdUsuarioIngresado() {
        return gestor.getIdUsuarioIngresado();
    }
    public Biblioteca getBibliotecaUsuarioIngresado() {
        return gestor.getBibliotecaUsuarioIngresado();
    }

    public boolean usuarioIngresadoEsAdmin() {
        return gestor.usuarioIngresadoEsAdmin();
    }

    public ControladorGeneral() {
        instancia = this;
    }

    //UTILES
    public String obtenerFechaActual() {
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String fecha = formato.format(LocalDate.now());
        return fecha;
    }
    public LocalDate fechaDesdeString(String pFechaNacimiento) {
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate nuevaFecha = null;
        nuevaFecha = LocalDate.parse(pFechaNacimiento, formato);

        return nuevaFecha;
    }
    public int calcularEdad(LocalDate pFechaNacimiento) {
        //Crear fecha a partir del string
        LocalDate fechaActual = LocalDate.now();

        Period periodo = Period.between(pFechaNacimiento, fechaActual);
        return periodo.getYears();
    }

    //LOGICA
    public void iniciarPrograma(Stage primaryStage) {
        gestor.verificarValoresPorDefecto();

        window = primaryStage;
        window.setOnCloseRequest(e -> {
            e.consume();    //Stops the base event.
            CloseProgram();
        });
        window.setTitle("NotSpotify");

        try {
            menuIniciarSesion();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    private void CloseProgram() {
        boolean result = new YesNoDialog().mostrar("Confirmar cierre", "Realmente quiere salir?");

        if(result) {
            System.out.println("Cerrando programa");
            window.close();
        }
    }

    private void cambiarVentana(String titulo, Scene escena) {
        ControladorGeneral.instancia.window.setTitle(titulo);
        ControladorGeneral.instancia.window.setScene(escena);
        ControladorGeneral.instancia.window.show();
    }

    public void menuIniciarSesion() {
        try {
            gestor.reiniciar();
            Parent root = FXMLLoader.load(getClass().getResource("../ui/ventanas/VentanaLogin.fxml"));
            ControladorGeneral.instancia.cambiarVentana("Inicio de sesion", new Scene(root, 420, 380));

            ControladorGeneral.instancia.window.setMinWidth(420);
            ControladorGeneral.instancia.window.setMinHeight(400);
            ControladorGeneral.instancia.window.setWidth(420);
            ControladorGeneral.instancia.window.setHeight(400);
            ControladorGeneral.instancia.window.setMaximized(false);
            ControladorGeneral.instancia.window.centerOnScreen();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void menuRegistroCliente() {
        try {
            Parent root;

            if(gestor.existeAdmin()) {
                root = FXMLLoader.load(getClass().getResource("../ui/ventanas/VentanaRegistroCliente.fxml"));
            } else {
                root = FXMLLoader.load(getClass().getResource("../ui/ventanas/VentanaRegistroAdmin.fxml"));
                AlertDialog alertDialog = new AlertDialog();
                alertDialog.mostrar("Aviso", "No se ha detectado un usuario admin, se va a crear uno");
            }

            ControladorGeneral.instancia.cambiarVentana("Registro de usuario", new Scene(root, 580, 440));

            ControladorGeneral.instancia.window.setMinWidth(580);
            ControladorGeneral.instancia.window.setMinHeight(440);
            ControladorGeneral.instancia.window.setWidth(580);
            ControladorGeneral.instancia.window.setHeight(440);
            ControladorGeneral.instancia.window.setMaximized(false);
            ControladorGeneral.instancia.window.centerOnScreen();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void menuPrincipal(boolean admin) {
        try {
            Parent root;

            if(admin) {
                root = FXMLLoader.load(getClass().getResource("../ui/ventanas/admin/VentanaPrincipalAdmin.fxml"));
            } else {
                root = FXMLLoader.load(getClass().getResource("../ui/ventanas/cliente/VentanaPrincipalCliente.fxml"));
            }

            ControladorGeneral.instancia.cambiarVentana("NotSpotify", new Scene(root, 1100, 620));

            ControladorGeneral.instancia.window.setMinWidth(1100);
            ControladorGeneral.instancia.window.setMinHeight(620);
            ControladorGeneral.instancia.window.setMaximized(false);
            ControladorGeneral.instancia.window.centerOnScreen();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //MUSICA
    public void reproducirLista(int idLista) {
        try {
            Optional<ListaReproduccion> listaEncontrada = gestor.buscarListaReproduccionPorId(idLista);

            if(listaEncontrada.isPresent()) {
                repoCancionesActual = listaEncontrada.get();
                reproducirRepoActual();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void reproducirAlbum(int idAlbum) {
        try {
            Optional<Album> albumEncontrado = gestor.buscarAlbumPorId(idAlbum);

            if(albumEncontrado.isPresent()) {
                repoCancionesActual = albumEncontrado.get();
                reproducirRepoActual();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void reproducirBibliotecaUsuarioIngresado() {
        if(gestor.usuarioIngresadoEsAdmin()) return;    //El admin no tiene biblioteca

        repoCancionesActual = gestor.getBibliotecaUsuarioIngresado();
        reproducirRepoActual();
    }

    private void reproducirRepoActual() {
        if(repoCancionesActual.getCanciones().size() > 0) {
            cargarCancionDeRepo(0);    //Reproduce la primer cancion
            reproducirCancion();
        }
    }
    public void cargarCancionDeRepo(int posicion) {
        if(repoCancionesActual == null) return;

        cancionActual = repoCancionesActual.getCanciones().get(posicion);
        cargarCancion(repoCancionesActual.getCanciones().get(posicion).getRecurso());
        reproducirCancion();
    }

    public void cargarCancion(String pRecurso) {
        if(mediaPlayer != null) {
            mediaPlayer.stop();
        }

        Media media = new Media(pRecurso);
        mediaPlayer = new MediaPlayer(media);
    }

    public void siguienteCancion() {
        if(repoCancionesActual == null) return;

        int siguienteCancion = repoCancionesActual.obtenerIndiceCancion(cancionActual.getId()) + 1;

        if(siguienteCancion < repoCancionesActual.getCanciones().size()) {  //Reproduce la siguiente cancion
            cargarCancionDeRepo(siguienteCancion);
            reproducirCancion();
        } else {    //Reproduce la ultima cancion
            cargarCancionDeRepo(repoCancionesActual.getCanciones().size() - 1);
            reproducirCancion();
        }
    }

    public void cancionAnterior() {
        if(repoCancionesActual == null) return;

        int cancionAnterior = repoCancionesActual.obtenerIndiceCancion(cancionActual.getId()) - 1;

        if(cancionAnterior > 0) {  //Reproduce la siguiente cancion
            cargarCancionDeRepo(cancionAnterior);
            reproducirCancion();
        } else {    //Reproduce la ultima cancion
            cargarCancionDeRepo(0);
            reproducirCancion();
        }
    }

    public void detenerCancion() {
        mediaPlayer.stop();
    }

    public void alternarEstadoCancion() {
        if (mediaPlayer != null) {
            if(pausado) {
                reproducirCancion();
            } else {
                pausarCancion();
            }
        }
    }

    public void pausarCancion() {
        mediaPlayer.pause();
        pausado = true;
    }

    public void reproducirCancion() {
        mediaPlayer.play();
        pausado = false;

        if(cancionActual == null) return;

        if(usuarioIngresadoEsAdmin()) {
            refVentanaPrincipalAdmin.actualizarInfoCancion(cancionActual.getNombre(), cancionActual.getNombreArtista());
        } else {
            refVentanaPrincipalCliente.actualizarInfoCancion(cancionActual.getNombre(), cancionActual.getNombreArtista());
        }
    }
}
