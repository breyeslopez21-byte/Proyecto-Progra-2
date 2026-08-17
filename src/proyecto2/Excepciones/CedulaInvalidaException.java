package proyecto2.Excepciones;

public class CedulaInvalidaException extends ConsultaPadronException {

    public CedulaInvalidaException(String mensaje) {
        super(mensaje, 400);
    }
}
