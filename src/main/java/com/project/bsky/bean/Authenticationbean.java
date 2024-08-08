package com.project.bsky.bean;

import lombok.Data;

@Data
public class Authenticationbean {

	private String hospitalcode;
	private String hospitalname;
	private String urn;
	private String patient;
	private String verifier;
	private String createon;
	private String prps;
	private String verifystatus;
}
