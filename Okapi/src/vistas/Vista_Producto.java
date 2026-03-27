
package vistas;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import modelo.*;
import vistas.*;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;
import java.awt.Panel;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.border.LineBorder;
import java.awt.Color;

public class Vista_Producto extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;
	private Vista_Producto vProducto;
	private static Ventana_principal1 principal;
	private Alta_Producto alta;
	
	//Botones.
	private JButton btnVolver, btnAlta;
	
	//Vistas.
	private JLabel lblNewLabel;
	private JTable table;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			Vista_Producto dialog = new Vista_Producto();
			dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE); //Al darle a la X no finaliza el programa.
			dialog.addWindowListener(new WindowAdapter() {
			    @Override
			    public void windowClosing(WindowEvent e) {
			    	principal = new Ventana_principal1();
			    	principal.setVisible(true);
			        dialog.dispose();
			    }
			});
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public Vista_Producto() {
		setTitle("Product list: Okapi");
		setBounds(100, 100, 1330, 754);
		getContentPane().setLayout(null);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBounds(0, 673, 1306, 34);
			getContentPane().add(buttonPane);
			buttonPane.setLayout(null);
			{
				btnVolver = new JButton("Back");
				btnVolver.addActionListener(this);
				btnVolver.setBounds(31, 5, 124, 23);
				btnVolver.setActionCommand("Back");
				buttonPane.add(btnVolver);
				getRootPane().setDefaultButton(btnVolver);
			}
			{
				btnAlta = new JButton("Add Product");
				btnAlta.addActionListener(this);
				btnAlta.setBounds(1147, 5, 124, 23);
				btnAlta.setActionCommand("Close");
				buttonPane.add(btnAlta);
			}
		}
		
		
		{
			table = new JTable();
			table.setBorder(new LineBorder(new Color(0, 0, 0)));
			table.setCellSelectionEnabled(true);
			table.setBounds(41, 103, 1237, 547);
			getContentPane().add(table);
		}
		{
			lblNewLabel = new JLabel("Available Products:");
			lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
			lblNewLabel.setBounds(45, 62, 132, 31);
			getContentPane().add(lblNewLabel);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == btnVolver) {
			principal = new Ventana_principal1();
			principal.setVisible(true);
			dispose();
		}
		
		if(e.getSource() == btnAlta) {
			alta = new Alta_Producto();
			alta.setVisible(true);
		}
		
		
	}
}
