package com.project.bsky.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

@Data
@Entity
@Table(name ="PACKAGEMASTERBSKY_DUP")
public class PackageMasterBSKY implements Serializable {
	
	@Id
	@GenericGenerator(name = "catInc", strategy = "increment")
	@GeneratedValue(generator = "catInc")
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "PROCEDURECODE")
	private String procedureCode;
	
	@Column(name = "PROCEDURES")
	private String procedures;
	
	@Column(name = "STATUS_FLAG")
	private Integer statusFlag;
	
	
	
	
	
	
	

}
