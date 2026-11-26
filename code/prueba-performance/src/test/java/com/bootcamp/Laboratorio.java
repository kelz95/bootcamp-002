package com.bootcamp;

/**
 * Configuración del laboratorio por variables de entorno (las mismas que pruebas-funcionales).
 * NUNCA IPs ni credenciales en el código: antes la contraseña de juanperez estaba aquí
 * y en las diapositivas, y quedó expuesta en el historial de Git.
 *   LAB_URL       https://<IP de Kong>
 *   LAB_USER      usuario de LLDAP (cada alumno el suyo)
 *   LAB_PASSWORD  su contraseña  (read -rsp 'Contraseña: ' LAB_PASSWORD; echo; export LAB_PASSWORD)
 */
final class Laboratorio {

    private Laboratorio() {
    }

    static String url() {
        return requerida("LAB_URL");
    }

    /** JSON del login de Authelia, con usuario y contraseña escapados. */
    static String loginJson() {
        return "{\"username\":\"" + escapar(requerida("LAB_USER"))
                + "\",\"password\":\"" + escapar(requerida("LAB_PASSWORD"))
                + "\",\"keepMeLoggedIn\":false}";
    }

    private static String escapar(String valor) {
        return valor.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    private static String requerida(String nombre) {
        String valor = System.getenv(nombre);
        if (valor == null || valor.isBlank()) {
            throw new IllegalStateException("Falta la variable de entorno " + nombre
                    + ". Ejemplo: export LAB_URL=https://<IP-KONG> LAB_USER=<usuario>;"
                    + " read -rsp 'Contraseña: ' LAB_PASSWORD; echo; export LAB_PASSWORD");
        }
        return valor;
    }
}
