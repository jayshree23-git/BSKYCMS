/**
 * 
 */
package com.project.bsky.bean;

import java.sql.Date;

import lombok.Data;

/**
 * @author Hrusikesh Mohanty
 */
@Data
public class PostPaymentRequestNew {
	private Long userId;
	private Long bankModeId;
	private Long bankId;
	private String typeNumber;
	private Date currentDate;
	private Double totalPaidAmount;
	private Double paidAmount;
	private String hospitacode;
	private Double snaApprovedamount;
	private Long approvedclaims;
	private String floatno;
}
