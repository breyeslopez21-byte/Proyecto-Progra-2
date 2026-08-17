package proyecto2.Excepciones;

public class FormatoDatosException extends ConsultaPadronException {

    public FormatoDatosException(String mensaje) {
        super(mensaje, 500);
    }

    public FormatoDatosException(String mensaje, Throwable causa) {
        super(mensaje, 500, causa);
    }
}
