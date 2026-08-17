package proyecto2.Servicios;

import java.io.IOException;
import proyecto2.DTO.PersonaDTO;
import proyecto2.Entidades.DistritoElectoral;
import proyecto2.Entidades.Persona;
import proyecto2.Excepciones.ArchivoPadronException;
import proyecto2.Excepciones.CedulaInvalidaException;
import proyecto2.Excepciones.ConsultaPadronException;
import proyecto2.Excepciones.FormatoDatosException;
import proyecto2.Excepciones.PersonaNoEncontradaException;
import proyecto2.Repositorios.RepositorioDistritos;
import proyecto2.Repositorios.RepositorioPadron;

public class ServicioPadron {

    private final RepositorioPadron repositorioPadron;
    private final RepositorioDistritos repositorioDistritos;

    public ServicioPadron(RepositorioPadron repositorioPadron, RepositorioDistritos repositorioDistritos) {
        this.repositorioPadron = repositorioPadron;
        this.repositorioDistritos = repositorioDistritos;
    }

    public PersonaDTO consultarPorCedula(String cedula) throws ConsultaPadronException {
        String cedulaNormalizada = normalizarCedula(cedula);
        validarCedula(cedulaNormalizada);

        try {
            Persona persona = repositorioPadron.buscarPorCedula(cedulaNormalizada)
                    .orElseThrow(() -> new PersonaNoEncontradaException(
                    "No se encontro una persona con la cedula indicada."));

            DistritoElectoral distrito = repositorioDistritos.buscarPorCodigo(persona.getCodigoElectoral())
                    .orElseThrow(() -> new ConsultaPadronException(
                    "No se encontro el distrito electoral asociado al codigo indicado.", 404));

            return new PersonaDTO(
                    persona.getCedula(),
                    persona.getNombre(),
                    persona.getPrimerApellido(),
                    persona.getSegundoApellido(),
                    persona.getCodigoElectoral(),
                    distrito.getProvincia(),
                    distrito.getCanton(),
                    distrito.getDistrito()
            );
        } catch (IOException ex) {
            throw new ArchivoPadronException("Error al leer los archivos del padron.", ex);
        } catch (FormatoDatosException ex) {
            throw ex;
        }
    }

    private void validarCedula(String cedula) throws CedulaInvalidaException {
        if (cedula.isEmpty()) {
            throw new CedulaInvalidaException("La cedula no puede ir vacia.");
        }

        if (!cedula.matches("\\d{9}")) {
            throw new CedulaInvalidaException("La cedula debe contener exactamente 9 digitos numericos.");
        }
    }

    private String normalizarCedula(String cedula) {
        return cedula == null ? "" : cedula.trim();
    }
}
