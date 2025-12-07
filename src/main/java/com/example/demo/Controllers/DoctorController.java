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

    @PostMapping("/crear")
    public ResponseEntity<?> crearDoctor(@RequestBody Doctor doctor) {

        Usuario existe = usuarioDAO.findByCorreo(doctor.getCorreo());
        if (existe != null) {
            return ResponseEntity.badRequest().body("Ya existe un usuario con ese correo");
        }

        Doctor nuevo = doctorDAO.save(doctor);

        Usuario usuario = new Usuario();
        usuario.setCorreo(doctor.getCorreo());
        usuario.setContraseña(doctor.getContraseña());
        usuario.setRol("DOCTOR"); // o ENUM

        usuarioDAO.save(usuario);

        return ResponseEntity.ok(nuevo);
    }

    @PutMapping("/actualizar/{id}")
    public Doctor actualizarDoctor(@PathVariable Long id, @RequestBody Doctor nuevo) {
        Doctor existente = doctorDAO.findById(id);

        existente.setNombre(nuevo.getNombre());
        existente.setApellido(nuevo.getApellido());
        existente.setCorreo(nuevo.getCorreo());
        existente.setContraseña(nuevo.getContraseña());

        return doctorDAO.save(existente);
    }

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
