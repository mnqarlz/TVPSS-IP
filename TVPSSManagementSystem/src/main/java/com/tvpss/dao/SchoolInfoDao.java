package com.tvpss.dao;

import com.tvpss.entity.SchoolInfo;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class SchoolInfoDao {

    @PersistenceContext
    private EntityManager entityManager;

    public SchoolInfo findById(Long id) {
        return entityManager.find(SchoolInfo.class, id);
    }
    
    public SchoolInfo findFirst() {
        try {
            return entityManager.createQuery("SELECT s FROM SchoolInfo s", SchoolInfo.class)
                    .setMaxResults(1)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public SchoolInfo findBySchoolCode(String schoolCode) {
        try {
            return entityManager.createQuery("SELECT s FROM SchoolInfo s WHERE s.schoolCode = :schoolCode", SchoolInfo.class)
                    .setParameter("schoolCode", schoolCode)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<SchoolInfo> findAll() {
        return entityManager.createQuery("SELECT s FROM SchoolInfo s", SchoolInfo.class).getResultList();
    }

    public void save(SchoolInfo schoolInfo) {
        if (schoolInfo.getId() == null) {
            entityManager.persist(schoolInfo);
        } else {
            entityManager.merge(schoolInfo);
        }
    }

    public void deleteById(Long id) {
        SchoolInfo schoolInfo = findById(id);
        if (schoolInfo != null) {
            entityManager.remove(schoolInfo);
        }
    }

    public boolean existsBySchoolCode(String schoolCode) {
        try {
            Long count = entityManager.createQuery("SELECT COUNT(s) FROM SchoolInfo s WHERE s.schoolCode = :schoolCode", Long.class)
                    .setParameter("schoolCode", schoolCode)
                    .getSingleResult();
            return count > 0;
        } catch (Exception e) {
            return false;
        }
    }
}
