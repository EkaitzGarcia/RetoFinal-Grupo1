package test;

import clases_test.Validadoremail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests de la clase ValidadorEmail")
class ValidadorEmailTest {

    private Validadoremail validador;

    @BeforeEach
    void setUp() {
        validador = new Validadoremail();
    }

    // ── assertTrue ────────────────────────────────────────────────

    @Test
    @DisplayName("Un email con formato correcto debe ser válido")
    void testEmailFormatoCorrecto() {
        assertTrue(validador.esValido("ekaitz@gmail.com"),
                "ekaitz@gmail.com debería ser un email válido");
    }

    @Test
    @DisplayName("Un email corporativo debe ser reconocido como tal")
    void testEmailCorporativoReconocido() {
        assertTrue(validador.tieneDominioCorporativo("ekaitz@empresa.com"),
                "empresa.com debería ser dominio corporativo");
    }

    // ── assertFalse ───────────────────────────────────────────────

    @Test
    @DisplayName("Un email sin arroba no debe ser válido")
    void testEmailSinArrobaNoEsValido() {
        assertFalse(validador.esValido("emailsindominio.com"),
                "Un email sin @ no debería ser válido");
    }

    @Test
    @DisplayName("Un email de Gmail no debe ser corporativo")
    void testEmailGmailNoEsCorporativo() {
        assertFalse(validador.tieneDominioCorporativo("usuario@gmail.com"),
                "gmail.com no debería ser dominio corporativo");
    }

    // ── assertEquals ──────────────────────────────────────────────

    @Test
    @DisplayName("Extraer dominio de un email debe devolver la parte correcta")
    void testExtraerDominioCorrectamente() {
        String dominio = validador.extraerDominio("ekaitz@empresa.com");
        assertEquals("empresa.com", dominio, "El dominio extraído debería ser empresa.com");
    }

    // ── assertNull ────────────────────────────────────────────────

    @Test
    @DisplayName("Extraer dominio de un email nulo debe devolver null")
    void testExtraerDominioDeNuloDevuelveNull() {
        String dominio = validador.extraerDominio(null);
        assertNull(dominio, "Extraer dominio de null debería devolver null");
    }

    @Test
    @DisplayName("Extraer dominio de un string sin arroba debe devolver null")
    void testExtraerDominioDeCadenaInvalidaDevuelveNull() {
        String dominio = validador.extraerDominio("noesunemail");
        assertNull(dominio, "Sin @ no se puede extraer dominio, debería ser null");
    }

    // ── assertThrows ──────────────────────────────────────────────

    @Test
    @DisplayName("Validar un email nulo debe lanzar IllegalArgumentException")
    void testValidarEmailNuloLanzaExcepcion() {
        assertThrows(
                IllegalArgumentException.class,
                () -> validador.esValido(null),
                "null debería lanzar IllegalArgumentException"
        );
    }

    @Test
    @DisplayName("Validar un email demasiado largo debe lanzar IllegalArgumentException")
    void testValidarEmailDemasiadoLargoLanzaExcepcion() {
        String emailLargo = "a".repeat(40) + "@dominio.com"; // > 50 chars
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> validador.esValido(emailLargo),
                "Un email de más de 50 caracteres debería lanzar excepción"
        );
        assertTrue(ex.getMessage().contains("longitud máxima"),
                "El mensaje de error debería mencionar la longitud máxima");
    }
}