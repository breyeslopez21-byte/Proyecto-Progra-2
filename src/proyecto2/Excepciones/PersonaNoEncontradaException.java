package proyecto2.Excepciones;

public class PersonaNoEncontradaException extends ConsultaPadronException {

    public PersonaNoEncontradaException(String mensaje) {
        super(mensaje, 404);
    }
}
