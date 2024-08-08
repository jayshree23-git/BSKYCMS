/**
 * 
 */
package com.project.bsky.bean;


import org.springframework.web.multipart.MultipartFile;

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
public class SnaSytemRejectedActionBean {

	private Long rejectionId;
	private Long transactionDetailsId;
	private int statusflag;
    private String claimBy;
    private String snaRemark;
    private int sysRejStatus;
    private MultipartFile ApproveDoc;
}
