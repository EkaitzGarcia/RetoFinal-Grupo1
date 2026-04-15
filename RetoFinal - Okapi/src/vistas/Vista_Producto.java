package vistas;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import modelo.AccesoBD;
import modelo.Producto;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.Map;
import java.util.TreeMap;

public class Vista_Producto extends JDialog implements ActionListener {

    private static final long serialVersionUID = 1L;

    // Paleta de colores (igual que Ventana_principal1)
    private static final Color BG_MAIN = new Color(117, 85, 64);
    private static final Color BTN_NORMAL = new Color(126, 129, 115);
    private static final Color BTN_HOVER = new Color(255, 192, 203);
    private static final Color BTN_BORDER = new Color(255, 192, 203);
    private static final Color TEXT_PRIMARY = new Color(248, 250, 252);
    private static final Color ACCENT = new Color(248, 250, 252);

    private static Ventana_principal1 principal;
    private Alta_Producto alta;

    private static void reproducirSonido(String recurso) {
        try {
            java.net.URL url = Vista_Producto.class.getResource(recurso);
            if (url == null) return;
            AudioInputStream ais = AudioSystem.getAudioInputStream(url);
            Clip clip = AudioSystem.getClip();
            clip.open(ais);
            clip.start();
        } catch (Exception ex) { }
    }

    private JButton btnVolver, btnAlta;
    private JLabel lblNewLabel;
    private JTable table;
    private DefaultTableModel modeloTabla;

    public static void main(String[] args) {
        try {
            Vista_Producto dialog = new Vista_Producto();
            dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
            dialog.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    principal = new Ventana_principal1();
                    principal.setVisible(true);
                    dialog.dispose();
                }
            });
            dialog.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Vista_Producto() {
        setTitle("Product list: Okapi");
        setBounds(100, 100, 1330, 754);

        // Fondo principal
        JPanel root = new JPanel();
        root.setBackground(BG_MAIN);
        root.setLayout(null);
        setContentPane(root);

        // Etiqueta título
        lblNewLabel = new JLabel("Available Products:");
        lblNewLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblNewLabel.setForeground(TEXT_PRIMARY);
        lblNewLabel.setBounds(45, 20, 200, 31);
        root.add(lblNewLabel);

        // Tabla
        String[] columnas = {"Reference", "Name", "Price (€)", "Discount (%)"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(modeloTabla);
        table.setBackground(new Color(245, 240, 235));
        table.setForeground(new Color(60, 40, 20));
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(24);
        table.getTableHeader().setBackground(new Color(126, 129, 115));
        table.getTableHeader().setForeground(TEXT_PRIMARY);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.setCellSelectionEnabled(false);
        table.setRowSelectionAllowed(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setSelectionBackground(new Color(255, 192, 203));
        table.setSelectionForeground(new Color(60, 40, 20));
        table.setFillsViewportHeight(true);

        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < columnas.length; i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }
        ((DefaultTableCellRenderer) table.getTableHeader().getDefaultRenderer())
                .setHorizontalAlignment(SwingConstants.CENTER);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(41, 60, 1237, 600);
        scrollPane.setBorder(new LineBorder(new Color(255, 192, 203), 1));
        root.add(scrollPane);

        // Panel botones abajo
        JPanel buttonPane = new JPanel();
        buttonPane.setBackground(BG_MAIN);
        buttonPane.setBounds(0, 673, 1306, 44);
        buttonPane.setLayout(null);
        root.add(buttonPane);

        java.net.URL salirImgVP = getClass().getResource("/resources/salida.png");
        if (salirImgVP != null) {
            javax.swing.ImageIcon iconoSalir = new javax.swing.ImageIcon(salirImgVP);
            java.awt.Image imgSalir = iconoSalir.getImage().getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH);
            btnVolver = createButton("Back");
            btnVolver.setIcon(new javax.swing.ImageIcon(imgSalir));
        } else {
            btnVolver = createButton("Back");
        }
        btnVolver.setBounds(31, 8, 140, 30);
        btnVolver.addActionListener(this);
        btnVolver.setActionCommand("Back");
        buttonPane.add(btnVolver);

        btnAlta = createButton("Add Product");
        btnAlta.setBounds(1150, 8, 120, 30);
        btnAlta.addActionListener(this);
        buttonPane.add(btnAlta);

        // Doble clic → abre Detalle_Producto
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int fila = table.getSelectedRow();
                    if (fila != -1) {
                        String ref      = (String) modeloTabla.getValueAt(fila, 0);
                        String nombre   = (String) modeloTabla.getValueAt(fila, 1);
                        String precio   = (String) modeloTabla.getValueAt(fila, 2);
                        int descuento   = (int)    modeloTabla.getValueAt(fila, 3);

                        Detalle_Producto detalle = new Detalle_Producto(ref, nombre, precio, descuento);
                        detalle.setVisible(true);

                        detalle.addWindowListener(new WindowAdapter() {
                            @Override
                            public void windowClosed(WindowEvent we) {
                                cargarProductos();
                            }
                        });
                    }
                }
            }
        });

        cargarProductos();
    }

    private JButton createButton(String label) {
        JButton btn = new JButton(label);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(TEXT_PRIMARY);
        btn.setBackground(BTN_NORMAL);
        btn.setFocusPainted(false);
        btn.setBorder(new CompoundBorder(
                new LineBorder(BTN_BORDER, 1, true),
                new EmptyBorder(4, 16, 4, 16)));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        btn.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) {
                btn.setBackground(BTN_HOVER);
                btn.setForeground(new Color(60, 40, 20));
                btn.setBorder(new CompoundBorder(
                        new LineBorder(ACCENT, 1, true),
                        new EmptyBorder(4, 16, 4, 16)));
            }
            @Override public void mouseExited(MouseEvent e) {
                btn.setBackground(BTN_NORMAL);
                btn.setForeground(TEXT_PRIMARY);
                btn.setBorder(new CompoundBorder(
                        new LineBorder(BTN_BORDER, 1, true),
                        new EmptyBorder(4, 16, 4, 16)));
            }
        });
        return btn;
    }

    private void cargarProductos() {
        Map<Integer, Producto> productos = new TreeMap<>();
        AccesoBD acceso = new AccesoBD();
        try {
            acceso.verProductos(productos);
            modeloTabla.setRowCount(0);
            for (Producto p : productos.values()) {
                Object[] fila = {
                        p.getRef_producto(),
                        p.getNom_prod(),
                        String.format("%.2f", p.getPrecio()),
                        p.getDescuento()
                };
                modeloTabla.addRow(fila);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar los productos:\n" + e.getMessage(),
                    "Error de conexión",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnVolver) {
            reproducirSonido("/resources/salida.wav");
            principal = new Ventana_principal1();
            principal.setVisible(true);
            dispose();
        }
        if (e.getSource() == btnAlta) {
            alta = new Alta_Producto();
            alta.setVisible(true);
            alta.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent we) {
                    cargarProductos();
                }
            });
        }
    }
}