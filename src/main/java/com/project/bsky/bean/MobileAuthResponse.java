/**
 * 
 */
package com.project.bsky.bean;

import com.project.bsky.model.MobileUserModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author preetam.mishra
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MobileAuthResponse {
	private int userId;
	private String userName;
	private String email;
	private String token;
}
