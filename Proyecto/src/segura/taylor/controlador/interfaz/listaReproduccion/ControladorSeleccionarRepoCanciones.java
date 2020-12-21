package segura.taylor.controlador.interfaz.listaReproduccion;

import javafx.scene.control.ComboBox;
import segura.taylor.bl.entidades.ListaReproduccion;
import segura.taylor.bl.enums.TipoListaReproduccion;
import segura.taylor.controlador.ControladorGeneral;

public class ControladorSeleccionarRepoCanciones {
    public ComboBox txtLista;

    public void initialize() {
        actualizarComboBoxCanciones();
    }

    private void actualizarComboBoxCanciones() {
        try {
            txtLista.getItems().clear();

            for (ListaReproduccion lista : ControladorGeneral.instancia.getGestor().listarListasReproduccionDeBibliotecaUsuario(ControladorGeneral.instancia.getIdUsuarioIngresado())) {
                if(lista.getTipoLista().equals(TipoListaReproduccion.PARA_USUARIO)) {   //Se puede agregar canciones solo a las listas que fueron creadas por el usuario actual
                    txtLista.getItems().add(lista.toComboBoxItem());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
