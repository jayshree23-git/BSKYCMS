package com.project.bsky.bean;

import lombok.Data;

@Data
public class Documentmstbean {
	
	private Long docid;
	private Integer preauth;
	private Integer claim;
	private Integer status;
}
