/**
 * 
 */
package com.project.bsky.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;

/**
 * Rajendra
 */
@Entity
@Data
@Table(name = "DC_REFERRAL_HOSPITAL_MAPPING")
public class DcGovtHospitalMapping {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DC_REFERRAL_HOSPITAL_MAPPING_ID_SEQ")
	@SequenceGenerator(name = "DC_REFERRAL_HOSPITAL_MAPPING_ID_SEQ", sequenceName = "DC_REFERRAL_HOSPITAL_MAPPING_ID_SEQ", allocationSize = 1)
	@Column(name = "MAPPING_ID")
	private Long dcHospMappingId;
	
	@Column(name = "DCUSER_ID")
	private Long dcuserId;
	
	@Column(name = "HOSPITAL_ID")
	private Long hospitalId;
	
	@Column(name = "CREATEDON")
	private Date createon;
	
	@Column(name = "CREATEDBY")
	private Long createby;
	
	@Column(name = "UPDATEDON")
	private Date updateon;
	
	@Column(name = "UPDATEDBY")
	private Long updateby;
	
	@Column(name = "STATUSFLAG")
	private Integer statusflag;
	
	@Column(name = "DC_IMAGE")
	private String dcImage;
	
	@Column(name = "DC_IMAGE_LATITUDE")
	private String dcImageLatitude;
	
	@Column(name = "DC_IMAGE_LONGITUDE")
	private String dcImageLongitude;
	
	@Column(name = "DC_IMAGE_DATE")
	private Date dcImageDate;
}
