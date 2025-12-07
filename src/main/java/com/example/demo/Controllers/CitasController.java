package com.example.demo.Controllers;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

    //CREAR UNA CITA A X PACIENTE
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

    LocalTime horaNueva = cita.getHora();

    LocalTime horaInicio = doctor.getHoraInicio();
    LocalTime horaFin   = doctor.getHoraFin();

    LocalTime limiteFin = horaFin.minusMinutes(30);

    if (horaNueva.isBefore(horaInicio) || horaNueva.isAfter(limiteFin)) {
    return ResponseEntity.badRequest().body(
            "La hora seleccionada está fuera del horario permitido del doctor. " +
            "Debe ser entre " + horaInicio + " y " + limiteFin + ".");
    }

    List<Cita> citasDelDoctor = citaDAO.findByIdDoctor(cita.getIdDoctor());
    LocalDate fechaNueva = cita.getFecha();

    for (Cita c : citasDelDoctor) {

        if (c.getFecha().equals(fechaNueva) &&
            c.getHora().equals(horaNueva)) {
            
            return ResponseEntity.badRequest()
                    .body("El doctor ya tiene una cita en esa fecha y hora.");
        }
    }

        Cita nueva = citaDAO.save(cita);
        return ResponseEntity.ok(nueva);
    }



    @PutMapping("/actualizar/{id}")
    public Cita actualizarCita(@PathVariable Long id, @RequestBody Cita nuevaCita) {
        Cita existente = citaDAO.findById(id);
        existente.setIdPaciente(nuevaCita.getIdPaciente());
        existente.setIdDoctor(nuevaCita.getIdDoctor());
        existente.setFecha(nuevaCita.getFecha());
        existente.setHora(nuevaCita.getHora());


        return citaDAO.save(existente);
    }

    @DeleteMapping("/eliminar/{id}")
    public String eliminarCita(@PathVariable Long id) {
        Cita cita = citaDAO.findById(id);
        if (cita == null) {
            return "Error: Cita no encontrada con id " + id;
        }

        citaDAO.delete(cita.getId());
        return "Cita eliminada correctamente";
    }
}
