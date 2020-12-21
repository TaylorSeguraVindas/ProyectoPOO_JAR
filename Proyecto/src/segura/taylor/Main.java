package segura.taylor;

import javafx.application.Application;
import javafx.stage.Stage;
import segura.taylor.controlador.ControladorGeneral;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        ControladorGeneral controladorGeneral = new ControladorGeneral();
        controladorGeneral.iniciarPrograma(primaryStage);
    }
}
