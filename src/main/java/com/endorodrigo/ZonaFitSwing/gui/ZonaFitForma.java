package com.endorodrigo.ZonaFitSwing.gui;

import com.endorodrigo.ZonaFitSwing.modelo.Cliente;
import com.endorodrigo.ZonaFitSwing.servicio.ClienteServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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
    private Integer IdCliente;


    @Autowired
    public ZonaFitForma(ClienteServicio clienteServicio){
        this.clientService = clienteServicio;
        iniciarForma();
        guardarButton.addActionListener(e -> crearCliente());
        clientesTabla.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                CargarClienteSelecionado();
            }
        });
        eliminarButton.addActionListener(e -> eliminarCliente());
        limpiarButton.addActionListener(e -> LimpiarFormulario());
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
        var cliente = new Cliente(this.IdCliente, nombre, apellido, membresia);
        this.clientService.guardarCliente(cliente);
        if (this.IdCliente == null) {
            JOptionPane.showMessageDialog(null,"Cliente guardado correctamente");
        }else{
            JOptionPane.showMessageDialog(null,"Cliente Actualizado correctamente");
        }
        LimpiarFormulario();
        listarClientes();
    }

    private void LimpiarFormulario(){
        this.IdCliente = null;
        nombreTexto.setText("");
        apellidoTexto.setText("");
        membresiaTexto.setText("");
        this.clientesTabla.getSelectionModel().clearSelection();
    }

    public void CargarClienteSelecionado(){
        var seleccionado = this.clientesTabla.getSelectedRow();
        if(seleccionado != -1){
            var id = this.clientesTabla.getModel().getValueAt(seleccionado,0);
            this.IdCliente = (Integer) id;
            nombreTexto.setText(this.clientesTabla.getModel().getValueAt(seleccionado,1).toString());
            apellidoTexto.setText(this.clientesTabla.getModel().getValueAt(seleccionado,2).toString());
            membresiaTexto.setText(this.clientesTabla.getModel().getValueAt(seleccionado,3).toString());

        }
    }

    public void eliminarCliente(){
        var seleccionado = this.clientesTabla.getSelectedRow();
        if(seleccionado != -1){
            var idcliente = this.clientesTabla.getModel().getValueAt(seleccionado,0);
            this.IdCliente = (Integer) idcliente;
            var data = new Cliente();
            data.setId(this.IdCliente);
            clientService.eliminarCliente(data);
            JOptionPane.showMessageDialog(null,"Cliente eliminado correctamente");
            listarClientes();
            LimpiarFormulario();
        }else {
            JOptionPane.showMessageDialog(null,"Seleccione un cliente");
        }
    }
}
