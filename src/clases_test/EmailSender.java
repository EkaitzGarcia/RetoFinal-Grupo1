package clases_test;

public interface EmailSender {
    boolean enviar(String destinatario, String asunto, String cuerpo);
    int contarEnviadosA(String destinatario);
}
