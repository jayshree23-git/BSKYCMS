package com.project.bsky.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Data;
@Entity
@Data
	@Table(name = "MST_OUTOF_POCKET_EXPENDITURE")
	public class OutofpacketexpenditureMaster {

		@Id
		@Column(name = "EXPENDITURE_ID",unique = true,nullable = false)
		@GeneratedValue(strategy=GenerationType.IDENTITY)
		private Long expenditureId;
		
		@Column(name = "EXPENDITURE_NAME")
		private String expenditurename;
		
		
		@Column(name = "CREATEDBY")
		private Long createdby;
		
		@Column(name = "CREATEDON")
		private Date createdon;
		
		@Column(name = "UPDATEDBY")
		private Long updatedby;
		
		@Column(name = "UPDATEDON")
		private Date updatedon;
		
		@Column(name = "STATUSFLAG")
		private Integer statusflag;
		
		@Transient
		private String createbyname;
		
		@Transient
		private String createtime;
}
