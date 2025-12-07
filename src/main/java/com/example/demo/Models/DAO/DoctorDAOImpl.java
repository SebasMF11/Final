package com.example.demo.Models.DAO;

import java.time.LocalTime;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.example.demo.Models.Entity.Doctor;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Repository
@Transactional
public class DoctorDAOImpl implements DoctorDAO {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Doctor findById(long id) {
        return em.find(Doctor.class, id);
    }

    @Override
    public List<Doctor> findAll() {
        return em.createQuery("FROM Doctor", Doctor.class).getResultList();
    }

    @Override
    public Doctor save(Doctor doctor) {
        if (doctor.getId() == 0) {  
            em.persist(doctor);
            return doctor; 
        } else {            
            return em.merge(doctor);
        }
    }

    @Override
    public void delete(long id) {
        Doctor doctor = findById(id);
        if (doctor != null) {
            em.remove(doctor);
        }
    }

    @Override
    public Doctor findByCorreo(String correo) {
        return em.createQuery("FROM Doctor d WHERE d.correo = :correo", Doctor.class)
                .setParameter("correo", correo)
                .getResultStream()
                .findFirst()
                .orElse(null);
    }

    @Override
    public Doctor login(String correo, String contraseña) {
        return em.createQuery("FROM Doctor d WHERE d.correo = :correo AND d.contraseña = :contraseña", Doctor.class)
                .setParameter("correo", correo)
                .setParameter("contraseña", contraseña)
                .getResultStream()
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Doctor> findByEspecialidad(String especialidad) {
        return em.createQuery("FROM Doctor d WHERE d.especialidad = :especialidad", Doctor.class)
                .setParameter("especialidad", especialidad)
                .getResultList();
    }

    @Override
    public List<Doctor> findByEstado(String estado) {
        return em.createQuery("FROM Doctor d WHERE d.Estado = :estado", Doctor.class)
                .setParameter("estado", estado)
                .getResultList();
    }

    @Override
    public List<Doctor> findAvailableBetween(LocalTime inicio, LocalTime fin) {
        return em.createQuery(
                "FROM Doctor d WHERE d.HoraInicio <= :inicio AND d.HoraFin >= :fin", Doctor.class)
                .setParameter("inicio", inicio)
                .setParameter("fin", fin)
                .getResultList();
    }
}
