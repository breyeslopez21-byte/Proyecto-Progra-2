package proyecto2.Repositorios;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import proyecto2.Entidades.Persona;
import proyecto2.Excepciones.FormatoDatosException;

public class RepositorioPadron {

    private static final Charset ENCODING = Charset.forName("Windows-1252");

    private final Path rutaPadron;

    public RepositorioPadron(Path rutaPadron) {
        this.rutaPadron = rutaPadron;
    }

    public Optional<Persona> buscarPorCedula(String cedula) throws IOException, FormatoDatosException {
        if (!Files.exists(rutaPadron)) {
            throw new IOException("No existe el archivo del padron: " + rutaPadron);
        }

        try (BufferedReader lector = Files.newBufferedReader(rutaPadron, ENCODING)) {
            String linea;
            while ((linea = lector.readLine()) != null) {
                if (linea.trim().isEmpty()) {
                    continue;
                }

                String[] datos = linea.split(",", -1);
                if (datos.length < 7) {
                    throw new FormatoDatosException("Formato invalido en PADRON.txt: " + linea);
                }

                if (normalizar(datos[0]).equals(normalizar(cedula))) {
                    return Optional.of(new Persona(
                            normalizar(datos[0]),
                            normalizar(datos[1]),
                            normalizar(datos[4]),
                            normalizar(datos[5]),
                            normalizar(datos[6])
                    ));
                }
            }
        }

        return Optional.empty();
    }

    private String normalizar(String valor) {
        return valor == null ? "" : valor.trim();
    }
}
