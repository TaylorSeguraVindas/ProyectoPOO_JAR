package segura.taylor.controlador.interfaz.tienda;

import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import segura.taylor.bl.entidades.Calificacion;
import segura.taylor.bl.entidades.Cancion;
import segura.taylor.controlador.ControladorGeneral;
import segura.taylor.ui.dialogos.AlertDialog;
import segura.taylor.ui.dialogos.VentanaMetodoPago;

import java.text.DecimalFormat;
import java.util.Optional;

public class ControladorInfoCancion {
    public static int idCancionSeleccionada;

    public Label lblNombre;
    public Label lblDuracion;
    public Label lblFechaLanzamiento;
    public Label lblGenero;
    public Label lblArtista;
    public Label lblCompositor;
    public Label lblCalificacion;
    public Label lblMiCalificacion;
    public ComboBox txtMiCalificacion;
    public Label lblPrecio;

    public Button btnComprar;

    private Calificacion calificacionUsuarioActual;

    public void initialize() {
        Optional<Cancion> cancionSeleccionada = ControladorGeneral.instancia.getGestor().buscarCancionPorId(idCancionSeleccionada);

        if(cancionSeleccionada.isPresent()) {
            Cancion cancion = cancionSeleccionada.get();

            lblNombre.setText(cancion.getNombre());
            lblDuracion.setText("Duracion: " + cancion.getDuracion());
            lblFechaLanzamiento.setText("Fecha de lanzamiento: " + cancion.getFechaLanzamiento().toString());
            lblGenero.setText("Genero: " + cancion.getNombreGenero());
            lblArtista.setText("Artista: " + cancion.getNombreArtista());
            lblCompositor.setText("Compositor: " + cancion.getNombreCompositor());

            lblCalificacion.setText("Calificación promedio: " + new DecimalFormat("#.##").format(cancion.getCalificacionPromedio()));
            lblPrecio.setText("Precio: " + cancion.getPrecio());

            txtMiCalificacion.getItems().addAll("-Sin calificar-", "1 estrella", "2 estrellas", "3 estrellas", "4 estrellas", "5 estrellas");
            txtMiCalificacion.setOnAction(e -> { modificarCalificacion(txtMiCalificacion.getValue().toString()); });


            try {
                if(ControladorGeneral.instancia.usuarioIngresadoEsAdmin()) {    //Admin no puede comprar
                    lblMiCalificacion.setVisible(false);
                    txtMiCalificacion.setVisible(false);

                    btnComprar.setVisible(false);
                } else {
                    if(ControladorGeneral.instancia.getGestor().buscarCancionEnBibliotecaUsuario(ControladorGeneral.instancia.getIdUsuarioIngresado(), idCancionSeleccionada).isPresent()) {
                        //La canción ha sido comprada por el usuario.
                        lblMiCalificacion.setVisible(true);
                        txtMiCalificacion.setVisible(true);

                        btnComprar.setDisable(true);
                        btnComprar.setText("Comprada");

                        Optional<Calificacion> calificacionEncontrada = ControladorGeneral.instancia.getGestor().buscarCalificacion(idCancionSeleccionada, ControladorGeneral.instancia.getIdUsuarioIngresado());

                        if(calificacionEncontrada.isPresent()) {    //Se actualiza la info para mostrar la calificacion del usuario que está viendo la canción
                            calificacionUsuarioActual = calificacionEncontrada.get();
                            txtMiCalificacion.setValue(obtenerValorDeEstrellas(calificacionUsuarioActual.getEstrellas()));
                        } else {
                            //Crear calificacion si no existe ninguna
                            int idNuevaCalificacion = ControladorGeneral.instancia.getGestor().registrarCalificacion(0, ControladorGeneral.instancia.getIdUsuarioIngresado(), idCancionSeleccionada);

                            if(idNuevaCalificacion != -1) {
                                calificacionUsuarioActual = ControladorGeneral.instancia.getGestor().buscarCalificacion(idNuevaCalificacion).get();
                            }
                        }

                    } else {
                        //La canción NO ha sido comprada por el usuario.
                        lblMiCalificacion.setVisible(false);
                        txtMiCalificacion.setVisible(false);

                        btnComprar.setDisable(false);
                        btnComprar.setText("Comprar");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void modificarCalificacion(String valorSeleccionado) {
        int estrellas = obtenerEstrellasDeString(valorSeleccionado);

        try {
            boolean resultado = ControladorGeneral.instancia.getGestor().modificarCalificacion(calificacionUsuarioActual.getId(), estrellas);
            if(resultado) {
                System.out.println("Correcto");
            } else {
                System.out.println("Mamo");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private String obtenerValorDeEstrellas(int estrellas) {
        String resultado = "";

        switch (estrellas) {
            case 0:
                resultado = "-Sin calificar-";
                break;
            case 1:
                resultado = "1 estrella";
                break;
            case 2:
                resultado = "2 estrellas";
                break;
            case 3:
                resultado = "3 estrellas";
                break;
            case 4:
                resultado = "4 estrellas";
                break;
            case 5:
                resultado = "5 estrellas";
                break;
        }

        return resultado;
    }
    private int obtenerEstrellasDeString(String valor) {
        char[] caracteres = valor.toCharArray();

        if(caracteres[0] == '-') {  //Si el primer caracter es un guion no se ha calificado
            return 0;
        }

        return Integer.parseInt(String.valueOf(caracteres[0])); //Devuelve el primer caracter que define la cantidad de estrellas
    }
    public void comprarCancion() {
        VentanaMetodoPago ventanaMetodoPago = new VentanaMetodoPago();
        boolean transaccionCorrecta = ventanaMetodoPago.mostrar();

        if(transaccionCorrecta) {
            AlertDialog alertDialog = new AlertDialog();
            alertDialog.mostrar("Éxito", "Canción comprada correctamente");

            //Copiar a biblioteca
            try {
                ControladorGeneral.instancia.getGestor().agregarCancionABibliotecaUsuario(ControladorGeneral.instancia.getIdUsuarioIngresado(), idCancionSeleccionada);

                //Actualizar botón y calificaciones
                //La canción ha sido comprada por el usuario.
                lblMiCalificacion.setVisible(true);
                txtMiCalificacion.setVisible(true);

                btnComprar.setDisable(true);
                btnComprar.setText("Comprada");

                //Crear calificacion para este usuario
                int idNuevaCalificacion = ControladorGeneral.instancia.getGestor().registrarCalificacion(0, ControladorGeneral.instancia.getIdUsuarioIngresado(), idCancionSeleccionada);

                if(idNuevaCalificacion != -1) {
                    calificacionUsuarioActual = ControladorGeneral.instancia.getGestor().buscarCalificacion(idNuevaCalificacion).get();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void volver() {
        if(ControladorGeneral.instancia.usuarioIngresadoEsAdmin()) {
            ControladorGeneral.instancia.refVentanaPrincipalAdmin.mostrarTienda();
        } else {
            ControladorGeneral.instancia.refVentanaPrincipalCliente.mostrarTienda();
        }
    }
}
