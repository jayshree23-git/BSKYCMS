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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import lombok.Data;
import lombok.ToString;

/**
 * @author rajendra.sahoo
 *
 */
@Entity
@Data
@Table(name = "TBL_FO_REMARK")
public class FoRemark implements Serializable {

	@Id
	@Column(name = "REMARKID")
	@GenericGenerator(name = "catInc", strategy = "increment")
	@GeneratedValue(generator = "catInc") //for oracle sequence generate
	private Long remarkid;

	@Column(name = "REMARK")
	private String remark;
	
	@Column(name = "CREATEDBY")
	private Long createdBy;
	
	@Column(name = "CREATEDON")
	private Date createdOn;

	@Column(name = "UPDATEDBY")
	private Long updatedBy;

	@Column(name = "UPDATEDON")
	private Date updatedOn;

	@Column(name = "STATUSFLAG")
	private Integer status;
	
	@Column(name = "REMARK_DESCRIPTION")
	private String description;
}
