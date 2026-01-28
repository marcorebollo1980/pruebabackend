
# Red Social - Documentación

Este documento proporciona una descripción general de la aplicación de red social, su arquitectura de microservicios y las instrucciones para su instalación, ejecución y acceso a la documentación de la API a través de Swagger.

## 1. Descripción del Proyecto

La aplicación es una plataforma de red social simple construida sobre una arquitectura de microservicios. Permite a los usuarios registrarse, iniciar sesión, crear publicaciones y ver las publicaciones de otros usuarios. El sistema está dividido en tres servicios principales que se encargan de la autenticación, la gestión de usuarios y la gestión de publicaciones.

## 2. Arquitectura

El proyecto sigue una arquitectura de microservicios, con los siguientes componentes principales:

*   **Auth Service (`auth-service`):** Responsable de la autenticación de usuarios. Proporciona un endpoint para que los usuarios inicien sesión y obtengan un token JWT (JSON Web Token) que se utilizará para autorizar las solicitudes a otros servicios.
*   **User Service (`user-service`):** Gestiona toda la lógica relacionada con los usuarios. Permite el registro de nuevos usuarios, la obtención de perfiles de usuario y la gestión de la información del usuario.
*   **Post Service (`post-service`):** Se encarga de la funcionalidad relacionada con las publicaciones. Permite a los usuarios autenticados crear nuevas publicaciones, ver un feed de publicaciones y, potencialmente, interactuar con ellas (por ejemplo, dar "Me gusta").

Cada servicio es una aplicación Spring Boot independiente con su propia base de datos y lógica de negocio.

## 3. Instalación y Ejecución

El proyecto está configurado para ser ejecutado fácilmente utilizando Docker y Docker Compose, lo que orquesta el despliegue de los tres microservicios.

### Prerrequisitos

*   [Docker](https://www.docker.com/get-started)
*   [Docker Compose](https://docs.docker.com/compose/install/)
*   [Apache Maven](https://maven.apache.org/install.html) (para construir los proyectos manualmente si es necesario)

### Pasos para la ejecución

1.  **Construir los proyectos:**
    Cada microservicio debe ser empaquetado en un archivo JAR para que Docker pueda construir la imagen. Abre una terminal en el directorio raíz de cada servicio (`auth-service`, `user-service`, `post-service`) y ejecuta el siguiente comando de Maven:
    ```bash
    ./mvnw clean package
    ```
    *Nota: Si estás en Windows, usa `mvnw.cmd clean package`.*

2.  **Levantar los servicios con Docker Compose:**
    Una vez que los archivos JAR se hayan generado en los directorios `target` de cada servicio, vuelve al directorio raíz del proyecto (donde se encuentra el archivo `docker-compose.yml`) y ejecuta el siguiente comando:
    ```bash
    docker-compose up --build
    ```
    Este comando construirá las imágenes de Docker para cada servicio y los iniciará. Verás los logs de los tres servicios en tu terminal.

3.  **Detener los servicios:**
    Para detener todos los contenedores, presiona `Ctrl + C` en la terminal donde se están ejecutando o ejecuta el siguiente comando desde el directorio raíz del proyecto:
    ```bash
    docker-compose down
    ```

## 4. Acceso a Swagger UI

Para facilitar la interacción y prueba de las APIs, cada microservicio expone su propia documentación de API a través de Swagger UI. Una vez que los servicios se están ejecutando, puedes acceder a la documentación en las siguientes URLs:

*   **Auth Service:**
    *   URL: [http://localhost:8081/swagger-ui.html](http://localhost:8081/swagger-ui.html)
    *   *Nota: El puerto por defecto para el servicio de autenticación es `8081`.*

*   **User Service:**
    *   URL: [http://localhost:8082/swagger-ui.html](http://localhost:8082/swagger-ui.html)
    *   *Nota: El puerto por defecto para el servicio de usuario es `8082`.*

*   **Post Service:**
    *   URL: [http://localhost:8083/swagger-ui.html](http://localhost:8083/swagger-ui.html)
    *   *Nota: El puerto por defecto para el servicio de publicaciones es `8083`.*

Desde la interfaz de Swagger, podrás ver todos los endpoints disponibles para cada servicio, sus parámetros, y probarlos directamente desde el navegador. Para los endpoints que requieren autenticación, primero deberás obtener un token JWT del `auth-service` y luego usarlo en el botón "Authorize" de Swagger UI.
