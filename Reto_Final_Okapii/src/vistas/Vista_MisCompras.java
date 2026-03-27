package vistas;

import modelo.AccesoBD;
import modelo.Cliente;
import modelo.Compra;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import java.awt.event.*;
import java.util.*;

public class Vista_MisCompras extends JDialog implements ActionListener {

    private JPanel contentPanel = new JPanel();
    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private JButton btnCerrar, btnGuardar;
    private Cliente clienteActual;

    public Vista_MisCompras(Cliente cliente) {
        this.clienteActual = cliente;

        setBounds(100, 100, 450, 350);
        getContentPane().setLayout(new java.awt.BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, java.awt.BorderLayout.CENTER);
        contentPanel.setLayout(null);

        JLabel lblTitulo = new JLabel("Mis compras - " + clienteActual.getNom());
        lblTitulo.setBounds(10, 10, 400, 20);
        contentPanel.add(lblTitulo);

        String[] columnas = {"ID", "Fecha", "Total (€)", "Método Pago"};

        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3;
            }
        };

        tabla = new JTable(modeloTabla);
        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBounds(10, 35, 410, 230);
        contentPanel.add(scroll);

        JComboBox<String> comboPago = new JComboBox<>(new String[]{"TARJETA", "EFECTIVO"});
        tabla.getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(comboPago));

        btnGuardar = new JButton("Guardar cambios");
        btnGuardar.setBounds(50, 275, 150, 25);
        btnGuardar.addActionListener(this);
        contentPanel.add(btnGuardar);

        btnCerrar = new JButton("Cerrar");
        btnCerrar.setBounds(220, 275, 100, 25);
        btnCerrar.addActionListener(this);
        contentPanel.add(btnCerrar);

        cargarCompras();
    }

    public void cargarCompras() {
        AccesoBD accesoBD = new AccesoBD();
        Map<Integer, Compra> compras = new TreeMap<>();

        try {
            accesoBD.getComprasPorCliente(clienteActual.getDni(), compras);

            for (Compra c : compras.values()) {
                modeloTabla.addRow(new Object[]{
                    c.getId_compra(),
                    c.getFecha(),
                    String.format("%.2f €", c.getTotal_compra()),
                    c.getMetodo_pago().toString()  // ✅ FIX 1
                });
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void guardarCambios() {
        AccesoBD accesoBD = new AccesoBD();

        try {
            for (int i = 0; i < modeloTabla.getRowCount(); i++) {
                int id = (int) modeloTabla.getValueAt(i, 0);
                String metodo = modeloTabla.getValueAt(i, 3).toString();  

                accesoBD.actualizarMetodoPago(id, metodo);
            }

            JOptionPane.showMessageDialog(this, "Cambios guardados correctamente");
            dispose();
            new Vista_Cliente(clienteActual).setVisible(true);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnCerrar) {
            dispose();
            Vista_Cliente vistaCliente = new Vista_Cliente(clienteActual);
            vistaCliente.setVisible(true);
        }

        if (e.getSource() == btnGuardar) {
            guardarCambios();
       
        }
    }
}