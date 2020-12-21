package segura.taylor.controlador.interfaz.admin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import segura.taylor.bl.entidades.Cliente;
import segura.taylor.bl.entidades.Usuario;
import segura.taylor.controlador.ControladorGeneral;
import segura.taylor.controlador.interfaz.usuarios.ControladorRegistroCliente;
import segura.taylor.ui.dialogos.AlertDialog;
import segura.taylor.ui.dialogos.YesNoDialog;

import java.util.List;

public class ControladorUsuariosAdmin {
    public TableView tblUsuarios;
    public VBox ventanaPrincipal;

    public void initialize() {
        inicializarTabla();
        mostrarDatos();
    }

    public void inicializarTabla() {
        //ID
        TableColumn<Cliente, String> columnaId = new TableColumn("ID");
        columnaId.setMinWidth(100);
        columnaId.setCellValueFactory(new PropertyValueFactory<>("id"));

        //TipoUsuario
        TableColumn<Cliente, String> columnaTipoUsuario = new TableColumn("Tipo Usuario");
        columnaTipoUsuario.setMinWidth(100);
        columnaTipoUsuario.setCellValueFactory(new PropertyValueFactory<>("tipoUsuario"));

        //Correo
        TableColumn<Cliente, String> columnaCorreo = new TableColumn("Correo");
        columnaCorreo.setMinWidth(100);
        columnaCorreo.setCellValueFactory(new PropertyValueFactory<>("correo"));

        //Contraseña
        TableColumn<Cliente, String> columnaContrasenna = new TableColumn("Contraseña");
        columnaContrasenna.setMinWidth(100);
        columnaContrasenna.setCellValueFactory(new PropertyValueFactory<>("contrasenna"));
        
        //Nombre
        TableColumn<Cliente, String> columnaNombre = new TableColumn("Nombre");
        columnaNombre.setMinWidth(100);
        columnaNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));

        //Apellidos
        TableColumn<Cliente, String> columnaApellidos = new TableColumn("Apellidos");
        columnaApellidos.setMinWidth(100);
        columnaApellidos.setCellValueFactory(new PropertyValueFactory<>("apellidos"));

        //Nombre usuario
        TableColumn<Cliente, String> columnaNombreUsuario = new TableColumn("Nombre de usuario");
        columnaNombreUsuario.setMinWidth(100);
        columnaNombreUsuario.setCellValueFactory(new PropertyValueFactory<>("nombreUsuario"));

        //FechaNacimiento
        TableColumn<Cliente, String> columnaFechaNacimiento = new TableColumn("Fecha Nacimiento");
        columnaFechaNacimiento.setMinWidth(100);

        columnaFechaNacimiento.setCellValueFactory(new PropertyValueFactory<>("fechaNacimiento"));

        //Edad
        TableColumn<Cliente, String> columnaEdad = new TableColumn("Edad");
        columnaEdad.setMinWidth(100);
        columnaEdad.setCellValueFactory(new PropertyValueFactory<>("edad"));

        //Pais
        TableColumn<Cliente, String> columnaPais = new TableColumn("Pais");
        columnaPais.setMinWidth(100);
        columnaPais.setCellValueFactory(new PropertyValueFactory<>("nombrePais"));
        
        tblUsuarios.getColumns().addAll(columnaId, columnaTipoUsuario, columnaCorreo, columnaContrasenna, columnaNombre, columnaApellidos, columnaNombreUsuario, columnaFechaNacimiento, columnaEdad, columnaPais);
    }
    private void mostrarDatos() {
        tblUsuarios.getItems().clear();
        tblUsuarios.setItems(obtenerClientes());
    }

    public ObservableList<Cliente> obtenerClientes() {
        List<Usuario> usuarios = ControladorGeneral.instancia.getGestor().listarUsuarios();

        ObservableList<Cliente> clientesFinal = FXCollections.observableArrayList();

        for(Usuario usuario : usuarios) {
            if(!usuario.esAdmin()) {    //Se excluye al admin
                clientesFinal.add((Cliente) usuario);
            }
        }

        return clientesFinal;
    }

    public void modificarUsuario() {
        try {
            //Referencias para el controlador
            Cliente clienteSeleccionado = (Cliente) tblUsuarios.getSelectionModel().getSelectedItem();

            if (clienteSeleccionado == null) {
                AlertDialog alertDialog = new AlertDialog();
                alertDialog.mostrar("Error", "No hay ningún Usuario seleccionado");
                return;
            }

            Stage ventanaRegistroAdmin = new Stage();
            //This locks previous window interacivity until this one is closed.
            ventanaRegistroAdmin.initModality(Modality.APPLICATION_MODAL);

            ControladorRegistroCliente.ventana = ventanaRegistroAdmin;
            ControladorRegistroCliente.idCliente = clienteSeleccionado.getId();
            ControladorRegistroCliente.modificando = true;

            VBox root = FXMLLoader.load(getClass().getResource("../../../ui/ventanas/VentanaRegistroCliente.fxml"));

            //Referencia a los campos
            TextField txtCorreo = (TextField) root.lookup("#txtCorreo");
            PasswordField txtContrasenna = (PasswordField) root.lookup("#txtContrasenna");
            TextField txtNombre = (TextField) root.lookup("#txtNombre");
            TextField txtApellidos = (TextField) root.lookup("#txtApellidos");
            TextField txtNombreUsuario = (TextField) root.lookup("#txtNombreUsuario");
            ImageView imagenPerfil = (ImageView) root.lookup("#imagenPerfil");
            DatePicker txtFechaNacimiento = (DatePicker) root.lookup("#txtFechaNacimiento");
            ComboBox txtPais = (ComboBox) root.lookup("#txtPais");

            //Actualizar campos
            txtCorreo.setText(clienteSeleccionado.getCorreo());
            txtContrasenna.setText(clienteSeleccionado.getContrasenna());
            txtNombre.setText(clienteSeleccionado.getNombre());
            txtApellidos.setText(clienteSeleccionado.getApellidos());
            txtNombreUsuario.setText(clienteSeleccionado.getNombreUsuario());
            txtFechaNacimiento.setValue(clienteSeleccionado.getFechaNacimiento());
            txtPais.setValue(clienteSeleccionado.getPais().toComboBoxItem());

            ControladorRegistroCliente.urlImagenPerfil = "";

            if(!clienteSeleccionado.getImagenPerfil().equals("")) {
                ControladorRegistroCliente.urlImagenPerfil = clienteSeleccionado.getImagenPerfil();
                try {
                    imagenPerfil.setImage(new Image(clienteSeleccionado.getImagenPerfil()));
                } catch (Exception e) {
                    System.out.println("Imagen invalida");
                }
            }

            //Desactivar campos inmodificables
            txtContrasenna.setDisable(true);
            txtFechaNacimiento.setDisable(true);
            txtPais.setDisable(true);

            Scene escena = new Scene(root, 580, 440);

            ventanaRegistroAdmin.setScene(escena);
            ventanaRegistroAdmin.setTitle("Modificación de cliente");
            ventanaRegistroAdmin.setResizable(false);
            ventanaRegistroAdmin.showAndWait();

            mostrarDatos(); //Actualizar tabla
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void eliminarUsuario() {
        YesNoDialog yesNoDialog = new YesNoDialog();
        boolean resultado = yesNoDialog.mostrar("Aviso", "Realmente quiere eliminar al Cliente seleccionado?");

        if (resultado) {
            Cliente ClienteSeleccionado = (Cliente) tblUsuarios.getSelectionModel().getSelectedItem();

            if (ClienteSeleccionado == null) {
                AlertDialog alertDialog = new AlertDialog();
                alertDialog.mostrar("Error", "No hay ningún Cliente seleccionado");
                return;
            }

            int idCliente = ClienteSeleccionado.getId();
            try {
                resultado = ControladorGeneral.instancia.getGestor().eliminarUsuario(idCliente);
                if (resultado) {
                    AlertDialog alertDialog = new AlertDialog();
                    alertDialog.mostrar("Exito", "Cliente eliminado correctamente");
                    mostrarDatos();
                } else {
                    AlertDialog alertDialog = new AlertDialog();
                    alertDialog.mostrar("Error", "No se pudo eliminar el Cliente");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
