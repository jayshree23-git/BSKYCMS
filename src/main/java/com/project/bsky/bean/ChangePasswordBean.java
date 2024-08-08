/**
 * 
 */
package com.project.bsky.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author preetam.mishra
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordBean {

	private String oldPassword;
	private String userName;
	private String mobileNo;
	private String newPassword;
	private String confirmPassword;

}
