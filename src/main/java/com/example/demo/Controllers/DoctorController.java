package com.example.demo.Controllers;

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

import com.example.demo.Models.DAO.DoctorDAO;
import com.example.demo.Models.DAO.UsuarioDAO;
import com.example.demo.Models.Entity.Doctor;
import com.example.demo.Models.Entity.Usuario;

@RestController
@RequestMapping("/doctores")
@CrossOrigin(origins = "http://localhost:3000")
public class DoctorController {

    @Autowired
    private DoctorDAO doctorDAO;

    @Autowired
    private UsuarioDAO usuarioDAO;

    @GetMapping("/mostrar")
    public List<Doctor> obtenerDoctores() {
        return doctorDAO.findAll();
    }

    @GetMapping("/{id}")
    public Doctor obtenerDoctor(@PathVariable Long id) {
        return doctorDAO.findById(id);
    }

    //VALIDAR FORMATO DE CORREO
    private boolean correoValido(String correo) {
    String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    return correo != null && correo.matches(regex);
}



    //CREAR DOCTOR NUEVO, SOLO LO HACE EL ADMIN
    @PostMapping("/crear")
    public ResponseEntity<?> crearDoctor(@RequestBody Doctor doctor) {

        if (!correoValido(doctor.getCorreo())) {
        return ResponseEntity
                .badRequest()
                .body("Error: El correo ingresado no es válido.");
    }

        if (doctor.getHoraInicio() == null || doctor.getHoraFin() == null) {
        return ResponseEntity.badRequest().body("Debe ingresar hora de inicio y hora final.");
    }

    if (!doctor.getHoraInicio().isBefore(doctor.getHoraFin())) {
        return ResponseEntity
                .badRequest()
                .body("La hora de inicio debe ser menor que la hora final de la jornada.");
    }


        Usuario existe = usuarioDAO.findByCorreo(doctor.getCorreo());
        if (existe != null) {
            return ResponseEntity.badRequest().body("Ya existe un Doctor con ese correo");
        }

        Doctor nuevo = doctorDAO.save(doctor);

        Usuario usuario = new Usuario();
        usuario.setCorreo(doctor.getCorreo());
        usuario.setContraseña(doctor.getContraseña());
        usuario.setRol("DOCTOR");

        usuarioDAO.save(usuario);

        return ResponseEntity.ok(nuevo);
    }



    //MOSTRAR DOCTORES POR ESPECIALIDAD
    @GetMapping("/especialidad/{esp}")
    public List<Doctor> obtenerPorEspecialidad(@PathVariable String esp) {
        return doctorDAO.findByEspecialidad(esp);
    }


    //ACTUALIZAR DOCTOR, SOLO LO HACE EL ADMIN
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<?> actualizarDoctor(@PathVariable Long id, @RequestBody Doctor nuevo) {

    Doctor existente = doctorDAO.findById(id);
    if (existente == null) {
        return ResponseEntity.badRequest().body("Error: No existe el doctor con id " + id);
    }

    if (!correoValido(nuevo.getCorreo())) {
        return ResponseEntity
                .badRequest()
                .body("Error: El correo ingresado no es válido.");
    }

    Doctor doctorCorreo = doctorDAO.findByCorreo(nuevo.getCorreo());
    if (doctorCorreo != null && doctorCorreo.getId() != id) {
        return ResponseEntity.badRequest().body("Error: Ya existe un doctor con ese correo.");
    }

    if (nuevo.getHoraInicio() == null || nuevo.getHoraFin() == null) {
        return ResponseEntity.badRequest().body("Debe ingresar hora de inicio y hora final.");
    }

    if (!nuevo.getHoraInicio().isBefore(nuevo.getHoraFin())) {
        return ResponseEntity
                .badRequest()
                .body("La hora de inicio debe ser menor que la hora final de la jornada.");
    }

    String correoAnterior = existente.getCorreo();

    existente.setNombre(nuevo.getNombre());
    existente.setApellido(nuevo.getApellido());
    existente.setCorreo(nuevo.getCorreo());
    existente.setEspecialidad(nuevo.getEspecialidad());
    existente.setContraseña(nuevo.getContraseña());
    existente.setHoraInicio(nuevo.getHoraInicio());
    existente.setHoraFin(nuevo.getHoraFin());

    Doctor actualizado = doctorDAO.save(existente);

    Usuario usuario = usuarioDAO.findByCorreo(correoAnterior);
    if (usuario != null) {
        usuario.setCorreo(nuevo.getCorreo());
        usuario.setContraseña(nuevo.getContraseña());
        usuarioDAO.save(usuario);
    }

    return ResponseEntity.ok(actualizado);
}


    //ELIMINAR DOCTOR Y SU USUARIO ASOCIADO, SOLO LO HACE EL ADMIN
    @DeleteMapping("/eliminar/{id}")
    public String eliminarDoctor(@PathVariable Long id) {

        Doctor doctor = doctorDAO.findById(id);
        if (doctor == null) {
            return "Error: Doctor no encontrado con id " + id;
        }

        Usuario usuario = usuarioDAO.findByCorreo(doctor.getCorreo());

        if (usuario != null) {
            usuarioDAO.delete(usuario.getId());
        }

        doctorDAO.delete(doctor.getId());

        return "Doctor y usuario asociado eliminados correctamente";
    }
}
