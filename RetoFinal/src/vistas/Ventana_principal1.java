package vistas;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;


import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Ventana_principal1 extends JFrame implements ActionListener {
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JButton btnAdmin, btnCliente, btnTrabajador, btnProducto;
    private Vista_Admin vAdmin;
    private Vista_Trabajador vTrabajador;
    private Vista_Cliente vCliente;
    private Vista_Producto vProducto;
 

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Ventana_principal1 frame = new Ventana_principal1();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public Ventana_principal1() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 650, 400);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(null);
        setContentPane(contentPane);

        btnProducto = new JButton("Producto");
        btnProducto.setBounds(250, 98, 150, 35);
        btnProducto.addActionListener(this);
        contentPane.add(btnProducto);

        btnTrabajador = new JButton("Trabajador");
        btnTrabajador.setBounds(250, 143, 150, 35);
        btnTrabajador.addActionListener(this);
        contentPane.add(btnTrabajador);

        btnAdmin = new JButton("Administrador");
        btnAdmin.setBounds(250, 188, 150, 35);
        btnAdmin.addActionListener(this);
        contentPane.add(btnAdmin);

        btnCliente = new JButton("Cliente");
        btnCliente.setBounds(250, 233, 150, 35);
        btnCliente.addActionListener(this);
        contentPane.add(btnCliente);
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
        	vCliente = new Vista_Cliente();
        	vCliente.setVisible(true);
        }
    }
}