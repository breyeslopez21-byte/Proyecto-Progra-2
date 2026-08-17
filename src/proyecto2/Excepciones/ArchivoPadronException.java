package proyecto2.Excepciones;

public class ArchivoPadronException extends ConsultaPadronException {

    public ArchivoPadronException(String mensaje, Throwable causa) {
        super(mensaje, 500, causa);
    }
}
