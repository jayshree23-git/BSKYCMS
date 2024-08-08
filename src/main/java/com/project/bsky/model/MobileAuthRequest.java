/**
 * 
 */
package com.project.bsky.model;

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
public class MobileAuthRequest {
	private String userName;
	private String passWord;
	private String deviceId;
	private String deviceName;
}
