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
public class MobileUserChangePassword {
private String userId;
private String mobileNo;
private String oldPassword;
private String newPassword;
private String confirmPassword;
}
