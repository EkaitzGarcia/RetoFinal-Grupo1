package vistas;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import controlador.Principal;
import excepciones.DniException;
import excepciones.FormatoIncorrectoException;
import excepciones.NssException;
import excepciones.TelefonoException;
import modelo.Cliente;
import modelo.Trabajador;

/**
 * Diálogo de administración del sistema.
 * Permite gestionar clientes y trabajadores mediante pestañas.
 *
 * @version 1.0
 */
public class Vista_Admin extends JDialog implements ActionListener {

    private static final long serialVersionUID = 1L;

    // ── Paleta de colores
    private static final Color BG_MAIN      = new Color(117, 85, 64);
    private static final Color BTN_NORMAL   = new Color(126, 129, 115);
    private static final Color BTN_HOVER    = new Color(255, 192, 203);
    private static final Color BTN_BORDER   = new Color(255, 192, 203);
    private static final Color BTN_DANGER   = new Color(160, 60, 60);
    private static final Color BTN_DANGER_H = new Color(200, 80, 80);
    private static final Color BTN_SUCCESS  = new Color(60, 120, 80);
    private static final Color BTN_SUCCESS_H = new Color(80, 160, 100);
    private static final Color BTN_WARNING  = new Color(160, 120, 0);
    private static final Color BTN_WARNING_H = new Color(210, 160, 0);
    private static final Color TEXT_PRIMARY = new Color(248, 250, 252);
    private static final Color FIELD_BG     = new Color(90, 63, 47);
    private static final Color FIELD_BORDER = new Color(180, 140, 110);

    /** Panel con pestañas que contiene todos los paneles de gestión */
    private JTabbedPane tabbedPane;

    /** Paneles de gestión */
    private JPanel panelInicio, panelClientes, panelTrabajadores;

    // ── Botones CLIENTES
    private JButton btnAltaCliente, btnBajaCliente, btnModificarCliente, btnLimpiarCliente;

    // ── Campos CLIENTES
    private JTextField txtDniC, txtNombreC, txtApellidoC, txtTelefonoC, txtCorreoC, txtDireccionC;

    // ── Botones TRABAJADORES
    private JButton btnAltaTrab, btnBajaTrab, btnModificarTrab, btnLimpiarTrab;

    // ── Campos TRABAJADORES
    private JTextField txtNss, txtNombreT, txtApellidoT, txtTelefonoT, txtCorreoT;

    // ── Botones INICIO
    private JButton btnVerClientes, btnVerTrabajadores;

    private Ventana_principal1 vPrincipal;

    // ── Método utilitario para reproducir sonidos
    public static void reproducirSonido(String recurso) {
        try {
            java.net.URL url = Vista_Admin.class.getResource(recurso);
            if (url == null) return;
            AudioInputStream ais = AudioSystem.getAudioInputStream(url);
            Clip clip = AudioSystem.getClip();
            clip.open(ais);
            clip.start();
        } catch (Exception ex) {
            // Sonido no disponible, continuar sin él
        }
    }

    public Vista_Admin(Ventana_principal1 padre) {
        this.vPrincipal = padre;
        setTitle("Panel Administrador");
        setBounds(100, 100, 700, 480);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setModal(true);
        setResizable(false);
        setLocationRelativeTo(null);

        tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(BG_MAIN);
        tabbedPane.setForeground(TEXT_PRIMARY);
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 13));
        getContentPane().setBackground(BG_MAIN);
        getContentPane().add(tabbedPane, BorderLayout.CENTER);

        // =====================================================================
        // PANEL INICIO
        // =====================================================================
        panelInicio = new JPanel();
        panelInicio.setBackground(BG_MAIN);
        panelInicio.setLayout(null);
        tabbedPane.addTab("Inicio", panelInicio);

        JLabel lblTitulo = new JLabel("¿Qué deseas gestionar?", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(TEXT_PRIMARY);
        lblTitulo.setBounds(150, 50, 350, 30);
        panelInicio.add(lblTitulo);

        btnVerClientes = new JButton("👤  Clientes");
        btnVerClientes.setBounds(210, 110, 260, 50);
        btnVerClientes.setFont(new Font("Segoe UI Emoji", Font.BOLD, 14));
        btnVerClientes.setForeground(TEXT_PRIMARY);
        btnVerClientes.setBackground(BTN_NORMAL);
        btnVerClientes.setFocusPainted(false);
        btnVerClientes.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnVerClientes.setOpaque(true);
        btnVerClientes.setBorder(new CompoundBorder(new LineBorder(BTN_BORDER, 1, true), new EmptyBorder(10, 24, 10, 24)));
        btnVerClientes.addActionListener(this);
        panelInicio.add(btnVerClientes);

        btnVerTrabajadores = new JButton("👷  Trabajadores");
        btnVerTrabajadores.setBounds(210, 180, 260, 50);
        btnVerTrabajadores.setFont(new Font("Segoe UI Emoji", Font.BOLD, 14));
        btnVerTrabajadores.setForeground(TEXT_PRIMARY);
        btnVerTrabajadores.setBackground(BTN_NORMAL);
        btnVerTrabajadores.setFocusPainted(false);
        btnVerTrabajadores.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnVerTrabajadores.setOpaque(true);
        btnVerTrabajadores.setBorder(new CompoundBorder(new LineBorder(BTN_BORDER, 1, true), new EmptyBorder(10, 24, 10, 24)));
        btnVerTrabajadores.addActionListener(this);
        panelInicio.add(btnVerTrabajadores);

        // Botón Salir con imagen volver_admin.png y sonido salida.mp3
        ImageIcon iconoOriginal = new ImageIcon(getClass().getResource("/resources/volver_admin.png"));
        Image imagenEscalada = iconoOriginal.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        ImageIcon iconoEscalado = new ImageIcon(imagenEscalada);

        JButton btnSalir = new JButton("  Salir");
        btnSalir.setBounds(24, 331, 160, 55);
        btnSalir.setFont(new Font("Segoe UI Emoji", Font.BOLD, 14));
        btnSalir.setForeground(TEXT_PRIMARY);
        btnSalir.setBackground(BTN_DANGER);
        btnSalir.setFocusPainted(false);
        btnSalir.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSalir.setOpaque(true);
        btnSalir.setIcon(iconoEscalado);
        btnSalir.setHorizontalAlignment(SwingConstants.LEFT);
        btnSalir.setBorder(new CompoundBorder(new LineBorder(BTN_DANGER_H, 1, true), new EmptyBorder(6, 12, 6, 12)));
        btnSalir.addActionListener(ev -> {
            reproducirSonido("/resources/salida.wav");
            dispose();
        });
        panelInicio.add(btnSalir);

        // =====================================================================
        // PANEL CLIENTES
        // =====================================================================
        panelClientes = new JPanel();
        panelClientes.setBackground(BG_MAIN);
        panelClientes.setLayout(null);
        tabbedPane.addTab("Clientes", panelClientes);

        JLabel lblDniC = new JLabel("DNI:");
        lblDniC.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblDniC.setForeground(TEXT_PRIMARY);
        lblDniC.setBounds(50, 40, 140, 28);
        panelClientes.add(lblDniC);

        txtDniC = new JTextField(10);
        txtDniC.setBounds(210, 40, 200, 28);
        txtDniC.setBackground(FIELD_BG);
        txtDniC.setForeground(TEXT_PRIMARY);
        txtDniC.setCaretColor(TEXT_PRIMARY);
        txtDniC.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtDniC.setBorder(new CompoundBorder(new LineBorder(FIELD_BORDER, 1, true), new EmptyBorder(2, 8, 2, 8)));
        panelClientes.add(txtDniC);

        JLabel lblNombreC = new JLabel("Nombre:");
        lblNombreC.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblNombreC.setForeground(TEXT_PRIMARY);
        lblNombreC.setBounds(50, 80, 140, 28);
        panelClientes.add(lblNombreC);

        txtNombreC = new JTextField(10);
        txtNombreC.setBounds(210, 80, 200, 28);
        txtNombreC.setBackground(FIELD_BG);
        txtNombreC.setForeground(TEXT_PRIMARY);
        txtNombreC.setCaretColor(TEXT_PRIMARY);
        txtNombreC.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtNombreC.setBorder(new CompoundBorder(new LineBorder(FIELD_BORDER, 1, true), new EmptyBorder(2, 8, 2, 8)));
        panelClientes.add(txtNombreC);

        JLabel lblApellidoC = new JLabel("Apellido:");
        lblApellidoC.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblApellidoC.setForeground(TEXT_PRIMARY);
        lblApellidoC.setBounds(50, 120, 140, 28);
        panelClientes.add(lblApellidoC);

        txtApellidoC = new JTextField(10);
        txtApellidoC.setBounds(210, 120, 200, 28);
        txtApellidoC.setBackground(FIELD_BG);
        txtApellidoC.setForeground(TEXT_PRIMARY);
        txtApellidoC.setCaretColor(TEXT_PRIMARY);
        txtApellidoC.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtApellidoC.setBorder(new CompoundBorder(new LineBorder(FIELD_BORDER, 1, true), new EmptyBorder(2, 8, 2, 8)));
        panelClientes.add(txtApellidoC);

        JLabel lblTelefonoC = new JLabel("Teléfono:");
        lblTelefonoC.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTelefonoC.setForeground(TEXT_PRIMARY);
        lblTelefonoC.setBounds(50, 160, 140, 28);
        panelClientes.add(lblTelefonoC);

        txtTelefonoC = new JTextField(10);
        txtTelefonoC.setBounds(210, 160, 200, 28);
        txtTelefonoC.setBackground(FIELD_BG);
        txtTelefonoC.setForeground(TEXT_PRIMARY);
        txtTelefonoC.setCaretColor(TEXT_PRIMARY);
        txtTelefonoC.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtTelefonoC.setBorder(new CompoundBorder(new LineBorder(FIELD_BORDER, 1, true), new EmptyBorder(2, 8, 2, 8)));
        panelClientes.add(txtTelefonoC);

        JLabel lblCorreoC = new JLabel("Correo:");
        lblCorreoC.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblCorreoC.setForeground(TEXT_PRIMARY);
        lblCorreoC.setBounds(50, 200, 140, 28);
        panelClientes.add(lblCorreoC);

        txtCorreoC = new JTextField(10);
        txtCorreoC.setBounds(210, 200, 200, 28);
        txtCorreoC.setBackground(FIELD_BG);
        txtCorreoC.setForeground(TEXT_PRIMARY);
        txtCorreoC.setCaretColor(TEXT_PRIMARY);
        txtCorreoC.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtCorreoC.setBorder(new CompoundBorder(new LineBorder(FIELD_BORDER, 1, true), new EmptyBorder(2, 8, 2, 8)));
        panelClientes.add(txtCorreoC);

        JLabel lblDireccionC = new JLabel("Dirección:");
        lblDireccionC.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblDireccionC.setForeground(TEXT_PRIMARY);
        lblDireccionC.setBounds(50, 240, 140, 28);
        panelClientes.add(lblDireccionC);

        txtDireccionC = new JTextField(10);
        txtDireccionC.setBounds(210, 240, 200, 28);
        txtDireccionC.setBackground(FIELD_BG);
        txtDireccionC.setForeground(TEXT_PRIMARY);
        txtDireccionC.setCaretColor(TEXT_PRIMARY);
        txtDireccionC.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtDireccionC.setBorder(new CompoundBorder(new LineBorder(FIELD_BORDER, 1, true), new EmptyBorder(2, 8, 2, 8)));
        panelClientes.add(txtDireccionC);

        btnAltaCliente = new JButton("Alta");
        btnAltaCliente.setBounds(40, 290, 130, 35);
        btnAltaCliente.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnAltaCliente.setForeground(TEXT_PRIMARY);
        btnAltaCliente.setBackground(BTN_SUCCESS);
        btnAltaCliente.setFocusPainted(false);
        btnAltaCliente.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAltaCliente.setOpaque(true);
        btnAltaCliente.setBorder(new CompoundBorder(new LineBorder(BTN_SUCCESS_H, 1, true), new EmptyBorder(6, 12, 6, 12)));
        btnAltaCliente.addActionListener(this);
        panelClientes.add(btnAltaCliente);

        btnBajaCliente = new JButton("Baja");
        btnBajaCliente.setBounds(185, 290, 130, 35);
        btnBajaCliente.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnBajaCliente.setForeground(TEXT_PRIMARY);
        btnBajaCliente.setBackground(BTN_DANGER);
        btnBajaCliente.setFocusPainted(false);
        btnBajaCliente.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBajaCliente.setOpaque(true);
        btnBajaCliente.setBorder(new CompoundBorder(new LineBorder(BTN_DANGER_H, 1, true), new EmptyBorder(6, 12, 6, 12)));
        btnBajaCliente.addActionListener(this);
        panelClientes.add(btnBajaCliente);

        btnModificarCliente = new JButton("Modificar");
        btnModificarCliente.setBounds(330, 290, 130, 35);
        btnModificarCliente.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnModificarCliente.setForeground(TEXT_PRIMARY);
        btnModificarCliente.setBackground(BTN_WARNING);
        btnModificarCliente.setFocusPainted(false);
        btnModificarCliente.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnModificarCliente.setOpaque(true);
        btnModificarCliente.setBorder(new CompoundBorder(new LineBorder(BTN_WARNING_H, 1, true), new EmptyBorder(6, 12, 6, 12)));
        btnModificarCliente.addActionListener(this);
        panelClientes.add(btnModificarCliente);

        btnLimpiarCliente = new JButton("Limpiar");
        btnLimpiarCliente.setBounds(475, 290, 130, 35);
        btnLimpiarCliente.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnLimpiarCliente.setForeground(TEXT_PRIMARY);
        btnLimpiarCliente.setBackground(BTN_NORMAL);
        btnLimpiarCliente.setFocusPainted(false);
        btnLimpiarCliente.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLimpiarCliente.setOpaque(true);
        btnLimpiarCliente.setBorder(new CompoundBorder(new LineBorder(BTN_HOVER, 1, true), new EmptyBorder(6, 12, 6, 12)));
        btnLimpiarCliente.addActionListener(this);
        panelClientes.add(btnLimpiarCliente);

        btnBajaCliente.setEnabled(false);
        btnModificarCliente.setEnabled(false);

        // =====================================================================
        // PANEL TRABAJADORES
        // =====================================================================
        panelTrabajadores = new JPanel();
        panelTrabajadores.setBackground(BG_MAIN);
        panelTrabajadores.setLayout(null);
        tabbedPane.addTab("Trabajadores", panelTrabajadores);

        JLabel lblNssT = new JLabel("NSS:");
        lblNssT.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblNssT.setForeground(TEXT_PRIMARY);
        lblNssT.setBounds(50, 40, 140, 28);
        panelTrabajadores.add(lblNssT);

        txtNss = new JTextField(10);
        txtNss.setBounds(210, 40, 200, 28);
        txtNss.setBackground(FIELD_BG);
        txtNss.setForeground(TEXT_PRIMARY);
        txtNss.setCaretColor(TEXT_PRIMARY);
        txtNss.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtNss.setBorder(new CompoundBorder(new LineBorder(FIELD_BORDER, 1, true), new EmptyBorder(2, 8, 2, 8)));
        panelTrabajadores.add(txtNss);

        JLabel lblNombreT = new JLabel("Nombre:");
        lblNombreT.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblNombreT.setForeground(TEXT_PRIMARY);
        lblNombreT.setBounds(50, 80, 140, 28);
        panelTrabajadores.add(lblNombreT);

        txtNombreT = new JTextField(10);
        txtNombreT.setBounds(210, 80, 200, 28);
        txtNombreT.setBackground(FIELD_BG);
        txtNombreT.setForeground(TEXT_PRIMARY);
        txtNombreT.setCaretColor(TEXT_PRIMARY);
        txtNombreT.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtNombreT.setBorder(new CompoundBorder(new LineBorder(FIELD_BORDER, 1, true), new EmptyBorder(2, 8, 2, 8)));
        panelTrabajadores.add(txtNombreT);

        JLabel lblApellidoT = new JLabel("Apellido:");
        lblApellidoT.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblApellidoT.setForeground(TEXT_PRIMARY);
        lblApellidoT.setBounds(50, 120, 140, 28);
        panelTrabajadores.add(lblApellidoT);

        txtApellidoT = new JTextField(10);
        txtApellidoT.setBounds(210, 120, 200, 28);
        txtApellidoT.setBackground(FIELD_BG);
        txtApellidoT.setForeground(TEXT_PRIMARY);
        txtApellidoT.setCaretColor(TEXT_PRIMARY);
        txtApellidoT.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtApellidoT.setBorder(new CompoundBorder(new LineBorder(FIELD_BORDER, 1, true), new EmptyBorder(2, 8, 2, 8)));
        panelTrabajadores.add(txtApellidoT);

        JLabel lblTelefonoT = new JLabel("Teléfono:");
        lblTelefonoT.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTelefonoT.setForeground(TEXT_PRIMARY);
        lblTelefonoT.setBounds(50, 160, 140, 28);
        panelTrabajadores.add(lblTelefonoT);

        txtTelefonoT = new JTextField(10);
        txtTelefonoT.setBounds(210, 160, 200, 28);
        txtTelefonoT.setBackground(FIELD_BG);
        txtTelefonoT.setForeground(TEXT_PRIMARY);
        txtTelefonoT.setCaretColor(TEXT_PRIMARY);
        txtTelefonoT.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtTelefonoT.setBorder(new CompoundBorder(new LineBorder(FIELD_BORDER, 1, true), new EmptyBorder(2, 8, 2, 8)));
        panelTrabajadores.add(txtTelefonoT);

        JLabel lblCorreoT = new JLabel("Correo:");
        lblCorreoT.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblCorreoT.setForeground(TEXT_PRIMARY);
        lblCorreoT.setBounds(50, 200, 140, 28);
        panelTrabajadores.add(lblCorreoT);

        txtCorreoT = new JTextField(10);
        txtCorreoT.setBounds(210, 200, 200, 28);
        txtCorreoT.setBackground(FIELD_BG);
        txtCorreoT.setForeground(TEXT_PRIMARY);
        txtCorreoT.setCaretColor(TEXT_PRIMARY);
        txtCorreoT.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtCorreoT.setBorder(new CompoundBorder(new LineBorder(FIELD_BORDER, 1, true), new EmptyBorder(2, 8, 2, 8)));
        panelTrabajadores.add(txtCorreoT);

        btnAltaTrab = new JButton("Alta");
        btnAltaTrab.setBounds(40, 290, 130, 35);
        btnAltaTrab.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnAltaTrab.setForeground(TEXT_PRIMARY);
        btnAltaTrab.setBackground(BTN_SUCCESS);
        btnAltaTrab.setFocusPainted(false);
        btnAltaTrab.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAltaTrab.setOpaque(true);
        btnAltaTrab.setBorder(new CompoundBorder(new LineBorder(BTN_SUCCESS_H, 1, true), new EmptyBorder(6, 12, 6, 12)));
        btnAltaTrab.addActionListener(this);
        panelTrabajadores.add(btnAltaTrab);

        btnBajaTrab = new JButton("Baja");
        btnBajaTrab.setBounds(185, 290, 130, 35);
        btnBajaTrab.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnBajaTrab.setForeground(TEXT_PRIMARY);
        btnBajaTrab.setBackground(BTN_DANGER);
        btnBajaTrab.setFocusPainted(false);
        btnBajaTrab.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBajaTrab.setOpaque(true);
        btnBajaTrab.setBorder(new CompoundBorder(new LineBorder(BTN_DANGER_H, 1, true), new EmptyBorder(6, 12, 6, 12)));
        btnBajaTrab.addActionListener(this);
        panelTrabajadores.add(btnBajaTrab);

        btnModificarTrab = new JButton("Modificar");
        btnModificarTrab.setBounds(330, 290, 130, 35);
        btnModificarTrab.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnModificarTrab.setForeground(TEXT_PRIMARY);
        btnModificarTrab.setBackground(BTN_WARNING);
        btnModificarTrab.setFocusPainted(false);
        btnModificarTrab.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnModificarTrab.setOpaque(true);
        btnModificarTrab.setBorder(new CompoundBorder(new LineBorder(BTN_WARNING_H, 1, true), new EmptyBorder(6, 12, 6, 12)));
        btnModificarTrab.addActionListener(this);
        panelTrabajadores.add(btnModificarTrab);

        btnLimpiarTrab = new JButton("Limpiar");
        btnLimpiarTrab.setBounds(475, 290, 130, 35);
        btnLimpiarTrab.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnLimpiarTrab.setForeground(TEXT_PRIMARY);
        btnLimpiarTrab.setBackground(BTN_NORMAL);
        btnLimpiarTrab.setFocusPainted(false);
        btnLimpiarTrab.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLimpiarTrab.setOpaque(true);
        btnLimpiarTrab.setBorder(new CompoundBorder(new LineBorder(BTN_HOVER, 1, true), new EmptyBorder(6, 12, 6, 12)));
        btnLimpiarTrab.addActionListener(this);
        panelTrabajadores.add(btnLimpiarTrab);

        btnBajaTrab.setEnabled(false);
        btnModificarTrab.setEnabled(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(btnVerClientes)) {
            new MostrarClientes(this, true).setVisible(true);
        } else if (e.getSource().equals(btnVerTrabajadores)) {
            new MostrarTrabajadores(this, true).setVisible(true);
        } else if (e.getSource().equals(btnLimpiarCliente)) {
            limpiarClientes();
        } else if (e.getSource().equals(btnLimpiarTrab)) {
            limpiarTrabajadores();
        } else if (e.getSource().equals(btnAltaCliente)) {
            altaCliente();
        } else if (e.getSource().equals(btnBajaCliente)) {
            bajaCliente();
        } else if (e.getSource().equals(btnModificarCliente)) {
            modificarCliente();
        } else if (e.getSource().equals(btnAltaTrab)) {
            altaTrabajador();
        } else if (e.getSource().equals(btnBajaTrab)) {
            bajaTrabajador();
        } else if (e.getSource().equals(btnModificarTrab)) {
            modificarTrabajador();
        }
    }

    private void altaCliente() {
        if (!validarCliente()) return;
        try {
            validarDni(txtDniC.getText().trim());
            validarTelefono(txtTelefonoC.getText().trim());
            validarEmail(txtCorreoC.getText().trim());

            Cliente c = new Cliente();
            c.setDni(txtDniC.getText().trim());
            c.setNom(txtNombreC.getText().trim());
            c.setApellido(txtApellidoC.getText().trim());
            c.setTelefono(txtTelefonoC.getText().trim());
            c.setCorreo(txtCorreoC.getText().trim());
            c.setDireccion(txtDireccionC.getText().trim());

            Principal.altaCliente(c);
            JOptionPane.showMessageDialog(this, "ALTA CORRECTA!!", "MENSAJE", JOptionPane.INFORMATION_MESSAGE);
            limpiarClientes();
            tabbedPane.setSelectedComponent(panelInicio);

        } catch (DniException e) {
            reproducirSonido("/resources/excepcion.wav");
            e.visualizarMsg();
        } catch (FormatoIncorrectoException e) {
            reproducirSonido("/resources/excepcion.wav");
            e.visualizarMsg();
        } catch (TelefonoException e) {
            reproducirSonido("/resources/excepcion.wav");
            e.visualizarMsg();
        } catch (Exception e) {
            reproducirSonido("/resources/excepcion.wav");
            JOptionPane.showMessageDialog(this, e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void bajaCliente() {
        Cliente c = new Cliente();
        c.setDni(txtDniC.getText().trim());
        try {
            Principal.bajaCliente(c);
            JOptionPane.showMessageDialog(this, "BAJA CORRECTA!!", "MENSAJE", JOptionPane.INFORMATION_MESSAGE);
            limpiarClientes();
            tabbedPane.setSelectedComponent(panelInicio);
        } catch (Exception ex) {
            reproducirSonido("/resources/excepcion.wav");
            JOptionPane.showMessageDialog(this, "Error al dar de baja el cliente: " + ex.getMessage(),
                    "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void modificarCliente() {
        if (!validarCliente()) return;
        try {
            validarTelefono(txtTelefonoC.getText().trim());
            validarEmail(txtCorreoC.getText().trim());
            Cliente c = new Cliente();
            c.setDni(txtDniC.getText().trim());
            c.setNom(txtNombreC.getText().trim());
            c.setApellido(txtApellidoC.getText().trim());
            c.setTelefono(txtTelefonoC.getText().trim());
            c.setCorreo(txtCorreoC.getText().trim());
            c.setDireccion(txtDireccionC.getText().trim());
            Principal.modificarCliente(c);
            JOptionPane.showMessageDialog(this, "MODIFICACIÓN CORRECTA!!", "MENSAJE", JOptionPane.INFORMATION_MESSAGE);
            limpiarClientes();
            tabbedPane.setSelectedComponent(panelInicio);
        } catch (FormatoIncorrectoException e) {
            reproducirSonido("/resources/excepcion.wav");
            e.visualizarMsg();
        } catch (TelefonoException e) {
            reproducirSonido("/resources/excepcion.wav");
            e.visualizarMsg();
        }
    }

    private void limpiarClientes() {
        txtDniC.setText("");
        txtDniC.setEditable(true);
        txtNombreC.setText("");
        txtApellidoC.setText("");
        txtTelefonoC.setText("");
        txtCorreoC.setText("");
        txtDireccionC.setText("");
        btnAltaCliente.setEnabled(true);
        btnBajaCliente.setEnabled(false);
        btnModificarCliente.setEnabled(false);
    }

    private void altaTrabajador() {
        if (!validarTrabajador()) return;
        try {
            validarNss(txtNss.getText().trim());
            validarTelefono(txtTelefonoT.getText().trim());
            validarEmail(txtCorreoT.getText().trim());
            Trabajador t = new Trabajador(
                txtNss.getText().trim(),
                txtNombreT.getText().trim(),
                txtApellidoT.getText().trim(),
                txtTelefonoT.getText().trim(),
                txtCorreoT.getText().trim()
            );
            try {
                String mensaje = Principal.altaTrabajador(t);
                JOptionPane.showMessageDialog(this, mensaje, "MENSAJE", JOptionPane.INFORMATION_MESSAGE);
                if (!mensaje.contains("YA EXISTE")) {
                    limpiarTrabajadores();
                    tabbedPane.setSelectedComponent(panelInicio);
                }
            } catch (Exception ex) {
                reproducirSonido("/resources/excepcion.wav");
                JOptionPane.showMessageDialog(this, "Error al dar de alta el trabajador: " + ex.getMessage(),
                        "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NssException e) {
            reproducirSonido("/resources/excepcion.wav");
            e.visualizarMsg();
        } catch (TelefonoException e) {
            reproducirSonido("/resources/excepcion.wav");
            e.visualizarMsg();
        } catch (FormatoIncorrectoException e) {
            reproducirSonido("/resources/excepcion.wav");
            e.visualizarMsg();
        }
    }

    private void bajaTrabajador() {
        Trabajador t = new Trabajador(txtNss.getText().trim(), "", "", "", "");
        Principal.bajaTrabajador(t);
        JOptionPane.showMessageDialog(this, "BAJA CORRECTA!!", "MENSAJE", JOptionPane.INFORMATION_MESSAGE);
        limpiarTrabajadores();
        tabbedPane.setSelectedComponent(panelInicio);
    }

    private void modificarTrabajador() {
        if (!validarTrabajador()) return;
        try {
            validarTelefono(txtTelefonoT.getText().trim());
            validarEmail(txtCorreoT.getText().trim());
            Trabajador t = new Trabajador(
                txtNss.getText().trim(),
                txtNombreT.getText().trim(),
                txtApellidoT.getText().trim(),
                txtTelefonoT.getText().trim(),
                txtCorreoT.getText().trim()
            );
            Principal.modificarTrabajador(t);
            JOptionPane.showMessageDialog(this, "MODIFICACIÓN CORRECTA!!", "MENSAJE", JOptionPane.INFORMATION_MESSAGE);
            limpiarTrabajadores();
            tabbedPane.setSelectedComponent(panelInicio);
        } catch (FormatoIncorrectoException e) {
            reproducirSonido("/resources/excepcion.wav");
            e.visualizarMsg();
        } catch (TelefonoException e) {
            reproducirSonido("/resources/excepcion.wav");
            e.visualizarMsg();
        }
    }

    private void limpiarTrabajadores() {
        txtNss.setText("");
        txtNss.setEditable(true);
        txtNombreT.setText("");
        txtApellidoT.setText("");
        txtTelefonoT.setText("");
        txtCorreoT.setText("");
        btnAltaTrab.setEnabled(true);
        btnBajaTrab.setEnabled(false);
        btnModificarTrab.setEnabled(false);
    }

    private boolean validarCliente() {
        if (txtDniC.getText().trim().isEmpty() ||
            txtNombreC.getText().trim().isEmpty() ||
            txtApellidoC.getText().trim().isEmpty() ||
            txtTelefonoC.getText().trim().isEmpty() ||
            txtCorreoC.getText().trim().isEmpty() ||
            txtDireccionC.getText().trim().isEmpty()) {
            reproducirSonido("/resources/excepcion.wav");
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "ERROR", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private boolean validarTrabajador() {
        if (txtNss.getText().trim().isEmpty() ||
            txtNombreT.getText().trim().isEmpty() ||
            txtApellidoT.getText().trim().isEmpty() ||
            txtTelefonoT.getText().trim().isEmpty() ||
            txtCorreoT.getText().trim().isEmpty()) {
            reproducirSonido("/resources/excepcion.wav");
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "ERROR", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private void validarDni(String dni) throws DniException {
        if (!dni.matches("\\d{8}[A-Z]")) {
            throw new DniException("Formato de DNI incorrecto (12345678A)");
        }
    }

    public static void validarEmail(String email) throws FormatoIncorrectoException {
        Pattern modelo = Pattern.compile("^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");
        Matcher matcher = modelo.matcher(email);
        if (!matcher.matches()) { throw new FormatoIncorrectoException("Gmail con formato incorrecto"); }
    }

    public static void validarNss(String nss) throws NssException {
        if (!nss.matches("\\d{12}")) {
            throw new NssException("El NSS debe tener exactamente 12 números");
        }
    }

    public static void validarTelefono(String telefono) throws TelefonoException {
        if (!telefono.matches("\\d{9}")) {
            throw new TelefonoException("El teléfono debe tener exactamente 9 números");
        }
    }

    public void cargarDatosCliente(String dni, String nombre, String ape, String tel, String correo, String direccion) {
        tabbedPane.setSelectedComponent(panelClientes);
        txtDniC.setText(dni);
        txtDniC.setEditable(false);
        txtNombreC.setText(nombre);
        txtApellidoC.setText(ape);
        txtTelefonoC.setText(tel);
        txtCorreoC.setText(correo);
        txtDireccionC.setText(direccion);
        btnAltaCliente.setEnabled(false);
        btnBajaCliente.setEnabled(true);
        btnModificarCliente.setEnabled(true);
    }

    public void cargarDatosTrabajador(String nss, String nombre, String ape, String tel, String correo) {
        tabbedPane.setSelectedComponent(panelTrabajadores);
        txtNss.setText(nss);
        txtNss.setEditable(false);
        txtNombreT.setText(nombre);
        txtApellidoT.setText(ape);
        txtTelefonoT.setText(tel);
        txtCorreoT.setText(correo);
        btnAltaTrab.setEnabled(false);
        btnBajaTrab.setEnabled(true);
        btnModificarTrab.setEnabled(true);
    }

    public static void main(String[] args) {
        new Vista_Admin(null).setVisible(true);
    }
}
