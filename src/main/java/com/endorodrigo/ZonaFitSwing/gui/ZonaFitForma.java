package com.endorodrigo.ZonaFitSwing.gui;

import com.endorodrigo.ZonaFitSwing.modelo.Cliente;
import com.endorodrigo.ZonaFitSwing.servicio.ClienteServicio;
import com.endorodrigo.ZonaFitSwing.servicio.IClienteServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@Component
public class ZonaFitForma extends JFrame {
    private JPanel panelPrincipal;
    private JTable clientesTabla;
    private JTextField nombreTexto;
    private JTextField apellidoTexto;
    private JTextField membresiaTexto;
    private JButton guardarButton;
    private JButton eliminarButton;
    private JButton limpiarButton;
    ClienteServicio clientService;
    private DefaultTableModel tablaModeloClientes;


    @Autowired
    public ZonaFitForma(ClienteServicio clienteServicio){
        this.clientService = clienteServicio;
        iniciarForma();
        guardarButton.addActionListener(e -> crearCliente());
    }



    private void iniciarForma(){
        setContentPane(panelPrincipal);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900,700);
        setLocationRelativeTo(null);//centra ventana
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        this.tablaModeloClientes = new DefaultTableModel(0, 4);
        String[] cabeceros = {"Id", "Nombre", "Apellido", "Membresia"};
        this.tablaModeloClientes.setColumnIdentifiers(cabeceros);
        this.clientesTabla = new JTable(tablaModeloClientes);
        // Cargar listado de clientes
        listarClientes();
    }

    private void listarClientes(){
        this.tablaModeloClientes.setRowCount(0);
        var clientes = this.clientService.listarClientes();
        clientes.forEach(cliente -> {
            Object[] renglonCliente = {
                    cliente.getId(),
                    cliente.getNombre(),
                    cliente.getApellido(),
                    cliente.getMembresia()
            };
            this.tablaModeloClientes.addRow(renglonCliente);
        });
    }

    private void crearCliente() {
        if (nombreTexto.getText().equals("") ) {
            JOptionPane.showMessageDialog(null,"El campo nombre es obligatorio");
            nombreTexto.requestFocusInWindow();
            return;
        }
        if (apellidoTexto.getText().equals("")) {
            JOptionPane.showMessageDialog(null,"El campo apellido es obligatorio");
            apellidoTexto.requestFocusInWindow();
            return;
        }
        if (membresiaTexto.getText().equals("")) {
            JOptionPane.showMessageDialog(null,"El campo membresia es obligatorio");
            membresiaTexto.requestFocusInWindow();
            return;
        }

        // Obteniendo los campos del formulario
        var nombre = nombreTexto.getText();
        var apellido = apellidoTexto.getText();
        var membresia = Integer.parseInt(membresiaTexto.getText());
        var cliente = new Cliente();
        cliente.setNombre(nombre);
        cliente.setApellido(apellido);
        cliente.setMembresia(membresia);
        this.clientService.guardarCliente(cliente);
        JOptionPane.showMessageDialog(null,"Cliente guardado correctamente");
        LimpiarFormulario();
        listarClientes();
    }

    private void LimpiarFormulario(){
        nombreTexto.setText("");
        apellidoTexto.setText("");
        membresiaTexto.setText("");
    }
}
