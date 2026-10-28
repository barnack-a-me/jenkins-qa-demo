package edu.qa.tienda;

import edu.qa.tienda.model.Producto;
import edu.qa.tienda.repository.ProductoJdbcRepository;
import edu.qa.tienda.service.ProductoService;

/** Aplicación de consola: lista productos, vende una unidad y muestra el precio final. */
public class App {

    public static void main(String[] args) {
        String url = System.getenv().getOrDefault("DB_URL", "jdbc:postgresql://localhost:5432/tienda");
        String user = System.getenv().getOrDefault("DB_USER", "qa_user");
        String pass = System.getenv().getOrDefault("DB_PASS", "qa_pass");

        ProductoService service = new ProductoService(new ProductoJdbcRepository(url, user, pass));

        System.out.println("=== Inventario inicial ===");
        service.listar().forEach(p ->
                System.out.printf("%d | %-18s | Bs %8.2f | stock %d%n", p.id(), p.nombre(), p.precio(), p.stock()));

        service.vender(1L, 1);
        System.out.println("Se vendio 1 unidad del producto 1");
        System.out.println("Precio final (25 % desc. + IVA 13 %) producto 1: Bs "
                + service.precioFinal(1L, 25));

        System.out.println("=== Inventario final ===");
        for (Producto p : service.listar()) {
            System.out.printf("%d | %-18s | stock %d%n", p.id(), p.nombre(), p.stock());
        }
    }
}
