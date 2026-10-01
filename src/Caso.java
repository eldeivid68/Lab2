import java.util.ArrayList;

public class Caso {
    public static final int MAX_UBICACIONES = 5;
    private final String nombre;
    private final String codigo;
    private final String detective;
    private final Ubicacion[] ubicaciones;
    private final ArrayList<Pista> pistas;

    // Cada caso nuevo empieza con cinco espacios vacios y ninguna pista.
    public Caso(String nombre, String codigo, String detective) {
        this.nombre = validarTexto(nombre, "Nombre del caso");
        this.codigo = validarTexto(codigo, "Codigo del caso");
        this.detective = validarTexto(detective, "Detective");
        ubicaciones = new Ubicacion[MAX_UBICACIONES];
        pistas = new ArrayList<Pista>();
    }

    public String getNombre() { return nombre; }
    public String getCodigo() { return codigo; }
    public String getDetective() { return detective; }

    // Devolvemos copias de las colecciones para proteger sus espacios y registros.
    // Los objetos que contienen siguen siendo los mismos, no son copias profundas.
    public Ubicacion[] getUbicaciones() { return ubicaciones.clone(); }
    public ArrayList<Pista> getPistas() { return new ArrayList<Pista>(pistas); }

    public void validarPosicionDisponible(int posicion) {
        validarPosicion(posicion);
        if (ubicaciones[posicion] != null) {
            throw new IllegalArgumentException("Esa posicion ya tiene una ubicacion.");
        }
    }

    public void registrarUbicacion(int posicion, Ubicacion ubicacion) {
        validarPosicionDisponible(posicion);
        if (ubicacion == null) {
            throw new IllegalArgumentException("La ubicacion no puede ser null.");
        }
        ubicaciones[posicion] = ubicacion;
    }

    public Ubicacion consultarUbicacion(int posicion) {
        validarPosicion(posicion);
        if (ubicaciones[posicion] == null) {
            throw new IllegalArgumentException("Esa posicion esta vacia.");
        }
        return ubicaciones[posicion];
    }

    public void modificarUbicacion(int posicion, int riesgo, String estado) {
        consultarUbicacion(posicion).actualizar(riesgo, estado);
    }

    public void descartarUbicacion(int posicion) {
        consultarUbicacion(posicion);
        // Con null dejamos ese espacio libre otra vez.
        ubicaciones[posicion] = null;
    }

    public void registrarPista(Pista pista) {
        if (pista == null) {
            throw new IllegalArgumentException("La pista no puede ser null.");
        }
        if (buscarPista(pista.getCodigo()) != null) {
            throw new IllegalArgumentException("Ya existe una pista con ese codigo.");
        }
        pistas.add(pista);
    }

    // Recorremos el ArrayList hasta encontrar el codigo que nos pidieron.
    public Pista buscarPista(String codigo) {
        String codigoValido = validarTexto(codigo, "Codigo de pista");
        for (Pista pista : pistas) {
            if (pista.getCodigo().equals(codigoValido)) {
                return pista;
            }
        }
        return null;
    }

    public void modificarPista(String codigo, String descripcion, String tipoEvidencia,
                              int importancia, int confiabilidad) {
        exigirPista(codigo).actualizar(descripcion, tipoEvidencia, importancia, confiabilidad);
    }

    public void eliminarPista(String codigo) {
        // La busqueda termina antes de quitar el objeto del ArrayList.
        pistas.remove(exigirPista(codigo));
    }

    private Pista exigirPista(String codigo) {
        Pista pista = buscarPista(codigo);
        if (pista == null) {
            throw new IllegalArgumentException("No existe una pista con ese codigo.");
        }
        return pista;
    }

    private void validarPosicion(int posicion) {
        if (posicion < 0 || posicion >= ubicaciones.length) {
            throw new IllegalArgumentException("La posicion debe estar entre 1 y 5.");
        }
    }

    private static String validarTexto(String texto, String campo) {
        if (texto == null || texto.trim().isEmpty()) {
            throw new IllegalArgumentException(campo + " no puede estar vacio.");
        }
        return texto.trim();
    }

    public String generarReporte() {
        int registradas = 0;
        Ubicacion mayorRiesgo = null;
        for (Ubicacion ubicacion : ubicaciones) {
            if (ubicacion != null) {
                registradas++;
                if (mayorRiesgo == null || ubicacion.getRiesgo() > mayorRiesgo.getRiesgo()) {
                    mayorRiesgo = ubicacion;
                }
            }
        }
        String reporte = "===== REPORTE DE INVESTIGACION =====\n"
                + "Caso: " + nombre + " | Codigo: " + codigo + "\n"
                + "Detective: " + detective + "\n"
                + "Ubicaciones registradas: " + registradas + "\n"
                + "Espacios disponibles: " + (MAX_UBICACIONES - registradas) + "\n"
                + "Ubicacion de mayor riesgo: "
                + (mayorRiesgo == null ? "Sin ubicaciones" : mayorRiesgo) + "\n"
                + "Pistas registradas: " + pistas.size() + "\n";

        // Si no hay pistas, no usamos get(0) ni dividimos entre cero.
        if (pistas.isEmpty()) {
            return reporte + "Pista mas importante: Sin pistas\n"
                    + "Pista mas confiable: Sin pistas\n"
                    + "Promedio de importancia: No aplica";
        }
        Pista masImportante = pistas.get(0);
        Pista masConfiable = pistas.get(0);
        double sumaImportancia = 0;
        for (Pista pista : pistas) {
            sumaImportancia += pista.getImportancia();
            if (pista.getImportancia() > masImportante.getImportancia()) {
                masImportante = pista;
            }
            if (pista.getConfiabilidad() > masConfiable.getConfiabilidad()) {
                masConfiable = pista;
            }
        }
        // Si hay empate, conservamos la primera que encontramos.
        return reporte + "Pista mas importante: " + masImportante + "\n"
                + "Pista mas confiable: " + masConfiable + "\n"
                + String.format("Promedio de importancia: %.2f", sumaImportancia / pistas.size());
    }
}
