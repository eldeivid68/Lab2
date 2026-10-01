public class Pista {
    private final String codigo;
    private String descripcion;
    private String tipoEvidencia;
    private int importancia;
    private int confiabilidad;

    public Pista(String codigo, String descripcion, String tipoEvidencia,
                 int importancia, int confiabilidad) {
        this.codigo = validarTexto(codigo, "Codigo");
        actualizar(descripcion, tipoEvidencia, importancia, confiabilidad);
    }

    public String getCodigo() { return codigo; }
    public String getDescripcion() { return descripcion; }
    public String getTipoEvidencia() { return tipoEvidencia; }
    public int getImportancia() { return importancia; }
    public int getConfiabilidad() { return confiabilidad; }

    // Si un dato falla, salimos antes de cambiar la pista que ya teniamos.
    public void actualizar(String descripcion, String tipoEvidencia,
                           int importancia, int confiabilidad) {
        String descripcionValida = validarTexto(descripcion, "Descripcion");
        String tipoValido = validarTexto(tipoEvidencia, "Tipo de evidencia");
        if (importancia < 1 || importancia > 10) {
            throw new IllegalArgumentException("La importancia debe estar entre 1 y 10.");
        }
        if (confiabilidad < 0 || confiabilidad > 100) {
            throw new IllegalArgumentException("La confiabilidad debe estar entre 0 y 100.");
        }
        this.descripcion = descripcionValida;
        this.tipoEvidencia = tipoValido;
        this.importancia = importancia;
        this.confiabilidad = confiabilidad;
    }

    private static String validarTexto(String texto, String campo) {
        if (texto == null || texto.trim().isEmpty()) {
            throw new IllegalArgumentException(campo + " no puede estar vacio.");
        }
        return texto.trim();
    }

    @Override
    public String toString() {
        return codigo + " | " + descripcion + " | tipo: " + tipoEvidencia
                + " | importancia: " + importancia + "/10"
                + " | confiabilidad: " + confiabilidad + "%";
    }
}
