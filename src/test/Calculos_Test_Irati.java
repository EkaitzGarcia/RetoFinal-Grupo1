package test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import clases_test.Calculos_Irati;

public class Calculos_Test_Irati {

    @Test
    void testCalcularTotal_precioNegativo() {
        Calculos_Irati c = new Calculos_Irati();

        assertThrows(IllegalArgumentException.class, () -> {
            c.calcularTotal(-10, 2, 0);
        });
    }

    @Test
    void testCalcularTotal_cantidadCero() {
        Calculos_Irati c = new Calculos_Irati();

        assertThrows(IllegalArgumentException.class, () -> {
            c.calcularTotal(10, 0, 0);
        });
    }

    @Test
    void testCalcularTotal_sinDescuento() {
        Calculos_Irati c = new Calculos_Irati();

        double resultado = c.calcularTotal(10, 2, 0);

        assertEquals(20.0, resultado);
    }

    @Test
    void testCalcularTotal_conDescuento() {
        Calculos_Irati c = new Calculos_Irati();

        double resultado = c.calcularTotal(100, 1, 10);

        assertEquals(90.0, resultado);
    }

    @Test
    void testPuedeComprar_true() {
        Calculos_Irati c = new Calculos_Irati();

        assertTrue(c.puedeComprar(100, 50));
    }

    @Test
    void testPuedeComprar_false() {
        Calculos_Irati c = new Calculos_Irati();

        assertFalse(c.puedeComprar(30, 50));
    }
    
    @Test
    void testObtenerResultado_null() {
        Calculos_Irati c = new Calculos_Irati();

        String resultado = c.obtenerResultado(30, 50);

        assertNull(resultado);
    }
}