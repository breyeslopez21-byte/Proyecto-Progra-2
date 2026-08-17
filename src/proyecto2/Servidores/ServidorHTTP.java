package proyecto2.Servidores;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import proyecto2.DTO.PersonaDTO;
import proyecto2.Excepciones.ConsultaPadronException;
import proyecto2.Servicios.ServicioPadron;
import proyecto2.Utilidades.JsonUtil;

public class ServidorHTTP {

    private final int puerto;
    private final ExecutorService executor;
    private final ServicioPadron servicioPadron;
    private HttpServer server;

    public ServidorHTTP(int puerto, int cantidadHilos, ServicioPadron servicioPadron) {
        this.puerto = puerto;
        this.executor = Executors.newFixedThreadPool(Math.max(1, cantidadHilos));
        this.servicioPadron = servicioPadron;
    }

    public void iniciar() throws IOException {
        server = HttpServer.create(new InetSocketAddress(puerto), 0);
        server.createContext("/padron", new ManejadorPadron());
        server.setExecutor(executor);
        server.start();
    }

    public void detener() {
        if (server != null) {
            server.stop(0);
        }
        executor.shutdownNow();
    }

    private class ManejadorPadron implements HttpHandler {

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            try {
                if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
                    responderJson(exchange, 405, JsonUtil.errorAJson(405, "Metodo HTTP no permitido."));
                    return;
                }

                URI uri = exchange.getRequestURI();
                String ruta = uri.getPath();
                String prefijo = "/padron";

                if (!ruta.startsWith(prefijo)) {
                    responderJson(exchange, 404, JsonUtil.errorAJson(404, "Ruta HTTP inexistente."));
                    return;
                }

                String cedula = extraerCedula(ruta, prefijo);
                if (cedula == null) {
                    responderJson(exchange, 400, JsonUtil.errorAJson(400, "Ruta HTTP incompleta. Usa /padron/{cedula}."));
                    return;
                }

                PersonaDTO persona = servicioPadron.consultarPorCedula(cedula);
                responderJson(exchange, 200, JsonUtil.personaAJson(persona));
            } catch (ConsultaPadronException ex) {
                responderJson(exchange, ex.getCodigo(), JsonUtil.errorAJson(ex.getCodigo(), ex.getMessage()));
            } catch (Exception ex) {
                responderJson(exchange, 500, JsonUtil.errorAJson(500, "Error inesperado durante el procesamiento HTTP."));
            }
        }

        private String extraerCedula(String ruta, String prefijo) {
            if (ruta.equals(prefijo) || ruta.equals(prefijo + "/")) {
                return null;
            }

            if (!ruta.startsWith(prefijo + "/")) {
                return null;
            }

            String cedula = ruta.substring((prefijo + "/").length()).trim();
            if (cedula.isEmpty() || cedula.contains("/")) {
                return null;
            }

            return cedula;
        }

        private void responderJson(HttpExchange exchange, int codigo, String json) throws IOException {
            byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
            Headers headers = exchange.getResponseHeaders();
            headers.set("Content-Type", "application/json; charset=UTF-8");
            exchange.sendResponseHeaders(codigo, bytes.length);
            try (OutputStream salida = exchange.getResponseBody()) {
                salida.write(bytes);
            } finally {
                exchange.close();
            }
        }
    }
}
