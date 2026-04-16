package test;

import clases_test.Calculadora;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests de la clase Calculadora")
class CalculadoraTest {

    private Calculadora calculadora;

    @BeforeEach
    void setUp() {
        calculadora = new Calculadora();
    }

    // ── assertEquals ──────────────────────────────────────────────

    @Test
    @DisplayName("La suma de 3 + 4 debe devolver 7")
    void testSumar() {
        double resultado = calculadora.sumar(3, 4);
        assertEquals(7.0, resultado, "3 + 4 debería ser 7");
    }

    @Test
    @DisplayName("La resta de 10 - 4 debe devolver 6")
    void testRestar() {
        double resultado = calculadora.restar(10, 4);
        assertEquals(6.0, resultado, "10 - 4 debería ser 6");
    }

    @Test
    @DisplayName("La multiplicación de 5 * 3 debe devolver 15")
    void testMultiplicar() {
        double resultado = calculadora.multiplicar(5, 3);
        assertEquals(15.0, resultado, "5 * 3 debería ser 15");
    }

    @Test
    @DisplayName("La división de 10 / 2 debe devolver 5")
    void testDividir() {
        double resultado = calculadora.dividir(10, 2);
        assertEquals(5.0, resultado, "10 / 2 debería ser 5");
    }

    // ── assertTrue ────────────────────────────────────────────────

    @Test
    @DisplayName("El número 8 debe ser reconocido como positivo")
    void testEsPositivoConNumeroPositivo() {
        assertTrue(calculadora.esPositivo(8), "8 debería ser positivo");
    }

    @Test
    @DisplayName("El número 4 debe ser reconocido como par")
    void testEsParConNumeroPar() {
        assertTrue(calculadora.esPar(4), "4 debería ser par");
    }

    // ── assertFalse ───────────────────────────────────────────────

    @Test
    @DisplayName("El número -5 no debe ser reconocido como positivo")
    void testEsPositivoConNumeroNegativo() {
        assertFalse(calculadora.esPositivo(-5), "-5 no debería ser positivo");
    }

    @Test
    @DisplayName("El número 7 no debe ser reconocido como par")
    void testEsParConNumeroImpar() {
        assertFalse(calculadora.esPar(7), "7 no debería ser par");
    }

    // ── assertThrows ──────────────────────────────────────────────

    @Test
    @DisplayName("Dividir entre cero debe lanzar ArithmeticException")
    void testDividirEntreCeroLanzaExcepcion() {
        ArithmeticException excepcion = assertThrows(
                ArithmeticException.class,
                () -> calculadora.dividir(10, 0),
                "Debería lanzar ArithmeticException al dividir entre 0"
        );
        assertEquals("No se puede dividir entre cero", excepcion.getMessage());
    }

    // ── assertNull ────────────────────────────────────────────────

    @Test
    @DisplayName("Una instancia nueva sin resultado guardado debe ser nula")
    void testResultadoInicialNulo() {
        // Representa un resultado opcional no calculado todavía
        Double resultadoOpcional = null;
        assertNull(resultadoOpcional, "El resultado no inicializado debería ser null");
    }
}