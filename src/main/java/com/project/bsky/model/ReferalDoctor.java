/**
 * 
 */
package com.project.bsky.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

/**
 * @author rajendra.sahoo
 *
 */
@Entity
@Table(name = "user_details_referal_doctor")
@Data
public class ReferalDoctor {
	
	@Id
	@GenericGenerator(name = "catInc", strategy = "increment")
	@GeneratedValue(generator = "catInc") // for oracle sequence generate
	@Column(name = "REFERAL_DOCTOR_ID")	
	private Long doctorid;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "USERID")
	private UserDetails userid;
	
	@Column(name = "FULL_NAME")
	private String fullname;
	
	@Column(name = "USER_NAME")
	private String username;
	
	@Column(name = "MOBILE_NO")
	private String mobileno;
	
	@Column(name = "EMAIL_ID")
	private String emailid;
	
	@Column(name = "LICSENSE_NO")
	private String licenseno;
	
//	private String distcode;
//	
//	private String blockcode;
//	
//	private Integer Hospitaltype;
//	
//	private String hospitalname;
	
	@Column(name = "CREATED_ON")
	private Date createon;	
	
	@Column(name = "CREATED_BY")
	private Long createby;
	
	@Column(name = "UPDATED_ON")
	private Date updateon;
	
	@Column(name = "UPDATED_BY")
	private Long updateby;
	
	@Column(name = "STATUS_FLAG")
	private Integer status;

}
