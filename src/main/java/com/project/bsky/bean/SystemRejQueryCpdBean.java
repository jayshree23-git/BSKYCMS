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
public class SystemRejQueryCpdBean {

	private Long transactiondetailsid;
	private Long userId;
	private String description;
	private String hospitalcode;
	private String urnNo;
    private int statusflag;
    private Date claimBy;
    private int claimstatus;
    
}
