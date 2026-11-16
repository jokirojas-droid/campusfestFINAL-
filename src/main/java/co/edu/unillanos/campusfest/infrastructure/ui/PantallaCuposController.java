package co.edu.unillanos.campusfest.infrastructure.ui;

import co.edu.unillanos.campusfest.application.usecase.ConsultarCupos;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/** Adaptador JavaFX del CU1: no implementa reglas de disponibilidad. */
public final class PantallaCuposController {
    private final ConsultarCupos casoUso;

    @FXML private TextField campoEventoId;
    @FXML private Label etiquetaMensaje;

    public PantallaCuposController(ConsultarCupos casoUso) {
        this.casoUso = java.util.Objects.requireNonNull(casoUso);
    }

    @FXML
    private void alConsultarCupos() {
        try {
            int cupos = casoUso.consultar(campoEventoId.getText().trim());
            etiquetaMensaje.setText("Cupos disponibles: " + cupos);
        } catch (RuntimeException error) {
            etiquetaMensaje.setText(error.getMessage());
        }
    }
}
