/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto2.main;

import java.util.Scanner;
import proyecto2.DTO.PersonaDTO;
import proyecto2.Excepciones.ConsultaPadronException;
import proyecto2.Servidores.ServidorPadronApp;

public class Main {

    public static void main(String[] args) {
        ServidorPadronApp aplicacion = new ServidorPadronApp();

        try {
            aplicacion.iniciar();
            System.out.println("Servidor de padrón iniciado.");
            System.out.println("TCP  -> puerto " + proyecto2.Utilidades.Configuracion.puertoTcp());
            System.out.println("HTTP -> puerto " + proyecto2.Utilidades.Configuracion.puertoHttp());
            System.out.println("PADRON   -> " + proyecto2.Utilidades.Configuracion.rutaPadron());
            System.out.println("DISTELEC -> " + proyecto2.Utilidades.Configuracion.rutaDistritos());

            System.out.println();
            System.out.println("Ingrese una cedula para consultar o escriba 'salir' para cerrar.");

            try (Scanner scanner = new Scanner(System.in)) {
                while (true) {
                    System.out.print("Cedula: ");
                    if (!scanner.hasNextLine()) {
                        break;
                    }

                    String entrada = scanner.nextLine().trim();
                    if (entrada.equalsIgnoreCase("salir")) {
                        break;
                    }

                    if (entrada.isEmpty()) {
                        System.out.println("Error 400: La cedula no puede ir vacia.");
                        continue;
                    }

                    try {
                        PersonaDTO persona = aplicacion.consultarPersona(entrada);
                        System.out.println("Cedula: " + persona.getCedula());
                        System.out.println("Nombre: " + persona.getNombre());
                        System.out.println("Apellidos: " + persona.getPrimerApellido() + " " + persona.getSegundoApellido());
                        System.out.println("Codigo electoral: " + persona.getCodigoElectoral());
                        System.out.println("Provincia: " + persona.getProvincia());
                        System.out.println("Canton: " + persona.getCanton());
                        System.out.println("Distrito: " + persona.getDistrito());
                    } catch (ConsultaPadronException ex) {
                        System.out.println("Error " + ex.getCodigo() + ": " + ex.getMessage());
                    }

                    System.out.println();
                }
            }
        } catch (Exception ex) {
            System.err.println("No se pudo iniciar el servidor: " + ex.getMessage());
            ex.printStackTrace(System.err);
        } finally {
            aplicacion.detener();
        }
    }
}
