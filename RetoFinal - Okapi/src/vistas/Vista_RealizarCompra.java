package vistas;

import modelo.AccesoBD;
import modelo.Cliente;
import modelo.Producto;

import javax.swing.*;
import java.awt.event.*;
import java.util.*;

public class Vista_RealizarCompra extends JDialog implements ActionListener {

    private static final long serialVersionUID = 1L;
    private final JPanel contentPanel = new JPanel();

    private JLabel lblProductos, lblCantidad, lblPago, lblTotal;
    private JList<String> listaProductos;
    private DefaultListModel<String> modeloLista;
    private JComboBox<Integer> comboCantidad;
    private JCheckBox chkDescuento, chkTerminos;
    private JRadioButton rbTarjeta, rbEfectivo;
    private JButton btnComprar, btnCancelar;

    private Cliente clienteActual;
    private Map<String, Producto> todosProductos = new TreeMap<>();

    public Vista_RealizarCompra(Cliente cliente) {
        this.clienteActual = cliente;
        setBounds(100, 100, 500, 450);
        getContentPane().setLayout(new java.awt.BorderLayout());
        contentPanel.setBorder(new javax.swing.border.EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, java.awt.BorderLayout.CENTER);
        contentPanel.setLayout(null);

        lblProductos = new JLabel("Productos disponibles:");
        lblProductos.setBounds(10, 10, 200, 20);
        contentPanel.add(lblProductos);

        modeloLista = new DefaultListModel<>();
        listaProductos = new JList<>(modeloLista);
        listaProductos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollLista = new JScrollPane(listaProductos);
        scrollLista.setBounds(10, 35, 460, 180);
        contentPanel.add(scrollLista);

        chkDescuento = new JCheckBox("Solo productos con descuento");
        chkDescuento.setBounds(10, 225, 220, 20);
        chkDescuento.addActionListener(this);
        contentPanel.add(chkDescuento);

        lblCantidad = new JLabel("Cantidad:");
        lblCantidad.setBounds(10, 260, 70, 20);
        contentPanel.add(lblCantidad);

        comboCantidad = new JComboBox<>(new Integer[]{1,2,3,4,5,6,7,8,9,10});
        comboCantidad.setBounds(80, 260, 60, 20);
        contentPanel.add(comboCantidad);

        lblPago = new JLabel("Método de pago:");
        lblPago.setBounds(10, 295, 120, 20);
        contentPanel.add(lblPago);

        rbTarjeta = new JRadioButton("Tarjeta", true);
        rbTarjeta.setBounds(135, 295, 80, 20);
        contentPanel.add(rbTarjeta);

        rbEfectivo = new JRadioButton("Efectivo");
        rbEfectivo.setBounds(220, 295, 80, 20);
        contentPanel.add(rbEfectivo);

        ButtonGroup grupo = new ButtonGroup();
        grupo.add(rbTarjeta);
        grupo.add(rbEfectivo);

        chkTerminos = new JCheckBox("Acepto los términos y condiciones");
        chkTerminos.setBounds(10, 330, 250, 20);
        contentPanel.add(chkTerminos);

        btnComprar = new JButton("Confirmar compra");
        btnComprar.setBounds(270, 325, 150, 25);
        btnComprar.addActionListener(this);
        contentPanel.add(btnComprar);

        btnCancelar = new JButton("Cancelar");
        btnCancelar.setBounds(270, 360, 150, 25);
        btnCancelar.addActionListener(this);
        contentPanel.add(btnCancelar);

        cargarProductos();
    }

    public void cargarProductos() {
        AccesoBD accesoBD = new AccesoBD();
        try {
            accesoBD.getTodosProductos(todosProductos);
            mostrarProductos();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void mostrarProductos() {
        modeloLista.clear();
        for (Producto p : todosProductos.values()) {
            if (!chkDescuento.isSelected() || p.getDescuento() > 0) {
                if (p.getDescuento() > 0) {
                    modeloLista.addElement(p.getRef_producto() + " - " + p.getPrecio() + "€  *** DESCUENTO " + p.getDescuento() + "% ***");
                } else {
                    modeloLista.addElement(p.getRef_producto() + " - " + p.getPrecio() + "€");
                }
            }
        }
    }
    public void comprar() {
        String sel = listaProductos.getSelectedValue();
        if (sel == null) {
            JOptionPane.showMessageDialog(this, "Selecciona un producto.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!chkTerminos.isSelected()) {
            JOptionPane.showMessageDialog(this, "Acepta los términos.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String metodo;
        if (rbTarjeta.isSelected()) {
            metodo = "TARJETA";
        } else {
            metodo = "EFECTIVO";
        }

        List<Producto> listaP = new ArrayList<>(todosProductos.values());
        Producto p = listaP.get(listaProductos.getSelectedIndex());
        int cantidad = (Integer) comboCantidad.getSelectedItem();

        AccesoBD accesoBD = new AccesoBD();
        try {
            accesoBD.insertarCompraMultiple(clienteActual.getDni(), metodo, p, cantidad);
            JOptionPane.showMessageDialog(this, "¡Compra realizada!", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnComprar) 
        	comprar();
        if (e.getSource() == btnCancelar)  
        	setVisible(false);
        if (e.getSource() == chkDescuento) 
        	mostrarProductos();
    }
}