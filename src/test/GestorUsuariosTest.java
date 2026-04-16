package test;

import clases_test.GestorUsuarios;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests de la clase GestorUsuarios")
class GestorUsuariosTest {

    private GestorUsuarios gestor;

    @BeforeEach
    void setUp() {
        gestor = new GestorUsuarios();
    }

    // ── assertNull ────────────────────────────────────────────────

    @Test
    @DisplayName("Buscar un ID inexistente debe devolver null")
    void testBuscarUsuarioInexistenteDevuelveNull() {
        String resultado = gestor.buscarUsuario(999);
        assertNull(resultado, "Un ID que no existe debería devolver null");
    }

    // ── assertEquals ──────────────────────────────────────────────

    @Test
    @DisplayName("Buscar un usuario existente debe devolver su nombre")
    void testBuscarUsuarioExistente() {
        int id = gestor.agregarUsuario("Ekaitz");
        String nombre = gestor.buscarUsuario(id);
        assertEquals("Ekaitz", nombre, "Debería devolver el nombre del usuario registrado");
    }

    @Test
    @DisplayName("El total de usuarios debe ser correcto tras agregar varios")
    void testTotalUsuariosCorrectoTrasAgregar() {
        gestor.agregarUsuario("Ekaitz");
        gestor.agregarUsuario("Ander");
        gestor.agregarUsuario("Miren");
        assertEquals(3, gestor.totalUsuarios(), "Deberían existir 3 usuarios en el gestor");
    }

    // ── assertTrue ────────────────────────────────────────────────

    @Test
    @DisplayName("Debe confirmar que un usuario agregado existe")
    void testExisteUsuarioAgregado() {
        int id = gestor.agregarUsuario("Ander");
        assertTrue(gestor.existeUsuario(id), "El usuario recién agregado debería existir");
    }

    @Test
    @DisplayName("El gestor debe estar vacío al inicializarse")
    void testGestorVacioAlInicio() {
        assertTrue(gestor.estaVacio(), "El gestor debería estar vacío al crearse");
    }

    // ── assertFalse ───────────────────────────────────────────────

    @Test
    @DisplayName("Un usuario eliminado no debe existir")
    void testUsuarioEliminadoNoExiste() {
        int id = gestor.agregarUsuario("Miren");
        gestor.eliminarUsuario(id);
        assertFalse(gestor.existeUsuario(id), "El usuario eliminado no debería seguir existiendo");
    }

    @Test
    @DisplayName("El gestor no debe estar vacío tras agregar un usuario")
    void testGestorNoVacioTrasAgregar() {
        gestor.agregarUsuario("Ibai");
        assertFalse(gestor.estaVacio(), "El gestor no debería estar vacío tras agregar un usuario");
    }

    // ── assertThrows ──────────────────────────────────────────────

    @Test
    @DisplayName("Agregar un usuario con nombre nulo debe lanzar IllegalArgumentException")
    void testAgregarUsuarioNuloLanzaExcepcion() {
        assertThrows(
                IllegalArgumentException.class,
                () -> gestor.agregarUsuario(null),
                "Debería lanzar IllegalArgumentException con nombre null"
        );
    }

    @Test
    @DisplayName("Agregar un usuario con nombre vacío debe lanzar IllegalArgumentException")
    void testAgregarUsuarioVacioLanzaExcepcion() {
        assertThrows(
                IllegalArgumentException.class,
                () -> gestor.agregarUsuario("   "),
                "Debería lanzar IllegalArgumentException con nombre en blanco"
        );
    }
}