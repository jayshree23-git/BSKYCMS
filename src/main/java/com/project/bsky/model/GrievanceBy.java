package com.project.bsky.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

@Data
@Entity
@Table(name ="BSKY_MST_GRIEVANCEBY")

public class GrievanceBy implements Serializable{
	@Id
	@GenericGenerator(name = "catInc", strategy = "increment")
	@GeneratedValue(generator = "catInc")
	@Column(name = "GRIEVANCEBY_ID")
	private Long id;
	
	@Column(name = "GRIEVANCEBY_NAME")
	private String grivancename;
	
	@ManyToOne
	@JoinColumn(name = "CREATED_BY")
	//@Column(name = "CREATED_BY")
	private UserDetails createdby1;
	
	@Column(name = "UPDATED_BY")
	private Long updatedby;
	
	@Column(name = "CREATED_ON")
	private Date createdon;
	
	@Column(name = "UPDATED_ON")
	private Date updatedon;
	
	@Column(name = "STATUS_FLAG")
	private Integer statusFlag;
	
	@Transient
	private String screatedate;
	@Transient
	private Long createdby;
}
