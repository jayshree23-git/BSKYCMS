/**
 * 
 */
package com.project.bsky.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author rajendra.sahoo
 *
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IntGrvUserBean {

	private String fullname;

	private String username;

	private String userid;

	private String groupid;

	private String phoneno;

	private String email;

	private String statusflag;

	private Integer isOtp;
	
	private String groupName;

}
