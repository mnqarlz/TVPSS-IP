package com.tvpss.dao;

import com.tvpss.entity.TVPSSVersion;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class TVPSSVersionDao {

    @PersistenceContext
    private EntityManager entityManager;

    public TVPSSVersion findById(Long id) {
        return entityManager.find(TVPSSVersion.class, id);
    }

    public TVPSSVersion findBySchoolInfoId(Long schoolInfoId) {
        try {
            return entityManager.createQuery(
                    "SELECT t FROM TVPSSVersion t WHERE t.schoolInfo.id = :schoolInfoId", TVPSSVersion.class)
                    .setParameter("schoolInfoId", schoolInfoId)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<TVPSSVersion> findAll() {
        return entityManager.createQuery("SELECT t FROM TVPSSVersion t", TVPSSVersion.class)
                .getResultList();
    }

    public void save(TVPSSVersion tvpssVersion) {
        if (tvpssVersion.getId() == null) {
            entityManager.persist(tvpssVersion);
        } else {
            entityManager.merge(tvpssVersion);
        }
    }

    public void deleteById(Long id) {
        TVPSSVersion tvpssVersion = findById(id);
        if (tvpssVersion != null) {
            entityManager.remove(tvpssVersion);
        }
    }
}
