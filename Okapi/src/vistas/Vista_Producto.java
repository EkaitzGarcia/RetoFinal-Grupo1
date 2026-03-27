package vistas;

import modelo.AccesoBD;
import modelo.Producto;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.Map;
import java.util.TreeMap;

public class Vista_Producto extends JDialog implements ActionListener {

    private static final long serialVersionUID = 1L;
    private static Ventana_principal1 principal;
    private Alta_Producto alta;

    private JButton btnVolver, btnAlta;
    private JLabel lblNewLabel;
    private JTable table;
    private DefaultTableModel modeloTabla;

    public static void main(String[] args) {
        try {
            Vista_Producto dialog = new Vista_Producto(); // CORRECCIÓN: era Vista_Producto
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
        getContentPane().setLayout(null);

        JPanel buttonPane = new JPanel();
        buttonPane.setBounds(0, 673, 1306, 34);
        getContentPane().add(buttonPane);
        buttonPane.setLayout(null);

        btnVolver = new JButton("Back");
        btnVolver.addActionListener(this);
        btnVolver.setBounds(31, 5, 124, 23);
        btnVolver.setActionCommand("Back");
        buttonPane.add(btnVolver);
        getRootPane().setDefaultButton(btnVolver);

        btnAlta = new JButton("Add Product");
        btnAlta.addActionListener(this);
        btnAlta.setBounds(1147, 5, 124, 23);
        buttonPane.add(btnAlta);

        lblNewLabel = new JLabel("Available Products:");
        lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
        lblNewLabel.setBounds(45, 62, 132, 31);
        getContentPane().add(lblNewLabel);

        String[] columnas = { "Reference", "Name", "Price (€)", "Discount (%)" };
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(modeloTabla);
        table.setBorder(new LineBorder(new Color(0, 0, 0)));
        table.setCellSelectionEnabled(false);
        table.setRowSelectionAllowed(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setFillsViewportHeight(true);

        // Centrar el contenido de todas las columnas
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < columnas.length; i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(41, 103, 1237, 547);
        getContentPane().add(scrollPane);

        // Doble clic sobre una fila → abre la ventana de detalle/edición
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int fila = table.getSelectedRow();
                    if (fila != -1) {
                        // Obtiene la referencia del producto de la fila seleccionada
                        int ref = (int) modeloTabla.getValueAt(fila, 0);

                        // TODO: sustituye "Detalle_Producto" por el nombre real de tu ventana
                        // Detalle_Producto detalle = new Detalle_Producto(ref);
                        // detalle.setVisible(true);

                        JOptionPane.showMessageDialog(null,
                                "Doble clic en producto con referencia: " + ref,
                                "Detalle producto",
                                JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
        });

        cargarProductos();
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