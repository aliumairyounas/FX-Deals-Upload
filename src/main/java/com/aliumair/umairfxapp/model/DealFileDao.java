package com.aliumair.umairfxapp.model;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.aliumair.umairfxapp.domain.DealFile;

@Repository
@Transactional
public class DealFileDao {

	@Autowired
	EntityManager entityManager;

	public List<DealFile> findAll() {
		TypedQuery<DealFile> namedQuery = entityManager.createNamedQuery("find_all_dealsFile", DealFile.class);
		return namedQuery.getResultList();
	}

	public DealFile findById(int id) {
		return entityManager.find(DealFile.class, id);// JPA
	}

	public DealFile save(DealFile dealFile) {
		entityManager.persist(dealFile);
		entityManager.flush();
		return dealFile;
	}

	public void deleteById(int id) {
		DealFile dealFile = findById(id);
		entityManager.remove(dealFile);
	}
	
	public boolean checkFileAlreadyFound(String fileName) {
		return ((Long)entityManager.createQuery("select count(*) from DealFile where name=:fileName")
									.setParameter("fileName", fileName)
									.getSingleResult())
									.intValue() > 0 ? true : false;
	}
	
	public DealFile findByName(String fileName) {
		return ((DealFile)entityManager.createQuery("select d from DealFile d where name=:fileName")
									.setParameter("fileName", fileName)
									.getSingleResult());
	}
}
