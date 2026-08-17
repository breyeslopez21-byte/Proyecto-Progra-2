package proyecto2.Repositorios;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import proyecto2.Entidades.DistritoElectoral;
import proyecto2.Excepciones.FormatoDatosException;

public class RepositorioDistritos {

    private static final Charset ENCODING = Charset.forName("Windows-1252");

    private final Path rutaDistritos;

    public RepositorioDistritos(Path rutaDistritos) {
        this.rutaDistritos = rutaDistritos;
    }

    public Optional<DistritoElectoral> buscarPorCodigo(String codigoElectoral) throws IOException, FormatoDatosException {
        if (!Files.exists(rutaDistritos)) {
            throw new IOException("No existe el archivo de distritos: " + rutaDistritos);
        }

        try (BufferedReader lector = Files.newBufferedReader(rutaDistritos, ENCODING)) {
            String linea;
            while ((linea = lector.readLine()) != null) {
                if (linea.trim().isEmpty()) {
                    continue;
                }

                String[] datos = linea.split(",", -1);
                if (datos.length < 4) {
                    throw new FormatoDatosException("Formato invalido en distelec.txt: " + linea);
                }

                if (normalizar(datos[0]).equals(normalizar(codigoElectoral))) {
                    return Optional.of(new DistritoElectoral(
                            normalizar(datos[0]),
                            normalizar(datos[1]),
                            normalizar(datos[2]),
                            normalizar(datos[3])
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
