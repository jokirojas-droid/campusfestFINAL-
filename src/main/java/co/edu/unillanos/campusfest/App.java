package co.edu.unillanos.campusfest;

import co.edu.unillanos.campusfest.application.port.Reloj;
import co.edu.unillanos.campusfest.application.port.RepositorioEventos;
import co.edu.unillanos.campusfest.application.usecase.ConsultarCupos;
import co.edu.unillanos.campusfest.application.usecase.EditarEvento;
import co.edu.unillanos.campusfest.application.usecase.RegistrarIngreso;
import co.edu.unillanos.campusfest.application.usecase.ReservarEntrada;
import co.edu.unillanos.campusfest.domain.Evento;
import co.edu.unillanos.campusfest.infrastructure.persistence.RepositorioEventosH2;
import co.edu.unillanos.campusfest.infrastructure.reloj.RelojDelSistema;
import co.edu.unillanos.campusfest.infrastructure.ui.PantallaCuposController;
import co.edu.unillanos.campusfest.infrastructure.ui.PantallaEventoController;
import co.edu.unillanos.campusfest.infrastructure.ui.PantallaIngresoController;
import co.edu.unillanos.campusfest.infrastructure.ui.PantallaReservaController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
<<<<<<< HEAD
import java.time.LocalDateTime;
=======

>>>>>>> origin/main

public class App extends Application {

    private Connection conexion;

    @Override
    public void start(Stage stage) throws Exception {
        conexion = DriverManager.getConnection("jdbc:h2:./campusfest;AUTO_SERVER=TRUE", "sa", "");
        ejecutarSchema(conexion);

        RepositorioEventos repositorio = new RepositorioEventosH2(conexion);
        Reloj reloj = new RelojDelSistema();

<<<<<<< HEAD
        // Evento inicial de prueba si aún no existe en la base de datos
        if (repositorio.buscar("EV-1") == null) {
            repositorio.guardar(new Evento(
                    "EV-1",
                    "CampusFest Unillanos 2026",
                    LocalDateTime.now().plusDays(15),
                    "Auditorio Eduardo Carranza",
                    100,
                    10
            ));
        }

=======
    
>>>>>>> origin/main
        ConsultarCupos consultarCupos = new ConsultarCupos(repositorio, reloj);
        ReservarEntrada reservarEntrada = new ReservarEntrada(repositorio, reloj);
        EditarEvento editarEvento = new EditarEvento(repositorio, reloj);
        RegistrarIngreso registrarIngreso = new RegistrarIngreso(repositorio, reloj);

        Parent vistaCupos = cargarVista(
                "/co/edu/unillanos/campusfest/vistas/pantalla-cupos.fxml",
                new PantallaCuposController(consultarCupos)
        );
        Parent vistaReserva = cargarVista(
                "/co/edu/unillanos/campusfest/vistas/pantalla-reserva.fxml",
                new PantallaReservaController(reservarEntrada)
        );
        Parent vistaEvento = cargarVista(
                "/co/edu/unillanos/campusfest/vistas/pantalla-evento.fxml",
                new PantallaEventoController(editarEvento)
        );
        Parent vistaIngreso = cargarVista(
                "/co/edu/unillanos/campusfest/vistas/pantalla-ingreso.fxml",
                new PantallaIngresoController(registrarIngreso)
        );

        BorderPane raiz = new BorderPane();
        Button btnCupos = new Button("CU1 · Cupos");
        Button btnReserva = new Button("CU2 · Reservas");
        Button btnEvento = new Button("CU3 · Evento");
        Button btnIngreso = new Button("CU4 · Ingreso QR");

        btnCupos.setOnAction(e -> raiz.setCenter(vistaCupos));
        btnReserva.setOnAction(e -> raiz.setCenter(vistaReserva));
        btnEvento.setOnAction(e -> raiz.setCenter(vistaEvento));
        btnIngreso.setOnAction(e -> raiz.setCenter(vistaIngreso));

        HBox barraNavegacion = new HBox(10, btnCupos, btnReserva, btnEvento, btnIngreso);
        barraNavegacion.setStyle("-fx-padding: 10; -fx-alignment: center;");

        raiz.setTop(barraNavegacion);
        raiz.setCenter(vistaCupos);

        Scene escena = new Scene(raiz, 820, 560);
        var cssUrl = getClass().getResource("/co/edu/unillanos/campusfest/vistas/estilos.css");
        if (cssUrl != null) {
            escena.getStylesheets().add(cssUrl.toExternalForm());
        }

        stage.setTitle("CampusFest — Unillanos");
        stage.setScene(escena);
        stage.show();
    }

    private Parent cargarVista(String rutaFxml, Object controlador) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(rutaFxml));
        loader.setController(controlador);
        return loader.load();
    }

    private void ejecutarSchema(Connection conn) throws Exception {
        try (InputStream is = getClass().getResourceAsStream("/db/schema.sql")) {
            if (is == null) {
                throw new IllegalStateException("No se encontró /db/schema.sql");
            }
            String sql = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            try (Statement st = conn.createStatement()) {
                st.execute(sql);
            }
        }
    }

    @Override
    public void stop() throws Exception {
        if (conexion != null && !conexion.isClosed()) {
            conexion.close();
        }
    }
}