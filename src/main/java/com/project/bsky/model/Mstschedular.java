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
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

/**
 * @author rajendra.sahoo
 *
 */
@Data
@Entity
@Table(name = "mst_scheduler")
public class Mstschedular {
	
	@Id
	@GenericGenerator(name = "catInc", strategy = "increment")
	@GeneratedValue(generator = "catInc") // for oracle sequence generate
	@Column(name = "ID")
	private Integer id;
	
	@Column(name = "PROCEDURE_NAME")
	private String procedurename;
	
	@Column(name = "PURPOSE")
	private String proceduredescrioption;
	
	@Column(name = "SCHEDULAR_NAME")
	private String schedularname;
	
	@Column(name = "RUNNINGINTERVAL")
	private String runninginterval;
	
	@Column(name = "RUNNINGTIME")
	private String runningtime;
	
	@Column(name = "STATUS")
	private Integer status;
	
	@Column(name = "CREATED_BY")
	private Integer createdby;
	
	@Column(name = "CREATED_ON")
	private Date createdon;
	
	@Column(name = "UPDATED_BY")
	private Integer updatedby;
	
	@Column(name = "UPDATED_ON")
	private Date updatedon;
	
	@Transient
	private String createdate;
	
	@Transient
	private String statusflag;
	
	@Transient
	private String remark;

}
