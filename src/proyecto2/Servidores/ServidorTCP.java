package proyecto2.Servidores;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import proyecto2.DTO.PersonaDTO;
import proyecto2.Excepciones.ConsultaPadronException;
import proyecto2.Servicios.ServicioPadron;
import proyecto2.Utilidades.JsonUtil;

public class ServidorTCP {

    private final int puerto;
    private final ExecutorService executor;
    private final ServicioPadron servicioPadron;
    private volatile boolean ejecutando;
    private ServerSocket serverSocket;
    private Thread hiloAceptador;

    public ServidorTCP(int puerto, int cantidadHilos, ServicioPadron servicioPadron) {
        this.puerto = puerto;
        this.executor = Executors.newFixedThreadPool(Math.max(1, cantidadHilos));
        this.servicioPadron = servicioPadron;
    }

    public void iniciar() throws IOException {
        serverSocket = new ServerSocket(puerto);
        ejecutando = true;
        hiloAceptador = new Thread(this::aceptarClientes, "servidor-tcp-aceptador");
        hiloAceptador.start();
    }

    public void detener() {
        ejecutando = false;
        if (serverSocket != null && !serverSocket.isClosed()) {
            try {
                serverSocket.close();
            } catch (IOException ex) {
                // Se ignora al cerrar.
            }
        }
        executor.shutdownNow();
    }

    private void aceptarClientes() {
        while (ejecutando) {
            try {
                Socket socket = serverSocket.accept();
                executor.submit(() -> atenderCliente(socket));
            } catch (SocketException ex) {
                if (ejecutando) {
                    System.err.println("Error en el servidor TCP: " + ex.getMessage());
                }
                break;
            } catch (IOException ex) {
                if (ejecutando) {
                    System.err.println("Error aceptando cliente TCP: " + ex.getMessage());
                }
            }
        }
    }

    private void atenderCliente(Socket socket) {
        try (Socket cliente = socket;
                BufferedReader entrada = new BufferedReader(new InputStreamReader(cliente.getInputStream(), StandardCharsets.UTF_8));
                PrintWriter salida = new PrintWriter(cliente.getOutputStream(), true, StandardCharsets.UTF_8)) {

            String solicitud = entrada.readLine();
            String respuesta = procesarSolicitud(solicitud);
            salida.println(respuesta);
        } catch (IOException ex) {
            System.err.println("Error atendiendo cliente TCP: " + ex.getMessage());
        }
    }

    private String procesarSolicitud(String solicitud) {
        try {
            if (solicitud == null || solicitud.trim().isEmpty()) {
                return JsonUtil.errorAJson(400, "Solicitud TCP vacia o incompleta.");
            }

            String[] partes = solicitud.split("\\|", -1);
            if (partes.length != 2) {
                return JsonUtil.errorAJson(400, "Solicitud TCP invalida. Formato esperado: GET|cedula");
            }

            String comando = limpiarBOM(partes[0].trim());
            String cedula = limpiarBOM(partes[1].trim());

            if (!"GET".equalsIgnoreCase(comando)) {
                return JsonUtil.errorAJson(400, "Comando TCP desconocido.");
            }

            PersonaDTO persona = servicioPadron.consultarPorCedula(cedula);
            return JsonUtil.personaAJson(persona);
        } catch (ConsultaPadronException ex) {
            return JsonUtil.errorAJson(ex.getCodigo(), ex.getMessage());
        } catch (Exception ex) {
            return JsonUtil.errorAJson(500, "Error inesperado durante el procesamiento TCP.");
        }
    }

    private String limpiarBOM(String valor) {
        if (valor == null || valor.isEmpty()) {
            return "";
        }

        return valor.charAt(0) == '\uFEFF' ? valor.substring(1) : valor;
    }
}
