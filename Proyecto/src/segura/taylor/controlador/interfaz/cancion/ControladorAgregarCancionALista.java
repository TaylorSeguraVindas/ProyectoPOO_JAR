package segura.taylor.controlador.interfaz.cancion;

import javafx.scene.control.ComboBox;
import segura.taylor.bl.entidades.Cancion;
import segura.taylor.controlador.ControladorGeneral;

public class ControladorAgregarCancionALista {
    public ComboBox txtCancion;

    public void initialize() {
        actualizarComboBoxCanciones();
    }
    
    private void actualizarComboBoxCanciones() {
        txtCancion.getItems().clear();

        for (Cancion cancion : ControladorGeneral.instancia.getGestor().listarCanciones()) {
            txtCancion.getItems().add(cancion.toComboBoxItem());
        }
    }
}
