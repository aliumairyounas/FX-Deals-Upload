package com.aliumair.umairfxapp.model;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.aliumair.umairfxapp.controllers.FxDealsFileUploadController;
import com.aliumair.umairfxapp.domain.DealFile;
import com.aliumair.umairfxapp.domain.FxDealInValid;
import com.aliumair.umairfxapp.domain.FxDealValid;
import com.aliumair.umairfxapp.vo.FxDealVO;
import com.aliumair.umairfxapp.vo.SummaryVO;

import org.apache.logging.log4j.Logger;


@Transactional
@Repository
public class FxDealsUploadDao{
	
	private static final Logger logger = LogManager.getLogger(FxDealsFileUploadController.class);
	
	@PersistenceContext
	EntityManager entityManager;
	
	@Autowired
	DealFileDao fileRepository;
	
	@Autowired
	FxCurrencyDao currencyRepository;
	
	@Value("${spring.jpa.properties.hibernate.jdbc.batch_size}")
	private int batchSize;

	public void savedDeals(List<FxDealVO> fxValidDeals,List<FxDealVO> fxInValidDeals) {
		
		DealFile dealFile = new DealFile();
		if(fxValidDeals.size() > 0) {
			dealFile.setName(fxValidDeals.get(0).getDealFileName());
		}else if(fxInValidDeals.size() > 0){
			dealFile.setName(fxInValidDeals.get(0).getDealFileName());
		}else {
			dealFile.setName("Invalid File Name");
		}
		if(!fileRepository.checkFileAlreadyFound(dealFile.getName())){
			fileRepository.save(dealFile);
		}else {
			dealFile = fileRepository.findByName(dealFile.getName());
		}
		if(fxValidDeals.size() > 0)
      	  saveFxValidDeals(fxValidDeals,dealFile);
        if(fxInValidDeals.size() > 0)
      	  saveFxInValidDeals(fxInValidDeals,dealFile);
	}
	
	private <T extends FxDealValid>Collection<T> saveFxValidDeals(List<FxDealVO> fxValidDeals,DealFile dealFile) {
		  logger.info("Saving Valid Deals started ");
		  final List<T> savedEntities = new ArrayList<T>(fxValidDeals.size());
		  int i = 0;
		  for (FxDealVO dealVo : fxValidDeals) {
			  FxDealValid deal = new FxDealValid();
			  
			  deal.setDealId(dealVo.getDealId());
			  deal.setToCurrency(currencyRepository.findCurrency(dealVo.getToCurrency()));
			  System.out.println("From Currency Value is:"+dealVo.getFromCurrency());
			  deal.setFromCurrency(currencyRepository.findCurrency(dealVo.getFromCurrency()));
			  deal.setDealFileName(dealFile);
			  deal.setDealDate(dealVo.getDealDate());
			  deal.setAmount(dealVo.getAmount());
			  
			  entityManager.persist(deal);
		    
			  i++;
		    if (i % batchSize == 0) {
		      entityManager.flush();
		    }
		  }
		  logger.info("Saving Valid Deals finished");
		  return savedEntities;
	}
	
	private <T extends FxDealInValid>Collection<T> saveFxInValidDeals(List<FxDealVO> inValidDeals,DealFile dealFile) {
		logger.info("Saving InValid Deals started ");
		final List<T> savedEntities = new ArrayList<T>(inValidDeals.size());
		int i = 0;
		for (FxDealVO dealVo : inValidDeals) {
			FxDealInValid deal = new FxDealInValid();
			  
			  deal.setDealId(dealVo.getDealId());
			  deal.setToCurrency(currencyRepository.findCurrency(dealVo.getToCurrency()));
			  System.out.println("From Currency Value is:"+dealVo.getFromCurrency());
			  deal.setFromCurrency(currencyRepository.findCurrency(dealVo.getFromCurrency()));
			  deal.setDealFileName(dealFile);
			  deal.setDealDate(dealVo.getDealDate());
			  deal.setAmount(dealVo.getAmount());
			  deal.setDescription(dealVo.getDescription());
			  
			  entityManager.persist(deal);
		    
			  i++;
		    if (i % batchSize == 0) {
		    	entityManager.flush();
		    }
		}
		logger.info("Saving InValid Deals finished");
		return savedEntities;
	}
	
	public List<SummaryVO> getAcummulativeStats() {
		Query q = entityManager.createNativeQuery("SELECT to_currency_id,code_name, name, count(*) as totalCount FROM fx_deal_valid fxdv, deal_currency dc where dc.id = to_currency_id group by to_currency_id;");
		List summaryList =  q.getResultList();
		List result = new ArrayList<SummaryVO>(); 
		for (Iterator iterator = summaryList.iterator(); iterator.hasNext();) {
			Object[] object = (Object[]) iterator.next();
			SummaryVO summary = new SummaryVO();
			summary.setCodeName((String)object[1]);
			summary.setName((String)object[2]);
			summary.setTotalCount((BigInteger)object[3]);
			result.add(summary);
		}
		
		return result;
	}
	
}