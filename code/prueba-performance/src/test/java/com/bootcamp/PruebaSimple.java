package com.bootcamp;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;
import java.time.Duration;

/**
 * Demostración de SEGURIDAD: carga SIN sesión.
 * Authelia debe frenar TODAS las peticiones (302 al login) antes de que lleguen al servicio.
 * Pasa si ninguna petición obtiene 200. Después revisa Prometheus: productos-service
 * NO debe registrar este tráfico, porque nunca le llegó.
 *
 * mvn gatling:test -Dgatling.simulationClass=com.bootcamp.PruebaSimple
 */
public class PruebaSimple extends Simulation {

    private final HttpProtocolBuilder httpProtocol = http
            .baseUrl(Laboratorio.url())
            .disableFollowRedirect(); // Evita que Gatling siga el 302 hacia el login

    private final ScenarioBuilder scn = scenario("Acceso sin sesión (debe bloquearse)")
            .exec(http("GET /api/v1/products/1 sin cookie")
                    .get("/api/v1/products/1")
                    .check(status().in(302, 401, 403)));

    {
        setUp(scn.injectOpen(rampUsers(30).during(Duration.ofSeconds(10))))
                .protocols(httpProtocol)
                .assertions(global().successfulRequests().percent().is(100.0));
    }
}
