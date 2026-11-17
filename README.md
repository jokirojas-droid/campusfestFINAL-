# CampusFest — Sistema de Gestión de Eventos y Cupos (Unillanos)

Aplicación de escritorio en Java 21, JavaFX y H2 para la gestión de cupos, reservas, edición de eventos y control de ingreso en CampusFest.

> La arquitectura, roles del equipo, decisiones de diseño (D1–D8) y el registro de uso de IA están documentados en [`docs/BITACORA.md`](docs/BITACORA.md).

## 1. Requisitos previos

- **Java JDK 21**
- **Apache Maven 3.9+**

## 2. Cómo ejecutar y verificar el proyecto

 1. Ejecutar la suite de pruebas (90 pruebas)
    mvn test
 2. Iniciar la aplicación (JavaFX + H2)
    mvn clean javafx:run
 3. Ver la base de datos H2 en el navegador
    mvn exec:java "-Dexec.mainClass=org.h2.tools.Console" "-Dexec.args=-url jdbc:h2:./campusfest -user sa"