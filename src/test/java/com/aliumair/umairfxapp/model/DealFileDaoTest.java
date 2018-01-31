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
import com.aliumair.umairfxapp.domain.DealFile;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UmairFxAppApplication.class)
public class DealFileDaoTest {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	DealFileDao repository;

	@Test
	public void findById_basic() {
		DealFile dealFile = repository.findById(112);
		String name = dealFile.getName();
		assertEquals("JPA in 50 Steps", name);
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

		// get a dealFile
		DealFile dealFile = repository.findById(112);
		String name = dealFile.getName();
		assertEquals("Pakistan Rupee", name);

		// update details
		name = "Pakistan Rupee - Updated";
		dealFile.setName(name);
		repository.save(dealFile);

		// check the value
		DealFile dealFile1 = repository.findById(10001);
		name = dealFile1.getName();
		assertEquals("Pakistan Rupee - Updated", name);
	}


}
