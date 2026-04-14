package vistas;

import modelo.AccesoBD;
import modelo.Producto;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class Detalle_Producto extends JDialog implements ActionListener {

    private static final long serialVersionUID = 1L;

    // Paleta de colores (igual que Ventana_principal1)
    private static final Color BG_MAIN     = new Color(117, 85, 64);
    private static final Color BTN_NORMAL  = new Color(126, 129, 115);
    private static final Color BTN_HOVER   = new Color(255, 192, 203);
    private static final Color BTN_BORDER  = new Color(255, 192, 203);
    private static final Color TEXT_PRIMARY = new Color(248, 250, 252);
    private static final Color ACCENT      = new Color(248, 250, 252);
    private static final Color FIELD_BG    = new Color(245, 240, 235);
    private static final Color FIELD_FG    = new Color(60, 40, 20);
    private static final Color FIELD_DISABLED = new Color(200, 195, 190);

    private JButton btnGuardar, btnEliminar, btnCancelar;
    private JLabel lblRef, lblNom, lblPre, lblDesc;
    private JTextField refText, nomText, preText, descText;

    private String refOriginal;

    public Detalle_Producto(String ref, String nombre, String precio, int descuento) {
        this.refOriginal = ref;

        setTitle("Product detail: " + ref);
        setBounds(100, 100, 450, 320);

        JPanel root = new JPanel();
        root.setBackground(BG_MAIN);
        root.setLayout(null);
        setContentPane(root);

        // --- Etiquetas y campos ---
        lblRef = new JLabel("Reference:");
        lblRef.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblRef.setForeground(TEXT_PRIMARY);
        lblRef.setBounds(36, 30, 100, 20);
        root.add(lblRef);

        refText = createField(ref, false);
        refText.setBounds(36, 52, 140, 28);
        root.add(refText);

        lblNom = new JLabel("Name:");
        lblNom.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblNom.setForeground(TEXT_PRIMARY);
        lblNom.setBounds(240, 30, 100, 20);
        root.add(lblNom);

        nomText = createField(nombre, true);
        nomText.setBounds(240, 52, 160, 28);
        root.add(nomText);

        lblPre = new JLabel("Price:");
        lblPre.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblPre.setForeground(TEXT_PRIMARY);
        lblPre.setBounds(36, 110, 100, 20);
        root.add(lblPre);

        preText = createField(precio, true);
        preText.setBounds(36, 132, 140, 28);
        root.add(preText);

        lblDesc = new JLabel("Discount:");
        lblDesc.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblDesc.setForeground(TEXT_PRIMARY);
        lblDesc.setBounds(240, 110, 100, 20);
        root.add(lblDesc);

        descText = createField(String.valueOf(descuento), true);
        descText.setBounds(240, 132, 160, 28);
        root.add(descText);

        // --- Botones ---
        btnCancelar = createButton("Cancel");
        btnCancelar.setBounds(20, 220, 110, 34);
        btnCancelar.addActionListener(this);
        root.add(btnCancelar);

        btnEliminar = createButton("Delete");
        btnEliminar.setBounds(160, 220, 110, 34);
        btnEliminar.addActionListener(this);
        root.add(btnEliminar);

        btnGuardar = createButton("Save");
        btnGuardar.setBounds(300, 220, 110, 34);
        btnGuardar.addActionListener(this);
        root.add(btnGuardar);
    }

    private JTextField createField(String value, boolean editable) {
        JTextField field = new JTextField(value);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setForeground(FIELD_FG);
        field.setCaretColor(FIELD_FG);
        if (editable) {
            field.setBackground(FIELD_BG);
            field.setBorder(new CompoundBorder(
                    new LineBorder(BTN_BORDER, 1, true),
                    new EmptyBorder(2, 6, 2, 6)));
        } else {
            field.setBackground(FIELD_DISABLED);
            field.setEditable(false);
            field.setBorder(new CompoundBorder(
                    new LineBorder(new Color(180, 175, 170), 1, true),
                    new EmptyBorder(2, 6, 2, 6)));
        }
        return field;
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

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == btnGuardar) {

            if (nomText.getText().trim().isEmpty()
                    || preText.getText().trim().isEmpty()
                    || descText.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Todos los campos son obligatorios.",
                        "Error de validación",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            float precio;
            try {
                precio = Float.parseFloat(preText.getText().trim().replace(",", "."));
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "El precio debe ser un número válido (ej: 29.99 o 29,99).",
                        "Error de validación",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            int descuento;
            try {
                descuento = Integer.parseInt(descText.getText().trim());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "El descuento debe ser un número entero (ej: 10).",
                        "Error de validación",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            Producto producto = new Producto();
            producto.setRef_producto(refOriginal);
            producto.setNom_prod(nomText.getText().trim());
            producto.setPrecio(precio);
            producto.setDescuento(descuento);

            AccesoBD acceso = new AccesoBD();
            try {
                acceso.actualizarProducto(producto);
                JOptionPane.showMessageDialog(this,
                        "Producto actualizado correctamente.",
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Error al actualizar el producto:\n" + ex.getMessage(),
                        "Error de base de datos",
                        JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }

        if (e.getSource() == btnEliminar) {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "¿Estás seguro de que quieres eliminar el producto " + refOriginal + "?",
                    "Confirmar eliminación",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                AccesoBD acceso = new AccesoBD();
                try {
                    acceso.eliminarProducto(refOriginal);
                    JOptionPane.showMessageDialog(this,
                            "Producto eliminado correctamente.",
                            "Éxito",
                            JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this,
                            "Error al eliminar el producto:\n" + ex.getMessage(),
                            "Error de base de datos",
                            JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        }

        if (e.getSource() == btnCancelar) {
            dispose();
        }
    }
}