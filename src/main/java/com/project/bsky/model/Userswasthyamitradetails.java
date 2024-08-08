/**
 * 
 */
package com.project.bsky.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

/**
 * @author rajendra.sahoo
 *
 */
@Data
@Entity
@Table(name = "USER_SWASTHYA_MITRA_DETAILS")
public class Userswasthyamitradetails implements Serializable {
	
	@Id
	@GenericGenerator(name = "catInc", strategy = "increment")
	@GeneratedValue(generator = "catInc")
	@Column(name = "SWASTHYA_MITRA_DETAILS_ID")
	private Long detailsid;
	
	@Column(name = "USER_ID")
	private Integer userid;
	
	@Column(name = "FACE_PATH")
	private String facedata;
	
	@Column(name = "USER_NAME")
	private String username;
	
	@Column(name = "FULL_NAME")
	private String fullname;
	
	@Column(name = "CREATEDBY")
	private Integer createdby;
	
	@Column(name = "CREATEDON")
	private Date createdon;
	
	@Column(name = "UPDATEDBY")
	private Integer updatedby;
	
	@Column(name = "UPDATEDON")
	private Date updatedon;
	
	@Column(name = "STATUSFLAG")
	private Integer statusflag;
	
	@Transient
	private String regdate;
	
	@Transient
	private String mobile;
	
	@Transient
	private String emailid;
	

}
