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

import com.example.demo.Models.DAO.PacienteDAO;
import com.example.demo.Models.DAO.UsuarioDAO;
import com.example.demo.Models.Entity.Paciente;
import com.example.demo.Models.Entity.Usuario;

@RestController
@RequestMapping("/pacientes")
@CrossOrigin(origins = "http://localhost:3000")
public class PacienteController {

    @Autowired
    private PacienteDAO pacienteDAO;

    @Autowired
    private UsuarioDAO usuarioDAO;

  //  public PacienteController(PacienteDAO pacienteDAO) {
  //      this.pacienteDAO = pacienteDAO;
  //  }

    @GetMapping("/mostrar")
    public List<Paciente> listaPacientes() {
        return pacienteDAO.findAll();
    }

    @GetMapping("/{id}")
    public Paciente obtenerPaciente(@PathVariable Long id) {
        return pacienteDAO.findById(id);
    }

    @PostMapping("/crear")
public ResponseEntity<?> crearPaciente(@RequestBody Paciente paciente) {

    Paciente existente = pacienteDAO.findByCorreo(paciente.getCorreo());
    if (existente != null) {
        return ResponseEntity
            .status(400)
            .body("Error: Ya existe un paciente registrado con ese correo.");
    }

    Usuario usuarioExistente = usuarioDAO.findByCorreo(paciente.getCorreo());
    if (usuarioExistente != null) {
        return ResponseEntity
            .status(400)
            .body("Error: Ya existe un usuario registrado con ese correo.");
    }

    Paciente nuevo = pacienteDAO.save(paciente);

    Usuario usuario = new Usuario();
    usuario.setCorreo(paciente.getCorreo());
    usuario.setContraseña(paciente.getContraseña());
    usuario.setRol("PACIENTE");

    usuarioDAO.save(usuario);

    return ResponseEntity.ok(nuevo);
}

    @PutMapping("/actualizar/{id}")
public Paciente actualizarPaciente(@PathVariable Long id, @RequestBody Paciente nuevo) {
    
    Paciente existente = pacienteDAO.findById(id);

    String correoAnterior = existente.getCorreo();

    existente.setNombre(nuevo.getNombre());
    existente.setApellido(nuevo.getApellido());
    existente.setCorreo(nuevo.getCorreo());
    existente.setContraseña(nuevo.getContraseña());

    Usuario usuario = usuarioDAO.findByCorreo(correoAnterior);

    if (usuario != null) {
        usuario.setCorreo(nuevo.getCorreo());
        usuario.setContraseña(nuevo.getContraseña());
        
        usuarioDAO.save(usuario); 
    }

    return pacienteDAO.save(existente);
}

    @DeleteMapping("/eliminar/{id}")
    public String eliminarPaciente(@PathVariable Long id) {
        Paciente paciente = pacienteDAO.findById(id);
        if (paciente == null) {
            return "Error: Paciente no encontrado con id " + id;
        }
        Usuario usuario = usuarioDAO.findByCorreo(paciente.getCorreo());
        if (usuario != null) {
            usuarioDAO.delete(usuario.getId());
        }
        pacienteDAO.delete(paciente.getId());

        return "Paciente eliminado correctamente";
    }
}
