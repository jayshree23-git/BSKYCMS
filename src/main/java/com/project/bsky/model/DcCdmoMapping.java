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

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

/**
 * 
 */
@Entity
@Data
@Table(name = "TBL_DC_CDMO_MAPPING")
public class DcCdmoMapping {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TBL_DC_CDMO_MAPPING_MAPPING_ID_SEQ")
	@SequenceGenerator(name = "TBL_DC_CDMO_MAPPING_MAPPING_ID_SEQ", sequenceName = "TBL_DC_CDMO_MAPPING_MAPPING_ID_SEQ", allocationSize = 1)
	@Column(name = "MAPPING_ID")
	private Long dcCdmoMappingId;
	
	@Column(name = "DCUSER_ID")
	private Long dcuserId;
	
	@Column(name = "CDMO_USERID")
	private Long cdmouserId;
	
	@Column(name = "STATECODE")
	private String statecode;
	
	@Column(name = "DISTRICTCODE")
	private String distcode;
	
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
