package proyecto2.Utilidades;

import java.nio.file.Path;
import java.nio.file.Paths;
import proyecto2.LogicaNegocio.RutasProyecto;

public final class Configuracion {

    public static final int PUERTO_TCP_DEFECTO = 5000;
    public static final int PUERTO_HTTP_DEFECTO = 8080;
    public static final String PADRON_DEFECTO = "C:\\Users\\breye\\Downloads\\padron_completo\\PADRON_COMPLETO.txt";
    public static final String DISTELECT_DEFECTO = "C:\\Users\\breye\\Downloads\\padron_completo\\distelec.txt";
    public static final int HILOS_TCP_DEFECTO = 8;
    public static final int HILOS_HTTP_DEFECTO = 8;

    private Configuracion() {
    }

    public static int puertoTcp() {
        return leerEntero("padron.tcp.port", PUERTO_TCP_DEFECTO);
    }

    public static int puertoHttp() {
        return leerEntero("padron.http.port", PUERTO_HTTP_DEFECTO);
    }

    public static int hilosTcp() {
        return leerEntero("padron.tcp.threads", HILOS_TCP_DEFECTO);
    }

    public static int hilosHttp() {
        return leerEntero("padron.http.threads", HILOS_HTTP_DEFECTO);
    }

    public static Path rutaPadron() {
        return resolverRuta("padron.file", PADRON_DEFECTO);
    }

    public static Path rutaDistritos() {
        return resolverRuta("distritos.file", DISTELECT_DEFECTO);
    }

    private static int leerEntero(String clave, int defecto) {
        String valor = System.getProperty(clave);
        if (valor == null || valor.trim().isEmpty()) {
            return defecto;
        }

        try {
            return Integer.parseInt(valor.trim());
        } catch (NumberFormatException ex) {
            return defecto;
        }
    }

    private static Path resolverRuta(String clave, String defecto) {
        String valor = System.getProperty(clave);
        if (valor == null || valor.trim().isEmpty()) {
            valor = defecto;
        }

        Path ruta = Paths.get(valor.trim());
        if (ruta.isAbsolute()) {
            return ruta.normalize();
        }

        return RutasProyecto.resolver(valor.trim());
    }
}
