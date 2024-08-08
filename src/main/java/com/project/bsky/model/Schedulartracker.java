/**
 * 
 */
package com.project.bsky.model;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * @author rajendra.sahoo
 *
 */
@Data
@Entity
@Table(name = "tbl_schedulerrunningtracker")
public class Schedulartracker {

	@Id
//	@GenericGenerator(name = "catInc", strategy = "increment")
//	@GeneratedValue(generator = "catInc") // for oracle sequence generate
	@Column(name = "SLNO")
	private Long slno;
	
	@Column(name = "PROCEDURENAME")
	private String procedurename;
	
	@Column(name = "EXECUTIONSTARTDATE")
	private Date starttime;
	
	@Column(name = "EXECUTIONENDDATE")
	private Date endtime;
	
	@Column(name = "TOTALTIMEINSECONDS")
	private String timeinsecound;
	
	@Column(name = "TOTALRECORD_PROCESSED")
	private Long recordprocessed;
}
