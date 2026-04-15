package vistas;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import modelo.Cliente;

public class Vista_Cliente extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JButton btnVerCompras, btnRealizarCompra;
	private Cliente clienteActual;

	/*
	 * public static void main(String[] args) { try { Cliente prueba = new
	 * Cliente("12345678A", "Juan", "Gomez", "611111111", "juan@gmail.com",
	 * "Madrid"); Vista_Cliente dialog = new Vista_Cliente(prueba);
	 * dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	 * dialog.setVisible(true); } catch (Exception e) { e.printStackTrace(); } }
	 */

	public Vista_Cliente(Cliente cliente) {
		this.clienteActual = cliente;

		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		btnRealizarCompra = new JButton("Realizar compra");
		btnRealizarCompra.setBounds(38, 120, 162, 20);
		btnRealizarCompra.addActionListener(this);
		contentPanel.add(btnRealizarCompra);

		btnVerCompras = new JButton("Ver mis Compras");
		btnVerCompras.setBounds(227, 120, 153, 20);
		btnVerCompras.addActionListener(this);
		contentPanel.add(btnVerCompras);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnRealizarCompra) {
			Vista_RealizarCompra vCompra = new Vista_RealizarCompra(clienteActual);
			vCompra.setVisible(true);
		} else if (e.getSource() == btnVerCompras) {
			Vista_MisCompras vMisCompras = new Vista_MisCompras(clienteActual);
			vMisCompras.setVisible(true);
		}
	}
}