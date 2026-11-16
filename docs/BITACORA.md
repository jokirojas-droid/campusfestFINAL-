# Bitácora de Diseño y Desarrollo — CampusFest

## 1. Equipo y roles

| Rol | Nombre | Usuario de GitHub |
|---|---|---|
| Integrador | Johan Kirley Rojas Bobadilla | jokirojas-droid |
| Líder de Dominio | Johan Sebastian Jaime Patiño | johansebastianreinbor |
| Casos de Uso, Memoria y H2 | Manuela Mican Vargas | ManuelaM4 |
| Interfaz Gráfica | Hanner Stiven Contreras Carvajal | hsccarvajal-sudo |

Cadena de revisión: Rol 1↔Rol 2, Rol 2↔Rol 3, Rol 3↔Rol 4, Rol 4↔Rol 1. Nadie aprueba su propio PR; el Rol 1 fusiona todos.

## 2. Acuerdos de pantallas

| CU | FXML (`vistas/`) | Controlador | Constructor | Llama a |
|---|---|---|---|---|
| CU1 | `pantalla-cupos.fxml` | `PantallaCuposController` | `(ConsultarCupos)` | `consultar` |
| CU2 | `pantalla-reserva.fxml` | `PantallaReservaController` | `(ReservarEntrada)` | `solicitarReserva`, `confirmarReserva` |
| CU3 | `pantalla-evento.fxml` | `PantallaEventoController` | `(EditarEvento)` | `obtener`, `editar` |
| CU4 | `pantalla-ingreso.fxml` | `PantallaIngresoController` | `(RegistrarIngreso)` | `registrar` |

Cada `fx:id` es un campo `private` con `@FXML` y el mismo nombre; cada `onAction="#metodo"` es un método `@FXML`; todas las pantallas tienen `<Label fx:id="etiquetaMensaje"/>`. Los FXML **no** declaran `fx:controller`: `App` crea el controlador con su caso de uso y lo asigna con `loader.setController(controlador)` antes de llamar a `loader.load()`.

## 3. Decisiones de diseño (D1–D8)

- **D1.** `PantallaReservaController` es adaptador de UI; el Controller de GRASP lo cumplen los casos de uso. Si el controlador tomara decisiones de negocio, las reglas quedarían fuera de las entidades y no se podrían probar con `RelojFijo`.
- **D2.** La hora entra por el puerto `Reloj`; solo `RelojDelSistema` llama a `now()`. Si una entidad llamara a `now()`, las pruebas con `RelojFijo` serían imposibles y el resultado dependería del reloj de la máquina.
- **D3.** `Evento` calcula cupos (`aforo − bloqueados − confirmadas − pendientes vigentes`) y crea `Reserva` (Experto/Creador); `Reserva` crea `Asistencia`. Cada regla vive una sola vez, en la entidad que tiene los datos.
- **D4.** Puertos en `application.port`, adaptadores en `infrastructure` (Regla de dependencia + Fabricación pura para el repositorio).
- **D5.** `RepositorioEventosH2` atrapa `SQLException` y la traduce a `IllegalStateException`; la UI nunca ve `java.sql`.
- **D6.** Se agregan `Reserva.reconstruir(...)`, constructor de `Evento` con reservas, `getBloqueados()` y `getReservas()` (solo lectura) para que H2 pueda reconstruir el evento. No cambia el contrato, no rompe pruebas y **ninguna regla sale de la entidad**.
- **D7.** Orden de fusión: Base → Dominio → Casos de uso / UI → H2 → Ensamblaje → README. Lo obliga la dependencia: casos de uso solo pasan a verde con `Evento`/`Reserva` reales; H2 necesita la reconstrucción de Dominio; `App` necesita todo.
- **D8.** Predicción: al pasar de memoria a H2 solo cambia la instanciación del repositorio en `App`. Se compara al cierre (abajo).

## 4. Registro de uso de IA

| Herramienta | Para qué se usó | En qué clases | ¿Respetó el diseño? |
|---|---|---|---|
| Gemini | Estructura base Maven, esqueleto del contrato, guía de integración | `pom.xml`, esqueleto, `RelojDelSistema`, `App` | Sí (se ajustó el constructor de `Evento` a 6 parámetros según `CONTRATO.md`). |
| Gemini | Documentación inicial de integración en bitácora | `docs/BITACORA.md` | No — propuso `setControllerFactory` en lugar de `loader.setController(controlador)` antes de `load()` para FXML sin `fx:controller`; corregido en revisión de PR #2. |

> **Criterio 8.2:** esta segunda fila ya deja documentada una propuesta de la IA que violaba el funcionamiento esperado de JavaFX sin `fx:controller` y cómo se corrigió en la revisión de PR #2.

## 5. Predicción D8 vs. lo que realmente cambió (al cierre)

| Capa | ¿Cambió? | Observación |
|---|---|---|
| `domain/*` | No | Reglas independientes de la persistencia. |
| `application/*` | No | Los casos de uso hablan con el puerto. |
| `infrastructure/ui/*` | No | Controladores no conocen el repositorio. |
| `RepositorioEventosH2` | Sí (nuevo) | Adaptador nuevo; el de memoria se conserva. |
| `App` | Sí | Único lugar donde se instancia H2 (raíz de composición). |