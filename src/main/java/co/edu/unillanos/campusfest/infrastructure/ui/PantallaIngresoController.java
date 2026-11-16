package co.edu.unillanos.campusfest.infrastructure.ui;

import co.edu.unillanos.campusfest.application.usecase.RegistrarIngreso;
import co.edu.unillanos.campusfest.domain.Asistencia;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/** Adaptador JavaFX del CU4: recibe el contenido del QR como texto. */
public final class PantallaIngresoController {
    private final RegistrarIngreso casoUso;

    @FXML private TextField campoEventoId;
    @FXML private TextField campoCodigoQR;
    @FXML private Label etiquetaMensaje;

    public PantallaIngresoController(RegistrarIngreso casoUso) {
        this.casoUso = java.util.Objects.requireNonNull(casoUso);
    }

    @FXML
    private void alRegistrarIngreso() {
        try {
            Asistencia asistencia = casoUso.registrar(
                campoEventoId.getText().trim(), campoCodigoQR.getText().trim());
            etiquetaMensaje.setText("Ingreso registrado: " + asistencia.getHora());
        } catch (RuntimeException error) {
            etiquetaMensaje.setText(error.getMessage());
        }
    }
}
