/**
 * 
 */
package com.project.bsky.bean;

import com.project.bsky.model.BankDetails;
import com.project.bsky.model.UserDetails;
import com.project.bsky.model.UserDetailsCpd;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author rajendra.sahoo
 *
 */
@Getter
@Setter
@ToString
public class Updatecpdbean {
	
	private UserDetailsCpd cpduserdetails;
	private BankDetails bankdetails;
	private UserDetails userdetails;
}
