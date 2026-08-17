package proyecto2.Servidores;

import java.io.IOException;
import java.nio.file.Path;
import proyecto2.DTO.PersonaDTO;
import proyecto2.Excepciones.ConsultaPadronException;
import proyecto2.Repositorios.RepositorioDistritos;
import proyecto2.Repositorios.RepositorioPadron;
import proyecto2.Servicios.ServicioPadron;
import proyecto2.Utilidades.Configuracion;

public class ServidorPadronApp {

    private final ServidorTCP servidorTCP;
    private final ServidorHTTP servidorHTTP;
    private final ServicioPadron servicioPadron;

    public ServidorPadronApp() {
        Path rutaPadron = Configuracion.rutaPadron();
        Path rutaDistritos = Configuracion.rutaDistritos();

        this.servicioPadron = new ServicioPadron(
                new RepositorioPadron(rutaPadron),
                new RepositorioDistritos(rutaDistritos)
        );

        this.servidorTCP = new ServidorTCP(Configuracion.puertoTcp(), Configuracion.hilosTcp(), servicioPadron);
        this.servidorHTTP = new ServidorHTTP(Configuracion.puertoHttp(), Configuracion.hilosHttp(), servicioPadron);
    }

    public void iniciar() throws IOException {
        servidorTCP.iniciar();
        servidorHTTP.iniciar();
    }

    public void detener() {
        servidorTCP.detener();
        servidorHTTP.detener();
    }

    public PersonaDTO consultarPersona(String cedula) throws ConsultaPadronException {
        return servicioPadron.consultarPorCedula(cedula);
    }
}
