/**
 * 
 */
package com.project.bsky.bean;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author arabinda.guin
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClaimFilterBean {
	
	private Long userId;
	private String flag;
	private Date fromDate;
	private Date toDate;
	private String stateCode;
	private String distCode;
	private String hospitalCode;

}
