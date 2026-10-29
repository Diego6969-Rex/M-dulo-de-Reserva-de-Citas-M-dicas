import java.util.ArrayList;
import java.util.List;

interface Observador {
    void actualizar(String mensaje);
}

class Paciente implements Observador {
    private String nombre;

    public Paciente(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public void actualizar(String mensaje) {
        System.out.println("[NOTIFICACION ENVIADA A " + this.nombre.toUpperCase() + "]: " + mensaje);
    }
}

class SistemaNotificacion {
    private List<Observador> observadores = new ArrayList<>();

    public void suscribir(Observador observador) {
        observadores.add(observador);
    }

    public void desuscribir(Observador observador) {
        observadores.remove(observador);
    }

    public void notificar(String mensaje) {
        for (Observador obs : observadores) {
            obs.actualizar(mensaje);
        }
    }
}

interface CitaMedica {
    String obtenerDetalles();
}

class CitaPresencial implements CitaMedica {
    @Override
    public String obtenerDetalles() {
        return "Cita Presencial reservada en el Consultorio 101, Piso 1.";
    }
}

class CitaVirtual implements CitaMedica {
    @Override
    public String obtenerDetalles() {
        return "Cita Virtual reservada. Enlace de Zoom: https://zoom.us/hospital/cita";
    }
}

class CitaFactory {
    public static CitaMedica crearCita(String tipo) throws IllegalArgumentException {
        if (tipo.equalsIgnoreCase("presencial")) {
            return new CitaPresencial();
        } else if (tipo.equalsIgnoreCase("virtual")) {
            return new CitaVirtual();
        } else {
            throw new IllegalArgumentException("Tipo de cita '" + tipo + "' no soportado por el hospital.");
        }
    }
}

class ReservaCitasFacade {
    private SistemaNotificacion sistemaNotificacion;

    public ReservaCitasFacade() {
        this.sistemaNotificacion = new SistemaNotificacion();
    }

    public void programarCita(String nombrePaciente, String tipoCita) {
        Paciente paciente = new Paciente(nombrePaciente);
        this.sistemaNotificacion.suscribir(paciente);

        try {
            CitaMedica cita = CitaFactory.crearCita(tipoCita);
            String mensajeExito = cita.obtenerDetalles();
            
            this.sistemaNotificacion.notificar(mensajeExito);
            System.out.println("Estado: Reserva completada con exito.\n");
            
        } catch (IllegalArgumentException error) {
            System.out.println("Estado: Error en la reserva. " + error.getMessage() + "\n");
        } finally {
            this.sistemaNotificacion.desuscribir(paciente);
        }
    }
}

public class Main {
    public static void main(String[] args) {
        ReservaCitasFacade fachadaHospital = new ReservaCitasFacade();

        fachadaHospital.programarCita("Juan Perez", "presencial");
        fachadaHospital.programarCita("Maria Lopez", "virtual");
        fachadaHospital.programarCita("Carlos Ruiz", "domicilio");
    }
}
