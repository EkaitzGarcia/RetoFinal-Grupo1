package clases_test;

import java.util.HashMap;
import java.util.Map;

public class GestorUsuarios {

    private final Map<Integer, String> usuarios = new HashMap<>();
    private int nextId = 1;

    public int agregarUsuario(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre no puede ser nulo o vacío");
        }
        int id = nextId++;
        usuarios.put(id, nombre);
        return id;
    }

    public String buscarUsuario(int id) {
        return usuarios.get(id); // Devuelve null si no existe
    }

    public boolean eliminarUsuario(int id) {
        return usuarios.remove(id) != null;
    }

    public boolean existeUsuario(int id) {
        return usuarios.containsKey(id);
    }

    public boolean estaVacio() {
        return usuarios.isEmpty();
    }

    public int totalUsuarios() {
        return usuarios.size();
    }
}