package com.aliumair.umairfxapp.vo;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FxDealVO {
	private Integer dealId;
	private String fromCurrency;
	private String toCurrency;
	private Date dealDate;
	private Long amount;
	private String description;
	private String dealFileName;
}
