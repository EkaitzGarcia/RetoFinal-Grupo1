package vistas;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.Map;
import java.util.TreeMap;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.BorderFactory;
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

    private static final Color COLOR_FONDO        = new Color(245, 240, 235);
    private static final Color COLOR_PANEL_HEADER  = new Color(117, 85, 64);
    private static final Color COLOR_ACENTO        = new Color(160, 110, 75);
    private static final Color COLOR_BOTON_PPAL    = new Color(117, 85, 64);
    private static final Color COLOR_BOTON_CANCEL  = new Color(180, 80, 60);
    private static final Color COLOR_TEXTO_CLARO   = Color.WHITE;
    private static final Color COLOR_TEXTO_OSCURO  = new Color(50, 30, 15);
    private static final Color COLOR_LISTA_BG      = new Color(255, 252, 248);
    private static final Color COLOR_TABLA_HEADER  = new Color(140, 100, 65);

    private static void reproducirSonido(String recurso) {
        try {
            URL url = Vista_RealizarCompra.class.getResource(recurso);
            if (url == null) return;
            AudioInputStream ais = AudioSystem.getAudioInputStream(url);
            Clip clip = AudioSystem.getClip();
            clip.open(ais);
            clip.start();
        } catch (Exception ex) { }
    }

    public Vista_RealizarCompra(Cliente cliente) {
        this.clienteActual = cliente;

        setTitle("OKAPI - Make a purchase");
        setBounds(100, 100, 820, 580);
        getContentPane().setLayout(new java.awt.BorderLayout());
        contentPanel.setBackground(COLOR_FONDO);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        getContentPane().add(contentPanel, java.awt.BorderLayout.CENTER);
        contentPanel.setLayout(null);

        JPanel header = new JPanel();
        header.setBackground(COLOR_PANEL_HEADER);
        header.setBounds(0, 0, 820, 55);
        header.setLayout(null);
        contentPanel.add(header);

        JLabel lblBienvenida = new JLabel("  Welcome, " + cliente.getNom() + " " + cliente.getApellido() + "!");
        lblBienvenida.setBounds(15, 12, 700, 30);
        lblBienvenida.setFont(new Font("Segoe UI", Font.BOLD, 17));
        lblBienvenida.setForeground(COLOR_TEXTO_CLARO);
        header.add(lblBienvenida);

        JLabel lblBuscar = new JLabel("Search by reference:");
        lblBuscar.setBounds(15, 70, 160, 20);
        lblBuscar.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblBuscar.setForeground(COLOR_TEXTO_OSCURO);
        contentPanel.add(lblBuscar);

        txtBuscarRef = new JTextField();
        txtBuscarRef.setBounds(15, 92, 175, 28);
        txtBuscarRef.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtBuscarRef.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_ACENTO, 1),
            BorderFactory.createEmptyBorder(2, 5, 2, 5)));
        contentPanel.add(txtBuscarRef);

        btnBuscar = crearBoton("Search", 198, 92, 85, 28, COLOR_ACENTO);
        contentPanel.add(btnBuscar);

        btnMostrarTodos = crearBoton("All products", 291, 92, 130, 28, new Color(100, 130, 100));
        contentPanel.add(btnMostrarTodos);

        chkDescuento = new JCheckBox("Discounted only");
        chkDescuento.setBounds(15, 126, 165, 22);
        chkDescuento.setBackground(COLOR_FONDO);
        chkDescuento.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        chkDescuento.setForeground(COLOR_TEXTO_OSCURO);
        chkDescuento.addActionListener(this);
        contentPanel.add(chkDescuento);

        lblProductos = new JLabel("Available products:");
        lblProductos.setBounds(15, 152, 200, 20);
        lblProductos.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblProductos.setForeground(COLOR_TEXTO_OSCURO);
        contentPanel.add(lblProductos);

        modeloLista = new DefaultListModel<>();
        listaProductos = new JList<>(modeloLista);
        listaProductos.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        listaProductos.setBackground(COLOR_LISTA_BG);
        listaProductos.setForeground(COLOR_TEXTO_OSCURO);
        listaProductos.setSelectionBackground(COLOR_ACENTO);
        listaProductos.setSelectionForeground(Color.WHITE);
        listaProductos.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane scrollLista = new JScrollPane(listaProductos);
        scrollLista.setBounds(15, 175, 400, 205);
        scrollLista.setBorder(BorderFactory.createLineBorder(COLOR_ACENTO, 1));
        contentPanel.add(scrollLista);

        lblCantidad = new JLabel("Qty:");
        lblCantidad.setBounds(15, 390, 35, 25);
        lblCantidad.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblCantidad.setForeground(COLOR_TEXTO_OSCURO);
        contentPanel.add(lblCantidad);

        comboCantidad = new JComboBox<>(new Integer[]{1,2,3,4,5,6,7,8,9,10});
        comboCantidad.setBounds(52, 390, 60, 25);
        comboCantidad.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        contentPanel.add(comboCantidad);

        btnAgregarCarrito = crearBoton("+ Add to cart", 120, 388, 150, 30, COLOR_ACENTO);
        contentPanel.add(btnAgregarCarrito);

        JLabel lblCarrito = new JLabel("Your cart:");
        lblCarrito.setBounds(435, 70, 160, 20);
        lblCarrito.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblCarrito.setForeground(COLOR_TEXTO_OSCURO);
        contentPanel.add(lblCarrito);

        String[] columnas = {"Ref", "Qty", "Unit Price", "Discount %", "Total"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(modeloTabla);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(28);
        table.setGridColor(new Color(220, 210, 200));
        table.setBackground(COLOR_LISTA_BG);
        table.setForeground(COLOR_TEXTO_OSCURO);
        table.getTableHeader().setBackground(COLOR_TABLA_HEADER);
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        JScrollPane scrollTabla = new JScrollPane(table);
        scrollTabla.setBounds(435, 92, 362, 280);
        scrollTabla.setBorder(BorderFactory.createLineBorder(COLOR_ACENTO, 1));
        contentPanel.add(scrollTabla);

        JPanel panelPago = new JPanel();
        panelPago.setBackground(new Color(235, 225, 215));
        panelPago.setBounds(435, 385, 362, 140);
        panelPago.setLayout(null);
        panelPago.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_ACENTO, 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        contentPanel.add(panelPago);

        lblPago = new JLabel("Payment method:");
        lblPago.setBounds(10, 10, 130, 20);
        lblPago.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblPago.setForeground(COLOR_TEXTO_OSCURO);
        panelPago.add(lblPago);

        rbTarjeta = new JRadioButton("CARD", true);
        rbTarjeta.setBounds(145, 8, 90, 24);
        rbTarjeta.setBackground(new Color(235, 225, 215));
        rbTarjeta.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        panelPago.add(rbTarjeta);

        rbEfectivo = new JRadioButton("CASH");
        rbEfectivo.setBounds(240, 8, 90, 24);
        rbEfectivo.setBackground(new Color(235, 225, 215));
        rbEfectivo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        panelPago.add(rbEfectivo);

        ButtonGroup grupo = new ButtonGroup();
        grupo.add(rbTarjeta);
        grupo.add(rbEfectivo);

        chkTerminos = new JCheckBox("I accept the terms and conditions");
        chkTerminos.setBounds(10, 40, 320, 22);
        chkTerminos.setBackground(new Color(235, 225, 215));
        chkTerminos.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        chkTerminos.setForeground(COLOR_TEXTO_OSCURO);
        panelPago.add(chkTerminos);

        btnComprar = crearBoton("Confirm order", 10, 75, 160, 35, COLOR_BOTON_PPAL);
        btnComprar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        panelPago.add(btnComprar);

        btnCancelar = crearBoton("Cancel", 180, 75, 160, 35, COLOR_BOTON_CANCEL);
        panelPago.add(btnCancelar);

        cargarProductos();
    }

    private JButton crearBoton(String texto, int x, int y, int w, int h, Color fondo) {
        JButton btn = new JButton(texto);
        btn.setBounds(x, y, w, h);
        btn.setBackground(fondo);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn.addActionListener(this);
        return btn;
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
                        String.format("%.2f", p.getPrecio()) + "EUR  *** DISCOUNT " + p.getDescuento() + "% ***");
                } else {
                    modeloLista.addElement(p.getRef_producto() + " - " +
                        String.format("%.2f", p.getPrecio()) + "EUR");
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

            // SONIDO DE COMPRA - se reproduce tras confirmar con exito
            reproducirSonido("/resources/compra.wav");

            JOptionPane.showMessageDialog(this,
                "Purchase completed!\nTotal: " + String.format("%.2f", totalCompra) + "EUR",
                "Success", JOptionPane.INFORMATION_MESSAGE);
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
                    modeloLista.addElement(p.getRef_producto() + " - " + String.format("%.2f", p.getPrecio()) + "EUR  *** DISCOUNT " + p.getDescuento() + "% ***");
                } else {
                    modeloLista.addElement(p.getRef_producto() + " - " + String.format("%.2f", p.getPrecio()) + "EUR");
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

        // Evitar duplicados: sumar cantidad si ya esta en el carrito
        for (int i = 0; i < modeloTabla.getRowCount(); i++) {
            if (modeloTabla.getValueAt(i, 0).equals(p.getRef_producto())) {
                int cantActual = (Integer) modeloTabla.getValueAt(i, 1);
                int nuevaCant = cantActual + cantidad;
                modeloTabla.setValueAt(nuevaCant, i, 1);
                modeloTabla.setValueAt(p.getPrecio(), i, 2);
                modeloTabla.setValueAt(p.getDescuento(), i, 3);
                modeloTabla.setValueAt(String.format("%.2f", calcularTotal(p, nuevaCant)) + "EUR", i, 4);
                return;
            }
        }

        modeloTabla.addRow(new Object[]{
            p.getRef_producto(),
            cantidad,
            p.getPrecio(),
            p.getDescuento(),
            String.format("%.2f", calcularTotal(p, cantidad)) + "EUR"
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnComprar) {
            comprar();
        }
        if (e.getSource() == btnCancelar) {
            dispose();
            Vista_Cliente vistaCliente = new Vista_Cliente(clienteActual);
            vistaCliente.setVisible(true);
        }
        if (e.getSource() == chkDescuento)
            mostrarTodosProductos();
        if (e.getSource() == btnBuscar)
            buscarProducto();
        if (e.getSource() == btnAgregarCarrito)
            agregarAlCarrito();
        if (e.getSource() == btnMostrarTodos)
            mostrarTodosProductos();
    }
}
