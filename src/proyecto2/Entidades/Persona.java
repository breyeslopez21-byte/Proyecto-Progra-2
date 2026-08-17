package proyecto2.Entidades;

public class Persona {

    private final String cedula;
    private final String codigoElectoral;
    private final String nombre;
    private final String primerApellido;
    private final String segundoApellido;

    public Persona(String cedula, String codigoElectoral, String nombre, String primerApellido, String segundoApellido) {
        this.cedula = cedula;
        this.codigoElectoral = codigoElectoral;
        this.nombre = nombre;
        this.primerApellido = primerApellido;
        this.segundoApellido = segundoApellido;
    }

    public String getCedula() {
        return cedula;
    }

    public String getCodigoElectoral() {
        return codigoElectoral;
    }

    public String getNombre() {
        return nombre;
    }

    public String getPrimerApellido() {
        return primerApellido;
    }

    public String getSegundoApellido() {
        return segundoApellido;
    }
}
