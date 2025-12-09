package com.example.demo.Models.DAO;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.example.demo.Models.Entity.Cita;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Repository
@Transactional
public class CitaDAOImpl implements CitaDAO {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Cita findById(long id) {
        return em.find(Cita.class, id);
    }

    @Override
    public List<Cita> findAll() {
        return em.createQuery("FROM Cita", Cita.class).getResultList();
    }

    @Override
public Cita save(Cita cita) {
    if (cita.getId() == 0) {
        em.persist(cita);
        return cita; 
    } else {
        return em.merge(cita); 
    }
}

    @Override
    public void delete(long id) {
        Cita c = findById(id);
        if (c != null) {
            em.remove(c);
        }
    }

    @Override
    public List<Cita> findByIdPaciente(long idPaciente) {
        return em.createQuery(
                "FROM Cita c WHERE c.IdPaciente = :idPaciente", Cita.class)
                .setParameter("idPaciente", idPaciente)
                .getResultList();
    }

    @Override
    public List<Cita> findByIdDoctor(long idDoctor) {
        return em.createQuery(
                "FROM Cita c WHERE c.IdDoctor = :idDoctor", Cita.class)
                .setParameter("idDoctor", idDoctor)
                .getResultList();
    }

    @Override
    public List<Cita> findByEstado(String estado) {
        return em.createQuery(
                "FROM Cita c WHERE c.estado = :estado", Cita.class)
                .setParameter("estado", estado)
                .getResultList();
    }
}
