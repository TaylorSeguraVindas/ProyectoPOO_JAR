package segura.taylor.controlador.interfaz.artista;

import javafx.scene.control.ComboBox;
import segura.taylor.bl.entidades.Artista;
import segura.taylor.controlador.ControladorGeneral;

public class ControladorAgregarArtistaALista {
    public ComboBox txtArtista;

    public void initialize() {
        actualizarComboBoxArtistas();
    }

    private void actualizarComboBoxArtistas() {
        txtArtista.getItems().clear();

        for (Artista artista : ControladorGeneral.instancia.getGestor().listarArtistas()) {
            txtArtista.getItems().add(artista.toComboBoxItem());
        }
    }
}
