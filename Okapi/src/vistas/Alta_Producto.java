package vistas;

import modelo.*;
import vistas.Vista_Producto;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class Alta_Producto extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();

	private static Vista_Producto prod; // Vista

	private JButton btnAlta, btnCancelar; // Botones

	/**
	 * Launch the application.
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
	 * Create the dialog.
	 */
	public Alta_Producto() {
		setTitle("Add Product: Okapi");
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		btnAlta = new JButton("Add");
		btnAlta.setBounds(341, 232, 85, 21);
		btnAlta.addActionListener(this);
		contentPanel.add(btnAlta);

		btnCancelar = new JButton("Cancel");
		btnCancelar.setBounds(10, 232, 85, 21);
		btnCancelar.addActionListener(this);
		contentPanel.add(btnCancelar);

		JLabel lblNewLabel = new JLabel("Reference:");
		lblNewLabel.setBounds(36, 36, 59, 13);
		contentPanel.add(lblNewLabel);

		refText = new JTextField();
		refText.setBounds(36, 52, 96, 19);
		contentPanel.add(refText);
		refText.setColumns(10);

		JLabel lblNewLabel_1 = new JLabel("Name:");
		lblNewLabel_1.setBounds(261, 36, 59, 13);
		contentPanel.add(lblNewLabel_1);

		nomText = new JTextField();
		nomText.setBounds(261, 52, 96, 19);
		contentPanel.add(nomText);
		nomText.setColumns(10);

		JLabel lblNewLabel_2 = new JLabel("Price:");
		lblNewLabel_2.setBounds(36, 120, 59, 13);
		contentPanel.add(lblNewLabel_2);

		preText = new JTextField();
		preText.setBounds(36, 134, 96, 19);
		contentPanel.add(preText);
		preText.setColumns(10);

		JLabel lblNewLabel_3 = new JLabel("Discount:");
		lblNewLabel_3.setBounds(261, 120, 59, 13);
		contentPanel.add(lblNewLabel_3);

		descText = new JTextField();
		descText.setBounds(261, 134, 96, 19);
		contentPanel.add(descText);
		descText.setColumns(10);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() == btnAlta) {

		}
		if (e.getSource() == btnCancelar) {
			dispose();
		}
	}
}
