/**
 * 
 */
package com.project.bsky.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

/**
 * Rajendra
 */
@Data
@Entity
@Table(name = "USER_DC_FACE_DETAILS")
public class UserDCFaceDetails {

	@Id
	@GenericGenerator(name = "catInc", strategy = "increment")
	@GeneratedValue(generator = "catInc")
	@Column(name = "FACE_DETAILS_ID")
	private Long detailsid;
	
	@Column(name = "USER_ID")
	private Long userid;
	
	@Column(name = "FACE_PATH")
	private String facedata;
	
	@Column(name = "USER_NAME")
	private String username;
	
	@Column(name = "FULL_NAME")
	private String fullname;
	
	@Column(name = "CREATEDBY")
	private Long createdby;
	
	@Column(name = "CREATEDON")
	private Date createdon;
	
	@Column(name = "UPDATEDBY")
	private Long updatedby;
	
	@Column(name = "UPDATEDON")
	private Date updatedon;
	
	@Column(name = "STATUSFLAG")
	private Integer statusflag;
}
