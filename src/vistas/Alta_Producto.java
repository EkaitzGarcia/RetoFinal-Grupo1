package vistas;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import modelo.AccesoBD;
import modelo.Producto;

/**
 * Diálogo modal para dar de alta un nuevo producto en el catálogo Okapi.
 * <p>
 * Presenta un formulario con cuatro campos obligatorios:
 * <b>Referencia</b>, <b>Nombre</b>, <b>Precio</b> y <b>Descuento</b>.
 * Valida los datos antes de persistirlos mediante {@link modelo.AccesoBD#insertarProducto}.
 * </p>
 *
 * @see modelo.AccesoBD#insertarProducto(modelo.Producto)
 * @see Vista_Producto
 */
public class Alta_Producto extends JDialog implements ActionListener {

    private static final long serialVersionUID = 1L;

    // Paleta de colores (igual que Ventana_principal1)
    private static final Color BG_MAIN = new Color(117, 85, 64);
    private static final Color BTN_NORMAL = new Color(126, 129, 115);
    private static final Color BTN_HOVER = new Color(255, 192, 203);
    private static final Color BTN_BORDER = new Color(255, 192, 203);
    private static final Color TEXT_PRIMARY = new Color(248, 250, 252);
    private static final Color ACCENT = new Color(248, 250, 252);
    private static final Color FIELD_BG = new Color(245, 240, 235);
    private static final Color FIELD_FG = new Color(60, 40, 20);

    private JButton btnAlta, btnCancelar;
    private JLabel lblRef, lblNom, lblPre, lblDesc;
    private JTextField refText, nomText, preText, descText;

    /**
     * Reproduce un efecto de sonido (.wav) desde los recursos del classpath.
     * Falla silenciosamente si el recurso no existe o se produce cualquier error.
     *
     * @param recurso Ruta del recurso relativa al classpath
     *                (p. ej. {@code "/resources/alta.wav"}).
     */
    private static void reproducirSonido(String recurso) {
        try {
            URL url = Alta_Producto.class.getResource(recurso);
            if (url == null) return;
            AudioInputStream ais = AudioSystem.getAudioInputStream(url);
            Clip clip = AudioSystem.getClip();
            clip.open(ais);
            clip.start();
        } catch (Exception ex) { }
    }

    /**
     * Punto de entrada para probar el diálogo de forma aislada.
     *
     * @param args Argumentos de línea de comandos (no se usan).
     */
    public static void main(String[] args) {
        try {
            Alta_Producto dialog = new Alta_Producto();
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Construye y configura el formulario de alta de producto.
     * Inicializa etiquetas, campos de texto y los botones {@code Add} y {@code Cancel}.
     */
    public Alta_Producto() {
        setTitle("Add Product: Okapi");
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

        refText = createField();
        refText.setBounds(36, 52, 140, 28);
        root.add(refText);

        lblNom = new JLabel("Name:");
        lblNom.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblNom.setForeground(TEXT_PRIMARY);
        lblNom.setBounds(240, 30, 100, 20);
        root.add(lblNom);

        nomText = createField();
        nomText.setBounds(240, 52, 160, 28);
        root.add(nomText);

        lblPre = new JLabel("Price:");
        lblPre.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblPre.setForeground(TEXT_PRIMARY);
        lblPre.setBounds(36, 110, 100, 20);
        root.add(lblPre);

        preText = createField();
        preText.setBounds(36, 132, 140, 28);
        root.add(preText);

        lblDesc = new JLabel("Discount:");
        lblDesc.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblDesc.setForeground(TEXT_PRIMARY);
        lblDesc.setBounds(240, 110, 100, 20);
        root.add(lblDesc);

        descText = createField();
        descText.setBounds(240, 132, 160, 28);
        root.add(descText);

        // --- Botones ---
        btnCancelar = createButton("Cancel");
        btnCancelar.setBounds(30, 220, 120, 34);
        btnCancelar.addActionListener(this);
        root.add(btnCancelar);

        btnAlta = createButton("Add");
        btnAlta.setBounds(290, 220, 120, 34);
        btnAlta.addActionListener(this);
        root.add(btnAlta);
    }

    /**
     * Crea y devuelve un campo de texto con el estilo visual de la aplicación.
     *
     * @return {@link JTextField} estilizado listo para añadir al panel.
     */
    private JTextField createField() {
        JTextField field = new JTextField();
        field.setBackground(FIELD_BG);
        field.setForeground(FIELD_FG);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setBorder(new CompoundBorder(
                new LineBorder(BTN_BORDER, 1, true),
                new EmptyBorder(2, 6, 2, 6)));
        field.setCaretColor(FIELD_FG);
        return field;
    }

    /**
     * Crea un botón estilizado con efecto hover.
     *
     * @param label Texto que mostrará el botón.
     * @return {@link JButton} configurado con la paleta de colores de la aplicación.
     */
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

    /**
     * Gestiona los eventos de los botones {@code Add} y {@code Cancel}.
     * <ul>
     *   <li>{@code Add} — valida los campos, construye un {@link modelo.Producto}
     *       y lo persiste en BD vía {@link modelo.AccesoBD#insertarProducto}.</li>
     *   <li>{@code Cancel} — cierra el diálogo sin guardar cambios.</li>
     * </ul>
     *
     * @param e Evento de acción generado por el botón pulsado.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnAlta) {

            if (refText.getText().trim().isEmpty() || nomText.getText().trim().isEmpty()
                    || preText.getText().trim().isEmpty() || descText.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "All the fields are required.",
                        "Validation error",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            float precio;
            try {
                precio = Float.parseFloat(preText.getText().trim().replace(",", "."));
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "The price must be a valid number (ex: 29.99 or 29,99).",
                        "Validation error",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            int descuento;
            try {
                descuento = Integer.parseInt(descText.getText().trim());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "The discount must be an integer (ex: 10).",
                        "Validation error",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            Producto producto = new Producto();
            producto.setRef_producto(refText.getText().trim());
            producto.setNom_prod(nomText.getText().trim());
            producto.setPrecio(precio);
            producto.setDescuento(descuento);

            AccesoBD acceso = new AccesoBD();
            try {
                acceso.insertarProducto(producto);
                JOptionPane.showMessageDialog(this,
                        "Product added successfully.",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Error inserting product:\n" + ex.getMessage(),
                        "Database error",
                        JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }

        if (e.getSource() == btnCancelar) {
            dispose();
        }
    }
}