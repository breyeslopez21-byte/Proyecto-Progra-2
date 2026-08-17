package proyecto2.Excepciones;

public class ConsultaPadronException extends Exception {

    private final int codigo;

    public ConsultaPadronException(String mensaje, int codigo) {
        super(mensaje);
        this.codigo = codigo;
    }

    public ConsultaPadronException(String mensaje, int codigo, Throwable causa) {
        super(mensaje, causa);
        this.codigo = codigo;
    }

    public int getCodigo() {
        return codigo;
    }
}
