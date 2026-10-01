public class Ubicacion {
    private final String codigo;
    private final String nombre;
    private final String direccion;
    private int riesgo;
    private String estado;

    // Aqui validamos los datos desde que se crea el lugar.
    public Ubicacion(String codigo, String nombre, String direccion, int riesgo, String estado) {
        this.codigo = validarTexto(codigo, "Codigo");
        this.nombre = validarTexto(nombre, "Nombre");
        this.direccion = validarTexto(direccion, "Direccion o descripcion");
        actualizar(riesgo, estado);
    }

    public String getCodigo() { return codigo; }
    public String getNombre() { return nombre; }
    public String getDireccion() { return direccion; }
    public int getRiesgo() { return riesgo; }
    public String getEstado() { return estado; }

    // Primero revisamos todo, asi no queda un cambio guardado a medias.
    public void actualizar(int riesgo, String estado) {
        if (riesgo < 1 || riesgo > 10) {
            throw new IllegalArgumentException("El riesgo debe estar entre 1 y 10.");
        }
        String estadoValido = validarTexto(estado, "Estado");
        this.riesgo = riesgo;
        this.estado = estadoValido;
    }

    private static String validarTexto(String texto, String campo) {
        if (texto == null || texto.trim().isEmpty()) {
            throw new IllegalArgumentException(campo + " no puede estar vacio.");
        }
        return texto.trim();
    }

    @Override
    public String toString() {
        return codigo + " | " + nombre + " | " + direccion
                + " | riesgo: " + riesgo + "/10 | estado: " + estado;
    }
}
