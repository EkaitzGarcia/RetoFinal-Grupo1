package vistas;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.net.URL;

import modelo.AccesoBD;
import modelo.Cliente;
import modelo.Producto;
import modelo.Trabajador;
import utiles.convertiro_exportador_de_xml;

public class Vista_Trabajador extends JDialog {

	private static final long serialVersionUID = 1L;

	private static final Color BG_MAIN     = new Color(117, 85, 64);
	private static final Color BG_PANEL    = new Color(245, 240, 235);
	private static final Color ACCENT      = new Color(200, 160, 120);
	private static final Color BTN_GREEN   = new Color(34, 139, 34);
	private static final Color BTN_RED     = new Color(180, 40, 40);
	private static final Color BTN_BLUE    = new Color(50, 100, 180);
	private static final Color TOTAL_COLOR = new Color(0, 100, 0);
	private static final Color ROW_EVEN    = new Color(255, 252, 248);
	private static final Color ROW_ODD     = new Color(240, 230, 220);

	// Eliminado el path hardcodeado para usar Principal.getXmlAutoPath()

	private AccesoBD accesoBD = new AccesoBD();

	private DefaultTableModel modeloTablaCompras;
	private JTable tablaCompras;
	private JTextField txtBuscarIdCompra;

	private JTextField txtDniCliente;
	private JTextField txtNombreCliente;
	private JTextField txtApellidoCliente;
	private JTextField txtTelefonoCliente;
	private JTextField txtCorreoCliente;
	private JTextField txtDireccionCliente;
	private JButton btnModificarCliente;

	private JTextField txtIdCompra;
	private JTextField txtFechaCompra;
	private JTextField txtTotalCompra;
	private JComboBox<String> cmbMetodoPago;
	private JButton btnModificarCompra;
	private JButton btnEliminarCompra;

	private JTextField txtDniAltaCompra;
	private JComboBox<String> cmbMetodoPagoAlta;
	private JButton btnDarAltaCompra;
	private JPanel panelLineas;
	private List<JComboBox<String>> listaComboProductos = new ArrayList<>();
	private List<JSpinner> listaSpinners = new ArrayList<>();
	private Map<String, Producto> mapaProductos = new HashMap<>();
	private JLabel lblTotalActual;

	private DefaultTableModel modeloTablaDetalle;

	private JTextField txtDniAhorro;
	private JButton btnCalcularAhorro;
	private JLabel lblResultadoAhorro;

	private int idCompraSeleccionada = -1;
	private String dniClienteSeleccionado = null;

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			try {
				Vista_Trabajador d = new Vista_Trabajador();
				d.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				d.setVisible(true);
			} catch (Exception e) { e.printStackTrace(); }
		});
	}

	public Vista_Trabajador() {
		setTitle("OKAPI - Worker Panel");
		setSize(1080, 740);
		setLocationRelativeTo(null);
		setModal(true); // Hacerlo modal para asegurar el flujo
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		getContentPane().setBackground(BG_MAIN);
		getContentPane().setLayout(new BorderLayout());

		JLabel titulo = new JLabel("  Worker Panel", SwingConstants.LEFT);
		titulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
		titulo.setForeground(Color.WHITE);
		titulo.setBackground(BG_MAIN);
		titulo.setOpaque(true);
		titulo.setBorder(new EmptyBorder(10, 14, 10, 14));

		JPanel panelNorte = new JPanel(new BorderLayout());
		panelNorte.setBackground(BG_MAIN);
		panelNorte.add(titulo, BorderLayout.WEST);

		// Configurar botón de salida con imagen
		JButton btnSalir = new JButton("Exit");
		URL salirURL = getClass().getResource("/resources/salida.png");
		if (salirURL != null) {
			ImageIcon ic = new ImageIcon(salirURL);
			Image img = ic.getImage().getScaledInstance(18, 18, Image.SCALE_SMOOTH);
			btnSalir.setIcon(new ImageIcon(img));
		}
		btnSalir.setBackground(BTN_RED);
		btnSalir.setForeground(Color.WHITE);
		btnSalir.setFont(new Font("Segoe UI", Font.BOLD, 12));
		btnSalir.setFocusPainted(false);
		btnSalir.setBorder(new EmptyBorder(5, 15, 5, 15));
		btnSalir.addActionListener(e -> {
			reproducirSonido("/resources/salida.wav");
			this.setVisible(false);
			this.dispose();
		});

		JPanel panelBotonSalir = new JPanel(new FlowLayout(FlowLayout.RIGHT, 14, 10));
		panelBotonSalir.setBackground(BG_MAIN);
		panelBotonSalir.add(btnSalir);
		panelNorte.add(panelBotonSalir, BorderLayout.EAST);

		getContentPane().add(panelNorte, BorderLayout.NORTH);

		JTabbedPane tabs = new JTabbedPane();
		tabs.setFont(new Font("Segoe UI", Font.BOLD, 13));
		tabs.addTab("Manage Purchases", crearPanelGestionarCompras());
		tabs.addTab("Make a Purchase",    crearPanelAltaCompra());
		tabs.addTab("Client Savings",    crearPanelAhorro());
		getContentPane().add(tabs, BorderLayout.CENTER);

		cargarTodasLasCompras();
	}

	private JPanel crearPanelGestionarCompras() {
		JPanel panel = new JPanel(new BorderLayout(6, 6));
		panel.setBackground(BG_PANEL);
		panel.setBorder(new EmptyBorder(10, 10, 10, 10));

		JPanel panelBuscar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
		panelBuscar.setBackground(ACCENT);
		panelBuscar.setBorder(BorderFactory.createTitledBorder("Search purchase by ID"));
		txtBuscarIdCompra = new JTextField(8);
		JButton btnBuscar   = crearBoton("Search", BTN_BLUE);
		JButton btnVerTodas = crearBoton("View all", new Color(100, 100, 100));
		panelBuscar.add(new JLabel("Purchase ID:"));
		panelBuscar.add(txtBuscarIdCompra);
		panelBuscar.add(btnBuscar);
		panelBuscar.add(btnVerTodas);
		panel.add(panelBuscar, BorderLayout.NORTH);

		String[] colsCompras = {"ID", "Date", "Total (EUR)", "Payment Method", "DNI", "Name", "Surname"};
		modeloTablaCompras = new DefaultTableModel(colsCompras, 0) {
			public boolean isCellEditable(int r, int c) { return false; }
		};
		tablaCompras = new JTable(modeloTablaCompras) {
			public java.awt.Component prepareRenderer(javax.swing.table.TableCellRenderer r, int row, int col) {
				java.awt.Component c = super.prepareRenderer(r, row, col);
				if (!isRowSelected(row)) c.setBackground(row % 2 == 0 ? ROW_EVEN : ROW_ODD);
				return c;
			}
		};
		tablaCompras.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tablaCompras.getTableHeader().setReorderingAllowed(false);
		tablaCompras.setRowHeight(24);
		tablaCompras.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		tablaCompras.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
		tablaCompras.getTableHeader().setBackground(BG_MAIN);
		tablaCompras.getTableHeader().setForeground(Color.WHITE);

		String[] colsDetalle = {"Product Ref.", "Quantity", "Unit Price", "Discount %", "Subtotal"};
		modeloTablaDetalle = new DefaultTableModel(colsDetalle, 0) {
			public boolean isCellEditable(int r, int c) { return false; }
		};
		JTable tablaDetalle = new JTable(modeloTablaDetalle);
		tablaDetalle.setRowHeight(22);
		tablaDetalle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		tablaDetalle.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
		tablaDetalle.getTableHeader().setBackground(BG_MAIN);
		tablaDetalle.getTableHeader().setForeground(Color.WHITE);

		JScrollPane scrollCompras = new JScrollPane(tablaCompras);
		scrollCompras.setBorder(BorderFactory.createTitledBorder("List of purchases"));
		JScrollPane scrollDetalle = new JScrollPane(tablaDetalle);
		scrollDetalle.setBorder(BorderFactory.createTitledBorder("Products of the selected purchase"));

		JPanel panelTablas = new JPanel(new GridLayout(2, 1, 0, 6));
		panelTablas.setBackground(BG_PANEL);
		panelTablas.add(scrollCompras);
		panelTablas.add(scrollDetalle);
		panel.add(panelTablas, BorderLayout.CENTER);

		JPanel panelFormas = new JPanel(new GridLayout(1, 2, 10, 0));
		panelFormas.setBackground(BG_PANEL);
		panelFormas.add(crearFormModificarCliente());
		panelFormas.add(crearFormModificarCompra());
		panel.add(panelFormas, BorderLayout.SOUTH);

		btnBuscar.addActionListener(e -> buscarCompraId());
		btnVerTodas.addActionListener(e -> cargarTodasLasCompras());
		tablaCompras.getSelectionModel().addListSelectionListener(e -> {
			if (!e.getValueIsAdjusting()) seleccionarFilaTabla();
		});
		btnModificarCliente.addActionListener(e -> modificarCliente());
		btnModificarCompra.addActionListener(e -> modificarCompra());
		btnEliminarCompra.addActionListener(e -> eliminarCompra());

		return panel;
	}

	private JPanel crearFormModificarCliente() {
		JPanel p = new JPanel(new GridBagLayout());
		p.setBackground(BG_PANEL);
		p.setBorder(BorderFactory.createTitledBorder("Modify selected client"));
		GridBagConstraints g = new GridBagConstraints();
		g.insets = new Insets(3, 5, 3, 5);
		g.fill = GridBagConstraints.HORIZONTAL;

		txtDniCliente       = campoReadOnly();
		txtNombreCliente    = new JTextField(15);
		txtApellidoCliente  = new JTextField(15);
		txtTelefonoCliente  = new JTextField(12);
		txtCorreoCliente    = new JTextField(15);
		txtDireccionCliente = new JTextField(15);
		btnModificarCliente = crearBoton("Save changes", BTN_BLUE);

		String[] labels = {"DNI:", "Name:", "Surname:", "Phone:", "Email:", "Address:"};
		JTextField[] fields = {txtDniCliente, txtNombreCliente, txtApellidoCliente,
		                       txtTelefonoCliente, txtCorreoCliente, txtDireccionCliente};
		for (int i = 0; i < labels.length; i++) {
			g.gridx = 0; g.gridy = i; g.weightx = 0; p.add(etiqueta(labels[i]), g);
			g.gridx = 1; g.weightx = 1;               p.add(fields[i], g);
		}
		g.gridx = 0; g.gridy = labels.length; g.gridwidth = 2;
		p.add(btnModificarCliente, g);
		return p;
	}

	private JPanel crearFormModificarCompra() {
		JPanel p = new JPanel(new GridBagLayout());
		p.setBackground(BG_PANEL);
		p.setBorder(BorderFactory.createTitledBorder("Modify / Delete selected purchase"));
		GridBagConstraints g = new GridBagConstraints();
		g.insets = new Insets(3, 5, 3, 5);
		g.fill = GridBagConstraints.HORIZONTAL;

		txtIdCompra    = campoReadOnly();
		txtFechaCompra = new JTextField(12);
		txtTotalCompra = campoReadOnly();
		txtTotalCompra.setFont(new Font("Segoe UI", Font.BOLD, 13));
		txtTotalCompra.setForeground(TOTAL_COLOR);
		cmbMetodoPago      = new JComboBox<>(new String[]{"TARJETA", "EFECTIVO"});
		btnModificarCompra = crearBoton("Save changes", BTN_BLUE);
		btnEliminarCompra  = crearBoton("Delete purchase", BTN_RED);

		String[] labels = {"Purchase ID:", "Date (yyyy-mm-dd):", "Total:", "Payment Method:"};
		Component[] comps = {txtIdCompra, txtFechaCompra, txtTotalCompra, cmbMetodoPago};
		for (int i = 0; i < labels.length; i++) {
			g.gridx = 0; g.gridy = i; g.weightx = 0; p.add(etiqueta(labels[i]), g);
			g.gridx = 1; g.weightx = 1;               p.add(comps[i], g);
		}
		g.gridx = 0; g.gridy = labels.length; g.gridwidth = 2;
		p.add(btnModificarCompra, g);
		g.gridy++;
		p.add(btnEliminarCompra, g);
		return p;
	}

	private JPanel crearPanelAltaCompra() {
		try { accesoBD.getTodosProductos(mapaProductos); }
		catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Error loading products: " + ex.getMessage());
		}

		JPanel panel = new JPanel(new BorderLayout(8, 8));
		panel.setBackground(BG_PANEL);
		panel.setBorder(new EmptyBorder(16, 24, 16, 24));

		JPanel panelCabecera = new JPanel(new GridBagLayout());
		panelCabecera.setBackground(BG_PANEL);
		panelCabecera.setBorder(BorderFactory.createTitledBorder("Purchase data"));
		GridBagConstraints g = new GridBagConstraints();
		g.insets = new Insets(5, 8, 5, 8);
		g.fill = GridBagConstraints.HORIZONTAL;
		txtDniAltaCompra  = new JTextField(14);
		cmbMetodoPagoAlta = new JComboBox<>(new String[]{"TARJETA", "EFECTIVO"});
		g.gridx = 0; g.gridy = 0; g.weightx = 0; panelCabecera.add(etiqueta("Client DNI:"), g);
		g.gridx = 1; g.weightx = 1;               panelCabecera.add(txtDniAltaCompra, g);
		g.gridx = 2; g.weightx = 0;               panelCabecera.add(etiqueta("Payment Method:"), g);
		g.gridx = 3; g.weightx = 1;               panelCabecera.add(cmbMetodoPagoAlta, g);
		panel.add(panelCabecera, BorderLayout.NORTH);

		panelLineas = new JPanel();
		panelLineas.setLayout(new BoxLayout(panelLineas, BoxLayout.Y_AXIS));
		panelLineas.setBackground(BG_PANEL);
		JScrollPane scrollLineas = new JScrollPane(panelLineas);
		scrollLineas.setBorder(BorderFactory.createTitledBorder("Products (you can add several)"));
		scrollLineas.setPreferredSize(new Dimension(0, 230));
		panel.add(scrollLineas, BorderLayout.CENTER);

		JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 14, 8));
		panelBotones.setBackground(BG_PANEL);
		JButton btnAñadir = crearBoton("+ Add product", BTN_BLUE);
		btnDarAltaCompra  = crearBoton("Make purchase", BTN_GREEN);
		btnDarAltaCompra.setFont(btnDarAltaCompra.getFont().deriveFont(Font.BOLD, 14f));

		lblTotalActual = new JLabel("Total: 0.00 EUR");
		lblTotalActual.setFont(new Font("Segoe UI", Font.BOLD, 16));
		lblTotalActual.setForeground(TOTAL_COLOR);

		panelBotones.add(btnAñadir);
		panelBotones.add(btnDarAltaCompra);
		panelBotones.add(new JLabel("    "));
		panelBotones.add(lblTotalActual);
		panel.add(panelBotones, BorderLayout.SOUTH);

		// Inicializar primera línea
		agregarLineaProducto();

		btnAñadir.addActionListener(e -> agregarLineaProducto());
		btnDarAltaCompra.addActionListener(e -> darAltaCompra());
		return panel;
	}

	private void agregarLineaProducto() {
		JPanel fila = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
		fila.setBackground(listaComboProductos.size() % 2 == 0 ? ROW_EVEN : ROW_ODD);
		fila.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));

		JLabel num = new JLabel(String.format("  %d.", listaComboProductos.size() + 1));
		num.setFont(new Font("Segoe UI", Font.BOLD, 13));
		num.setForeground(BG_MAIN);
		num.setPreferredSize(new Dimension(30, 28));

		JComboBox<String> combo = new JComboBox<>();
		combo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		combo.setPreferredSize(new Dimension(200, 28));
		for (String ref : mapaProductos.keySet()) combo.addItem(ref);

		JSpinner spinner = new JSpinner(new SpinnerNumberModel(1, 1, 999, 1));
		spinner.setPreferredSize(new Dimension(65, 28));

		JLabel lblPrecio = new JLabel("", SwingConstants.LEFT);
		lblPrecio.setFont(new Font("Segoe UI", Font.ITALIC, 11));
		lblPrecio.setForeground(new Color(60, 80, 60));
		lblPrecio.setPreferredSize(new Dimension(220, 28));
		actualizarPrecioLabel(combo, spinner, lblPrecio);
		combo.addActionListener(e -> actualizarPrecioLabel(combo, spinner, lblPrecio));
		spinner.addChangeListener(e -> actualizarPrecioLabel(combo, spinner, lblPrecio));

		JButton btnQuitar = new JButton("X");
		btnQuitar.setFont(new Font("Segoe UI", Font.BOLD, 11));
		btnQuitar.setForeground(Color.WHITE);
		btnQuitar.setBackground(BTN_RED);
		btnQuitar.setFocusPainted(false);
		btnQuitar.setPreferredSize(new Dimension(32, 28));
		btnQuitar.addActionListener(e -> {
			if (listaComboProductos.size() <= 1) {
				JOptionPane.showMessageDialog(this, "You must have at least one product."); return;
			}
			listaComboProductos.remove(combo);
			listaSpinners.remove(spinner);
			panelLineas.remove(fila);
			panelLineas.revalidate(); panelLineas.repaint();
			actualizarTotalVenta();
		});

		fila.add(num);
		fila.add(etiqueta("Product:"));
		fila.add(combo);
		fila.add(etiqueta("  Quantity:"));
		fila.add(spinner);
		fila.add(lblPrecio);
		fila.add(btnQuitar);

		listaComboProductos.add(combo);
		listaSpinners.add(spinner);
		panelLineas.add(fila);
		panelLineas.revalidate(); panelLineas.repaint();
		actualizarTotalVenta();
	}

	private void actualizarTotalVenta() {
		if (lblTotalActual == null) return;
		double total = 0;
		for (int i = 0; i < listaComboProductos.size(); i++) {
			String ref = (String) listaComboProductos.get(i).getSelectedItem();
			if (ref != null && mapaProductos.containsKey(ref)) {
				Producto p = mapaProductos.get(ref);
				int cant = (int) listaSpinners.get(i).getValue();
				total += p.getPrecio() * (1 - p.getDescuento() / 100.0) * cant;
			}
		}
		lblTotalActual.setText(String.format("Total: %.2f EUR", total));
	}

	private void actualizarPrecioLabel(JComboBox<String> combo, JSpinner spinner, JLabel lbl) {
		String ref = (String) combo.getSelectedItem();
		if (ref == null || !mapaProductos.containsKey(ref)) { lbl.setText(""); return; }
		Producto p = mapaProductos.get(ref);
		int cant = (int) spinner.getValue();
		double unit = p.getPrecio() * (1 - p.getDescuento() / 100.0);
		lbl.setText(String.format("  %.2f EUR/u  |  Subtotal: %.2f EUR", unit, unit * cant));
		actualizarTotalVenta();
	}

	private JPanel crearPanelAhorro() {
		JPanel panel = new JPanel(new GridBagLayout());
		panel.setBackground(BG_PANEL);
		panel.setBorder(new EmptyBorder(20, 40, 20, 40));

		JPanel inner = new JPanel(new GridBagLayout());
		inner.setBackground(BG_PANEL);
		inner.setBorder(BorderFactory.createTitledBorder("Funcion AhorroCliente(DNI)"));
		GridBagConstraints g = new GridBagConstraints();
		g.insets = new Insets(8, 8, 8, 8);
		g.fill = GridBagConstraints.HORIZONTAL;

		txtDniAhorro       = new JTextField(12);
		btnCalcularAhorro  = crearBoton("Calculate savings", BTN_GREEN);
		lblResultadoAhorro = new JLabel("---", SwingConstants.CENTER);
		lblResultadoAhorro.setFont(new Font("Segoe UI", Font.BOLD, 20));
		lblResultadoAhorro.setForeground(TOTAL_COLOR);

		g.gridy = 0; g.gridwidth = 1; g.weightx = 0; inner.add(etiqueta("Client DNI:"), g);
		g.gridx = 1; g.weightx = 1; inner.add(txtDniAhorro, g);
		g.gridx = 0; g.gridy = 1; g.gridwidth = 2; inner.add(btnCalcularAhorro, g);
		g.gridy = 2; inner.add(etiqueta("Total savings:"), g);
		g.gridy = 3; inner.add(lblResultadoAhorro, g);

		GridBagConstraints outer = new GridBagConstraints();
		outer.anchor = GridBagConstraints.CENTER;
		panel.add(inner, outer);

		btnCalcularAhorro.addActionListener(e -> calcularAhorro());
		return panel;
	}

	private void cargarTodasLasCompras() {
		modeloTablaCompras.setRowCount(0);
		try {
			for (Object[] fila : accesoBD.getTodasLasComprasConCliente())
				modeloTablaCompras.addRow(fila);
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Error loading purchases: " + ex.getMessage(),
				"Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void buscarCompraId() {
		String texto = txtBuscarIdCompra.getText().trim();
		if (texto.isEmpty()) { cargarTodasLasCompras(); return; }
		try {
			int id = Integer.parseInt(texto);
			modeloTablaCompras.setRowCount(0);
			List<Object[]> filas = accesoBD.getCompraConClientePorId(id);
			for (Object[] fila : filas) modeloTablaCompras.addRow(fila);
			if (filas.isEmpty())
				JOptionPane.showMessageDialog(this, "No purchase found with ID " + id);
		} catch (NumberFormatException ex) {
			JOptionPane.showMessageDialog(this, "The ID must be an integer.");
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Error searching: " + ex.getMessage(),
				"Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void seleccionarFilaTabla() {
		int fila = tablaCompras.getSelectedRow();
		if (fila < 0) return;

		idCompraSeleccionada   = (int) modeloTablaCompras.getValueAt(fila, 0);
		dniClienteSeleccionado = (String) modeloTablaCompras.getValueAt(fila, 4);

		txtIdCompra.setText(String.valueOf(idCompraSeleccionada));
		txtFechaCompra.setText(String.valueOf(modeloTablaCompras.getValueAt(fila, 1)));
		Object total = modeloTablaCompras.getValueAt(fila, 2);
		txtTotalCompra.setText(total != null ? String.format("%.2f EUR", ((Number) total).floatValue()) : "---");
		cmbMetodoPago.setSelectedItem(modeloTablaCompras.getValueAt(fila, 3));

		txtDniCliente.setText(dniClienteSeleccionado);
		try {
			Map<String, Cliente> clientes = new HashMap<>();
			accesoBD.getClientePorDni(dniClienteSeleccionado, clientes);
			Cliente c = clientes.get(dniClienteSeleccionado);
			if (c != null) {
				txtNombreCliente.setText(c.getNom());
				txtApellidoCliente.setText(c.getApellido());
				txtTelefonoCliente.setText(c.getTelefono());
				txtCorreoCliente.setText(c.getCorreo());
				txtDireccionCliente.setText(c.getDireccion());
			}
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Error loading client: " + ex.getMessage());
		}

		modeloTablaDetalle.setRowCount(0);
		try {
			for (Object[] p : accesoBD.getProductosDeCompra(idCompraSeleccionada)) {
				float precio    = (float) p[2];
				int descuento   = (int)   p[3];
				int cantidad    = (int)   p[1];
				double unit     = precio * (1 - descuento / 100.0);
				double subtotal = unit * cantidad;
				modeloTablaDetalle.addRow(new Object[]{
					p[0], cantidad,
					String.format("%.2f EUR", precio),
					descuento + "%",
					String.format("%.2f EUR", subtotal)
				});
			}
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Error loading products: " + ex.getMessage());
		}
	}

	private void modificarCliente() {
		if (dniClienteSeleccionado == null) {
			JOptionPane.showMessageDialog(this, "Select a row first."); return;
		}
		try {
				accesoBD.actualizarCliente(new Cliente(
					txtDniCliente.getText().trim(), txtNombreCliente.getText().trim(),
					txtApellidoCliente.getText().trim(), txtTelefonoCliente.getText().trim(),
					txtCorreoCliente.getText().trim(), txtDireccionCliente.getText().trim()
				));
				controlador.Principal.exportarXMLAutomatico();
				JOptionPane.showMessageDialog(this, "Client updated successfully.");
				cargarTodasLasCompras();
			} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Error updating client: " + ex.getMessage(),
				"Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void modificarCompra() {
		if (idCompraSeleccionada < 0) {
			JOptionPane.showMessageDialog(this, "Select a row first."); return;
		}
		try {
				accesoBD.actualizarCompra(idCompraSeleccionada,
					txtFechaCompra.getText().trim(), (String) cmbMetodoPago.getSelectedItem());
				controlador.Principal.exportarXMLAutomatico();
				JOptionPane.showMessageDialog(this, "Purchase updated successfully.");
				cargarTodasLasCompras();
			} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Error updating purchase: " + ex.getMessage(),
				"Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void eliminarCompra() {
		if (idCompraSeleccionada < 0) {
			JOptionPane.showMessageDialog(this, "Select a row first."); return;
		}
		int ok = JOptionPane.showConfirmDialog(this,
			"Are you sure you want to delete purchase " + idCompraSeleccionada + "?",
			"Confirm deletion", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
		if (ok != JOptionPane.YES_OPTION) return;
		try {
				accesoBD.eliminarCompra(idCompraSeleccionada);
				controlador.Principal.exportarXMLAutomatico();
				JOptionPane.showMessageDialog(this, "Purchase deleted successfully.");
				idCompraSeleccionada = -1;
				limpiarFormularios();
				cargarTodasLasCompras();
			} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Error deleting purchase: " + ex.getMessage(),
				"Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void darAltaCompra() {
		String dni    = txtDniAltaCompra.getText().trim();
		String metodo = (String) cmbMetodoPagoAlta.getSelectedItem();
		if (dni.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Introduce client DNI."); return;
		}
		List<Producto> productos  = new ArrayList<>();
		List<Integer>  cantidades = new ArrayList<>();
		for (int i = 0; i < listaComboProductos.size(); i++) {
			String ref = (String) listaComboProductos.get(i).getSelectedItem();
			Producto prod = mapaProductos.get(ref);
			if (prod == null) {
				JOptionPane.showMessageDialog(this, "Product not found: " + ref); return;
			}
			productos.add(prod);
			cantidades.add((int) listaSpinners.get(i).getValue());
		}
			try {
				accesoBD.insertarCompraMultiple(dni, metodo, productos, cantidades);
				controlador.Principal.exportarXMLAutomatico();
				String pathMsg = controlador.Principal.getXmlAutoPath() != null ? 
					"\nXML updated at: " + controlador.Principal.getXmlAutoPath() : "\nXML path not set yet.";
				JOptionPane.showMessageDialog(this,
					"Purchase created with " + productos.size() + " product(s)." + pathMsg);
				txtDniAltaCompra.setText("");
			listaComboProductos.clear(); listaSpinners.clear();
			panelLineas.removeAll(); panelLineas.revalidate(); panelLineas.repaint();
			agregarLineaProducto();
			actualizarTotalVenta();
			cargarTodasLasCompras();
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Error creating purchase: " + ex.getMessage(),
				"Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	// Método exportarXMLAutomatico() eliminado: ahora se usa Principal.exportarXMLAutomatico()

	private void calcularAhorro() {
		String dni = txtDniAhorro.getText().trim();
		if (dni.isEmpty()) { JOptionPane.showMessageDialog(this, "Introduce client DNI."); return; }
		try {
			double ahorro = accesoBD.getAhorroCliente(dni);
			if (ahorro < 0) {
				lblResultadoAhorro.setText("Client not found");
				lblResultadoAhorro.setForeground(Color.RED);
			} else {
				lblResultadoAhorro.setText(String.format("%.2f EUR", ahorro));
				lblResultadoAhorro.setForeground(TOTAL_COLOR);
			}
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private JTextField campoReadOnly() {
		JTextField tf = new JTextField(12);
		tf.setEditable(false);
		tf.setBackground(new Color(228, 225, 215));
		tf.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		return tf;
	}

	private JLabel etiqueta(String texto) {
		JLabel lbl = new JLabel(texto);
		lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
		lbl.setForeground(new Color(60, 40, 20));
		return lbl;
	}

	private JButton crearBoton(String texto, Color fondo) {
		JButton btn = new JButton(texto);
		btn.setBackground(fondo);
		btn.setForeground(Color.WHITE);
		btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
		btn.setFocusPainted(false);
		btn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
		btn.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(fondo.darker(), 1, true),
			BorderFactory.createEmptyBorder(6, 14, 6, 14)));
		btn.setOpaque(true);
		return btn;
	}

	private void limpiarFormularios() {
		txtDniCliente.setText(""); txtNombreCliente.setText("");
		txtApellidoCliente.setText(""); txtTelefonoCliente.setText("");
		txtCorreoCliente.setText(""); txtDireccionCliente.setText("");
		txtIdCompra.setText(""); txtFechaCompra.setText("");
		txtTotalCompra.setText(""); dniClienteSeleccionado = null;
		modeloTablaDetalle.setRowCount(0);
	}

	private static void reproducirSonido(String recurso) {
		new Thread(() -> {
			try {
				URL url = Vista_Trabajador.class.getResource(recurso);
				if (url == null) return;
				AudioInputStream ais = AudioSystem.getAudioInputStream(url);
				Clip clip = AudioSystem.getClip();
				clip.open(ais);
				clip.start();
				long durationMs = clip.getMicrosecondLength() / 1000;
				Thread.sleep(Math.min(durationMs, 5500));
				clip.stop();
				clip.close();
			} catch (Exception ex) { }
		}).start();
	}
}
