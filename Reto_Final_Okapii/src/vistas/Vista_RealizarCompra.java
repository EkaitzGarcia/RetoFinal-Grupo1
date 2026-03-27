package vistas;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import modelo.AccesoBD;
import modelo.Cliente;
import modelo.Producto;
import javax.swing.JTable;

public class Vista_RealizarCompra extends JDialog implements ActionListener {

    private static final long serialVersionUID = 1L;
    private final JPanel contentPanel = new JPanel();
    

    private JLabel lblProductos, lblCantidad, lblPago;
    private JList<String> listaProductos;
    private DefaultListModel<String> modeloLista;
    private JComboBox<Integer> comboCantidad;
    private JCheckBox chkDescuento, chkTerminos;
    private JRadioButton rbTarjeta, rbEfectivo;
    private JButton btnComprar, btnCancelar, btnBuscar, btnMostrarTodos;
    private JTextField txtBuscarRef;
    private Cliente clienteActual;
    private Map<String, Producto> todosProductos = new TreeMap<>();
    private JTable table;
    private DefaultTableModel modeloTabla;
    private JButton btnAgregarCarrito;
    

    public Vista_RealizarCompra(Cliente cliente) {
        this.clienteActual = cliente;

        JLabel lblBienvenida = new JLabel("Welcome to the shopping panel, " + cliente.getNom() + " " + cliente.getApellido() + "!" );
        
        lblBienvenida.setBounds(10, 10, 600, 30); 
        lblBienvenida.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 16));
        contentPanel.add(lblBienvenida);

       
        setBounds(100, 100, 771, 537);
        getContentPane().setLayout(new java.awt.BorderLayout());
        contentPanel.setBorder(new javax.swing.border.EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, java.awt.BorderLayout.CENTER);
        contentPanel.setLayout(null);

        lblProductos = new JLabel("Available products:");
        lblProductos.setBounds(10, 51, 200, 20);
        contentPanel.add(lblProductos);

        
        modeloLista = new DefaultListModel<>();
        listaProductos = new JList<>(modeloLista);
        listaProductos.setBounds(12, 84, 304, 219);
        listaProductos.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        contentPanel.add(listaProductos);

        
        chkDescuento = new JCheckBox("Discounted products only");
        chkDescuento.setBounds(10, 364, 220, 20);
        chkDescuento.addActionListener(this);
        contentPanel.add(chkDescuento);

      
        lblCantidad = new JLabel("Amount:");
        lblCantidad.setBounds(10, 390, 70, 20);
        contentPanel.add(lblCantidad);

        comboCantidad = new JComboBox<>(new Integer[]{1,2,3,4,5,6,7,8,9,10});
        comboCantidad.setBounds(84, 390, 60, 20);
        contentPanel.add(comboCantidad);

        
        lblPago = new JLabel("Payment method:");
        lblPago.setBounds(10, 420, 120, 20);
        contentPanel.add(lblPago);

        rbTarjeta = new JRadioButton("CARD", true);
        rbTarjeta.setBounds(130, 420, 80, 20);
        contentPanel.add(rbTarjeta);

        rbEfectivo = new JRadioButton("CASH");
        rbEfectivo.setBounds(220, 420, 80, 20);
        contentPanel.add(rbEfectivo);

        ButtonGroup grupo = new ButtonGroup();
        grupo.add(rbTarjeta);
        grupo.add(rbEfectivo);

        chkTerminos = new JCheckBox("I accept the terms and conditions");
        chkTerminos.setBounds(6, 458, 250, 20);
        contentPanel.add(chkTerminos);

       
        btnComprar = new JButton("Confirm order");
        btnComprar.setBounds(574, 418, 150, 25);
        btnComprar.addActionListener(this);
        contentPanel.add(btnComprar);

        btnCancelar = new JButton("Cancel");
        btnCancelar.setBounds(574, 453, 150, 25);
        btnCancelar.addActionListener(this);
        contentPanel.add(btnCancelar);

        txtBuscarRef = new JTextField();
        txtBuscarRef.setBounds(10, 49, 200, 25);
        contentPanel.add(txtBuscarRef);

        btnBuscar = new JButton("Search");
        btnBuscar.setBounds(220, 49, 80, 25);
        btnBuscar.addActionListener(this);
        contentPanel.add(btnBuscar);

        btnMostrarTodos = new JButton("All products");
        btnMostrarTodos.setBounds(310, 49, 160, 24);
        btnMostrarTodos.addActionListener(this);
        contentPanel.add(btnMostrarTodos);

        btnAgregarCarrito = new JButton("Send to cart");
        btnAgregarCarrito.setBounds(179, 313, 137, 30);
        btnAgregarCarrito.addActionListener(this);
        contentPanel.add(btnAgregarCarrito);

        String[] columnas = {"Ref", "Stock", "Unit Price", "Discount %", "Total"};
        modeloTabla = new DefaultTableModel(columnas, 0);
        table = new JTable(modeloTabla);
        table.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 14));
        table.setRowHeight(30); 
        JScrollPane scrollTabla = new JScrollPane(table);
        scrollTabla.setBounds(345, 85, 402, 215);
        contentPanel.add(scrollTabla);
        
        cargarProductos();
    }

    public void cargarProductos() {
        AccesoBD accesoBD = new AccesoBD();
        try {
            accesoBD.getTodosProductos(todosProductos);
            mostrarTodosProductos();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarTodosProductos() {
    	modeloLista.clear();
        for (Producto p : todosProductos.values()) {
            if (!chkDescuento.isSelected() || p.getDescuento() > 0) {
                if (p.getDescuento() > 0) {
                    modeloLista.addElement(p.getRef_producto() + " - " +
                        String.format("%.2f", p.getPrecio()) + "€  *** DISCOUNT " + p.getDescuento() + "% ***");
                } else {
                    modeloLista.addElement(p.getRef_producto() + " - " +
                        String.format("%.2f", p.getPrecio()) + "€");
                }
            }
        }
    }
    public double calcularTotal(Producto p, int cantidad) {
        double precio = p.getPrecio();

        if (p.getDescuento() > 0) {
        	precio = precio - (precio * p.getDescuento() / 100.0);
        }

        return precio * cantidad;
    }
    public void comprar() {
        if (modeloTabla.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Add at least one product to your cart.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!chkTerminos.isSelected()) {
            JOptionPane.showMessageDialog(this, "Accept the terms.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String metodo = rbTarjeta.isSelected() ? "TARJETA" : "EFECTIVO";
        double totalCompra = 0.0;

        AccesoBD accesoBD = new AccesoBD();
        try {
        	for (int i = 0; i < modeloTabla.getRowCount(); i++) {
        	    String ref = (String) modeloTabla.getValueAt(i, 0);
        	    int cantidad = (Integer) modeloTabla.getValueAt(i, 1);

        	    Producto p = todosProductos.get(ref);
        	    double totalFila = Double.parseDouble(String.format("%.2f", calcularTotal(p, cantidad)).replace(",", "."));
        	    totalCompra += totalFila;

        	    accesoBD.insertarCompra(clienteActual.getDni(), metodo, p, cantidad, totalFila); 
        	}

            JOptionPane.showMessageDialog(this, "¡Purchase completed!\nTotal: " + String.format("%.2f", totalCompra) + "€", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
            Vista_Cliente vistaCliente = new Vista_Cliente(clienteActual);
            vistaCliente.setVisible(true); 
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }   
    }
    private void buscarProducto() {
        String ref = txtBuscarRef.getText();
        modeloLista.clear(); 
        boolean encontrado = false;

        for (Producto p : todosProductos.values()) {
            if (p.getRef_producto().equalsIgnoreCase(ref)) {
                encontrado = true;
                if (p.getDescuento() > 0) {
                    modeloLista.addElement(p.getRef_producto() + " - " + String.format("%.2f", p.getPrecio()) +"€  *** DISCOUNT " + p.getDescuento() + "% ***");
                } else {
                    modeloLista.addElement(p.getRef_producto() + " - " + String.format("%.2f", p.getPrecio()) + "€");
                }
                break;
            }
        }

        if (!encontrado) {
            JOptionPane.showMessageDialog(this, "No product was found with that reference.", "WARNING", JOptionPane.WARNING_MESSAGE);
        }  
    }
    private void agregarAlCarrito() {
        String seleccion = listaProductos.getSelectedValue();
        if (seleccion == null) {
            JOptionPane.showMessageDialog(this, "Select a product to add to your cart.", "WARNING", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Producto p = todosProductos.get(seleccion.substring(0, 4));
        int cantidad = (Integer) comboCantidad.getSelectedItem();

        modeloTabla.addRow(new Object[]{p.getRef_producto(), cantidad, p.getPrecio(), p.getDescuento(),String.format("%.2f", calcularTotal(p, cantidad)) + "€"});
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnComprar) 
        	comprar();
        	
        if (e.getSource() == btnCancelar) {
            dispose();
            Vista_Cliente vistaCliente = new Vista_Cliente(clienteActual);
            vistaCliente.setVisible(true);
        }
        if(e.getSource()==chkDescuento)
        	mostrarTodosProductos();
        
        if(e.getSource()==btnBuscar)
        	buscarProducto();
        
        if(e.getSource()==btnAgregarCarrito)
        	agregarAlCarrito();
        if(e.getSource()==btnMostrarTodos)
        	mostrarTodosProductos();

    }

}