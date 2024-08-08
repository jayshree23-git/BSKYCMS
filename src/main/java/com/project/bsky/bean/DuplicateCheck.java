package com.project.bsky.bean;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class DuplicateCheck {
	
	private String insertOrUpdate;
	private String whichTable;
	private String hospitalId;
	private String inputFlag;
	private String inputValue;
}
