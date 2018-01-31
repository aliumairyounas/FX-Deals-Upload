package com.aliumair.umairfxapp.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "fx_deal_valid")
public class FxDealValid extends KeyGenerator{

	@Column(name = "deal_id")
	private Integer dealId;
	
	@ManyToOne
	@JoinColumn(nullable = true)
	private Currency fromCurrency;
	
	@ManyToOne
	@JoinColumn(nullable = true)
	private Currency toCurrency;
	
	@Column(name = "deal_date")
	private Date dealDate;
	
	private Long amount;
	
	@ManyToOne
	private DealFile dealFileName;
}
