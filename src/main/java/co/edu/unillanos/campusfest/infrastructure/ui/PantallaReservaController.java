package co.edu.unillanos.campusfest.infrastructure.ui;

import co.edu.unillanos.campusfest.application.usecase.ReservarEntrada;
import co.edu.unillanos.campusfest.domain.Reserva;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/** Adaptador JavaFX del CU2: delega la reserva y la confirmación. */
public final class PantallaReservaController {
    private final ReservarEntrada casoUso;

    @FXML private TextField campoEventoId;
    @FXML private TextField campoEstudianteId;
    @FXML private TextField campoCodigoReserva;
    @FXML private Label etiquetaMensaje;

    public PantallaReservaController(ReservarEntrada casoUso) {
        this.casoUso = java.util.Objects.requireNonNull(casoUso);
    }

    @FXML
    private void alSolicitarReserva() {
        try {
            Reserva reserva = casoUso.solicitarReserva(
                campoEventoId.getText().trim(), campoEstudianteId.getText().trim());
            campoCodigoReserva.setText(reserva.getCodigo());
            etiquetaMensaje.setText("Reserva pendiente: " + reserva.getCodigo()
                + " · Vence: " + reserva.getVenceEn());
        } catch (RuntimeException error) {
            etiquetaMensaje.setText(error.getMessage());
        }
    }

    @FXML
    private void alConfirmarReserva() {
        try {
            Reserva reserva = casoUso.confirmarReserva(
                campoEventoId.getText().trim(), campoCodigoReserva.getText().trim());
            etiquetaMensaje.setText("Reserva confirmada: " + reserva.getCodigo());
        } catch (RuntimeException error) {
            etiquetaMensaje.setText(error.getMessage());
        }
    }
}
