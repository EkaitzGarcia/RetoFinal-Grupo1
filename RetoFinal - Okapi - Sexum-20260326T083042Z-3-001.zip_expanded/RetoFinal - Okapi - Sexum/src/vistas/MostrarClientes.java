package vistas;

import modelo.*;
import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.util.*;

public class MostrarClientes extends JDialog {

    private static final long serialVersionUID = 1L;
    private final JPanel contentPanel = new JPanel();
    private JTable table;
    private DefaultTableModel model;

    /**
     * Constructor que crea el diálogo para mostrar los clientes.
     *
     * @param padre El diálogo padre de este diálogo
     * @param modal Indica si el diálogo debe ser modal (true) o no (false)
     */
    public MostrarClientes(Vista_Admin padre, boolean modal) {
        super(padre);
        this.setModal(modal);
        setTitle("Lista de Clientes");
        setBounds(100, 100, 600, 400);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(new BorderLayout());

        String[] columnNames = {"DNI", "Nombre", "Apellido", "Teléfono", "Correo", "Direccion"};
        model = new DefaultTableModel(columnNames, 0) {
            private static final long serialVersionUID = 1L;
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = table.getSelectedRow();
                    if (row != -1) {
                        String dni    = (String) model.getValueAt(row, 0);
                        String nom    = (String) model.getValueAt(row, 1);
                        String ape    = (String) model.getValueAt(row, 2);
                        String tel    = (String) model.getValueAt(row, 3);
                        String correo = (String) model.getValueAt(row, 4);
                        String direccion = (String)model.getValueAt(row, 5);                        
                        		dispose();
                        padre.cargarDatosCliente(dni, nom, ape, tel, correo,direccion);
                        padre.setVisible(true);
                    }
                }
            }
        });

        cargarDatos();
    }

    /**
     * Carga los datos de los clientes desde la base de datos.
     */
    private void cargarDatos() {
        try {
            AccesoBD bd = new AccesoBD();
            List<Cliente> lista = new ArrayList<>();
            bd.getTodosClientes(lista);

            Object[][] datos = new Object[lista.size()][6];
            for (int i = 0; i < lista.size(); i++) {
                Cliente c = lista.get(i);
                datos[i][0] = c.getDni();
                datos[i][1] = c.getNom();
                datos[i][2] = c.getApellido();
                datos[i][3] = c.getTelefono();
                datos[i][4] = c.getCorreo();
                datos[i][5]= c.getDireccion();
            }
            actualizarDatos(datos);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Actualiza la tabla con los datos proporcionados.
     *
     * @param datos Matriz de objetos con los datos de clientes a mostrar
     */
    private void actualizarDatos(Object[][] datos) {
        model.setRowCount(0);
        for (Object[] fila : datos) {
            model.addRow(fila);
        }
    }
}