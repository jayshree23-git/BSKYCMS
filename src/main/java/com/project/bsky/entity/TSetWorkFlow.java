package com.project.bsky.entity;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;
@Data
@Entity
@Table(name="T_SET_WORKFLOW")
public class TSetWorkFlow implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="T_SET_WORKFLOW_SEQ")
	@SequenceGenerator(name="T_SET_WORKFLOW_SEQ", sequenceName="T_SET_WORKFLOW_SEQ", allocationSize=1)
	private Integer workflowId;
	private Integer projectId;
	private Integer serviceId;
	@Column(length=10485760)
	private String canvasData;
	private Integer deletedFlag;
	private Integer createdBy;
	private Date createdOn;
	private Integer tinType;
	private String vchCtrlName;
	private Integer intLabelId;
	private String vchPCategoryName;
}
