package com.aliumair.umairfxapp.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "fx_deal_invalid")
public class FxDealInValid extends KeyGenerator{

	@Column(name = "deal_id")
	private Integer dealId;
	
	@OneToOne
	private Currency fromCurrency;
	
	@OneToOne
	private Currency toCurrency;
	
	@Column(name = "deal_date")
	private Date dealDate;
	
	private Long amount;
	
	private String description;
	
	@ManyToOne
	private DealFile dealFileName;
}
