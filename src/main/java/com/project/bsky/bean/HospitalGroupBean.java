package com.project.bsky.bean;

import com.project.bsky.model.GroupTypeDetails;
import com.project.bsky.model.UserDetails;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HospitalGroupBean {

	private Integer authId;
	private String authName;
	private String hosCode;
	private String hosName;
	private Integer type;
	private Integer createdUser;
}
