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

    /*   public PacienteController(PacienteDAO pacienteDAO) {
        this.pacienteDAO = pacienteDAO;
    }*/

    @GetMapping("/mostrar")
    public List<Paciente> listaPacientes() {
        return pacienteDAO.findAll();
    }

    @GetMapping("/{id}")
    public Paciente obtenerPaciente(@PathVariable Long id) {
        return pacienteDAO.findById(id);
    }

    //CREAR PACIENTE NUEVO
    @PostMapping("/crear")
    public ResponseEntity<?> crearPaciente(@RequestBody Paciente paciente) {

    if (!correoValido(paciente.getCorreo())) {
        return ResponseEntity
                .badRequest()
                .body("Error: El correo ingresado no es válido.");
    }

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

    //VALIDAR FORMATO DE CORREO
    private boolean correoValido(String correo) {
    String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    return correo != null && correo.matches(regex);
}


    //ACTUALIZAR PACIENTE, Y A LA VEZ SU USUARIO
    @PutMapping("/actualizar/{id}")
public ResponseEntity<?> actualizarPaciente(@PathVariable Long id, @RequestBody Paciente nuevo) {

    Paciente existente = pacienteDAO.findById(id);
    if (existente == null) {
        return ResponseEntity.status(404).body("Error: Paciente no encontrado.");
    }

    String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    if (!nuevo.getCorreo().matches(regex)) {
        return ResponseEntity
            .status(400)
            .body("Error: El correo ingresado no es válido.");
    }

    Paciente pacienteConMismoCorreo = pacienteDAO.findByCorreo(nuevo.getCorreo());

    if (pacienteConMismoCorreo != null && pacienteConMismoCorreo.getId() != id) {
        return ResponseEntity
            .status(400)
            .body("Error: Ya existe otro paciente registrado con ese correo.");
    }

    Usuario usuarioConMismoCorreo = usuarioDAO.findByCorreo(nuevo.getCorreo());

    if (usuarioConMismoCorreo != null && !usuarioConMismoCorreo.getCorreo().equals(existente.getCorreo())) {
        return ResponseEntity
            .status(400)
            .body("Error: Ya existe un usuario registrado con ese correo.");
    }

    String correoAnterior = existente.getCorreo();

    existente.setNombre(nuevo.getNombre());
    existente.setApellido(nuevo.getApellido());
    existente.setCorreo(nuevo.getCorreo());
    existente.setContraseña(nuevo.getContraseña());

    pacienteDAO.save(existente);

    Usuario usuario = usuarioDAO.findByCorreo(correoAnterior);

    if (usuario != null) {
        usuario.setCorreo(nuevo.getCorreo());
        usuario.setContraseña(nuevo.getContraseña());
        usuarioDAO.save(usuario);
    }

    return ResponseEntity.ok(existente);
}


    //ELIMINAR PACIENTE Y SU USUARIO ASOCIADO
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
