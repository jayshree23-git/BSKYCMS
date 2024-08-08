package com.project.bsky.config.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthRequest {
	private String userName;
	private String passWord;
//	private String captcha;
//	private String captchaData;
}
