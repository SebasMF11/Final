package com.example.demo.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import com.example.demo.Models.DAO.HistoriaDAO;
import com.example.demo.Models.Entity.Cita;
import com.example.demo.Models.Entity.Historia;

@RestController
@RequestMapping("/historias")
@CrossOrigin(origins = "http://localhost:3000")
public class HistoriaController {

    @Autowired
    private HistoriaDAO historiaDAO;

    @Autowired
    private CitaDAO citaDAO;

    //CREAR HISTORIA, SE VERIFICA QUE SI SEA EL DOCTOR DE LA CITA SELECCIONADA
    @PostMapping("/crear/{idDoctor}")
    public ResponseEntity<?> crearHistoria(
        @PathVariable Long idDoctor,
        @RequestBody Historia historiaRequest) {

        Long idCita = historiaRequest.getIdCita();

        Cita cita = citaDAO.findById(idCita);
    if (cita == null) {
        return ResponseEntity.badRequest().body("Error: La cita no existe.");
    }

    if (cita.getIdDoctor() != idDoctor) {
        return ResponseEntity
                .badRequest()
                .body("Error: El doctor enviado no coincide con el asignado a esta cita.");
    }

    List<Historia> historiasExistentes = historiaDAO.findByIdCita(idCita);
    if (!historiasExistentes.isEmpty()) {
        return ResponseEntity.badRequest().body(
                "Error: Esta cita ya tiene una historia clínica registrada."
        );
    }

    Historia nueva = new Historia();
    nueva.setIdCita(idCita);
    nueva.setDescripcion(historiaRequest.getDescripcion());

    Historia guardada = historiaDAO.save(nueva);

    return ResponseEntity.ok(guardada);
    }



    
    //MOSTRAR TODAS LAS HISTORIAS DE X DOCTOR
    @GetMapping("/doctor/{idDoctor}")
    public ResponseEntity<?> obtenerHistoriasPorDoctor(@PathVariable Long idDoctor) {

    List<Cita> citasDelDoctor = citaDAO.findByIdDoctor(idDoctor);

    if (citasDelDoctor.isEmpty()) {
        return ResponseEntity.ok().body(List.of());
    }

    List<Long> idsCitas = citasDelDoctor.stream()
            .map(Cita::getId)
            .toList();

    List<Historia> historias = historiaDAO.findByIdCitaL(idsCitas);

    return ResponseEntity.ok(historias);
    }


    //MOSTRAR TODAS LAS HISTORIAS DE X PACIENTE
    @GetMapping("/paciente/{idPaciente}")
    public ResponseEntity<?> obtenerHistoriasPorPaciente(@PathVariable Long idPaciente) {

    List<Cita> citasDelPaciente = citaDAO.findByIdPaciente(idPaciente);

    if (citasDelPaciente.isEmpty()) {
        return ResponseEntity.ok().body(List.of());
    }

    List<Long> idsCitas = citasDelPaciente.stream()
            .map(Cita::getId)
            .toList();

    List<Historia> historias = historiaDAO.findByIdCitaL(idsCitas);

    return ResponseEntity.ok(historias);
    }


    @GetMapping("/mostrar")
    public List<Historia> listarHistorias() {
        return historiaDAO.findAll();
    }

    @GetMapping("/cita/{idCita}")
    public List<Historia> obtenerPorIdCita(@PathVariable Long idCita) {
        return historiaDAO.findByIdCita(idCita);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerHistoria(@PathVariable Long id) {
        Historia historia = historiaDAO.findById(id);
        if (historia == null) {
            return ResponseEntity.badRequest().body("No existe la historia con id " + id);
        }
        return ResponseEntity.ok(historia);
    }

   
    //ELIMINAR HISTORIA Y COMPROBAR QUE SI SEA EL DOCTOR DUEÑO DE LA HISTORIA
   @DeleteMapping("/eliminar/{idHistoria}/{idDoctor}")
public ResponseEntity<?> eliminarHistoria(
        @PathVariable Long idHistoria,
        @PathVariable Long idDoctor) {

    Historia historia = historiaDAO.findById(idHistoria);
    if (historia == null) {
        return ResponseEntity
                .badRequest()
                .body("No existe la historia con id " + idHistoria);
    }

    Cita cita = citaDAO.findById(historia.getIdCita());
    if (cita == null) {
        return ResponseEntity
                .badRequest()
                .body("La historia existe, pero su cita asociada no.");
    }

    if (cita.getIdDoctor() != idDoctor) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body("No tienes permiso para eliminar esta historia. "
                        + "Solo el doctor que atendió la cita puede hacerlo.");
    }

    historiaDAO.delete(idHistoria);

    return ResponseEntity.ok("Historia eliminada correctamente.");
}

}
