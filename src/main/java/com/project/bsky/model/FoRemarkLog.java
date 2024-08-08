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

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;
import lombok.ToString;

/**
 * @author rajendra.sahoo
 *
 */
@Entity
@Data
@Table(name = "TBL_FO_REMARK_LOG")
public class FoRemarkLog implements Serializable {

	@Id
	@Column(name = "REMARK_LOGID")
	@GenericGenerator(name = "catInc", strategy = "increment")
	@GeneratedValue(generator = "catInc") //for oracle sequence generate
	private Long remarklogid;

	@Column(name = "REMARKID")
	private Long remarkid;
	
	@Column(name = "OLD_REMARK")
	private String oldremark;
	
	@Column(name = "NEW_REMARK")
	private String newremark;

	@Column(name = "UPDATEDBY")
	private Long updatedBy;

	@Column(name = "UPDATEDON")
	private Date updatedOn;

	@Column(name = "STATUSFLAG")
	private Integer status;
	
	@Column(name = "REMARK_DESCRIPTION")
	private String description;
}
