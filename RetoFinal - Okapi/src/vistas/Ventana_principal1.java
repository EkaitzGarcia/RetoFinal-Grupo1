package vistas;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import modelo.AccesoBD;
import modelo.Cliente;
import modelo.Producto;
import modelo.Trabajador;
import utiles.convertiro_exportador_de_xml;

/**
 * Ventana principal del sistema de gestión Okapi.
 * <p>
 * Actúa como punto de entrada de la aplicación y panel de navegación
 * hacia las distintas secciones del sistema:
 * </p>
 * <ul>
 *   <li><b>Product</b> – Gestión del catálogo de productos.</li>
 *   <li><b>Worker</b> – Panel del trabajador (compras, clientes, ahorro).</li>
 *   <li><b>Administrator</b> – Administración de clientes y trabajadores.</li>
 *   <li><b>Client</b> – Acceso al área personal del cliente mediante DNI.</li>
 *   <li><b>Export XML</b> – Exportación de todos los datos a un fichero XML.</li>
 * </ul>
 * <p>
 * Implementa {@link ActionListener} para gestionar los eventos de los botones
 * de navegación.
 * </p>
 *
 * @see Vista_Admin
 * @see Vista_Trabajador
 * @see Vista_Cliente
 * @see Vista_Producto
 * @see modelo.AccesoBD
 */
public class Ventana_principal1 extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;

	// ── Paleta de colores
	private static final Color BG_MAIN = new Color(117, 85, 64);
	private static final Color ACCENT = new Color(248, 250, 252);
	private static final Color BTN_NORMAL = new Color(126, 129, 115);
	private static final Color BTN_HOVER = new Color(255, 192, 203);
	private static final Color BTN_BORDER = new Color(255, 192, 203);
	private static final Color TEXT_PRIMARY = new Color(248, 250, 252);
	private static final Color TEXT_SUBTLE = new Color(248, 250, 252);

	// ── Color propio del botón XML
	private static final Color BTN_XML_NORMAL = new Color(60, 120, 80);
	private static final Color BTN_XML_HOVER = new Color(80, 160, 100);
	private static final Color BTN_XML_BORDER = new Color(150, 230, 150);

	private JButton btnAdmin, btnCliente, btnTrabajador, btnProducto, btnExportarXML;

	private Vista_Admin vAdmin;
	private Vista_Trabajador vTrabajador;
	private Vista_Cliente vCliente;
	private Vista_Producto vProducto;

	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				new Ventana_principal1().setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	/**
	 * Constructor de {@code Ventana_principal1}.
	 * <p>
	 * Inicializa y configura todos los componentes visuales de la ventana principal:
	 * título, subtítulo, separadores decorativos y los cinco botones de navegación
	 * ({@code Product}, {@code Worker}, {@code Administrator}, {@code Client}
	 * y {@code Export XML}).
	 * </p>
	 * <p>
	 * Todos los botones se registran como oyentes de esta misma instancia
	 * a través de {@link #actionPerformed(ActionEvent)}.
	 * </p>
	 */
	public Ventana_principal1() {
		setTitle("Home: Okapi");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(500, 560);
		setLocationRelativeTo(null);
		setResizable(false);

		JPanel root = new JPanel();
		root.setBackground(BG_MAIN);
		root.setLayout(new BoxLayout(root, BoxLayout.Y_AXIS));
		root.setBorder(new EmptyBorder(40, 60, 30, 60));
		setContentPane(root);

		JLabel lblTitle = new JLabel("Management panel", SwingConstants.CENTER);
		lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
		lblTitle.setForeground(TEXT_PRIMARY);
		lblTitle.setAlignmentX(CENTER_ALIGNMENT);
		root.add(lblTitle);

		root.add(Box.createVerticalStrut(8));

		JLabel lblSub = new JLabel("Select a section to continue", SwingConstants.CENTER);
		lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		lblSub.setForeground(TEXT_SUBTLE);
		lblSub.setAlignmentX(CENTER_ALIGNMENT);
		root.add(lblSub);

		root.add(Box.createVerticalStrut(16));

		JPanel sep = new JPanel();
		sep.setBackground(ACCENT);
		sep.setMaximumSize(new Dimension(200, 2));
		sep.setPreferredSize(new Dimension(200, 2));
		sep.setAlignmentX(CENTER_ALIGNMENT);
		root.add(sep);

		root.add(Box.createVerticalStrut(24));

		btnProducto = createButton("📦", "Product");
		btnTrabajador = createButton("👷", "Worker");
		btnAdmin = createButton("🛡", "Administrator");
		btnCliente = createButton("👤", "Client");

		for (JButton btn : new JButton[] { btnProducto, btnTrabajador, btnAdmin, btnCliente }) {
			btn.setAlignmentX(CENTER_ALIGNMENT);
			btn.addActionListener(this);
			root.add(btn);
			root.add(Box.createVerticalStrut(14));
		}

		JPanel sep2 = new JPanel();
		sep2.setBackground(BTN_XML_BORDER);
		sep2.setMaximumSize(new Dimension(200, 1));
		sep2.setPreferredSize(new Dimension(200, 1));
		sep2.setAlignmentX(CENTER_ALIGNMENT);
		root.add(sep2);
		root.add(Box.createVerticalStrut(14));

		btnExportarXML = createButtonXML("📄", "Export XML");
		btnExportarXML.setAlignmentX(CENTER_ALIGNMENT);
		btnExportarXML.addActionListener(this);
		root.add(btnExportarXML);
		root.add(Box.createVerticalStrut(14));

		root.add(Box.createVerticalGlue());

		JLabel lblFooter = new JLabel("I don't care", SwingConstants.CENTER);
		lblFooter.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		lblFooter.setForeground(new Color(255, 192, 203));
		lblFooter.setAlignmentX(CENTER_ALIGNMENT);
		root.add(lblFooter);
	}

	/**
	 * Crea un botón de navegación estilizado con emoji e icono de texto.
	 * <p>
	 * El botón tiene un efecto hover que cambia el color de fondo y el borde
	 * al pasar el ratón por encima, y restaura el estilo original al salir.
	 * Su tamaño fijo es de 240 × 48 px.
	 * </p>
	 *
	 * @param emoji Emoji que se mostrará a la izquierda del texto (p. ej. {@code "📦"}).
	 * @param label Texto descriptivo del botón (p. ej. {@code "Product"}).
	 * @return Botón {@link JButton} completamente configurado con estilo y animación hover.
	 */
	private JButton createButton(String emoji, String label) {
		JButton btn = new JButton(emoji + "  " + label);
		btn.setFont(new Font("Segoe UI Emoji", Font.BOLD, 14));
		btn.setForeground(TEXT_PRIMARY);
		btn.setBackground(BTN_NORMAL);
		btn.setFocusPainted(false);
		btn.setBorder(new CompoundBorder(new LineBorder(BTN_BORDER, 1, true), new EmptyBorder(10, 24, 10, 24)));
		btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btn.setOpaque(true);
		btn.setContentAreaFilled(true);
		btn.setMaximumSize(new Dimension(240, 48));
		btn.setPreferredSize(new Dimension(240, 48));

		btn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				btn.setBackground(BTN_HOVER);
				btn.setForeground(ACCENT);
				btn.setBorder(new CompoundBorder(new LineBorder(ACCENT, 1, true), new EmptyBorder(10, 24, 10, 24)));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				btn.setBackground(BTN_NORMAL);
				btn.setForeground(TEXT_PRIMARY);
				btn.setBorder(new CompoundBorder(new LineBorder(BTN_BORDER, 1, true), new EmptyBorder(10, 24, 10, 24)));
			}
		});
		return btn;
	}

	/**
	 * Crea el botón especial de exportación XML con una paleta de colores verde,
	 * diferenciada visualmente del resto de botones de navegación.
	 * <p>
	 * Incluye efecto hover que aclara el verde al pasar el ratón, y restaura
	 * el color original al salir. Su tamaño fijo es de 240 × 48 px.
	 * </p>
	 *
	 * @param emoji Emoji que se mostrará a la izquierda del texto (p. ej. {@code "📄"}).
	 * @param label Texto descriptivo del botón (p. ej. {@code "Export XML"}).
	 * @return Botón {@link JButton} con estilo XML y animación hover configurados.
	 */
	private JButton createButtonXML(String emoji, String label) {
		JButton btn = new JButton(emoji + "  " + label);
		btn.setFont(new Font("Segoe UI Emoji", Font.BOLD, 14));
		btn.setForeground(TEXT_PRIMARY);
		btn.setBackground(BTN_XML_NORMAL);
		btn.setFocusPainted(false);
		btn.setBorder(new CompoundBorder(new LineBorder(BTN_XML_BORDER, 1, true), new EmptyBorder(10, 24, 10, 24)));
		btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btn.setOpaque(true);
		btn.setContentAreaFilled(true);
		btn.setMaximumSize(new Dimension(240, 48));
		btn.setPreferredSize(new Dimension(240, 48));

		btn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				btn.setBackground(BTN_XML_HOVER);
				btn.setBorder(new CompoundBorder(new LineBorder(ACCENT, 1, true), new EmptyBorder(10, 24, 10, 24)));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				btn.setBackground(BTN_XML_NORMAL);
				btn.setBorder(
						new CompoundBorder(new LineBorder(BTN_XML_BORDER, 1, true), new EmptyBorder(10, 24, 10, 24)));
			}
		});
		return btn;
	}

	/**
	 * Reproduce un efecto de sonido (.wav) desde los recursos del classpath.
	 * <p>
	 * Si el recurso no existe o se produce cualquier error durante la reproducción,
	 * el método falla silenciosamente sin lanzar ninguna excepción.
	 * </p>
	 *
	 * @param recurso Ruta del recurso de audio relativa al classpath
	 *                (p. ej. {@code "/resources/exportar_xml.wav"}).
	 */
	private static void reproducirSonido(String recurso) {
		try {
			java.net.URL url = Ventana_principal1.class.getResource(recurso);
			if (url == null) return;
			AudioInputStream ais = AudioSystem.getAudioInputStream(url);
			Clip clip = AudioSystem.getClip();
			clip.open(ais);
			clip.start();
		} catch (Exception ex) { }
	}

	/**
	 * Solicita el DNI al usuario mediante un diálogo de entrada y, si se encuentra
	 * en la base de datos, abre la ventana personal del cliente ({@link Vista_Cliente}).
	 * <p>
	 * En caso de que el DNI no exista en el sistema o se produzca un error de
	 * conexión, se muestra un mensaje de error informativo al usuario.
	 * </p>
	 *
	 * @see modelo.AccesoBD#getClientePorDni(String, java.util.Map)
	 * @see Vista_Cliente
	 */
	public void accesoBD() {
		AccesoBD accesoBD = new AccesoBD();
		Map<String, Cliente> clientes = new TreeMap<>();
		String dni = JOptionPane.showInputDialog(this, "Introduce your DNI:");
		try {
			accesoBD.getClientePorDni(dni.toUpperCase(), clientes);
			if (!clientes.isEmpty()) {
				vCliente = new Vista_Cliente(clientes.get(dni.toUpperCase()));
				vCliente.setVisible(true);
			} else {
				JOptionPane.showMessageDialog(this, "DNI not found.", "Error", JOptionPane.ERROR_MESSAGE);
			}
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Exporta todos los datos del sistema (clientes, productos, trabajadores
	 * y compras) a un fichero XML en la ruta que elija el usuario.
	 * <p>
	 * El proceso:
	 * <ol>
	 *   <li>Abre un diálogo {@link JFileChooser} para que el usuario seleccione
	 *       el destino y nombre del fichero.</li>
	 *   <li>Recupera de la BD: todas las compras con sus clientes, todos los
	 *       productos y todos los trabajadores.</li>
	 *   <li>Construye los objetos de dominio necesarios y delega la escritura
	 *       en {@link utiles.convertiro_exportador_de_xml#exportarTodo}.</li>
	 *   <li>Reproduce un sonido de confirmación y muestra un mensaje de éxito.</li>
	 * </ol>
	 * </p>
	 * <p>
	 * Si el usuario cancela el diálogo o se produce algún error, el método
	 * muestra un mensaje de error o simplemente retorna sin hacer nada.
	 * </p>
	 *
	 * @see utiles.convertiro_exportador_de_xml
	 * @see modelo.AccesoBD#getTodasLasComprasConCliente()
	 */
	private void exportarXML() {
		JFileChooser chooser = new JFileChooser();
		chooser.setDialogTitle("Save XML export");
		chooser.setSelectedFile(new java.io.File("okapi_export.xml"));
		chooser.setFileFilter(new FileNameExtensionFilter("XML files (*.xml)", "xml"));

		if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION)
			return;

		String ruta = chooser.getSelectedFile().getAbsolutePath();
		if (!ruta.toLowerCase().endsWith(".xml"))
			ruta += ".xml";

		AccesoBD bd = new AccesoBD();
		try {
			List<Object[]> filasCompras = bd.getTodasLasComprasConCliente();

			Map<String, Producto> mapaProductos = new TreeMap<>();
			bd.getTodosProductos(mapaProductos);
			List<Producto> productos = new ArrayList<>(mapaProductos.values());

			Map<String, Cliente> mapaClientes = new TreeMap<>();
			for (Object[] f : filasCompras) {
				String dni = (String) f[4];
				String nom = (String) f[5];
				String ape = (String) f[6];
				if (!mapaClientes.containsKey(dni)) {
					Cliente c = new Cliente();
					c.setDni(dni);
					c.setNom(nom);
					c.setApellido(ape);
					c.setTelefono((String) f[7]);
					c.setCorreo((String) f[8]);
					c.setDireccion((String) f[9]);
					mapaClientes.put(dni, c);
				}
			}
			List<Cliente> clientes = new ArrayList<>(mapaClientes.values());

			List<Trabajador> trabajadores = new ArrayList<>();
			bd.getTodosLosTrabajaores(trabajadores);

			new convertiro_exportador_de_xml().exportarTodo(clientes, productos, trabajadores, filasCompras, ruta);

			reproducirSonido("/resources/exportar_xml.wav");
			JOptionPane.showMessageDialog(this, "XML generated correctly:\n" + ruta, "Export completed",
					JOptionPane.INFORMATION_MESSAGE);

		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Error exporting:\n" + ex.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
		}
	}

	/**
	 * Gestiona los eventos de clic de los botones de navegación de la ventana principal.
	 * <p>
	 * Abre la vista correspondiente según el botón pulsado:
	 * </p>
	 * <ul>
	 *   <li>{@code btnProducto} → abre {@link Vista_Producto}.</li>
	 *   <li>{@code btnTrabajador} → abre {@link Vista_Trabajador}.</li>
	 *   <li>{@code btnAdmin} → abre {@link Vista_Admin}.</li>
	 *   <li>{@code btnCliente} → llama a {@link #accesoBD()} para autenticar por DNI.</li>
	 *   <li>{@code btnExportarXML} → llama a {@link #exportarXML()} para exportar datos.</li>
	 * </ul>
	 *
	 * @param e Evento de acción generado por el botón pulsado.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnProducto) {
			vProducto = new Vista_Producto();
			vProducto.setVisible(true);
		}
		if (e.getSource() == btnTrabajador) {
			vTrabajador = new Vista_Trabajador();
			vTrabajador.setVisible(true);
		}
		if (e.getSource() == btnAdmin) {
			vAdmin = new Vista_Admin(this);
			vAdmin.setVisible(true);
		}
		if (e.getSource() == btnCliente) {
			accesoBD();
		}
		if (e.getSource() == btnExportarXML) {
			exportarXML();
		}
	}
}