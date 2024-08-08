/**
 * 
 */
package com.project.bsky.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Developer:-Hrusikesh Mohanty
 * Purpose:-Mosarkar Report
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Mosarkar {
	
    private String district_id;
    private String name;
    private String mobile;
    private String age;
    private Long gender;
    private String department_institution_id;
    private String registration_date;
    private String registration_no;
    private String claim_amount;
    private String hospitalInfo;
    private String purpose;
    private String claimNo;
    private Long claimId;
    private Long transactionDetailId;
}
