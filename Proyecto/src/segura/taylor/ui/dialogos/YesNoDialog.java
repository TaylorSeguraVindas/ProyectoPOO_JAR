package segura.taylor.ui.dialogos;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class YesNoDialog {
    boolean resultado = false;

    public boolean mostrar(String titulo, String mensaje) {
        Stage window = new Stage();
        //This locks previous window interacivity until this one is closed.
        window.initModality(Modality.APPLICATION_MODAL);

        VBox contenedorPrincipal = new VBox();
        contenedorPrincipal.setSpacing(10);
        contenedorPrincipal.setAlignment(Pos.CENTER);
        contenedorPrincipal.setPadding(new Insets(10, 10, 10, 10));

        Label lblMensaje = new Label(mensaje);
        lblMensaje.setMinHeight(60);
        lblMensaje.setWrapText(true);
        lblMensaje.setTextAlignment(TextAlignment.CENTER);

        HBox contenedorBotones = new HBox();
        contenedorBotones.setAlignment(Pos.CENTER);

        contenedorBotones.setSpacing(10);

        Button btnSi = new Button("Si");
        btnSi.setOnAction(e -> {
            resultado = true;
            window.close();
        });
        Button btnNo = new Button("No");
        btnNo.setOnAction(e -> {
            resultado = false;
            window.close();
        });

        contenedorBotones.getChildren().addAll(btnSi, btnNo);
        contenedorPrincipal.getChildren().addAll(lblMensaje, contenedorBotones);
        Scene escena = new Scene(contenedorPrincipal, 280, 180);

        window.setScene(escena);
        window.setTitle(titulo);
        window.setResizable(false);
        window.showAndWait();

        return resultado;
    }
}
