package com.example.demo.Controllers;

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
    @PostMapping("/crear")
    public ResponseEntity<?> crearHistoria(@RequestBody Historia historia) {

    // 1. Validar que la cita no tenga ya una historia clínica
        List<Historia> historiasExistentes = historiaDAO.findByIdCita(historia.getIdCita());

        if (!historiasExistentes.isEmpty()) {
            return ResponseEntity.badRequest().body(
                "Error: Esta cita ya tiene una historia clínica registrada."
            );
        }

        Historia nueva = historiaDAO.save(historia);
        return ResponseEntity.ok(nueva);
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


    // ➤ 2. Obtener todas las historias
    @GetMapping("/mostrar")
    public List<Historia> listarHistorias() {
        return historiaDAO.findAll();
    }

    // ➤ 3. Obtener historias por id de cita
    @GetMapping("/cita/{idCita}")
    public List<Historia> obtenerPorIdCita(@PathVariable Long idCita) {
        return historiaDAO.findByIdCita(idCita);
    }

    // ➤ 4. Obtener historia por id
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerHistoria(@PathVariable Long id) {
        Historia historia = historiaDAO.findById(id);
        if (historia == null) {
            return ResponseEntity.badRequest().body("No existe la historia con id " + id);
        }
        return ResponseEntity.ok(historia);
    }

   
    // ➤ 6. Eliminar historia
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminarHistoria(@PathVariable Long id) {

        Historia historia = historiaDAO.findById(id);
        if (historia == null) {
            return ResponseEntity.badRequest().body("No existe la historia con id " + id);
        }

        historiaDAO.delete(historia.getId());
        return ResponseEntity.ok("Historia eliminada correctamente.");
    }
}
