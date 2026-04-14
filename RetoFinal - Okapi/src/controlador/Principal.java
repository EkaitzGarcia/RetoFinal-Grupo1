package controlador;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import modelo.AccesoBD;
import modelo.Cliente;
import modelo.Trabajador;

public class Principal {

    // =========================================================================
    // CLIENTES
    // =========================================================================

    /**
     * Devuelve todos los clientes de la base de datos.
     *
     * @return Lista de clientes
     */
    public static List<Cliente> devolverClientes() {
        List<Cliente> lista = new ArrayList<>();
        try {
            AccesoBD bd = new AccesoBD();
            Map<String, Cliente> mapa = new HashMap<>();
            bd.getTodosClientes(mapa);
            lista.addAll(mapa.values());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    /**
     * Da de alta un nuevo cliente en el sistema.
     *
     * @param c Cliente a insertar
     */
    public static void altaCliente(Cliente c) throws Exception {
        AccesoBD bd = new AccesoBD();
        Map<String, Cliente> mapa = new HashMap<>();
        bd.getTodosClientes(mapa);
        if (mapa.containsKey(c.getDni())) {
            throw new Exception("EL CLIENTE CON DNI " + c.getDni() + " YA EXISTE");
        }
        bd.insertarCliente(c);
    }

    /**
     * Da de baja un cliente del sistema.
     *
     * @param c Cliente a eliminar (se usa el DNI)
     */
    public static void bajaCliente(Cliente c) throws Exception {
        AccesoBD bd = new AccesoBD();
        bd.eliminarCliente(c);
    }

    /**
     * Modifica los datos de un cliente existente.
     *
     * @param c Cliente con los datos actualizados
     */
    public static void modificarCliente(Cliente c) {
        try {
            AccesoBD bd = new AccesoBD();
            bd.actualizarCliente(c);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    public static List<Trabajador> devolverTrabajadores() {
        List<Trabajador> lista = new ArrayList<>();
        try {
            AccesoBD bd = new AccesoBD();
            bd.getTodosLosTrabajaores(lista);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

  
    public static String altaTrabajador(Trabajador t) throws Exception {
        AccesoBD bd = new AccesoBD();
        return bd.altaTrabajadorProcedimiento(t);
    }

   
    public static void bajaTrabajador(Trabajador t) {
        try {
            AccesoBD bd = new AccesoBD();
            bd.eliminarTrabajador(t);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

   
    public static void modificarTrabajador(Trabajador t) {
        try {
            AccesoBD bd = new AccesoBD();
            bd.actualizarTrabajador(t);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}