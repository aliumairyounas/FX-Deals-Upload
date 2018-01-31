package com.aliumair.umairfxapp.model;

import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.aliumair.umairfxapp.domain.Currency;
import com.aliumair.umairfxapp.domain.FxDealValid;

@Repository
@Transactional
public class FxCurrencyDao {

	@PersistenceContext
	EntityManager entityManager;

	public List<Currency> findAll() {
		TypedQuery<Currency> namedQuery = entityManager.createNamedQuery("find_all_currencies", Currency.class);
		return namedQuery.getResultList();
	}

	public Currency findById(int id) {
		return entityManager.find(Currency.class, id);// JPA
	}

	public Currency save(Currency currency) {
		entityManager.merge(currency);
		return currency;
	}

	public void deleteById(int id) {
		Currency currency = findById(id);
		entityManager.remove(currency);
	}
	
	public boolean checkCurrencyCodeExist(String code) {
		return ((Long)entityManager.createQuery("select count(*) from Currency c where code=:codeName")
			.setParameter("codeName", code)
			.getSingleResult())
			.intValue() > 0 ? true : false;
	}
	
	public Currency findCurrency(String code) {
	/*	Query rqy = entityManager.createQuery("select c from Currency c where code=:codeName")
			.setParameter("codeName", code)
			;*/
		
		TypedQuery<Currency> q = (TypedQuery<Currency>) entityManager.createQuery("select c from Currency c where code=:codeName")
								.setParameter("codeName", code);
		List<Currency> results = q.getResultList();
		Currency currency = new Currency();
		if(results.size() > 0) {
			currency = results.get(0);
		}
		return currency;
	}
}
