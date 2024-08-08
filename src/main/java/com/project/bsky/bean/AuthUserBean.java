package com.project.bsky.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthUserBean {

	private Long userId;
	private String userName;
	private String phone;
	private String fullName;
	private Integer groupId;
	private Integer statusFlag;
	private long leftDays;
	private String email;
	private Integer isOtp;
}
