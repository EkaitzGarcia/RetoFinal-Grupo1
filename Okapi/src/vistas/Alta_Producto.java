package vistas;

import modelo.*;
import vistas.Vista_Producto;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class Alta_Producto extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	
	private static Vista_Producto prod; //Vista
	
	private JButton btnAlta, btnCancelar; //Botones
	

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
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == btnAlta) {
			
		}
		if(e.getSource() == btnCancelar) {
			dispose();
		}
	}
}
