package segura.taylor.controlador.interfaz.tienda;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import segura.taylor.bl.entidades.Album;
import segura.taylor.controlador.ControladorGeneral;

import java.util.List;
import java.util.Locale;

public class ControladorVistaAlbumes {
    public ScrollPane contenedorFlowPane;
    public FlowPane flowPaneContenido;

    public TextField txtBusqueda;

    public void initialize() {
        mostrarAlbumes(false);
        flowPaneContenido.prefWidthProperty().bind(contenedorFlowPane.widthProperty()); //Expandir
    }

    private void mostrarAlbumes(boolean usandoFiltro) {
        flowPaneContenido.getChildren().clear();    //Limpiar contenido

        List<Album> albumes = ControladorGeneral.instancia.getGestor().listarAlbunes();

        for (Album album : albumes) {
            if(usandoFiltro) {
                if(albumCoincideConBusqueda(album)) {
                    crearCartaListaReproduccion(album.getId(), album.getImagen(), album.getNombre());
                }
            } else {
                crearCartaListaReproduccion(album.getId(), album.getImagen(), album.getNombre());
            }
        }
    }

    private boolean albumCoincideConBusqueda(Album album) {
        String textoBusqueda = txtBusqueda.getText().trim().toUpperCase(Locale.ROOT);

        String nombreAlbum = album.getNombre().trim().toUpperCase(Locale.ROOT);
        if(nombreAlbum.equals(textoBusqueda) || nombreAlbum.contains(textoBusqueda)) {
            return true;
        }

        return false;
    }

    private void crearCartaListaReproduccion(int idAlbum, String imagen, String nombre) {
        try {
            VBox nuevaCarta = FXMLLoader.load(getClass().getResource("../../../ui/ventanas/tienda/ElementoAlbum.fxml"));

            //Referencias a campos
            ImageView imagenFondo = (ImageView) nuevaCarta.lookup("#imgFondo");
            Label txtNombre = (Label) nuevaCarta.lookup("#txtNombre");

            //Actualizar campos
            if(!imagen.equals("")) {
                imagenFondo.setImage(new Image(imagen));
            }

            txtNombre.setText(nombre);
            nuevaCarta.setOnMouseClicked( e -> {
                mostrarDetalleAlbum(idAlbum);
            });

            flowPaneContenido.getChildren().add(nuevaCarta);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void mostrarDetalleAlbum(int idAlbum) {
        ControladorInfoAlbum.idAlbumSeleccionado = idAlbum;

        if(ControladorGeneral.instancia.usuarioIngresadoEsAdmin()) {
            ControladorGeneral.refVentanaPrincipalAdmin.mostrarInfoAlbum();
        } else {
            ControladorGeneral.refVentanaPrincipalCliente.mostrarInfoAlbum();
        }
    }

    public void buscar() {
        //Actualizar lista
        mostrarAlbumes(true);
    }
}
