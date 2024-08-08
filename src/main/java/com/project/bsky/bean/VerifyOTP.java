package com.project.bsky.bean;

import lombok.Data;

/**
 * @Project : CSM Framework Backend
 * @Auther : Truptimayee Sa
 * @Created On : 23/11/2022 - 4:49 PM
 */
@Data
public class VerifyOTP {
    private String hosName;
    private String mobile;
    private String otp;
    private String encText;
    private Integer flag;
}
