package vistas;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
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
import javax.swing.border.MatteBorder;

import controlador.Principal;
import modelo.Cliente;
import modelo.Trabajador;

public class Vista_Admin extends JDialog implements ActionListener {

    private static final long serialVersionUID = 1L;

    // ── Paleta de colores (igual que Ventana_principal1)
    private static final Color BG_MAIN      = new Color(117, 85, 64);
    private static final Color BG_PANEL     = new Color(101, 72, 54);
    private static final Color BTN_NORMAL   = new Color(126, 129, 115);
    private static final Color BTN_HOVER    = new Color(255, 192, 203);
    private static final Color BTN_BORDER   = new Color(255, 192, 203);
    private static final Color BTN_DANGER   = new Color(160, 60, 60);
    private static final Color BTN_DANGER_H = new Color(200, 80, 80);
    private static final Color BTN_SUCCESS  = new Color(60, 120, 80);
    private static final Color BTN_SUCCESS_H= new Color(80, 160, 100);
    private static final Color TEXT_PRIMARY = new Color(248, 250, 252);
    private static final Color FIELD_BG     = new Color(90, 63, 47);
    private static final Color FIELD_BORDER = new Color(180, 140, 110);
    private static final Color BTN_WARNING   = new Color(160, 120, 0);
    private static final Color BTN_WARNING_H = new Color(210, 160, 0);

    // ── Componentes
    private JTabbedPane tabbedPane;
    private JPanel panelInicio, panelClientes, panelTrabajadores;

    private JButton btnAltaCliente, btnBajaCliente, btnModificarCliente, btnLimpiarCliente;
    private JTextField txtDniC, txtNombreC, txtApellidoC, txtTelefonoC, txtCorreoC;

    private JButton btnAltaTrab, btnBajaTrab, btnModificarTrab, btnLimpiarTrab;
    private JTextField txtNss, txtNombreT, txtApellidoT, txtTelefonoT, txtCorreoT;

    private JButton btnVerClientes, btnVerTrabajadores;

    public Vista_Admin() {
        setTitle("Panel Administrador");
        setBounds(100, 100, 700, 480);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setModal(true);
        setResizable(false);
        setLocationRelativeTo(null);

        // ── TabbedPane con colores
        tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(BG_MAIN);
        tabbedPane.setForeground(TEXT_PRIMARY);
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 13));
        getContentPane().setBackground(BG_MAIN);
        getContentPane().add(tabbedPane, BorderLayout.CENTER);

        crearInicio();
        crearClientes();
        crearTrabajadores();
    }

    // =========================================================================
    // PANELES
    // =========================================================================

    /** Construye el panel de inicio. */
    private void crearInicio() {
        panelInicio = new JPanel();
        panelInicio.setBackground(BG_MAIN);
        panelInicio.setLayout(new BoxLayout(panelInicio, BoxLayout.Y_AXIS));
        panelInicio.setBorder(new EmptyBorder(50, 100, 50, 100));

        JLabel lblTitulo = new JLabel("¿Qué deseas gestionar?", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(TEXT_PRIMARY);
        lblTitulo.setAlignmentX(CENTER_ALIGNMENT);
        panelInicio.add(lblTitulo);

        panelInicio.add(Box.createVerticalStrut(30));

        btnVerClientes = crearBotonEstilizado("👤  Clientes", BTN_NORMAL, BTN_HOVER);
        btnVerClientes.setAlignmentX(CENTER_ALIGNMENT);
        btnVerClientes.addActionListener(this);
        panelInicio.add(btnVerClientes);

        panelInicio.add(Box.createVerticalStrut(16));

        btnVerTrabajadores = crearBotonEstilizado("👷  Trabajadores", BTN_NORMAL, BTN_HOVER);
        btnVerTrabajadores.setAlignmentX(CENTER_ALIGNMENT);
        btnVerTrabajadores.addActionListener(this);
        panelInicio.add(btnVerTrabajadores);

        tabbedPane.addTab("Inicio", panelInicio);
    }

    /** Construye el panel de gestión de clientes. */
    private void crearClientes() {
        panelClientes = new JPanel();
        panelClientes.setBackground(BG_MAIN);
        panelClientes.setLayout(null);

        // ── Labels y campos
        crearLabelEnPanel(panelClientes, "DNI:",       50,  40);
        crearLabelEnPanel(panelClientes, "Nombre:",    50,  80);
        crearLabelEnPanel(panelClientes, "Apellido:",  50, 120);
        crearLabelEnPanel(panelClientes, "Teléfono:",  50, 160);
        crearLabelEnPanel(panelClientes, "Correo:",    50, 200);

        txtDniC      = crearCampo(panelClientes, 210,  40);
        txtNombreC   = crearCampo(panelClientes, 210,  80);
        txtApellidoC = crearCampo(panelClientes, 210, 120);
        txtTelefonoC = crearCampo(panelClientes, 210, 160);
        txtCorreoC   = crearCampo(panelClientes, 210, 200);

        // ── Botones
        btnAltaCliente      = crearBotonPanel("Alta",      BTN_SUCCESS,  BTN_SUCCESS_H);
        btnBajaCliente      = crearBotonPanel("Baja",      BTN_DANGER,   BTN_DANGER_H);
        btnModificarCliente = crearBotonPanel("Modificar", BTN_WARNING, BTN_WARNING_H);
        btnLimpiarCliente   = crearBotonPanel("Limpiar",   BTN_NORMAL,   BTN_HOVER);

        btnAltaCliente.setBounds(40,  290, 130, 35);
        btnBajaCliente.setBounds(185, 290, 130, 35);
        btnModificarCliente.setBounds(330, 290, 130, 35);
        btnLimpiarCliente.setBounds(475, 290, 130, 35);

        btnAltaCliente.addActionListener(this);
        btnBajaCliente.addActionListener(this);
        btnModificarCliente.addActionListener(this);
        btnLimpiarCliente.addActionListener(this);

        panelClientes.add(btnAltaCliente);
        panelClientes.add(btnBajaCliente);
        panelClientes.add(btnModificarCliente);
        panelClientes.add(btnLimpiarCliente);

        btnBajaCliente.setEnabled(false);
        btnModificarCliente.setEnabled(false);

        tabbedPane.addTab("Clientes", panelClientes);
    }

    /** Construye el panel de gestión de trabajadores. */
    private void crearTrabajadores() {
        panelTrabajadores = new JPanel();
        panelTrabajadores.setBackground(BG_MAIN);
        panelTrabajadores.setLayout(null);

        crearLabelEnPanel(panelTrabajadores, "NSS:",      50,  40);
        crearLabelEnPanel(panelTrabajadores, "Nombre:",   50,  80);
        crearLabelEnPanel(panelTrabajadores, "Apellido:", 50, 120);
        crearLabelEnPanel(panelTrabajadores, "Teléfono:", 50, 160);
        crearLabelEnPanel(panelTrabajadores, "Correo:",   50, 200);

        txtNss       = crearCampo(panelTrabajadores, 210,  40);
        txtNombreT   = crearCampo(panelTrabajadores, 210,  80);
        txtApellidoT = crearCampo(panelTrabajadores, 210, 120);
        txtTelefonoT = crearCampo(panelTrabajadores, 210, 160);
        txtCorreoT   = crearCampo(panelTrabajadores, 210, 200);

        btnAltaTrab      = crearBotonPanel("Alta",      BTN_SUCCESS, BTN_SUCCESS_H);
        btnBajaTrab      = crearBotonPanel("Baja",      BTN_DANGER,  BTN_DANGER_H);
        btnModificarTrab = crearBotonPanel("Modificar", BTN_WARNING, BTN_WARNING_H);
        btnLimpiarTrab   = crearBotonPanel("Limpiar",   BTN_NORMAL,  BTN_HOVER);

        btnAltaTrab.setBounds(40,  290, 130, 35);
        btnBajaTrab.setBounds(185, 290, 130, 35);
        btnModificarTrab.setBounds(330, 290, 130, 35);
        btnLimpiarTrab.setBounds(475, 290, 130, 35);

        btnAltaTrab.addActionListener(this);
        btnBajaTrab.addActionListener(this);
        btnModificarTrab.addActionListener(this);
        btnLimpiarTrab.addActionListener(this);

        panelTrabajadores.add(btnAltaTrab);
        panelTrabajadores.add(btnBajaTrab);
        panelTrabajadores.add(btnModificarTrab);
        panelTrabajadores.add(btnLimpiarTrab);

        btnBajaTrab.setEnabled(false);
        btnModificarTrab.setEnabled(false);

        tabbedPane.addTab("Trabajadores", panelTrabajadores);
    }

    // =========================================================================
    // MÉTODOS AUXILIARES DE CONSTRUCCIÓN (solo crean, no añaden al panel)
    // =========================================================================

    /**
     * Crea y añade un JLabel estilizado directamente sobre el panel dado.
     *
     * @param panel Panel donde se añade
     * @param texto Texto del label
     * @param x     Posición horizontal
     * @param y     Posición vertical
     */
    private void crearLabelEnPanel(JPanel panel, String texto, int x, int y) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(TEXT_PRIMARY);
        label.setBounds(x, y, 140, 28);
        panel.add(label);
    }

    /**
     * Crea un JTextField estilizado y lo añade al panel dado.
     *
     * @param panel Panel donde se añade
     * @param x     Posición horizontal
     * @param y     Posición vertical
     * @return JTextField configurado
     */
    private JTextField crearCampo(JPanel panel, int x, int y) {
        JTextField field = new JTextField(10);
        field.setBounds(x, y, 200, 28);
        field.setBackground(FIELD_BG);
        field.setForeground(TEXT_PRIMARY);
        field.setCaretColor(TEXT_PRIMARY);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setBorder(new CompoundBorder(
                new LineBorder(FIELD_BORDER, 1, true),
                new EmptyBorder(2, 8, 2, 8)));
        panel.add(field);
        return field;
    }

    /**
     * Crea un JButton estilizado con efecto hover para usar en paneles con layout null.
     *
     * @param texto      Texto del botón
     * @param bgNormal   Color de fondo normal
     * @param bgHover    Color de fondo al pasar el ratón
     * @return JButton configurado
     */
    private JButton crearBotonPanel(String texto, Color bgNormal, Color bgHover) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(TEXT_PRIMARY);
        btn.setBackground(bgNormal);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        btn.setBorder(new CompoundBorder(
                new LineBorder(bgHover, 1, true),
                new EmptyBorder(6, 12, 6, 12)));
        btn.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { btn.setBackground(bgHover); }
            @Override public void mouseExited(MouseEvent e)  { btn.setBackground(bgNormal); }
        });
        return btn;
    }

    /**
     * Crea un JButton grande estilizado con BoxLayout para el panel de inicio.
     *
     * @param texto    Texto del botón
     * @param bgNormal Color de fondo normal
     * @param bgHover  Color de fondo al pasar el ratón
     * @return JButton configurado
     */
    private JButton crearBotonEstilizado(String texto, Color bgNormal, Color bgHover) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI Emoji", Font.BOLD, 14));
        btn.setForeground(TEXT_PRIMARY);
        btn.setBackground(bgNormal);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        btn.setMaximumSize(new Dimension(260, 50));
        btn.setPreferredSize(new Dimension(260, 50));
        btn.setBorder(new CompoundBorder(
                new LineBorder(BTN_BORDER, 1, true),
                new EmptyBorder(10, 24, 10, 24)));
        btn.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) {
                btn.setBackground(bgHover);
                btn.setBorder(new CompoundBorder(
                        new LineBorder(TEXT_PRIMARY, 1, true),
                        new EmptyBorder(10, 24, 10, 24)));
            }
            @Override public void mouseExited(MouseEvent e) {
                btn.setBackground(bgNormal);
                btn.setBorder(new CompoundBorder(
                        new LineBorder(BTN_BORDER, 1, true),
                        new EmptyBorder(10, 24, 10, 24)));
            }
        });
        return btn;
    }

    // =========================================================================
    // EVENTOS
    // =========================================================================

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

    // =========================================================================
    // GESTIÓN CLIENTES
    // =========================================================================

    /** Da de alta un nuevo cliente en el sistema. */
    private void altaCliente() {
        Cliente c = new Cliente();
        c.setDni(txtDniC.getText().trim());
        c.setNom(txtNombreC.getText().trim());
        c.setApellido(txtApellidoC.getText().trim());
        c.setTelefono(txtTelefonoC.getText().trim());
        c.setCorreo(txtCorreoC.getText().trim());
        c.setDireccion("");
        Principal.altaCliente(c);
        JOptionPane.showMessageDialog(this, "ALTA CORRECTA!!", "MENSAJE", JOptionPane.INFORMATION_MESSAGE);
        limpiarClientes();
        tabbedPane.setSelectedComponent(panelInicio);
    }

    /** Da de baja el cliente cargado en el formulario. */
    private void bajaCliente() {
        Cliente c = new Cliente();
        c.setDni(txtDniC.getText().trim());
        try {
            Principal.bajaCliente(c);
            JOptionPane.showMessageDialog(this, "BAJA CORRECTA!!", "MENSAJE", JOptionPane.INFORMATION_MESSAGE);
            limpiarClientes();
            tabbedPane.setSelectedComponent(panelInicio);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al dar de baja el cliente: " + e.getMessage(),
                    "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    /** Modifica los datos del cliente cargado en el formulario. */
    private void modificarCliente() {
        Cliente c = new Cliente();
        c.setDni(txtDniC.getText().trim());
        c.setNom(txtNombreC.getText().trim());
        c.setApellido(txtApellidoC.getText().trim());
        c.setTelefono(txtTelefonoC.getText().trim());
        c.setCorreo(txtCorreoC.getText().trim());
        c.setDireccion("");
        Principal.modificarCliente(c);
        JOptionPane.showMessageDialog(this, "MODIFICACIÓN CORRECTA!!", "MENSAJE", JOptionPane.INFORMATION_MESSAGE);
        limpiarClientes();
        tabbedPane.setSelectedComponent(panelInicio);
    }

    /**
     * Limpia los campos del formulario de clientes y restablece los botones.
     */
    private void limpiarClientes() {
        txtDniC.setText("");
        txtDniC.setEditable(true);
        txtNombreC.setText("");
        txtApellidoC.setText("");
        txtTelefonoC.setText("");
        txtCorreoC.setText("");
        btnAltaCliente.setEnabled(true);
        btnBajaCliente.setEnabled(false);
        btnModificarCliente.setEnabled(false);
    }

    // =========================================================================
    // GESTIÓN TRABAJADORES
    // =========================================================================

    /** Da de alta un nuevo trabajador usando el procedimiento almacenado. */
    private void altaTrabajador() {
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
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al dar de alta el trabajador: " + e.getMessage(),
                    "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    /** Da de baja el trabajador cargado en el formulario. */
    private void bajaTrabajador() {
        Trabajador t = new Trabajador(txtNss.getText().trim(), "", "", "", "");
        Principal.bajaTrabajador(t);
        JOptionPane.showMessageDialog(this, "BAJA CORRECTA!!", "MENSAJE", JOptionPane.INFORMATION_MESSAGE);
        limpiarTrabajadores();
        tabbedPane.setSelectedComponent(panelInicio);
    }

    /** Modifica los datos del trabajador cargado en el formulario. */
    private void modificarTrabajador() {
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
    }

    /**
     * Limpia los campos del formulario de trabajadores y restablece los botones.
     */
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

    // =========================================================================
    // CARGAR DATOS
    // =========================================================================

    /**
     * Carga los datos de un cliente en el formulario para su gestión.
     *
     * @param dni    DNI del cliente
     * @param nombre Nombre del cliente
     * @param ape    Apellido del cliente
     * @param tel    Teléfono del cliente
     * @param correo Correo del cliente
     */
    public void cargarDatosCliente(String dni, String nombre, String ape, String tel, String correo) {
        tabbedPane.setSelectedComponent(panelClientes);
        txtDniC.setText(dni);
        txtDniC.setEditable(false);
        txtNombreC.setText(nombre);
        txtApellidoC.setText(ape);
        txtTelefonoC.setText(tel);
        txtCorreoC.setText(correo);
        btnAltaCliente.setEnabled(false);
        btnBajaCliente.setEnabled(true);
        btnModificarCliente.setEnabled(true);
    }

    /**
     * Carga los datos de un trabajador en el formulario para su gestión.
     *
     * @param nss    NSS del trabajador
     * @param nombre Nombre del trabajador
     * @param ape    Apellido del trabajador
     * @param tel    Teléfono del trabajador
     * @param correo Correo del trabajador
     */
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
        new Vista_Admin().setVisible(true);
    }
}