package co.edu.unillanos.campusfest.domain;

import java.time.LocalDateTime;
import java.util.UUID;

public class Reserva {
    private static final int MINUTOS_DE_VIGENCIA = 10;

    private final String codigo;
    private final String estudianteId;
    private final LocalDateTime venceEn;
    private EstadoReserva estado;
    private Asistencia asistencia;

    private Reserva(String codigo, String estudianteId, LocalDateTime venceEn,
                    EstadoReserva estado, Asistencia asistencia) {
        this.codigo = codigo;
        this.estudianteId = estudianteId;
        this.venceEn = venceEn;
        this.estado = estado;
        this.asistencia = asistencia;
    }

    public static Reserva pendiente(String estudianteId, LocalDateTime ahora) {
        return new Reserva(UUID.randomUUID().toString(), estudianteId,
                ahora.plusMinutes(MINUTOS_DE_VIGENCIA), EstadoReserva.PENDIENTE, null);
    }

    public static Reserva reconstruir(String codigo, String estudianteId, LocalDateTime venceEn,
                                      EstadoReserva estado, Asistencia asistencia) {
        return new Reserva(codigo, estudianteId, venceEn, estado, asistencia);
    }

    public boolean ocupaCupo(LocalDateTime ahora) {
        return estado == EstadoReserva.CONFIRMADA
                || (estado == EstadoReserva.PENDIENTE && !plazoVencido(ahora));
    }

    public void confirmar(LocalDateTime ahora) {
        if (estado != EstadoReserva.PENDIENTE) {
            throw new IllegalStateException("Solo se puede confirmar una reserva pendiente.");
        }
        if (plazoVencido(ahora)) {
            estado = EstadoReserva.VENCIDA;
            throw new IllegalStateException("La reserva está vencida.");
        }
        estado = EstadoReserva.CONFIRMADA;
    }

    public void cancelar() {
        if (estado != EstadoReserva.PENDIENTE && estado != EstadoReserva.CONFIRMADA) {
            throw new IllegalStateException("Solo se puede cancelar una reserva pendiente o confirmada.");
        }
        estado = EstadoReserva.CANCELADA;
    }

    public Asistencia registrarIngreso(LocalDateTime ahora) {
        if (estado != EstadoReserva.CONFIRMADA) {
            throw new IllegalStateException("El ingreso requiere una reserva confirmada.");
        }
        if (asistencia != null) {
            throw new IllegalStateException("La reserva ya tiene un ingreso registrado.");
        }
        asistencia = new Asistencia(ahora);
        return asistencia;
    }

    private boolean plazoVencido(LocalDateTime ahora) {
        return !ahora.isBefore(venceEn);
    }

    public String getCodigo() {
        return codigo;
    }

    public String getEstudianteId() {
        return estudianteId;
    }

    public LocalDateTime getVenceEn() {
        return venceEn;
    }

    public EstadoReserva getEstado() {
        return estado;
    }

    public Asistencia getAsistencia() {
        return asistencia;
    }
}
