package vistas;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
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

import modelo.AccesoBD;
import modelo.Cliente;
import modelo.Producto;

public class Vista_Trabajador extends JDialog {

	private static final long serialVersionUID = 1L;

	private AccesoBD accesoBD = new AccesoBD();

	private DefaultTableModel modeloTablaCompras;
	private JTable tablaCompras;

	private JTextField txtBuscarIdCompra;
	private JButton btnBuscarCompra;

	private JTextField txtDniCliente;
	private JTextField txtNombreCliente;
	private JTextField txtApellidoCliente;
	private JTextField txtTelefonoCliente;
	private JTextField txtCorreoCliente;
	private JTextField txtDireccionCliente;
	private JButton btnModificarCliente;

	private JTextField txtIdCompra;
	private JTextField txtFechaCompra;
	private JComboBox<String> cmbMetodoPago;
	private JButton btnModificarCompra;
	private JButton btnEliminarCompra;

	private JTextField txtDniAltaCompra;
	private JComboBox<String> cmbProductoAlta;
	private JSpinner spnCantidad;
	private JComboBox<String> cmbMetodoPagoAlta;
	private JButton btnDarAltaCompra;

	private JTextField txtDniAhorro;
	private JButton btnCalcularAhorro;
	private JLabel lblResultadoAhorro;

	private int idCompraSeleccionada = -1;
	private String dniClienteSeleccionado = null;

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			try {
				Vista_Trabajador dialog = new Vista_Trabajador();
				dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				dialog.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	public Vista_Trabajador() {
		setTitle("Panel de Trabajador - OKAPI");
		setSize(1000, 700);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout(5, 5));

		JTabbedPane tabs = new JTabbedPane();
		tabs.addTab("1. Gestionar Compras", crearPanelGestionarCompras());
		tabs.addTab("2. Alta de Compra",   crearPanelAltaCompra());
		tabs.addTab("3. Ahorro Cliente", crearPanelAhorro());

		getContentPane().add(tabs, BorderLayout.CENTER);

		cargarTodasLasCompras();
	}

	private JPanel crearPanelGestionarCompras() {
		JPanel panel = new JPanel(new BorderLayout(5, 5));
		panel.setBorder(new EmptyBorder(10, 10, 10, 10));

		JPanel panelBuscar = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panelBuscar.setBorder(BorderFactory.createTitledBorder("Buscar compra por ID"));

		txtBuscarIdCompra = new JTextField(8);
		btnBuscarCompra   = new JButton("Buscar");
		JButton btnVerTodas = new JButton("Ver todas");

		panelBuscar.add(new JLabel("ID compra:"));
		panelBuscar.add(txtBuscarIdCompra);
		panelBuscar.add(btnBuscarCompra);
		panelBuscar.add(btnVerTodas);

		panel.add(panelBuscar, BorderLayout.NORTH);

		String[] columnas = {"ID Compra", "Fecha", "Total (€)", "Método Pago",
		                     "DNI Cliente", "Nombre", "Apellido"};
		modeloTablaCompras = new DefaultTableModel(columnas, 0) {
			public boolean isCellEditable(int r, int c) { return false; }
		};
		tablaCompras = new JTable(modeloTablaCompras);
		tablaCompras.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tablaCompras.getTableHeader().setReorderingAllowed(false);
		tablaCompras.setRowHeight(22);

		JScrollPane scroll = new JScrollPane(tablaCompras);
		scroll.setBorder(BorderFactory.createTitledBorder("Listado de compras"));
		panel.add(scroll, BorderLayout.CENTER);

		JPanel panelFormas = new JPanel(new GridLayout(1, 2, 8, 0));
		panelFormas.add(crearFormModificarCliente());
		panelFormas.add(crearFormModificarCompra());
		panel.add(panelFormas, BorderLayout.SOUTH);

		btnBuscarCompra.addActionListener(e -> buscarCompraId());
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
		p.setBorder(BorderFactory.createTitledBorder("Modificar Cliente seleccionado"));
		GridBagConstraints g = new GridBagConstraints();
		g.insets = new Insets(3, 5, 3, 5);
		g.fill = GridBagConstraints.HORIZONTAL;

		txtDniCliente       = campoReadOnly();
		txtNombreCliente    = new JTextField(15);
		txtApellidoCliente  = new JTextField(15);
		txtTelefonoCliente  = new JTextField(12);
		txtCorreoCliente    = new JTextField(15);
		txtDireccionCliente = new JTextField(15);
		btnModificarCliente = new JButton("Guardar cambios cliente");

		String[] labels = {"DNI:", "Nombre:", "Apellido:", "Teléfono:", "Correo:", "Dirección:"};
		JTextField[] fields = {txtDniCliente, txtNombreCliente, txtApellidoCliente,
		                       txtTelefonoCliente, txtCorreoCliente, txtDireccionCliente};

		for (int i = 0; i < labels.length; i++) {
			g.gridx = 0; g.gridy = i; g.weightx = 0;
			p.add(new JLabel(labels[i]), g);
			g.gridx = 1; g.weightx = 1;
			p.add(fields[i], g);
		}
		g.gridx = 0; g.gridy = labels.length; g.gridwidth = 2;
		p.add(btnModificarCliente, g);
		return p;
	}

	private JPanel crearFormModificarCompra() {
		JPanel p = new JPanel(new GridBagLayout());
		p.setBorder(BorderFactory.createTitledBorder("Modificar / Eliminar Compra seleccionada"));
		GridBagConstraints g = new GridBagConstraints();
		g.insets = new Insets(3, 5, 3, 5);
		g.fill = GridBagConstraints.HORIZONTAL;

		txtIdCompra    = campoReadOnly();
		txtFechaCompra = new JTextField(12);
		cmbMetodoPago  = new JComboBox<>(new String[]{"TARJETA", "EFECTIVO"});
		btnModificarCompra = new JButton("Guardar cambios compra");
		btnEliminarCompra  = new JButton("Eliminar compra");
		btnEliminarCompra.setForeground(Color.RED);

		String[] labels = {"ID Compra:", "Fecha (yyyy-mm-dd):", "Método pago:"};
		Component[] comps = {txtIdCompra, txtFechaCompra, cmbMetodoPago};

		for (int i = 0; i < labels.length; i++) {
			g.gridx = 0; g.gridy = i; g.weightx = 0;
			p.add(new JLabel(labels[i]), g);
			g.gridx = 1; g.weightx = 1;
			p.add(comps[i], g);
		}
		g.gridx = 0; g.gridy = labels.length; g.gridwidth = 2;
		p.add(btnModificarCompra, g);
		g.gridy++;
		p.add(btnEliminarCompra, g);
		return p;
	}

	
	private JPanel crearPanelAltaCompra() {
		JPanel panel = new JPanel(new GridBagLayout());
		panel.setBorder(new EmptyBorder(20, 40, 20, 40));
		GridBagConstraints g = new GridBagConstraints();
		g.insets = new Insets(6, 8, 6, 8);
		g.fill = GridBagConstraints.HORIZONTAL;

		txtDniAltaCompra   = new JTextField(12);
		cmbMetodoPagoAlta  = new JComboBox<>(new String[]{"TARJETA", "EFECTIVO"});
		cmbProductoAlta    = new JComboBox<>();
		spnCantidad        = new JSpinner(new SpinnerNumberModel(1, 1, 999, 1));
		btnDarAltaCompra   = new JButton("Dar de alta compra");
		btnDarAltaCompra.setBackground(new Color(34, 139, 34));
		btnDarAltaCompra.setForeground(Color.WHITE);
		btnDarAltaCompra.setFont(btnDarAltaCompra.getFont().deriveFont(Font.BOLD));

		try {
			Map<String, Producto> prods = new HashMap<>();
			accesoBD.getTodosProductos(prods);
			for (Producto pr : prods.values())
				cmbProductoAlta.addItem(pr.getRef_producto());
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Error al cargar productos: " + ex.getMessage());
		}

		String[] labels = {"DNI del cliente:", "Producto (REF):", "Cantidad:", "Método de pago:"};
		Component[] comps = {txtDniAltaCompra, cmbProductoAlta, spnCantidad, cmbMetodoPagoAlta};

		JPanel form = new JPanel(new GridBagLayout());
		form.setBorder(BorderFactory.createTitledBorder("Nueva compra"));
		for (int i = 0; i < labels.length; i++) {
			g.gridx = 0; g.gridy = i; g.weightx = 0;
			form.add(new JLabel(labels[i]), g);
			g.gridx = 1; g.weightx = 1;
			form.add(comps[i], g);
		}
		g.gridx = 0; g.gridy = labels.length; g.gridwidth = 2;
		form.add(btnDarAltaCompra, g);

		GridBagConstraints outer = new GridBagConstraints();
		outer.gridx = 0; outer.gridy = 0; outer.weightx = 1; outer.weighty = 1;
		outer.fill = GridBagConstraints.NONE;
		outer.anchor = GridBagConstraints.CENTER;
		panel.add(form, outer);

		btnDarAltaCompra.addActionListener(e -> darAltaCompra());
		return panel;
	}

	private JPanel crearPanelAhorro() {
		JPanel panel = new JPanel(new GridBagLayout());
		panel.setBorder(new EmptyBorder(20, 40, 20, 40));

		JPanel inner = new JPanel(new GridBagLayout());
		inner.setBorder(BorderFactory.createTitledBorder("Función AhorroCliente(DNI)"));
		GridBagConstraints g = new GridBagConstraints();
		g.insets = new Insets(8, 8, 8, 8);
		g.fill = GridBagConstraints.HORIZONTAL;

		txtDniAhorro       = new JTextField(12);
		btnCalcularAhorro  = new JButton("Calcular ahorro");
		lblResultadoAhorro = new JLabel("—", SwingConstants.CENTER);
		lblResultadoAhorro.setFont(lblResultadoAhorro.getFont().deriveFont(Font.BOLD, 16f));
		lblResultadoAhorro.setForeground(new Color(0, 100, 0));

		g.gridy = 1; g.gridwidth = 1; g.weightx = 0;
		inner.add(new JLabel("DNI del cliente:"), g);
		g.gridx = 1; g.weightx = 1;
		inner.add(txtDniAhorro, g);
		g.gridx = 0; g.gridy = 2; g.gridwidth = 2;
		inner.add(btnCalcularAhorro, g);
		g.gridy = 3;
		inner.add(new JLabel("Ahorro total:"), g);
		g.gridy = 4;
		inner.add(lblResultadoAhorro, g);

		GridBagConstraints outer = new GridBagConstraints();
		outer.gridx = 0; outer.gridy = 0; outer.fill = GridBagConstraints.NONE;
		outer.anchor = GridBagConstraints.CENTER;
		panel.add(inner, outer);

		btnCalcularAhorro.addActionListener(e -> calcularAhorro());
		return panel;
	}

	private void cargarTodasLasCompras() {
		modeloTablaCompras.setRowCount(0);
		try {
			List<Object[]> filas = accesoBD.getTodasLasComprasConCliente();
			for (Object[] fila : filas)
				modeloTablaCompras.addRow(fila);
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this,
				"Error al cargar compras: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void buscarCompraId() {
		String texto = txtBuscarIdCompra.getText().trim();
		if (texto.isEmpty()) { cargarTodasLasCompras(); return; }
		try {
			int id = Integer.parseInt(texto);
			modeloTablaCompras.setRowCount(0);
			List<Object[]> filas = accesoBD.getCompraConClientePorId(id);
			for (Object[] fila : filas)
				modeloTablaCompras.addRow(fila);
			if (filas.isEmpty())
				JOptionPane.showMessageDialog(this, "No se encontró ninguna compra con ID " + id);
		} catch (NumberFormatException ex) {
			JOptionPane.showMessageDialog(this, "El ID debe ser un número entero.");
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this,
				"Error al buscar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void seleccionarFilaTabla() {
		int fila = tablaCompras.getSelectedRow();
		if (fila < 0) return;

		idCompraSeleccionada   = (int) modeloTablaCompras.getValueAt(fila, 0);
		dniClienteSeleccionado = (String) modeloTablaCompras.getValueAt(fila, 4);

		txtIdCompra.setText(String.valueOf(idCompraSeleccionada));
		txtFechaCompra.setText(String.valueOf(modeloTablaCompras.getValueAt(fila, 1)));
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
			JOptionPane.showMessageDialog(this, "Error al cargar cliente: " + ex.getMessage());
		}
	}

	private void modificarCliente() {
		if (dniClienteSeleccionado == null) {
			JOptionPane.showMessageDialog(this, "Selecciona primero una fila de la tabla.");
			return;
		}
		try {
			Cliente c = new Cliente(
				txtDniCliente.getText().trim(),
				txtNombreCliente.getText().trim(),
				txtApellidoCliente.getText().trim(),
				txtTelefonoCliente.getText().trim(),
				txtCorreoCliente.getText().trim(),
				txtDireccionCliente.getText().trim()
			);
			accesoBD.actualizarCliente(c);
			JOptionPane.showMessageDialog(this, "Cliente actualizado correctamente.");
			cargarTodasLasCompras();
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this,
				"Error al modificar cliente: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void modificarCompra() {
		if (idCompraSeleccionada < 0) {
			JOptionPane.showMessageDialog(this, "Selecciona primero una fila de la tabla.");
			return;
		}
		try {
			String fecha   = txtFechaCompra.getText().trim();
			String metodo  = (String) cmbMetodoPago.getSelectedItem();
			accesoBD.actualizarCompra(idCompraSeleccionada, fecha, metodo);
			JOptionPane.showMessageDialog(this, "Compra actualizada correctamente.");
			cargarTodasLasCompras();
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this,
				"Error al modificar compra: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void eliminarCompra() {
		if (idCompraSeleccionada < 0) {
			JOptionPane.showMessageDialog(this, "Selecciona primero una fila de la tabla.");
			return;
		}
		int confirm = JOptionPane.showConfirmDialog(this,
			"¿Seguro que quieres eliminar la compra " + idCompraSeleccionada + "?",
			"Confirmar eliminación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
		if (confirm != JOptionPane.YES_OPTION) return;
		try {
			accesoBD.eliminarCompra(idCompraSeleccionada);
			JOptionPane.showMessageDialog(this, "Compra eliminada correctamente.");
			idCompraSeleccionada = -1;
			limpiarFormularios();
			cargarTodasLasCompras();
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this,
				"Error al eliminar compra: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void darAltaCompra() {
		String dni    = txtDniAltaCompra.getText().trim();
		String refProd = (String) cmbProductoAlta.getSelectedItem();
		int cantidad  = (int) spnCantidad.getValue();
		String metodo = (String) cmbMetodoPagoAlta.getSelectedItem();

		if (dni.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Introduce el DNI del cliente.");
			return;
		}
		try {
			Map<String, Producto> prods = new HashMap<>();
			accesoBD.getTodosProductos(prods);
			Producto prod = prods.get(refProd);
			if (prod == null) {
				JOptionPane.showMessageDialog(this, "Producto no encontrado.");
				return;
			}
			accesoBD.insertarCompra(dni, metodo, prod, cantidad);
			JOptionPane.showMessageDialog(this, "¡Compra creada correctamente!");
			txtDniAltaCompra.setText("");
			spnCantidad.setValue(1);
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this,
				"Error al dar de alta la compra: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void calcularAhorro() {
		String dni = txtDniAhorro.getText().trim();
		if (dni.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Introduce un DNI.");
			return;
		}
		try {
			double ahorro = accesoBD.getAhorroCliente(dni);
			if (ahorro < 0) {
				lblResultadoAhorro.setText("Cliente no encontrado");
				lblResultadoAhorro.setForeground(Color.RED);
			} else {
				lblResultadoAhorro.setText(String.format("%.2f €", ahorro));
				lblResultadoAhorro.setForeground(new Color(0, 100, 0));
			}
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private JTextField campoReadOnly() {
		JTextField tf = new JTextField(12);
		tf.setEditable(false);
		tf.setBackground(new Color(230, 230, 230));
		return tf;
	}

	private void limpiarFormularios() {
		txtDniCliente.setText("");
		txtNombreCliente.setText("");
		txtApellidoCliente.setText("");
		txtTelefonoCliente.setText("");
		txtCorreoCliente.setText("");
		txtDireccionCliente.setText("");
		txtIdCompra.setText("");
		txtFechaCompra.setText("");
	}
}
