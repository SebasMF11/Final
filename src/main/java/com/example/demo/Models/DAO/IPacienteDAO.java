package com.example.demo.Models.DAO;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.example.demo.Models.Entity.Paciente;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Repository
@Transactional
public class IPacienteDAO implements PacienteDAO {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Paciente findById(long id) {
        return em.find(Paciente.class, id);
    }

    @Override
    public List<Paciente> findAll() {
        return em.createQuery("FROM Paciente", Paciente.class).getResultList();
    }

    @Override
    public void save(Paciente paciente) {
        if (paciente.getId() == 0) {
            em.persist(paciente);
        } else {
            em.merge(paciente);
        }
    }

    @Override
    public void delete(long id) {
        Paciente paciente = findById(id);
        if (paciente != null) {
            em.remove(paciente);
        }
    }

    @Override
    public Paciente findByCorreo(String correo) {
        return em.createQuery("FROM Paciente p WHERE p.correo = :correo", Paciente.class)
                .setParameter("correo", correo)
                .getResultStream()
                .findFirst()
                .orElse(null);
    }

    @Override
    public Paciente login(String correo, String contraseña) {
        return em.createQuery("FROM Paciente p WHERE p.correo = :correo AND p.contraseña = :contraseña", Paciente.class)
                .setParameter("correo", correo)
                .setParameter("contraseña", contraseña)
                .getResultStream()
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Paciente> findByNombre(String nombre) {
        return em.createQuery("FROM Paciente p WHERE p.nombre = :nombre", Paciente.class)
                .setParameter("nombre", nombre)
                .getResultList();
    }

    @Override
    public List<Paciente> findByApellido(String apellido) {
        return em.createQuery("FROM Paciente p WHERE p.apellido = :apellido", Paciente.class)
                .setParameter("apellido", apellido)
                .getResultList();
    }
}
