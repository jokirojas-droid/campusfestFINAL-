package co.edu.unillanos.campusfest.infrastructure.ui;

import co.edu.unillanos.campusfest.application.usecase.EditarEvento;
import co.edu.unillanos.campusfest.domain.Evento;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/** Adaptador JavaFX del CU3: convierte los campos y delega la edición. */
public final class PantallaEventoController {
    private static final DateTimeFormatter FORMATO_HORA = DateTimeFormatter.ofPattern("HH:mm");
    private final EditarEvento casoUso;

    @FXML private TextField campoEventoId;
    @FXML private TextField campoNombre;
    @FXML private DatePicker campoFecha;
    @FXML private TextField campoHora;
    @FXML private TextField campoLugar;
    @FXML private TextField campoAforo;
    @FXML private Label etiquetaMensaje;

    public PantallaEventoController(EditarEvento casoUso) {
        this.casoUso = java.util.Objects.requireNonNull(casoUso);
    }

    @FXML
    private void alObtenerEvento() {
        try {
            Evento evento = casoUso.obtener(campoEventoId.getText().trim());
            campoNombre.setText(evento.getNombre());
            campoFecha.setValue(evento.getFecha().toLocalDate());
            campoHora.setText(evento.getFecha().toLocalTime().format(FORMATO_HORA));
            campoLugar.setText(evento.getLugar());
            campoAforo.setText(Integer.toString(evento.getAforo()));
            etiquetaMensaje.setText("Evento cargado: " + evento.getId());
        } catch (RuntimeException error) {
            etiquetaMensaje.setText(error.getMessage());
        }
    }

    @FXML
    private void alGuardarEvento() {
        try {
            LocalTime hora = LocalTime.parse(campoHora.getText().trim(), FORMATO_HORA);
            LocalDateTime fecha = campoFecha.getValue().atTime(hora);
            int aforo = Integer.parseInt(campoAforo.getText().trim());
            casoUso.editar(campoEventoId.getText().trim(), campoNombre.getText().trim(),
                fecha, campoLugar.getText().trim(), aforo);
            etiquetaMensaje.setText("Evento actualizado correctamente.");
        } catch (RuntimeException error) {
            etiquetaMensaje.setText(error.getMessage());
        }
    }
}
