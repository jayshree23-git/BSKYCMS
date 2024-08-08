/**
 * 
 */
package com.project.bsky.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

/**
 * @author rajendra.sahoo
 *
 */
@Entity
@Data
@Table(name = "HOSPITALBACKDATECONFIG_LOG")
public class HospitalBackdateConfigLog implements Serializable {
	
	@Id
	@GenericGenerator(name = "catInc", strategy = "increment")
	@GeneratedValue(generator = "catInc")
	@Column(name = "HOSPITALBACKDATECONFIG_ID")
	private Long hospitalconfigid;
	
	@Column(name = "BACKDATE_DISCHARGE_DAYS")
	private Integer backdatedischargedate;
	
	@Column(name = "BACKDATE_ADMISSION_DAYS")
	private Integer backdateadmissiondate;
	
//	@Column(name = "CREATED_BY")
//	private Long createdby;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "CREATED_BY")
	private UserDetails createdby;
	
	@Column(name = "CREATED_ON")
	private Date createon;
	
//	@Column(name = "DISTRICT_CODE")
//	private String dist;
	@ManyToOne
	@JoinColumns({ @JoinColumn(name = "DISTRICT_CODE", referencedColumnName = "DISTRICTCODE"),
			@JoinColumn(name = "STATE_CODE", referencedColumnName = "STATECODE") })
	private DistrictMaster districtcode;
	
//	@Column(name = "HOSPITAL_CODE")
//	private String hospitalcode;
	
	@ManyToOne
	@JoinColumns({ @JoinColumn(name = "HOSPITAL_CODE", referencedColumnName = "HOSPITAL_CODE")})
	private HospitalInformation hospital;
	
//	@Column(name = "STATE_CODE")
//	private String state;
	
	@Column(name = "STATUS_FLAG")
	private Integer status;

}
