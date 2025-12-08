package com.example.demo.Controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Models.DAO.DoctorDAO;
import com.example.demo.Models.DAO.PacienteDAO;
import com.example.demo.Models.DAO.UsuarioDAO;
import com.example.demo.Models.Entity.Doctor;
import com.example.demo.Models.Entity.Paciente;
import com.example.demo.Models.Entity.Usuario;

@RestController
@RequestMapping("/login")
@CrossOrigin(origins = "http://localhost:3000")
public class LoginController {

    @Autowired
    private UsuarioDAO usuarioDAO;

    @Autowired
    private DoctorDAO doctorDAO;

    @Autowired
    private PacienteDAO pacienteDAO;

    @PostMapping
    public Object login(@RequestBody Usuario credenciales) {

    Usuario usuario = usuarioDAO.findByCorreo(credenciales.getCorreo());

    if (usuario == null) {
        return "Usuario no encontrado";
    }

    if (!usuario.getContraseña().equals(credenciales.getContraseña())) {
        return "Contraseña incorrecta";
    }

    Map<String, Object> response = new HashMap<>();
    response.put("rol", usuario.getRol());
    response.put("correo", usuario.getCorreo());

    if ("ADMIN".equals(usuario.getRol())) {

    response.put("mensaje", "Inicio de sesión exitoso. Rol: ADMIN");

    } else if ("DOCTOR".equals(usuario.getRol())) {

    Doctor doctor = doctorDAO.findByCorreo(usuario.getCorreo());
    if (doctor != null) {
        response.put("id", doctor.getId());
    }
    response.put("mensaje", "Inicio de sesión exitoso. Rol: DOCTOR");

    } else if ("PACIENTE".equals(usuario.getRol())) {

    Paciente paciente = pacienteDAO.findByCorreo(usuario.getCorreo());
    if (paciente != null) {
        response.put("id", paciente.getId());
    }
    response.put("mensaje", "Inicio de sesión exitoso. Rol: PACIENTE");
    }

return response;

}

}
