package proyecto2.Utilidades;

import proyecto2.DTO.ErrorDTO;
import proyecto2.DTO.PersonaDTO;

public final class JsonUtil {

    private JsonUtil() {
    }

    public static String personaAJson(PersonaDTO persona) {
        StringBuilder json = new StringBuilder(256);
        json.append('{');
        agregarCampo(json, "cedula", persona.getCedula());
        agregarCampo(json, "nombre", persona.getNombre());
        agregarCampo(json, "primerApellido", persona.getPrimerApellido());
        agregarCampo(json, "segundoApellido", persona.getSegundoApellido());
        agregarCampo(json, "codigoElectoral", persona.getCodigoElectoral());
        agregarCampo(json, "provincia", persona.getProvincia());
        agregarCampo(json, "canton", persona.getCanton());
        agregarCampo(json, "distrito", persona.getDistrito());
        eliminarUltimaComa(json);
        json.append('}');
        return json.toString();
    }

    public static String errorAJson(ErrorDTO error) {
        StringBuilder json = new StringBuilder(128);
        json.append('{');
        agregarCampo(json, "error", error.isError());
        agregarCampo(json, "codigo", error.getCodigo());
        agregarCampo(json, "mensaje", error.getMensaje());
        eliminarUltimaComa(json);
        json.append('}');
        return json.toString();
    }

    public static String errorAJson(int codigo, String mensaje) {
        return errorAJson(new ErrorDTO(codigo, mensaje));
    }

    private static void agregarCampo(StringBuilder json, String nombre, String valor) {
        json.append('"').append(escape(nombre)).append('"').append(':');
        if (valor == null) {
            json.append("null");
        } else {
            json.append('"').append(escape(valor)).append('"');
        }
        json.append(',');
    }

    private static void agregarCampo(StringBuilder json, String nombre, int valor) {
        json.append('"').append(escape(nombre)).append('"').append(':').append(valor).append(',');
    }

    private static void agregarCampo(StringBuilder json, String nombre, boolean valor) {
        json.append('"').append(escape(nombre)).append('"').append(':').append(valor).append(',');
    }

    private static void eliminarUltimaComa(StringBuilder json) {
        int longitud = json.length();
        if (longitud > 0 && json.charAt(longitud - 1) == ',') {
            json.deleteCharAt(longitud - 1);
        }
    }

    private static String escape(String valor) {
        StringBuilder resultado = new StringBuilder(valor.length() + 16);
        for (int i = 0; i < valor.length(); i++) {
            char caracter = valor.charAt(i);
            switch (caracter) {
                case '"':
                    resultado.append("\\\"");
                    break;
                case '\\':
                    resultado.append("\\\\");
                    break;
                case '\b':
                    resultado.append("\\b");
                    break;
                case '\f':
                    resultado.append("\\f");
                    break;
                case '\n':
                    resultado.append("\\n");
                    break;
                case '\r':
                    resultado.append("\\r");
                    break;
                case '\t':
                    resultado.append("\\t");
                    break;
                default:
                    if (caracter < 0x20) {
                        resultado.append(String.format("\\u%04x", (int) caracter));
                    } else {
                        resultado.append(caracter);
                    }
                    break;
            }
        }
        return resultado.toString();
    }
}
