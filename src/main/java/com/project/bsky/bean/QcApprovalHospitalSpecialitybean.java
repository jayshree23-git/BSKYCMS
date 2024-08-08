package com.project.bsky.bean;

import java.util.List;

import lombok.Data;

@Data
public class QcApprovalHospitalSpecialitybean {
	private Long userid;
	private List<String> specialistid;
	private Integer actiontype;
}
