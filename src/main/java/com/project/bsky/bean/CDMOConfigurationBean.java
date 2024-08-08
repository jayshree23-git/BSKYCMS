package com.project.bsky.bean;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CDMOConfigurationBean {

	private Integer cdmoMappingId;
	private Integer cdmoId;
	private String stateCode;
	private String districtCode;
	private Integer status;
	private String cdmoName;
	private String stateName;
	private String districtName;
	private Integer cpdUserId;

	private Integer createdBy;

	private Integer updatedBy;

}
