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
import java.util.Map;
import java.util.TreeMap;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import modelo.AccesoBD;
import modelo.Cliente;

public class Ventana_principal1 extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;

	// Paleta de colores
	private static final Color BG_MAIN = new Color(117, 85, 64);
	private static final Color ACCENT = new Color(248, 250, 252);
	private static final Color BTN_NORMAL = new Color(126, 129, 115);
	private static final Color BTN_HOVER = new Color(255, 192, 203);
	private static final Color BTN_BORDER = new Color(255, 192, 203);
	private static final Color TEXT_PRIMARY = new Color(248, 250, 252);
	private static final Color TEXT_SUBTLE = new Color(248, 250, 252);

	private JButton btnAdmin, btnCliente, btnTrabajador, btnProducto;

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

	public Ventana_principal1() {
		setTitle("Inicio: Okapi");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(500, 480);
		setLocationRelativeTo(null);
		setResizable(false);

		JPanel root = new JPanel();
		root.setBackground(BG_MAIN);
		root.setLayout(new BoxLayout(root, BoxLayout.Y_AXIS));
		root.setBorder(new EmptyBorder(40, 60, 30, 60));
		setContentPane(root);

		JLabel lblTitle = new JLabel("Panel de Gestión", SwingConstants.CENTER);
		lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
		lblTitle.setForeground(TEXT_PRIMARY);
		lblTitle.setAlignmentX(CENTER_ALIGNMENT);
		root.add(lblTitle);

		root.add(Box.createVerticalStrut(8));

		JLabel lblSub = new JLabel("Selecciona una sección para continuar", SwingConstants.CENTER);
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

		String[][] buttons = { 
				{ "📦", "Producto" }, 
				{ "👷", "Trabajador" }, 
				{ "🛡", "Administrador" },
				{ "👤", "Cliente" } };
		
		btnProducto = createButton(buttons[0][0], buttons[0][1]);
		btnTrabajador = createButton(buttons[1][0], buttons[1][1]);
        btnAdmin      = createButton(buttons[2][0], buttons[2][1]);
        btnCliente    = createButton(buttons[3][0], buttons[3][1]);	
        
        for(JButton btn : new JButton[] {btnProducto, btnTrabajador, btnAdmin, btnCliente}) {
        	btn.setAlignmentX(CENTER_ALIGNMENT);
        	btn.addActionListener(this);
        	root.add(btn);
        	root.add(Box.createVerticalStrut(14));
        }
        root.add(Box.createVerticalGlue());
        
        JLabel lblFooter = new JLabel("me da igual", SwingConstants.CENTER);
        lblFooter.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblFooter.setForeground(new Color(255, 192, 203));
        lblFooter.setAlignmentX(CENTER_ALIGNMENT);
        root.add(lblFooter);
	}
	
	private JButton createButton(String emoji, String label) {
        JButton btn = new JButton(emoji + "  " + label);
        btn.setFont(new Font("Segoe UI Emoji", Font.BOLD, 14));
        btn.setForeground(TEXT_PRIMARY);
        btn.setBackground(BTN_NORMAL);
        btn.setFocusPainted(false);
        btn.setBorder(new CompoundBorder(
            new LineBorder(BTN_BORDER, 1, true),
            new EmptyBorder(10, 24, 10, 24)
        ));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        btn.setMaximumSize(new Dimension(240, 48));
        btn.setPreferredSize(new Dimension(240, 48));

        btn.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) {
                btn.setBackground(BTN_HOVER);
                btn.setForeground(ACCENT);
                btn.setBorder(new CompoundBorder(
                    new LineBorder(ACCENT, 1, true),
                    new EmptyBorder(10, 24, 10, 24)
                ));
            }
            @Override public void mouseExited(MouseEvent e) {
                btn.setBackground(BTN_NORMAL);
                btn.setForeground(TEXT_PRIMARY);
                btn.setBorder(new CompoundBorder(
                    new LineBorder(BTN_BORDER, 1, true),
                    new EmptyBorder(10, 24, 10, 24)
                ));
            }
        });
		return btn;
	}
	public void accesoBD() {
		AccesoBD accesoBD = new AccesoBD();
		Map<String, Cliente> clientes = new TreeMap<>();

		String dni = JOptionPane.showInputDialog(this, "Enter your ID number:");

		try {
			accesoBD.getClientePorDni(dni.toUpperCase(), clientes);

			if (!clientes.isEmpty()) {
				vCliente = new Vista_Cliente(clientes.get(dni.toUpperCase()));
				vCliente.setVisible(true);
			} else {
				JOptionPane.showMessageDialog(this, "ID not found.", "Error", JOptionPane.ERROR_MESSAGE);
			}
		} catch (Exception exception) {
			JOptionPane.showMessageDialog(this, exception.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	

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
        	vAdmin = new Vista_Admin();
        	vAdmin.setVisible(true);
        }
        if (e.getSource() == btnCliente) {
        	accesoBD();
        	setVisible(false);
        }
    }
}
