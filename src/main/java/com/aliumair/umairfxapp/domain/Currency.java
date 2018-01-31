package com.aliumair.umairfxapp.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "deal_currency")
@NamedQuery(name="find_all_currencies", query="select c from Currency c")
@Cacheable
public class Currency extends KeyGenerator{

	@Getter
	@Setter
	private String name;
	
	@Column(name = "code_name")
	private String code;
	
	@UpdateTimestamp
	private LocalDateTime lastUpdatedDate;
	
	@CreationTimestamp
	private LocalDateTime createdDate;
	
	@OneToMany(mappedBy="fromCurrency")
	private List<FxDealValid> fromFxDealValids = new ArrayList<FxDealValid>(); 
	
	@OneToMany(mappedBy="toCurrency")
	private List<FxDealValid> toFxDealValids = new ArrayList<FxDealValid>();
	
	@OneToMany(mappedBy="fromCurrency")
	private List<FxDealInValid> fromFxDealInValids = new ArrayList<FxDealInValid>();
	
	@OneToMany(mappedBy="toCurrency")
	private List<FxDealInValid> toFxDealInValids = new ArrayList<FxDealInValid>();
}
