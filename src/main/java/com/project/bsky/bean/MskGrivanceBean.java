/**
 * 
 */
package com.project.bsky.bean;

import java.util.List;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.sun.istack.NotNull;

import lombok.Data;

/**
 * @author rajendra.sahoo
 *
 */
@Data
public class MskGrivanceBean {

	private String casetype;	
	
	private String casetypedata;
	
	@NotNull
	@NotBlank(message="Please Enter Full Name")
	@NotEmpty(message="Please Enter Full Name")	
	@Size(min=5,max = 50,message="Name Should greater then 5 character")  
	private String name;
	
	private String dob;
	
	private String mobile;
	
	@NotNull
	@NotBlank(message="Please Choose District name")
	@NotEmpty(message="Please Choose District name")	
	private String districtId;
	
	private String blockId;
	
	@NotNull
	@NotBlank(message="Please Choose Grievance Type")
	@NotEmpty(message="Please Choose Grievance Type")
	private String grivtype;
	
	@NotNull
	@NotBlank(message="Please Choose Hospital State")
	@NotEmpty(message="Please Choose Hospital State")
	private String hospstate;
	
	@NotNull
	@NotBlank(message="Please Choose Hospital District Name")
	@NotEmpty(message="Please Choose Hospital District Name")
	private String hospdist;
	
	private String hospital;
	
	@Size(max = 250,message="Not Allowed Morethen 250 Character")
	private String desc;
	private String servicedate;
	private String citizenfeedback;
	
	@NotNull
	private Long   userid;
	private String email;	
	
	private String gender;
	private String distname;
	private String hospname;
	private String grivtypename;
	private String blockname;
	private String hstatename;
	private String hdistname;
	private Integer otpverifyStatus;
	private Integer dcName;
	@NotNull
	@NotBlank(message="Please Choose Priority")
	@NotEmpty(message="Please Choose Priority")
	private String priorityType;
	@NotNull
	@NotBlank(message="Please Choose Grievance Medium")
	@NotEmpty(message="Please Choose Grievance Medium")
	private String grvMedium;
	private String grvMediumName;
	private String state;
	private String stateName;
	private Integer forwardStatus;
}
