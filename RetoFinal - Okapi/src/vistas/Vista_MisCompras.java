package vistas;

import modelo.AccesoBD;
import modelo.Cliente;
import modelo.Compra;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.*;
import java.net.URL;
import java.util.*;

public class Vista_MisCompras extends JDialog implements ActionListener {

    private JPanel contentPanel = new JPanel();
    private JTable tablaCompras;
    private DefaultTableModel modeloCompras;
    private JTable tablaProductos;
    private DefaultTableModel modeloProductos;
    private JButton btnCerrar, btnGuardar;
    private Cliente clienteActual;
    private JLabel lblDetalle;

    // Paleta coherente con Vista_Cliente
    private static final Color COLOR_FONDO        = new Color(245, 240, 235);
    private static final Color COLOR_HEADER        = new Color(117, 85, 64);
    private static final Color COLOR_ACENTO        = new Color(160, 110, 75);
    private static final Color COLOR_TABLA_HEADER  = new Color(140, 100, 65);
    private static final Color COLOR_SELECCION     = new Color(200, 165, 120);
    private static final Color COLOR_LISTA_BG      = new Color(255, 252, 248);
    private static final Color COLOR_TEXT_DARK     = new Color(50, 30, 15);
    private static final Color COLOR_BOTON_PPAL    = new Color(117, 85, 64);
    private static final Color COLOR_BOTON_CANCEL  = new Color(180, 80, 60);

    private static void reproducirSonido(String recurso) {
        try {
            URL url = Vista_MisCompras.class.getResource(recurso);
            if (url == null) return;
            AudioInputStream ais = AudioSystem.getAudioInputStream(url);
            Clip clip = AudioSystem.getClip();
            clip.open(ais);
            clip.start();
        } catch (Exception ex) { }
    }

    public Vista_MisCompras(Cliente cliente) {
        this.clienteActual = cliente;
        setTitle("OKAPI - My purchases");
        setBounds(100, 100, 780, 560);
        setResizable(false);
        getContentPane().setLayout(new java.awt.BorderLayout());
        contentPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
        contentPanel.setBackground(COLOR_FONDO);
        getContentPane().add(contentPanel, java.awt.BorderLayout.CENTER);
        contentPanel.setLayout(null);

        // ── Header ──────────────────────────────────────────────────────
        JPanel header = new JPanel();
        header.setBackground(COLOR_HEADER);
        header.setBounds(0, 0, 780, 50);
        header.setLayout(null);
        contentPanel.add(header);

        JLabel lblTitulo = new JLabel("My Purchases - " + cliente.getNom() + " " + cliente.getApellido());
        lblTitulo.setBounds(15, 12, 600, 26);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitulo.setForeground(Color.WHITE);
        header.add(lblTitulo);

        // ── Tabla de compras (izquierda) ─────────────────────────────────
        JLabel lblCompras = new JLabel("Orders — click one to see its products:");
        lblCompras.setBounds(15, 60, 380, 20);
        lblCompras.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblCompras.setForeground(COLOR_TEXT_DARK);
        contentPanel.add(lblCompras);

        String[] colCompras = {"ID", "Date", "Total", "Payment"};
        modeloCompras = new DefaultTableModel(colCompras, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3; // solo metodo de pago editable
            }
        };

        tablaCompras = new JTable(modeloCompras);
        estilizarTabla(tablaCompras);
        JComboBox<String> comboPago = new JComboBox<>(new String[]{"CARD", "CASH"});
        tablaCompras.getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(comboPago));

        JScrollPane scrollCompras = new JScrollPane(tablaCompras);
        scrollCompras.setBounds(15, 83, 360, 360);
        scrollCompras.setBorder(BorderFactory.createLineBorder(COLOR_ACENTO, 1));
        contentPanel.add(scrollCompras);

        // ── Tabla de productos (derecha) ─────────────────────────────────
        lblDetalle = new JLabel("Products in selected order:");
        lblDetalle.setBounds(390, 60, 370, 20);
        lblDetalle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblDetalle.setForeground(COLOR_TEXT_DARK);
        contentPanel.add(lblDetalle);

        String[] colProductos = {"Ref", "Qty", "Unit Price", "Discount %", "Line Total"};
        modeloProductos = new DefaultTableModel(colProductos, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        tablaProductos = new JTable(modeloProductos);
        estilizarTabla(tablaProductos);
        JScrollPane scrollProductos = new JScrollPane(tablaProductos);
        scrollProductos.setBounds(390, 83, 372, 360);
        scrollProductos.setBorder(BorderFactory.createLineBorder(COLOR_ACENTO, 1));
        contentPanel.add(scrollProductos);

        // ── Listener: al seleccionar compra mostrar sus productos ────────
        tablaCompras.getSelectionModel().addListSelectionListener(ev -> {
            if (!ev.getValueIsAdjusting()) {
                int fila = tablaCompras.getSelectedRow();
                if (fila >= 0) {
                    int idCompra = (int) modeloCompras.getValueAt(fila, 0);
                    cargarProductosDeCompra(idCompra);
                }
            }
        });

        // ── Botones inferiores ────────────────────────────────────────────
        btnGuardar = crearBoton("Save changes", 15, 460, 165, 34, COLOR_BOTON_PPAL);
        contentPanel.add(btnGuardar);

        URL salirURL = getClass().getResource("/resources/salida.png");
        if (salirURL != null) {
            ImageIcon ic = new ImageIcon(salirURL);
            Image img = ic.getImage().getScaledInstance(18, 18, Image.SCALE_SMOOTH);
            btnCerrar = new JButton("Close", new ImageIcon(img));
        } else {
            btnCerrar = new JButton("Close");
        }
        btnCerrar.setBounds(195, 460, 140, 34);
        btnCerrar.setBackground(COLOR_BOTON_CANCEL);
        btnCerrar.setForeground(Color.WHITE);
        btnCerrar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnCerrar.setFocusPainted(false);
        btnCerrar.setBorderPainted(false);
        btnCerrar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCerrar.addActionListener(this);
        contentPanel.add(btnCerrar);

        cargarCompras();
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

    private void estilizarTabla(JTable tabla) {
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tabla.setRowHeight(26);
        tabla.setGridColor(new Color(220, 210, 200));
        tabla.setBackground(COLOR_LISTA_BG);
        tabla.setForeground(COLOR_TEXT_DARK);
        tabla.setSelectionBackground(COLOR_SELECCION);
        tabla.setSelectionForeground(Color.WHITE);
        tabla.getTableHeader().setBackground(COLOR_TABLA_HEADER);
        tabla.getTableHeader().setForeground(Color.WHITE);
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < tabla.getColumnCount(); i++) {
            tabla.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }

    public void cargarCompras() {
        AccesoBD accesoBD = new AccesoBD();
        Map<Integer, Compra> compras = new TreeMap<>();
        try {
            accesoBD.getComprasPorCliente(clienteActual.getDni(), compras);
            for (Compra c : compras.values()) {
                String metodo = c.getMetodo_pago().toString();
                if (metodo.equals("TARJETA")) metodo = "CARD";
                else if (metodo.equals("EFECTIVO")) metodo = "CASH";

                modeloCompras.addRow(new Object[]{
                    c.getId_compra(),
                    c.getFecha(),
                    String.format("%.2f EUR", c.getTotal_compra()),
                    metodo
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Carga los productos de una compra agrupando por referencia
     * para que no haya filas duplicadas aunque se comprasen varias veces.
     */
    private void cargarProductosDeCompra(int idCompra) {
        modeloProductos.setRowCount(0);
        AccesoBD accesoBD = new AccesoBD();
        try {
            List<Object[]> filas = accesoBD.getProductosDeCompra(idCompra);

            // Agrupar por REF para evitar duplicados
            Map<String, Object[]> agrupado = new LinkedHashMap<>();
            for (Object[] fila : filas) {
                String ref      = (String) fila[0];
                int    cantidad = (int)    fila[1];
                float  precio   = (float)  fila[2];
                int    dto      = (int)    fila[3];

                if (agrupado.containsKey(ref)) {
                    // Ya existe: sumar la cantidad
                    Object[] existente = agrupado.get(ref);
                    int cantAcum = (int) existente[1] + cantidad;
                    existente[1] = cantAcum;
                    double pUnit = precio * (1 - dto / 100.0);
                    existente[4] = String.format("%.2f EUR", pUnit * cantAcum);
                } else {
                    double pUnit  = precio * (1 - dto / 100.0);
                    double total  = pUnit * cantidad;
                    agrupado.put(ref, new Object[]{
                        ref,
                        cantidad,
                        String.format("%.2f EUR", (double) precio),
                        dto + "%",
                        String.format("%.2f EUR", total)
                    });
                }
            }

            for (Object[] row : agrupado.values()) {
                modeloProductos.addRow(row);
            }

            lblDetalle.setText("Products in order #" + idCompra + "  (" + agrupado.size() + " distinct product(s))");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void guardarCambios() {
        AccesoBD accesoBD = new AccesoBD();
        try {
            for (int i = 0; i < modeloCompras.getRowCount(); i++) {
                int id = (int) modeloCompras.getValueAt(i, 0);
                String metodo = modeloCompras.getValueAt(i, 3).toString();
                if (metodo.equals("CARD"))  metodo = "TARJETA";
                else if (metodo.equals("CASH")) metodo = "EFECTIVO";
                accesoBD.actualizarMetodoPago(id, metodo);
            }
            JOptionPane.showMessageDialog(this, "Changes saved successfully");
            dispose();
            new Vista_Cliente(clienteActual).setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnCerrar) {
            reproducirSonido("/resources/salida.wav");
            dispose();
            new Vista_Cliente(clienteActual).setVisible(true);
        }
        if (e.getSource() == btnGuardar) {
            guardarCambios();
        }
    }
}
