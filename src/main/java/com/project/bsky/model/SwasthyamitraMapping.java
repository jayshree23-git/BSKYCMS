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
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author rajendra.sahoo
 *
 */

@Data
@Entity
@Table(name = "USER_SWATHYA_MITRA_MAPPING")
public class SwasthyamitraMapping implements Serializable {

	
	@Id
	@GenericGenerator(name = "catInc", strategy = "increment")
	@GeneratedValue(generator = "catInc")
	@Column(name = "MAPPINGID")
	private Long mappingId;
	
	@Column(name = "HOSPITALCODE")
	private String hospitalcode;

	
	
	
//	@Column(name = "USER_ID")
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "USER_ID")
	private UserDetails userdetails;
//	
//	
//	@Column(name = "FACE_PATH")
//	private String faceData;
	
	@Column(name = "STATUSFLAG")
	private Integer statusflag;
	
	@Column(name = "CREATEDBY")
	private Integer createby;
	
	@Column(name = "CREATEDON")
	private Date createon;
	
	@Column(name = "UPDATEDBY")
	private Integer updateby;
	
	@Column(name = "UPDATEDON")
	private Date updateon;

	@Transient
	private String fullname;
	
	@Transient
	private String userid;
	
	@Transient
	private String count;
	
	@Transient
	private String username;
	
}
