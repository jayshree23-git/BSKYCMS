package com.project.bsky.config.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class GetFormModuleName implements Serializable{
	private Integer intModuleId;
	private Integer intProcessId;
	private String vchProcessName;
	private String vchModuleName;

}
