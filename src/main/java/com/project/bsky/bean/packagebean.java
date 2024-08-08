package com.project.bsky.bean;

import lombok.Data;

@Data
public class packagebean {
	private Long id;
	private String packageheadercode;
	private String packageheader;
	private String packageconcat;
	
	
	//for Package List
	private String packageheadername;
	private String procedurecode;
	private String amount;
	private String nabh;
	private String nonnabh;
	private String implantexist;
	
	//for implant List
	private String implantcode;
	private String implantname;
	private String maximumunit;
	private String unitprice;
	private String priceeditable;
	private String uniteditable;
	private Boolean isCheckedImplant;
	
	//for implant HED
	private String hedcode;
	private String hedname;
	private Long price;
	private String unit;
	private String recomendeddose;
	
	
	
	
}
