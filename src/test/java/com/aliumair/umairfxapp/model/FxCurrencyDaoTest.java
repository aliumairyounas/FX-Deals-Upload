package com.aliumair.umairfxapp.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import com.aliumair.umairfxapp.UmairFxAppApplication;
import com.aliumair.umairfxapp.domain.Currency;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UmairFxAppApplication.class)
public class FxCurrencyDaoTest {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	FxCurrencyDao repository;

	@Test
	public void findById_basic() {
		Currency currency = repository.findById(112);
		assertEquals("JPA in 50 Steps", currency.getName());
	}

	@Test
	@DirtiesContext
	public void deleteById_basic() {
		repository.deleteById(112);
		assertNull(repository.findById(10002));
	}

	@Test
	@DirtiesContext
	public void save_basic() {

		// get a currency
		Currency currency = repository.findById(112);
		assertEquals("Pakistan Rupee", currency.getName());

		// update details
		currency.setName("Pakistan Rupee - Updated");
		repository.save(currency);

		// check the value
		Currency currency1 = repository.findById(10001);
		assertEquals("Pakistan Rupee - Updated", currency1.getName());
	}


}
