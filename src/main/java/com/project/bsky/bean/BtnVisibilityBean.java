package com.project.bsky.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BtnVisibilityBean {

	private Long userId;
	private String stateId;
	private String districtId;
	private String hospitalId;
}
