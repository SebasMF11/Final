package com.example.demo.Controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Models.DAO.UsuarioDAO;
import com.example.demo.Models.Entity.Usuario;

@RestController
@RequestMapping("/login")
@CrossOrigin(origins = "http://localhost:3000")
public class LoginController {

    @Autowired
    private UsuarioDAO usuarioDAO;

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
    response.put("id", usuario.getId());
    response.put("rol", usuario.getRol());
    response.put("correo", usuario.getCorreo());

    if ("ADMIN".equals(usuario.getRol())) {
        response.put("mensaje", "Inicio de sesión exitoso. Rol: ADMIN");
    } else if ("DOCTOR".equals(usuario.getRol())) {
        response.put("mensaje", "Inicio de sesión exitoso. Rol: DOCTOR");
    } else {
        response.put("mensaje", "Inicio de sesión exitoso. Rol: PACIENTE");
    }

    return response;
}

}
