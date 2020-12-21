package segura.taylor.controlador.interfaz.usuarios;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import segura.taylor.bl.entidades.Cliente;
import segura.taylor.controlador.ControladorGeneral;
import segura.taylor.ui.dialogos.AlertDialog;
import segura.taylor.ui.dialogos.VentanaContrasennaTemporal;
import segura.taylor.ui.dialogos.VentanaVerificarCorreo;

public class ControladorLogin {
    private boolean ingresadoCorrectamente = false;

    public TextField txtCorreo;
    public PasswordField txtContrasenna;

    public boolean mostrar() {
        return ingresadoCorrectamente;
    }

    public void iniciarSesion() {
        boolean resultado = ControladorGeneral.instancia.getGestor().iniciarSesion(txtCorreo.getText(), txtContrasenna.getText());
        if(!resultado) {
            AlertDialog alertDialog = new AlertDialog();
            alertDialog.mostrar("No se pudo iniciar sesion", "El correo o la contraseña son incorrectos");
        } else {
            boolean usuarioEsAdmin = ControladorGeneral.instancia.getGestor().usuarioIngresadoEsAdmin();
            System.out.println("Ingresando como admin: " + usuarioEsAdmin);

            if(!usuarioEsAdmin) {
                Cliente usuarioIngresado = (Cliente) ControladorGeneral.instancia.getUsuarioIngresado();

                if(usuarioIngresado.isCorreoVerificado()) {
                    ControladorGeneral.instancia.menuPrincipal(usuarioEsAdmin);
                } else {
                    System.out.println("No se ha verificado el correo");
                    VentanaVerificarCorreo ventanaVerificarCorreo = new VentanaVerificarCorreo();

                    resultado = ventanaVerificarCorreo.mostrar();
                    if(resultado) {
                        try {
                            ControladorGeneral.instancia.getGestor().verificarCorreoUsuario(ControladorGeneral.instancia.getIdUsuarioIngresado());  //Actualizar el estado de verificacion del correo del usuario actual.
                            ControladorGeneral.instancia.menuPrincipal(usuarioEsAdmin);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            } else {
                ControladorGeneral.instancia.menuPrincipal(usuarioEsAdmin);
            }
        }
    }

    public void registrarUsuario() {
        ControladorRegistroCliente.modificando = false;
        ControladorGeneral.instancia.menuRegistroCliente();
    }

    public void olvidarContrasenna(){
        VentanaContrasennaTemporal ventanaContrasennaTemporal = new VentanaContrasennaTemporal();
        ventanaContrasennaTemporal.mostrar();
    }
}
