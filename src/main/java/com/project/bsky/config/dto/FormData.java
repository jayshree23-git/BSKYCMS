package com.project.bsky.config.dto;

import lombok.Data;

@Data
public class FormData {
	private Integer[] ctrlTypeId;
	private String[] ctrlId;
	private String[] ctrlName;
	private String[] lblName;
	private String[] ctrlValue;
	private String[] uploadedFiles;
	
	private Integer processId;
	private Integer privatsecId;
	private Integer intOnlineServiceId; 
}
