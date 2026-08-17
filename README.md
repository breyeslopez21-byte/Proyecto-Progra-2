# Proyecto Progra 3

Servidor en Java para consultar información del padrón electoral desde archivos de texto, con soporte concurrente para:

- TCP con solicitudes `GET|cedula`
- HTTP con solicitudes `GET /padron/{cedula}`
- Respuestas JSON en ambos casos

## Estructura general

- `proyecto2.Entidades`: modelo de dominio (`Persona`, `DistritoElectoral`)
- `proyecto2.DTO`: objetos de respuesta (`PersonaDTO`, `ErrorDTO`)
- `proyecto2.Repositorios`: lectura y búsqueda en `PADRON.txt` y `distelec.txt`
- `proyecto2.Servicios`: lógica de negocio central
- `proyecto2.Servidores`: servidores TCP y HTTP
- `proyecto2.Utilidades`: configuración y JSON

## Ejecucion

El punto de entrada es `proyecto2.main.Main`.

Por defecto usa:

- TCP: `5000`
- HTTP: `8080`
- Padrón: `C:\Users\breye\Downloads\padron_completo\PADRON_COMPLETO.txt`
- Distritos: `C:\Users\breye\Downloads\padron_completo\distelec.txt`

### Sobrescribir configuracion

Se pueden ajustar mediante propiedades del sistema:

- `padron.tcp.port`
- `padron.http.port`
- `padron.file`
- `distritos.file`

Ejemplo:

```bash
java -Dpadron.tcp.port=5000 -Dpadron.http.port=8080 ^
     -Dpadron.file="C:\ruta\PADRON.txt" ^
     -Ddistritos.file="C:\ruta\distelec.txt" ^
     -cp "out;lib/*" proyecto2.main.Main
```

## Ejemplos de consulta

### TCP

```text
GET|115550555
```

Respuesta exitosa:

```json
{
  "cedula": "115550555",
  "nombre": "JUAN",
  "primerApellido": "PEREZ",
  "segundoApellido": "RODRIGUEZ",
  "codigoElectoral": "101001",
  "provincia": "SAN JOSE",
  "canton": "CENTRAL",
  "distrito": "CARMEN"
}
```

### HTTP

```http
GET /padron/115550555
```

Respuesta de error:

```json
{
  "error": true,
  "codigo": 404,
  "mensaje": "No se encontro una persona con la cedula indicada."
}
```

## Notas

- No se utiliza base de datos.
- La capa de presentación no accede directamente a los archivos.
- La atención de clientes es concurrente.

