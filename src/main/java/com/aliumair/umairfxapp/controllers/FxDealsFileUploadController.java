package com.aliumair.umairfxapp.controllers;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.aliumair.umairfxapp.domain.Currency;
import com.aliumair.umairfxapp.model.DealFileDao;
import com.aliumair.umairfxapp.model.FxCurrencyDao;
import com.aliumair.umairfxapp.model.FxDealsUploadDao;
import com.aliumair.umairfxapp.vo.FxDealVO;
import com.aliumair.umairfxapp.vo.SummaryVO;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;



@Controller
public class FxDealsFileUploadController {

	private static final Logger logger = LogManager.getLogger(FxDealsFileUploadController.class);
	
	@Autowired
    private MessageSource messageSource;
	
	@Autowired
	DealFileDao fileRepository;
	
	@Autowired
	FxCurrencyDao currencyRepository;
	
	@Autowired
	FxDealsUploadDao dealRepository;
	
    @GetMapping("/*")
	public String startApp() {

		logger.info("Start App method");
		return "addFxDeals";

	}
    
    @SuppressWarnings({ "null", "resource" })
	@GetMapping("/generateSampleDeals")
	public String generateSampleFile(ModelMap model, HttpServletRequest request,RedirectAttributes redirectAttributes) {
    	Map<String, String> responseMessages = new HashMap<String, String>();
    	String fileName = "sample.csv";
        String rootPath = request.getSession().getServletContext().getRealPath("/");
        File directory = new File(rootPath + File.separator + "fx-Deals-Generated-Files");
        if (!directory.exists()) {
        	directory.mkdirs();
        }
        
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");	
        String sampleDate = dateFormatter.format(new Date());
        File fullPath = new File(directory.getAbsolutePath() + File.separator + (fileName));
        logger.info("File Path "+ fullPath.getAbsolutePath()+ " and file name: "+fullPath.getName());
        CSVWriter writer = null;
        try {
        	
        	writer = new CSVWriter(new FileWriter(fullPath, true));
            
            int toId = 1, fromId = 162;
            
            for (int id = 1; id < 10000; id++) {
    			Currency curTo = currencyRepository.findById(toId);
    			Currency curFrom = currencyRepository.findById(fromId);
    			
    			String [] record = (id+","+curTo.getCode()+","+curFrom.getCode()+","+sampleDate+","+id).split(",");
    			writer.writeNext(record);
                writer.flush(); 
    			
    			if(fromId==1) {
    				fromId=162;
    			}
    			
    			if(toId == 162) {
    				toId = 1;
    			}
    			fromId--;
    			toId++;
    			
    		}
                
            logger.info("Generate Successfully on "+ fullPath.getAbsolutePath()+ " and file name: "+fullPath.getName());
        } catch (IOException ex) {
        	
        	logger.error("During generating sample file failed ", ex);
        	responseMessages.put("errorMessage", messageSource.getMessage("failed.msg",new Object [] {ex}, Locale.getDefault()) );
            model.put("messages", responseMessages);
            
        	try {
        		writer.close();
			} catch (IOException e) {
				logger.error("During Generating sample file failed ", e);
			}
        	            
            return "generateDeals";
        }
		return "generateDeals";

	}
	
    @GetMapping("/summaryReport")
	public String getAccumulativeStats(ModelMap model) {
    	
    	dealRepository.getAcummulativeStats();
    	
    	return "summaryDetails";
    
	}
    
    @PostMapping(value = "/addFxDeals")
    public String uploadFile(
            ModelMap model,
            @RequestParam("dealFile") MultipartFile dealFile,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes) {
    	
    	logger.info("FX Deals upload started");
    	Map<String, String> responseMessages = new HashMap<String, String>();
    	
        if (dealFile.isEmpty()) {
            logger.info("Input file is empty");
        	responseMessages.put("errorMessage", messageSource.getMessage("missing",null, Locale.getDefault()));
            model.put("messages", responseMessages);
            return "addFxDeals";
        }
        
        if(fileRepository.checkFileAlreadyFound(dealFile.getOriginalFilename())){
        	logger.info("Input File already fount in system");
        	responseMessages.put("errorMessage", "File already found in system");
            model.put("messages", responseMessages);
        	return "addFxDeals";
        }
        
        if(!dealFile.getOriginalFilename().split("\\.")[1].equals("csv")){
        	logger.info("Only CSV Files allowed to Upload");
        	responseMessages.put("errorMessage", "Only CSV Files allowed to Upload");
            model.put("messages", responseMessages);
        	return "addFxDeals";
        }
        
        logger.info("Upload Staring");
        
        String fileName = dealFile.getOriginalFilename();
        String rootPath = request.getSession().getServletContext().getRealPath("/");
        File directory = new File(rootPath + File.separator + "fx-Deals-Uploaded-Files");
        if (!directory.exists()) {
        	directory.mkdirs();
        }
        
     
        File fullPath = new File(directory.getAbsolutePath() + File.separator + (fileName));
        logger.info("File Path "+ fullPath.getAbsolutePath()+ " and file name: "+fullPath.getName());
        InputStream inputstream = null;
        BufferedOutputStream bufferedStream = null;
        
        try {
            inputstream = dealFile.getInputStream();
            bufferedStream = new BufferedOutputStream(new FileOutputStream(fullPath));	
            int val;
            while ((val = inputstream.read()) != -1) {
              	bufferedStream.write(val);
            }
            bufferedStream.flush();
                
            logger.info("Upload Successfully on "+ fullPath.getAbsolutePath()+ " and file name: "+fullPath.getName());
        } catch (IOException ex) {
        	
        	logger.error("During uploading failed ", ex);
        	responseMessages.put("errorMessage", messageSource.getMessage("failedMsg",new Object [] {ex}, Locale.getDefault()) );
            model.put("messages", responseMessages);
            
        	try {
				inputstream.close();
				bufferedStream.close();
			} catch (IOException e) {
				logger.error("During uploading failed ", e);
			}
        	            
            return "addFxDeals";
        }
        
        List<FxDealVO> fxValidDeals = new ArrayList<>();
        List<FxDealVO> fxInValidDeals = new ArrayList<>();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");	
           //Build reader instance
           CSVReader reader;
		try {
			reader = new CSVReader(new FileReader(fullPath), ',', '"', 1);
		
            
           //Read all rows at once
           List<String[]> allRows = reader.readAll();
            
           //Read CSV line by line and use the string array as you want
          for(String[] row : allRows){
             
             FxDealVO dealVo = new FxDealVO();
             dealVo.setDealFileName(dealFile.getOriginalFilename());
             StringBuffer logStr = new StringBuffer(); 
             boolean recordValid = true;
          	try {
          		if(row[0].trim().equals("")) {
          			recordValid = false;
         			dealVo.setDealId(0);
         			logStr.append("Exception while parsing Deal ID");
         			logger.debug("Exception while parsing Deal ID: ");
          		}else {
             		dealVo.setDealId(Integer.parseInt(row[0]));          			
          		}

     		} catch (NumberFormatException e) {
     			recordValid = false;
     			dealVo.setDealId(0);
     			logStr.append("Exception while parsing Deal ID");
     			logger.debug("Exception while parsing Deal ID: ", e);
     		}
             
          	if(!row[1].trim().equals("")) {
          		if(currencyRepository.checkCurrencyCodeExist(row[1])) {
          			dealVo.setToCurrency(row[1]);
          		}else {
          			recordValid = false;
              		dealVo.setToCurrency("");
              		logStr.append("Invalid From Currency Value Found");
         			logger.debug("Invalid From Currency Value Found {}",row[1]);
          		}
          	}
          	else {
          		recordValid = false;
          		dealVo.setToCurrency("");
          		logStr.append("Invalid To Currency Value Found");
     			logger.debug("Invalid To Currency Value Found {}",row[1]);
          	}
          	
          	if(!row[2].trim().equals("")) {
          		if(currencyRepository.checkCurrencyCodeExist(row[2])) {
          			dealVo.setFromCurrency(row[2]);
          		}else {
          			recordValid = false;
              		dealVo.setFromCurrency("");
              		logStr.append("Invalid From Currency Value Found");
         			logger.debug("Invalid From Currency Value Found {}",row[2]);
          		}
          	}
          	else {
          		recordValid = false;
          		dealVo.setFromCurrency("");
          		logStr.append("Invalid From Currency Value Found");
     			logger.debug("Invalid From Currency Value Found {}",row[2]);
          	}
          	
         	try {
         		
         		if(row[3].trim().equals("")) {
          			recordValid = false;
          			dealVo.setDealDate(null);
          			logStr.append("Exception while parsing Deal Date");
         			logger.debug("Exception while parsing Deal Date ");
         		}else {
          			dealVo.setDealDate(dateFormatter.parse(row[3]));          			
          		}
     		} catch (ParseException e) {
     			recordValid = false;
     			dealVo.setDealDate(null);
     			logStr.append("Exception while parsing Deal Date");
     			logger.debug("Exception while parsing Deal Date: ", e);
     		}
         	
         	try {
         		
         		if(row[4].trim().equals("")) {
          			recordValid = false;
          			dealVo.setAmount(0L);
         			logStr.append("Exception while parsing Deal Amount");
         			logger.debug("Exception while parsing Deal Amount");
         		}else {
         			dealVo.setAmount(Long.parseLong(row[4]));         			
          		}
     		} catch (NumberFormatException e) {
     			recordValid = false;
     			dealVo.setAmount(0L);
     			logStr.append("Exception while parsing Deal Amount");
     			logger.debug("Exception while parsing Deal Amount: ", e);
     		}
         	
         	dealVo.setDescription(logStr.toString());
         	
         	if(recordValid) {
         		fxValidDeals.add(dealVo);
         	}else {
         		fxInValidDeals.add(dealVo);
         	}
         	
         	if(fxValidDeals.size() >100) {
                dealRepository.savedDeals(fxValidDeals,fxInValidDeals);
                fxValidDeals = new ArrayList<>();
         	}
         	
         	if(fxInValidDeals.size() >100) {
                dealRepository.savedDeals(fxValidDeals,fxInValidDeals);
                fxInValidDeals = new ArrayList<>();
         	}
             
          }
          
          if(fxValidDeals.size() >0 || fxInValidDeals.size() >0) {
              dealRepository.savedDeals(fxValidDeals,fxInValidDeals);
              fxValidDeals = new ArrayList<>();
              fxInValidDeals = new ArrayList<>();
       	   }
          
		} catch (FileNotFoundException e1) {
			logger.error("Exception due to mentioned file not found: ", e1);
		} catch (IOException e1) {
			logger.error("Exception during read the csv file: ", e1);
		} 
     
		responseMessages.put("success", messageSource.getMessage("successMsg",new Object [] {fxValidDeals.size(),fxInValidDeals.size()}, Locale.getDefault()) );
        model.put("messages", responseMessages);
    	logger.info("Upload completed successfully");
        return "addFxDeals";
    }
	
	
	
	
}
