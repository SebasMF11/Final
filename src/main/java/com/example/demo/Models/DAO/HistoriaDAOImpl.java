package com.example.demo.Models.DAO;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.example.demo.Models.Entity.Historia;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Repository
@Transactional
public class HistoriaDAOImpl implements HistoriaDAO {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Historia findById(long id) {
        return em.find(Historia.class, id);
    }

    @Override
    public List<Historia> findAll() {
        return em.createQuery("FROM Historia", Historia.class).getResultList();
    }

    @Override
    public Historia save(Historia historia) {
        if (historia.getId() == 0) {
            em.persist(historia);
            return historia;
        } else {
            return em.merge(historia);
        }
    }


    @Override
    public void delete(long id) {
        Historia h = findById(id);
        if (h != null) em.remove(h);
    }


        @Override
    public List<Historia> findByIdCitaL(List<Long> idsCitas) {
        return em.createQuery(
                "SELECT h FROM Historia h WHERE h.idCita IN :ids",
                Historia.class
        )
        .setParameter("ids", idsCitas)
        .getResultList();
    }

    @Override
    public List<Historia> findByIdPaciente(long idPaciente) {
        return em.createQuery(
                "FROM Historia h WHERE h.IdPaciente = :idPaciente", Historia.class)
                .setParameter("idPaciente", idPaciente)
                .getResultList();
    }

    @Override
    public List<Historia> findByIdDoctor(long idDoctor) {
        return em.createQuery(
                "FROM Historia h WHERE h.IdDoctor = :idDoctor", Historia.class)
                .setParameter("idDoctor", idDoctor)
                .getResultList();
    }

    @Override
    public List<Historia> findByIdCita(Long idCita) {
        String jpql = "SELECT h FROM Historia h WHERE h.idCita = :idCita";
        return em.createQuery(jpql, Historia.class)
             .setParameter("idCita", idCita)
             .getResultList();
    }

}
