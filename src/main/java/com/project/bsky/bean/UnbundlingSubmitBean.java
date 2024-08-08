package com.project.bsky.bean;

import lombok.Data;

@Data
public class UnbundlingSubmitBean {
	private Long userid;
	private String  unbundlingpackage;
	private String[] packageid;
   }
