/**
 * 
 */
package com.project.bsky.bean;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

/**
 * @author hrusikesh.mohanty
 *
 */
@Data
public class ClaimRasieBean {
	private long refractionid;
	private String hospitalCode;
	private long updatedby;
	private String urnNumber;
	private long invoiceNumber;
	private String dateofAdmission;
	private String packageCode;
	private String casenumber;
	private String billnumber;
	private MultipartFile treatmentDetailsSlip;
	private MultipartFile hospitalBill;
	private MultipartFile presurgry;
	private MultipartFile postsurgery;
	private MultipartFile intasurgery;
	private MultipartFile specimansurgery;
	private MultipartFile patientpic;
}
