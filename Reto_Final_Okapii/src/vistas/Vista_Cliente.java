package vistas;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Image;

import javax.swing.ImageIcon;
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
    private JButton btnVerCompras, btnRealizarCompra,btnSalir;
    private Cliente clienteActual;

    /*public static void main(String[] args) {
        try {
            Cliente prueba = new Cliente("12345678A", "Juan", "Gomez",
                                         "611111111", "juan@gmail.com", "Madrid");
            Vista_Cliente dialog = new Vista_Cliente(prueba);
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    public Vista_Cliente(Cliente cliente) {
        this.clienteActual = cliente;
        setTitle("Panel de Cliente - OKAPI");
        setBounds(100, 100, 450, 300);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(null);
        contentPanel.setBackground(new java.awt.Color(117, 85, 64));
        
      
        
        ImageIcon icono = new ImageIcon("C:/Users/1dam/Desktop/CESTA_SI.png");
        Image imagenEscalada = icono.getImage().getScaledInstance(153, 121, Image.SCALE_SMOOTH);
        ImageIcon iconoEscalado = new ImageIcon(imagenEscalada);

        btnRealizarCompra = new JButton(iconoEscalado);
        btnRealizarCompra.setBounds(51, 65, 153, 121);
        btnRealizarCompra.addActionListener(this);
        contentPanel.add(btnRealizarCompra);
        
        btnVerCompras = new JButton("See my orders");
        btnVerCompras.setBounds(227, 65, 153, 121);
        btnVerCompras.addActionListener(this);
        contentPanel.add(btnVerCompras);
        
        btnSalir = new JButton("Exit");
        btnSalir.setBounds(10, 232, 85, 21);
        btnSalir.addActionListener(this);
        contentPanel.add(btnSalir);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnRealizarCompra) {
        	 Vista_RealizarCompra vCompra = new Vista_RealizarCompra(clienteActual);
             vCompra.setVisible(true);
             setVisible(false);
        } else if (e.getSource() == btnVerCompras) {
        	Vista_MisCompras vMisCompras = new Vista_MisCompras(clienteActual);
            vMisCompras.setVisible(true);
            setVisible(false);
        }else if(e.getSource()==btnSalir) {
        	dispose();
            new Ventana_principal1().setVisible(true);;
        	
        }
    }
}