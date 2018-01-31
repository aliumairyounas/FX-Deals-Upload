package com.aliumair.umairfxapp.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "deal_file")
@NamedQuery(name="find_all_dealsFile", query="select d from DealFile d")
@Cacheable
public class DealFile extends KeyGenerator{
	@Column(name = "file_name")
	private String name;
	
	@OneToMany(mappedBy="dealFileName")
	private List<FxDealValid> fxDealValids = new ArrayList<FxDealValid>(); 
	
	@OneToMany(mappedBy="dealFileName")
	private List<FxDealInValid> fxDealInValids = new ArrayList<FxDealInValid>();

}
