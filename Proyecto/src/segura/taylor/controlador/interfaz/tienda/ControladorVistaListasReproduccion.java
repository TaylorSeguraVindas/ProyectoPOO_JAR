package segura.taylor.controlador.interfaz.tienda;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import segura.taylor.bl.entidades.ListaReproduccion;
import segura.taylor.bl.enums.TipoListaReproduccion;
import segura.taylor.controlador.ControladorGeneral;

import java.util.List;
import java.util.Locale;

public class ControladorVistaListasReproduccion {
    public ScrollPane contenedorFlowPane;
    public FlowPane flowPaneContenido;

    public TextField txtBusqueda;

    public void initialize() {
        mostrarListasReproduccion(false);
        flowPaneContenido.prefWidthProperty().bind(contenedorFlowPane.widthProperty()); //Expandir
    }

    private void mostrarListasReproduccion(boolean usandoFiltro) {
        flowPaneContenido.getChildren().clear();    //Limpiar contenido

        List<ListaReproduccion> listasReproduccion = ControladorGeneral.instancia.getGestor().listarListasReproduccion();

        for (ListaReproduccion listaReproduccion : listasReproduccion) {

            //Solo se muestra las listas creadas por el admin
            if(listaReproduccion.getTipoLista().equals(TipoListaReproduccion.PARA_TIENDA)) {
                if(usandoFiltro) {
                    if(listaCoincideConBusqueda(listaReproduccion)) {
                        crearCartaListaReproduccion(listaReproduccion.getId(), listaReproduccion.getImagen(), listaReproduccion.getNombre(), listaReproduccion.getDescripcion());
                    }
                } else {
                    crearCartaListaReproduccion(listaReproduccion.getId(), listaReproduccion.getImagen(), listaReproduccion.getNombre(), listaReproduccion.getDescripcion());
                }
            }
        }
    }

    private boolean listaCoincideConBusqueda(ListaReproduccion lista) {
        String textoBusqueda = txtBusqueda.getText().trim().toUpperCase(Locale.ROOT);

        String nombreLista = lista.getNombre().trim().toUpperCase(Locale.ROOT);
        if(nombreLista.equals(textoBusqueda) || nombreLista.contains(textoBusqueda)) {
            return true;
        }

        return false;
    }
    private void crearCartaListaReproduccion(int idLista, String imagen, String nombre, String descripcion) {
        try {
            VBox nuevaCarta = FXMLLoader.load(getClass().getResource("../../../ui/ventanas/tienda/ElementoListaReproduccion.fxml"));

            //Referencias a campos
            ImageView imagenFondo = (ImageView) nuevaCarta.lookup("#imgFondo");
            Label txtNombre = (Label) nuevaCarta.lookup("#txtNombre");
            Label txtDescripcion = (Label) nuevaCarta.lookup("#txtDescripcion");

            //Actualizar campos
            if(!imagen.equals("")) {
                imagenFondo.setImage(new Image(imagen));
            }

            txtNombre.setText(nombre);
            txtDescripcion.setText(descripcion);

            nuevaCarta.setOnMouseClicked( e -> {
                mostrarDetalleListaReproduccion(idLista);
            });

            flowPaneContenido.getChildren().add(nuevaCarta);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void mostrarDetalleListaReproduccion(int idLista) {
        segura.taylor.controlador.interfaz.tienda.ControladorInfoListaReproduccion.desdeTienda = true;
        ControladorInfoListaReproduccion.idListaSeleccionada = idLista;

        if(ControladorGeneral.instancia.usuarioIngresadoEsAdmin()) {
            ControladorGeneral.refVentanaPrincipalAdmin.mostrarInfoListaReproduccion();
        } else {
            ControladorGeneral.refVentanaPrincipalCliente.mostrarInfoListaReproduccion();
        }
    }

    public void buscar() {
        //Actualizar lista
        mostrarListasReproduccion(true);
    }
}
