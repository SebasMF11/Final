package com.example.demo.Controllers;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Models.DAO.CitaDAO;
import com.example.demo.Models.DAO.DoctorDAO;
import com.example.demo.Models.DAO.PacienteDAO;
import com.example.demo.Models.Entity.Cita;
import com.example.demo.Models.Entity.Doctor;
import com.example.demo.Models.Entity.Paciente;

@RestController
@RequestMapping("/citas")
@CrossOrigin(origins = "http://localhost:3000")
public class CitasController {

    @Autowired
    private CitaDAO citaDAO;

    @Autowired
    private DoctorDAO doctorDAO;

    @Autowired
    private PacienteDAO pacienteDAO;

    @GetMapping("/mostrar")
    public List<Cita> obtenerCitas() {
        return citaDAO.findAll();
    }

    //MOSTRAR TODAS LAS CITAS DE X PACIENTE
    @GetMapping("/paciente/{idPaciente}")
    public ResponseEntity<?> obtenerCitasPorPaciente(@PathVariable Long idPaciente) {

    List<Cita> citas = citaDAO.findByIdPaciente(idPaciente);

    return ResponseEntity.ok(citas);
}

    //MOSTRAR TODAS LAS CITAS DE X DOCTOR
    @GetMapping("/doctor/{idDoctor}")
    public ResponseEntity<?> obtenerCitasPorDoctor(@PathVariable Long idDoctor) {
    Doctor doctor = doctorDAO.findById(idDoctor);
    if (doctor == null) {
        return ResponseEntity.badRequest().body("Error: El doctor no existe");
    }

    List<Cita> citas = citaDAO.findByIdDoctor(idDoctor);

    return ResponseEntity.ok(citas);
}

    @GetMapping("/{id}")
    public Cita obtenerCita(@PathVariable Long id) {
        return citaDAO.findById(id);
    }




    //CREAR UNA CITA A X PACIENTE CON 55 VALIDACIONES
    @PostMapping("/crear/{idPaciente}")
    public ResponseEntity<?> crearCita(@RequestBody Cita cita, @PathVariable Long idPaciente) {

    cita.setIdPaciente(idPaciente);

    Doctor doctor = doctorDAO.findById(cita.getIdDoctor());
    if (doctor == null) {
        return ResponseEntity.badRequest().body("Error: El doctor no existe");
    }

    Paciente paciente = pacienteDAO.findById(idPaciente);
    if (paciente == null) {
        return ResponseEntity.badRequest().body("Error: El paciente no existe");
    }

    LocalDate fechaNueva = cita.getFecha();
    if (fechaNueva.isBefore(LocalDate.now())) {
        return ResponseEntity.badRequest().body("La fecha no puede ser anterior a hoy.");
    }

    LocalTime horaNueva = cita.getHora();
    LocalTime horaInicio = doctor.getHoraInicio();
    LocalTime horaFin = doctor.getHoraFin();
    LocalTime limiteFin = horaFin.minusMinutes(30);  // No permitir cita que termine fuera del horario

    if (horaNueva.isBefore(horaInicio) || horaNueva.isAfter(limiteFin)) {
        return ResponseEntity.badRequest().body(
            "La hora seleccionada está fuera del horario permitido del doctor. " +
            "Debe ser entre " + horaInicio + " y " + limiteFin + "."
        );
    }

    List<Cita> citasDelDoctor = citaDAO.findByIdDoctor(cita.getIdDoctor());

    for (Cita c : citasDelDoctor) {

        if (c.getFecha().equals(fechaNueva)) {

            if (c.getHora().equals(horaNueva)) {
                return ResponseEntity.badRequest().body(
                    "El doctor ya tiene una cita exactamente en esa hora."
                );
            }

            long diferenciaMinutos = Math.abs(
                    ChronoUnit.MINUTES.between(c.getHora(), horaNueva)
            );

            if (diferenciaMinutos < 30) {
                return ResponseEntity.badRequest().body(
                    "El doctor ya tiene una cita muy cerca a esa hora. " +
                    "Debe haber al menos 30 minutos entre citas."
                );
            }
        }
    }

    Cita nueva = citaDAO.save(cita);
    return ResponseEntity.ok(nueva);
}


    //PACIENTE ELIMINA CITA, COMPROBAR QUE EL PACIENTE SI ES EL DUEÑO DE LA CITA
    @DeleteMapping("/eliminar1/{idCita}/{idPaciente}")
    public String eliminarCita(@PathVariable Long idCita, @PathVariable Long idPaciente) {

    Cita cita = citaDAO.findById(idCita);
    if (cita == null) {
        return "Error: Cita no encontrada con id " + idCita;
    }

    if (cita.getIdPaciente() != idPaciente) {
    return "Error: No tienes permiso para eliminar esta cita";
    }

    citaDAO.delete(cita.getId());

    return "Cita eliminada correctamente";
    }

    //DOCTOR ELIMINA CITA, COMPROBAR QUE EL DOCTOR SI ES EL DUEÑO DE LA CITA
    @DeleteMapping("/eliminar2/{idCita}/{idDoctor}")
    public String eliminarCita2(@PathVariable Long idCita, @PathVariable Long idDoctor) {

    Cita cita = citaDAO.findById(idCita);
    if (cita == null) {
        return "Error: Cita no encontrada con id " + idCita;
    }

    if (cita.getIdDoctor() != idDoctor) {
    return "Error: No tienes permiso para eliminar esta cita";
    }

    citaDAO.delete(cita.getId());

    return "Cita eliminada correctamente";
    }


}
