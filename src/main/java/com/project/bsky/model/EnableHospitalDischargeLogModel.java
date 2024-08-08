package com.project.bsky.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TBL_HOSPITALAPVBYSNA_LOG")
public class EnableHospitalDischargeLogModel {
	
	
	@Id
	@GenericGenerator(name = "catInc", strategy = "increment")
    @GeneratedValue(generator = "catInc")
	@Column(name = "LOGID")
	private Long logid;
	
	@Column(name = "HOSPITALAPVBYSNA_ID")
	private Long hospaprvlogid;
	
	@Column(name = "APPROVEFLAG")
	private Character approveflag;
	
	@Column(name = "CREATEDBY")
	private Long createby;
	
	@Column(name = "CREATEDON")
	private Date createon;
	
	@Column(name = "DISTRICTCODE")
	private String districtcode;
	
	@Column(name = "HOSPITALCODE")
	private String hospitalcode;
	
	
	
	@Column(name = "STATECODE")
	private String statecode;
	
	@Column(name = "STATUSFLAG")
	private Integer statusflag;
	
	@Column(name = "UPDATEDBY")
	private Long updateby;
	
	@Column(name = "UPDATEDON")
	private Date updateon;
	
}
