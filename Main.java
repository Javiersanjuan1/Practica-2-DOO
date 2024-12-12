import java.io.*;
import java.util.*;
import java.util.function.Predicate;

class Marca implements Serializable {
    private String nombre;
    private String pais;
    private double facturacion;

    public Marca(String nombre, String pais, double facturacion) {
        this.nombre = nombre;
        this.pais = pais;
        this.facturacion = facturacion;
    }

    public String getNombre() {
        return nombre;
    }

    public String getPais() {
        return pais;
    }

    public double getFacturacion() {
        return facturacion;
    }

    @Override
    public String toString() {
        return "Marca: " + nombre + ", País: " + pais + ", Facturación: " + facturacion;
    }
}

abstract class ArticuloElectronico implements Comparable<ArticuloElectronico>, Serializable {
    protected String nombre;
    protected Marca marca;
    protected double precio;

    public ArticuloElectronico(String nombre, Marca marca, double precio) {
        this.nombre = nombre;
        this.marca = marca;
        this.precio = precio;
    }

    public String getNombre() {
        return nombre;
    }

    public Marca getMarca() {
        return marca;
    }

    public double getPrecio() {
        return precio;
    }

    @Override
    public int compareTo(ArticuloElectronico otro) {
        int comparacionMarca = this.marca.getNombre().compareTo(otro.getMarca().getNombre());
        if (comparacionMarca != 0) return comparacionMarca;
        int comparacionPrecio = Double.compare(this.precio, otro.precio);
        if (comparacionPrecio != 0) return comparacionPrecio;
        return this.nombre.compareTo(otro.getNombre());
    }

    @Override
    public abstract String toString();
}

class Televisor extends ArticuloElectronico {
    private int pulgadas;
    private String tipoPantalla;

    public Televisor(String nombre, Marca marca, double precio, int pulgadas, String tipoPantalla) {
        super(nombre, marca, precio);
        this.pulgadas = pulgadas;
        this.tipoPantalla = tipoPantalla;
    }

    public int getPulgadas() {
        return pulgadas;
    }

    public String getTipoPantalla() {
        return tipoPantalla;
    }

    @Override
    public String toString() {
        return "Televisor: " + nombre + ", Marca: " + marca.getNombre() + ", Precio: " + precio +
                ", Pulgadas: " + pulgadas + ", Tipo Pantalla: " + tipoPantalla;
    }
}


class Movil extends ArticuloElectronico {
    private int ram;
    private String sistemaOperativo;

    public Movil(String nombre, Marca marca, double precio, int ram, String sistemaOperativo) {
        super(nombre, marca, precio);
        this.ram = ram;
        this.sistemaOperativo = sistemaOperativo;
    }

    public int getRam() {
        return ram;
    }

    public String getSistemaOperativo() {
        return sistemaOperativo;
    }

    @Override
    public String toString() {
        return "Móvil: " + nombre + ", Marca: " + marca.getNombre() + ", Precio: " + precio +
                ", RAM: " + ram + "GB, Sistema Operativo: " + sistemaOperativo;
    }
}

class GestorArticulos {
    private List<Marca> marcas = new ArrayList<>();
    private List<ArticuloElectronico> articulos = new ArrayList<>();

    public void agregarMarca(Marca marca) {
        if (buscarMarca(marca.getNombre()) == null) {
            marcas.add(marca);
        } else {
            System.out.println("La marca ya existe.");
        }
    }

    public Marca buscarMarca(String nombre) {
        return marcas.stream().filter(m -> m.getNombre().equalsIgnoreCase(nombre)).findFirst().orElse(null);
    }

    public void agregarArticulo(ArticuloElectronico articulo) {
        if (buscarMarca(articulo.getMarca().getNombre()) != null) {
            articulos.add(articulo);
        } else {
            System.out.println("La marca " + articulo.getMarca().getNombre() + " no existe. Por favor, agréguela primero.");
        }
    }

    public void listarMarcas() {
        if (marcas.isEmpty()) {
            System.out.println("No hay marcas registradas.");
        } else {
            marcas.stream().sorted(Comparator.comparingDouble(Marca::getFacturacion).reversed())
                    .forEach(System.out::println);
        }
    }

    public void listarArticulos() {
        if (articulos.isEmpty()) {
            System.out.println("No hay artículos registrados.");
        } else {
            articulos.stream().sorted().forEach(System.out::println);
            System.out.println("\nResumen de artículos:");
            long totalTelevisores = articulos.stream().filter(a -> a instanceof Televisor).count();
            long totalMoviles = articulos.stream().filter(a -> a instanceof Movil).count();
            System.out.println("Total de televisores: " + totalTelevisores);
            System.out.println("Total de móviles: " + totalMoviles);
        }
    }

    public void guardarDatos() throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("datos.dat"))) {
            out.writeObject(marcas);
            out.writeObject(articulos);
            System.out.println("Datos guardados correctamente.");
        }
    }

    @SuppressWarnings("unchecked")
    public void cargarDatos() throws IOException, ClassNotFoundException {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("datos.dat"))) {
            marcas = (List<Marca>) in.readObject();
            articulos = (List<ArticuloElectronico>) in.readObject();
            System.out.println("Datos cargados correctamente.");
        }
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        GestorArticulos gestor = new GestorArticulos();

        try {
            gestor.cargarDatos();
        } catch (Exception e) {
            System.out.println("No se encontraron datos previos.");
        }

        boolean salir = false;

        while (!salir) {
            System.out.println("\nGestión de Artículos Electrónicos");
            System.out.println("1. Añadir Marca");
            System.out.println("2. Añadir Televisor");
            System.out.println("3. Añadir Móvil");
            System.out.println("4. Listar Marcas");
            System.out.println("5. Listar Artículos");
            System.out.println("6. Salir");
            System.out.print("Seleccione una opción: ");

            int opcion = scanner.nextInt();
            scanner.nextLine(); // Consumir nueva línea

            switch (opcion) {
                case 1:
                    System.out.print("Ingrese nombre de la marca: ");
                    String nombreMarca = scanner.nextLine();
                    System.out.print("Ingrese país de la marca: ");
                    String pais = scanner.nextLine();
                    System.out.print("Ingrese facturación: ");
                    double facturacion = scanner.nextDouble();
                    scanner.nextLine();
                    gestor.agregarMarca(new Marca(nombreMarca, pais, facturacion));
                    break;
                case 2:
                    System.out.print("Ingrese nombre de la marca: ");
                    String marcaTv = scanner.nextLine();
                    Marca marcaEncontrada = gestor.buscarMarca(marcaTv);
                    if (marcaEncontrada == null) {
                        System.out.println("Marca no encontrada. Agregue la marca primero.");
                    } else {
                        System.out.print("Ingrese nombre del televisor: ");
                        String nombreTv = scanner.nextLine();
                        System.out.print("Ingrese precio del televisor: ");
                        double precioTv = scanner.nextDouble();
                        System.out.print("Ingrese tamaño en pulgadas: ");
                        int pulgadas = scanner.nextInt();
                        scanner.nextLine();
                        System.out.print("Ingrese tipo de pantalla (LED, QLED, OLED, QNED): ");
                        String tipoPantalla = scanner.nextLine();
                        gestor.agregarArticulo(new Televisor(nombreTv, marcaEncontrada, precioTv, pulgadas, tipoPantalla));
                    }
                    break;
                case 3:
                    System.out.print("Ingrese nombre de la marca: ");
                    String marcaMovil = scanner.nextLine();
                    Marca marcaMovilEncontrada = gestor.buscarMarca(marcaMovil);
                    if (marcaMovilEncontrada == null) {
                        System.out.println("Marca no encontrada. Agregue la marca primero.");
                    } else {
                        System.out.print("Ingrese nombre del móvil: ");
                        String nombreMovil = scanner.nextLine();
                        System.out.print("Ingrese precio del móvil: ");
                        double precioMovil = scanner.nextDouble();
                        System.out.print("Ingrese tamaño de RAM en GB: ");
                        int ram = scanner.nextInt();
                        scanner.nextLine();
                        System.out.print("Ingrese sistema operativo (Android, iOS): ");
                        String sistemaOperativo = scanner.nextLine();
                        gestor.agregarArticulo(new Movil(nombreMovil, marcaMovilEncontrada, precioMovil, ram, sistemaOperativo));
                    }
                    break;
                case 4:
                    gestor.listarMarcas();
                    break;
                case 5:
                    gestor.listarArticulos();
                    break;
                case 6:
                    try {
                        gestor.guardarDatos();
                    } catch (IOException e) {
                        System.out.println("Error al guardar datos: " + e.getMessage());
                    }
                    salir = true;
                    break;
                default:
                    System.out.println("Opción no válida. Por favor, elija una opción del menú.");
                    break;
            }
        }

        scanner.close();
    }
}

