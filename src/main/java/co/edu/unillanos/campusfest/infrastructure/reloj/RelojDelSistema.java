package co.edu.unillanos.campusfest.infrastructure.reloj;

import co.edu.unillanos.campusfest.application.port.Reloj;
import java.time.LocalDateTime;

public class RelojDelSistema implements Reloj {
    @Override
    public LocalDateTime ahora() {
        return LocalDateTime.now();
    }
}