package vistas;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import modelo.Cliente;

public class Vista_Cliente extends JDialog implements ActionListener {

    private static final long serialVersionUID = 1L;
    private final JPanel contentPanel = new JPanel();
    private JButton btnVerCompras, btnRealizarCompra, btnSalir;
    private Cliente clienteActual;

    // Paleta
    private static final Color COLOR_BG_TOP    = new Color(90, 60, 40);
    private static final Color COLOR_BG_BOTTOM  = new Color(160, 115, 75);
    private static final Color COLOR_CARD       = new Color(255, 252, 248);
    private static final Color COLOR_CARD_HOVER = new Color(245, 235, 220);
    private static final Color COLOR_ACENTO     = new Color(117, 85, 64);
    private static final Color COLOR_EXIT       = new Color(180, 80, 60);
    private static final Color COLOR_TEXT_DARK  = new Color(50, 30, 15);
    private static final Color COLOR_TEXT_LIGHT = Color.WHITE;

    static void reproducirSonido(String recurso) {
        try {
            URL url = Vista_Cliente.class.getResource(recurso);
            if (url == null) return;
            AudioInputStream ais = AudioSystem.getAudioInputStream(url);
            Clip clip = AudioSystem.getClip();
            clip.open(ais);
            clip.start();
        } catch (Exception ex) { }
    }

    public Vista_Cliente(Cliente cliente) {
        this.clienteActual = cliente;
        setTitle("OKAPI - Client Panel");
        setBounds(100, 100, 500, 370);
        setResizable(false);
        getContentPane().setLayout(new BorderLayout());

        // Panel con gradiente
        JPanel gradPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, COLOR_BG_TOP, 0, getHeight(), COLOR_BG_BOTTOM);
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        gradPanel.setLayout(null);
        getContentPane().add(gradPanel, BorderLayout.CENTER);

        // Titulo/header
        JLabel lblTitulo = new JLabel("OKAPI", SwingConstants.CENTER);
        lblTitulo.setBounds(0, 18, 500, 36);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitulo.setForeground(new Color(255, 220, 170));
        gradPanel.add(lblTitulo);

        JLabel lblSub = new JLabel("Welcome, " + cliente.getNom() + " " + cliente.getApellido(), SwingConstants.CENTER);
        lblSub.setBounds(0, 55, 500, 22);
        lblSub.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        lblSub.setForeground(new Color(240, 225, 205));
        gradPanel.add(lblSub);

        // Separador
        JPanel sep = new JPanel();
        sep.setBackground(new Color(255, 255, 255, 60));
        sep.setBounds(40, 83, 420, 1);
        gradPanel.add(sep);

        // Tarjetas de navegacion
        btnRealizarCompra = crearTarjeta("Make a Purchase", "/resources/CESTA_SI.png", 60, 105, 170, 155);
        gradPanel.add(btnRealizarCompra);

        btnVerCompras = crearTarjetaTexto("My Purchases", "View purchase history", 270, 105, 170, 155);
        gradPanel.add(btnVerCompras);

        // Boton salir estilo pill
        btnSalir = new JButton("Exit");
        URL salirURL = getClass().getResource("/resources/salida.png");
        if (salirURL != null) {
            ImageIcon ic = new ImageIcon(salirURL);
            Image img = ic.getImage().getScaledInstance(18, 18, Image.SCALE_SMOOTH);
            btnSalir.setIcon(new ImageIcon(img));
        }
        btnSalir.setBounds(185, 285, 130, 34);
        btnSalir.setBackground(COLOR_EXIT);
        btnSalir.setForeground(Color.WHITE);
        btnSalir.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnSalir.setFocusPainted(false);
        btnSalir.setBorderPainted(false);
        btnSalir.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSalir.addActionListener(this);
        gradPanel.add(btnSalir);
    }

    private JButton crearTarjeta(String texto, String imgPath, int x, int y, int w, int h) {
        JButton btn = new JButton();
        btn.setLayout(null);
        btn.setBounds(x, y, w, h);
        btn.setBackground(COLOR_CARD);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 180, 155), 1),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)));

        URL url = getClass().getResource(imgPath);
        if (url != null) {
            ImageIcon ic = new ImageIcon(url);
            Image img = ic.getImage().getScaledInstance(90, 75, Image.SCALE_SMOOTH);
            JLabel lblImg = new JLabel(new ImageIcon(img), SwingConstants.CENTER);
            lblImg.setBounds(0, 15, w, 80);
            btn.add(lblImg);
        }

        JLabel lbl = new JLabel(texto, SwingConstants.CENTER);
        lbl.setBounds(0, 100, w, 36);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setForeground(COLOR_TEXT_DARK);
        btn.add(lbl);

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(COLOR_CARD_HOVER); }
            public void mouseExited(MouseEvent e)  { btn.setBackground(COLOR_CARD); }
        });
        btn.addActionListener(this);
        return btn;
    }

    private JButton crearTarjetaTexto(String titulo, String subtitulo, int x, int y, int w, int h) {
        JButton btn = new JButton();
        btn.setLayout(null);
        btn.setBounds(x, y, w, h);
        btn.setBackground(COLOR_CARD);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 180, 155), 1),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)));

        JLabel icono = new JLabel("📋", SwingConstants.CENTER);
        icono.setBounds(0, 25, w, 50);
        icono.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 36));
        btn.add(icono);

        JLabel lbl = new JLabel(titulo, SwingConstants.CENTER);
        lbl.setBounds(0, 85, w, 24);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setForeground(COLOR_TEXT_DARK);
        btn.add(lbl);

        JLabel sub = new JLabel(subtitulo, SwingConstants.CENTER);
        sub.setBounds(0, 110, w, 18);
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        sub.setForeground(new Color(130, 100, 70));
        btn.add(sub);

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(COLOR_CARD_HOVER); }
            public void mouseExited(MouseEvent e)  { btn.setBackground(COLOR_CARD); }
        });
        btn.addActionListener(this);
        return btn;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnRealizarCompra) {
            Vista_RealizarCompra vCompra = new Vista_RealizarCompra(clienteActual);
            vCompra.setVisible(true);
            this.dispose();
        } else if (e.getSource() == btnVerCompras) {
            Vista_MisCompras vMisCompras = new Vista_MisCompras(clienteActual);
            vMisCompras.setVisible(true);
            this.dispose();
        } else if (e.getSource() == btnSalir) {
            reproducirSonido("/resources/salida.wav");
            this.dispose();
            new Ventana_principal1().setVisible(true);
        }
    }
}
