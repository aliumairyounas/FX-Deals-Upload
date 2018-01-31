package com.aliumair.umairfxapp;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import com.aliumair.umairfxapp.domain.Currency;
import com.aliumair.umairfxapp.model.FxCurrencyDao;

@SpringBootApplication
public class UmairFxAppApplication  extends SpringBootServletInitializer implements CommandLineRunner {

	private static final Logger logger = LogManager.getLogger(UmairFxAppApplication.class);
	@Autowired
	FxCurrencyDao currencyRepository;
	
	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(UmairFxAppApplication.class);
    }
	
	public static void main(String[] args) {
		SpringApplication.run(UmairFxAppApplication.class, args);
	}
	
	@Override
	public void run(String... args) throws Exception {
		
		logger.info("App Started");
		// Loading all currencies for using in second level cache
		for (int id = 1; id < 163; id++) {
			Currency cur = currencyRepository.findById(id);
			currencyRepository.findCurrency(cur.getCode());
		}
		
	}
	
}
