package com.bootcamp;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;
import java.time.Duration;

/**
 * Carga realista contra Kong + Authelia + productos (+ catálogo + descuento).
 *
 * Cada usuario virtual:
 *   1. inicia sesión UNA vez en Authelia (Gatling guarda la cookie por usuario),
 *   2. repite N veces: consulta el listado -> elige un producto al azar -> consulta su detalle,
 *      con pausas como un usuario real ("think time").
 *
 * Parámetros (con -D), con valores pensados para un grupo en clase:
 *   usuarios=10  rampa=30 (segundos)  iteraciones=20  p95=800 (ms)
 * Ejemplo:  mvn gatling:test -Dusuarios=50 -Drampa=60
 */
public class PruebaCompleja extends Simulation {

    private static final int USUARIOS = Integer.getInteger("usuarios", 10);
    private static final int RAMPA = Integer.getInteger("rampa", 30);
    private static final int ITERACIONES = Integer.getInteger("iteraciones", 20);
    private static final int P95_MS = Integer.getInteger("p95", 800);

    private static final String LOGIN = Laboratorio.loginJson();

    private final HttpProtocolBuilder httpProtocol = http
            .baseUrl(Laboratorio.url())
            .acceptHeader("application/json")
            .userAgentHeader("gatling-bootcamp")
            // Si la sesión se pierde, Authelia responde 302: queremos verlo como ERROR, no seguirlo
            .disableFollowRedirect();

    private final ChainBuilder login = exec(
            http("1. Login en Authelia")
                    .post("/authelia/api/firstfactor")
                    .header("Content-Type", "application/json")
                    // Función (no texto): evita que Gatling interprete #{...} dentro de la contraseña
                    .body(StringBody(session -> LOGIN))
                    .check(status().is(200))
                    .check(jsonPath("$.status").is("OK")));

    private final ChainBuilder navegar = repeat(ITERACIONES).on(
            exec(http("2. Listado de productos")
                    .get("/api/v1/products")
                    .queryParam("size", "10")
                    .check(status().is(200))
                    // Elige un ID real del listado: no depende de qué datos tenga la base
                    .check(jsonPath("$.items[*].id").findRandom().saveAs("productoId")))
            .pause(Duration.ofMillis(500), Duration.ofMillis(1500))
            .exec(http("3. Detalle de producto")
                    .get("/api/v1/products/#{productoId}")
                    .check(status().is(200))
                    .check(jsonPath("$.finalPrice").exists()))
            .pause(Duration.ofMillis(500), Duration.ofMillis(1500)));

    private final ScenarioBuilder scn = scenario("Usuario navegando el catálogo")
            .exec(login)
            .exitHereIfFailed()      // sin sesión no tiene caso seguir
            .exec(navegar);

    {
        setUp(scn.injectOpen(rampUsers(USUARIOS).during(Duration.ofSeconds(RAMPA))))
                .protocols(httpProtocol)
                // Criterios de aceptación: si no se cumplen, "mvn gatling:test" FALLA
                .assertions(
                        global().failedRequests().percent().lt(1.0),
                        details("3. Detalle de producto").responseTime().percentile3().lt(P95_MS));
    }
}
