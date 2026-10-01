import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Main {
    private static final Scanner sc = new Scanner(System.in);
    private static Caso caso;

    public static void main(String[] args) {
        try {
            // Antes del menu necesitamos los datos del primer caso.
            while (caso == null) {
                try {
                    caso = crearCaso();
                } catch (IllegalArgumentException e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }
            boolean continuar = true;
            while (continuar) {
                try {
                    mostrarMenu();
                    continuar = ejecutarOpcion(leerEntero("Seleccione una opcion: "));
                } catch (InputMismatchException e) {
                    System.out.println("Error: debe ingresar un numero entero valido.");
                } catch (IllegalArgumentException e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }
        } catch (NoSuchElementException e) {
            System.out.println("La entrada se cerro. Terminando el programa.");
        } finally {
            // Cerramos el Scanner al terminar, incluso si se cerro la entrada.
            sc.close();
        }
        System.out.println("Cerrando la agencia de detectives.");
    }

    private static Caso crearCaso() {
        System.out.println("===== NUEVO CASO =====");
        String nombre = leerTexto("Nombre del caso: ");
        String codigo = leerTexto("Codigo del caso: ");
        String detective = leerTexto("Detective responsable: ");
        return new Caso(nombre, codigo, detective);
    }

    private static void mostrarMenu() {
        System.out.println("\n===== AGENCIA DE DETECTIVES =====");
        System.out.println("Caso actual: " + caso.getNombre());
        System.out.println("1. Nuevo caso");
        System.out.println("2. Registrar ubicacion");
        System.out.println("3. Consultar ubicaciones");
        System.out.println("4. Consultar una ubicacion");
        System.out.println("5. Modificar ubicacion");
        System.out.println("6. Descartar ubicacion");
        System.out.println("7. Registrar pista");
        System.out.println("8. Consultar pistas");
        System.out.println("9. Buscar pista");
        System.out.println("10. Modificar pista");
        System.out.println("11. Eliminar pista");
        System.out.println("12. Mostrar reporte de investigacion");
        System.out.println("13. Salir");
    }

    private static boolean ejecutarOpcion(int opcion) {
        switch (opcion) {
            // Solo reemplazamos el caso cuando el nuevo ya paso sus validaciones.
            case 1: caso = crearCaso(); break;
            case 2: registrarUbicacion(); break;
            case 3: consultarUbicaciones(); break;
            case 4: System.out.println(caso.consultarUbicacion(leerPosicion())); break;
            case 5: modificarUbicacion(); break;
            case 6:
                caso.descartarUbicacion(leerPosicion());
                System.out.println("Ubicacion descartada.");
                break;
            case 7: registrarPista(); break;
            case 8: consultarPistas(); break;
            case 9: buscarPista(); break;
            case 10: modificarPista(); break;
            case 11:
                caso.eliminarPista(leerTexto("Codigo de la pista: "));
                System.out.println("Pista eliminada.");
                break;
            case 12: System.out.println(caso.generarReporte()); break;
            case 13: return false;
            default: throw new IllegalArgumentException("Elija una opcion entre 1 y 13.");
        }
        return true;
    }

    private static int leerEntero(String mensaje) {
        System.out.print(mensaje);
        try {
            return sc.nextInt();
        } finally {
            // Limpiamos la linea aunque hayan escrito letras en vez de numeros.
            if (sc.hasNextLine()) {
                sc.nextLine();
            }
        }
    }

    private static String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return sc.nextLine();
    }

    private static int leerPosicion() {
        int posicion = leerEntero("Posicion (1 a 5): ");
        if (posicion < 1 || posicion > Caso.MAX_UBICACIONES) {
            throw new IllegalArgumentException("La posicion debe estar entre 1 y 5.");
        }
        // El usuario cuenta desde 1, pero los indices del arreglo empiezan en 0.
        return posicion - 1;
    }

    private static void registrarUbicacion() {
        int posicion = leerPosicion();
        caso.validarPosicionDisponible(posicion);
        String codigo = leerTexto("Codigo: ");
        String nombre = leerTexto("Nombre: ");
        String direccion = leerTexto("Direccion o descripcion del lugar: ");
        int riesgo = leerEntero("Nivel de riesgo (1-10): ");
        String estado = leerTexto("Estado: ");
        caso.registrarUbicacion(posicion, new Ubicacion(codigo, nombre, direccion, riesgo, estado));
        System.out.println("Ubicacion registrada.");
    }

    private static void consultarUbicaciones() {
        Ubicacion[] ubicaciones = caso.getUbicaciones();
        boolean hayUbicaciones = false;
        for (int i = 0; i < ubicaciones.length; i++) {
            if (ubicaciones[i] != null) {
                System.out.println("Posicion " + (i + 1) + ": " + ubicaciones[i]);
                hayUbicaciones = true;
            }
        }
        if (!hayUbicaciones) {
            System.out.println("No hay ubicaciones registradas.");
        }
    }

    private static void modificarUbicacion() {
        int posicion = leerPosicion();
        System.out.println("Actual: " + caso.consultarUbicacion(posicion));
        int riesgo = leerEntero("Nuevo riesgo (1-10): ");
        String estado = leerTexto("Nuevo estado: ");
        caso.modificarUbicacion(posicion, riesgo, estado);
        System.out.println("Ubicacion modificada.");
    }

    private static void registrarPista() {
        String codigo = leerTexto("Codigo: ");
        if (caso.buscarPista(codigo) != null) {
            throw new IllegalArgumentException("Ya existe una pista con ese codigo.");
        }
        String descripcion = leerTexto("Descripcion: ");
        String tipo = leerTexto("Tipo de evidencia: ");
        int importancia = leerEntero("Importancia (1-10): ");
        int confiabilidad = leerEntero("Confiabilidad (0-100): ");
        caso.registrarPista(new Pista(codigo, descripcion, tipo, importancia, confiabilidad));
        System.out.println("Pista registrada.");
    }

    private static void consultarPistas() {
        ArrayList<Pista> pistas = caso.getPistas();
        if (pistas.isEmpty()) {
            System.out.println("No hay pistas registradas.");
        }
        for (Pista pista : pistas) {
            System.out.println(pista);
        }
    }

    private static void buscarPista() {
        Pista pista = caso.buscarPista(leerTexto("Codigo de la pista: "));
        if (pista == null) {
            System.out.println("No existe una pista con ese codigo.");
        } else {
            System.out.println(pista);
        }
    }

    private static void modificarPista() {
        String codigo = leerTexto("Codigo de la pista: ");
        if (caso.buscarPista(codigo) == null) {
            throw new IllegalArgumentException("No existe una pista con ese codigo.");
        }
        String descripcion = leerTexto("Nueva descripcion: ");
        String tipo = leerTexto("Nuevo tipo de evidencia: ");
        int importancia = leerEntero("Nueva importancia (1-10): ");
        int confiabilidad = leerEntero("Nueva confiabilidad (0-100): ");
        caso.modificarPista(codigo, descripcion, tipo, importancia, confiabilidad);
        System.out.println("Pista modificada.");
    }
}
