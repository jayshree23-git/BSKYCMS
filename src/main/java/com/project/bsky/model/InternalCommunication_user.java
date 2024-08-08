/**
 * 
 */
package com.project.bsky.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

/**
 * @author rajendra.sahoo
 *
 */
@Data
@Entity
@Table(name="TBL_INTERNAL_COMMUNICATION_USER")
public class InternalCommunication_user {
	
	@Id
	@Column(name = "COMMUNICATION_USER_ID")
	@GenericGenerator(name = "catInc", strategy = "increment")
	@GeneratedValue(generator = "catInc")
	private Long commid;
	
	@Column(name = "USERID")
	private Long Userid;
	
	@Column(name = "STATUS_FLAG")
	private Integer statusflag;
	
	@Column(name = "FULL_NAME")
	private String fullname;
	
	@Column(name = "EMAIL")
	private String email;
	
	@Column(name = "PHONE")
	private String phone;

}
