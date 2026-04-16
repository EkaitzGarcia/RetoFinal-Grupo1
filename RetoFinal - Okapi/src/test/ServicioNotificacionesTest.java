package test;

//No es nada, solo para hacer un commit de prueba.
import clases_test.EmailSender;
import clases_test.ServicioNotificaciones;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests de ServicioNotificaciones con Mockito")
class ServicioNotificacionesTest {

    @Mock
    private EmailSender emailSenderMock;

    private ServicioNotificaciones servicio;

    @BeforeEach
    void setUp() {
        servicio = new ServicioNotificaciones(emailSenderMock);
    }

    // ── assertTrue + Mockito (when/verify) ────────────────────────

    @Test
    @DisplayName("Notificar bienvenida debe devolver true cuando el envío tiene éxito")
    void testNotificarBienvenidaExitosa() {
        // Configuramos el mock para que devuelva true al enviar
        when(emailSenderMock.enviar(
                eq("ekaitz@empresa.com"),
                eq("¡Bienvenido!"),
                anyString()
        )).thenReturn(true);

        boolean resultado = servicio.notificarBienvenida("ekaitz@empresa.com");

        assertTrue(resultado, "El envío de bienvenida debería devolver true");
        // Verificamos que se llamó exactamente 1 vez con esos parámetros
        verify(emailSenderMock, times(1))
                .enviar("ekaitz@empresa.com", "¡Bienvenido!", "Gracias por unirte.");
    }

    @Test
    @DisplayName("El usuario debe tener historial cuando contarEnviadosA devuelve > 0")
    void testTieneHistorialCuandoHayEnvios() {
        when(emailSenderMock.contarEnviadosA("ekaitz@empresa.com")).thenReturn(5);

        assertTrue(
                servicio.tieneHistorialDeEnvios("ekaitz@empresa.com"),
                "Debería tener historial si se han enviado emails"
        );
        verify(emailSenderMock).contarEnviadosA("ekaitz@empresa.com");
    }

    // ── assertFalse + Mockito ─────────────────────────────────────

    @Test
    @DisplayName("Notificar alerta debe devolver false si el envío falla")
    void testNotificarAlertaFallida() {
        when(emailSenderMock.enviar(
                eq("ekaitz@empresa.com"),
                eq("ALERTA"),
                anyString()
        )).thenReturn(false);

        boolean resultado = servicio.notificarAlerta("ekaitz@empresa.com", "Acceso no autorizado");

        assertFalse(resultado, "El envío fallido debería devolver false");
        verify(emailSenderMock, times(1))
                .enviar("ekaitz@empresa.com", "ALERTA", "Acceso no autorizado");
    }

    @Test
    @DisplayName("Sin historial el usuario no debe tener envíos registrados")
    void testNoTieneHistorialCuandoNoHayEnvios() {
        when(emailSenderMock.contarEnviadosA("nuevo@empresa.com")).thenReturn(0);

        assertFalse(
                servicio.tieneHistorialDeEnvios("nuevo@empresa.com"),
                "Sin envíos previos no debería tener historial"
        );
    }

    // ── assertNull + Mockito ──────────────────────────────────────

    @Test
    @DisplayName("El resumen debe ser null cuando no hay historial de envíos")
    void testResumenNuloCuandoSinHistorial() {
        when(emailSenderMock.contarEnviadosA("vacio@empresa.com")).thenReturn(0);

        String resumen = servicio.obtenerResumenEnvios("vacio@empresa.com");

        assertNull(resumen, "El resumen debería ser null si no hay envíos registrados");
        verify(emailSenderMock).contarEnviadosA("vacio@empresa.com");
    }

    // ── assertEquals + Mockito ────────────────────────────────────

    @Test
    @DisplayName("El resumen debe contener el total correcto de envíos")
    void testResumenContieneConteoCorrectamente() {
        when(emailSenderMock.contarEnviadosA("ekaitz@empresa.com")).thenReturn(3);

        String resumen = servicio.obtenerResumenEnvios("ekaitz@empresa.com");

        assertEquals(
                "Total de emails enviados a ekaitz@empresa.com: 3",
                resumen,
                "El texto del resumen debería incluir el destinatario y el conteo exacto"
        );
    }

    // ── assertThrows + Mockito ────────────────────────────────────

    @Test
    @DisplayName("Notificar bienvenida con destinatario nulo debe lanzar excepción")
    void testNotificarBienvenidaConDestinatarioNuloLanzaExcepcion() {
        assertThrows(
                IllegalArgumentException.class,
                () -> servicio.notificarBienvenida(null),
                "Pasar null como destinatario debería lanzar IllegalArgumentException"
        );
        // El mock NO debe haber sido llamado, la validación ocurre antes
        verifyNoInteractions(emailSenderMock);
    }

    @Test
    @DisplayName("Construir servicio con EmailSender nulo debe lanzar excepción")
    void testConstruirServicioConSenderNuloLanzaExcepcion() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new ServicioNotificaciones(null),
                "Inyectar null como EmailSender debería lanzar IllegalArgumentException"
        );
    }

    @Test
    @DisplayName("Notificar alerta con mensaje nulo debe lanzar excepción")
    void testNotificarAlertaConMensajeNuloLanzaExcepcion() {
        assertThrows(
                IllegalArgumentException.class,
                () -> servicio.notificarAlerta("ekaitz@empresa.com", null),
                "Pasar null como mensaje debería lanzar IllegalArgumentException"
        );
        verifyNoInteractions(emailSenderMock);
    }
}