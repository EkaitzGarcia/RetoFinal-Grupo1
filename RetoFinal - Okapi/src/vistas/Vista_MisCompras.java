package vistas;

import modelo.AccesoBD;
import modelo.Cliente;
import modelo.Compra;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.*;
import java.util.*;

public class Vista_MisCompras extends JDialog implements ActionListener {

    private static final long serialVersionUID = 1L;
    private final JPanel contentPanel = new JPanel();

    private JList<String> listaCompras;
    private DefaultListModel<String> modeloLista;
    private JButton btnCerrar;

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

        modeloLista = new DefaultListModel<>();
        listaCompras = new JList<>(modeloLista);
        JScrollPane scroll = new JScrollPane(listaCompras);
        scroll.setBounds(10, 35, 410, 230);
        contentPanel.add(scroll);

        btnCerrar = new JButton("Cerrar");
        btnCerrar.setBounds(160, 275, 100, 25);
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
                modeloLista.addElement("ID: " + c.getId_compra() + 
                    "  |  Fecha: " + c.getFecha() + 
                    "  |  Total: " + c.getTotal_compra() + "€" +
                    "  |  Pago: " + c.getMetodo_pago());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnCerrar) dispose();
    }
}