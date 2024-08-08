package com.project.bsky.model;


/**
 * @author rajendra.sahoo
 *
 */

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="SUBGROUP")
@Getter
@Setter
@ToString
public class Subgroup implements Serializable {
	
	
	@Id
	@GenericGenerator(name = "catInc", strategy = "increment")
	@GeneratedValue(generator = "catInc") //for oracle sequence generate
	@Column(name="SUBGROUPID")
	private Long subgroupid;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="GROUPID")
	private Group groupid;
	
	@NotEmpty(message = "subgroupname cannot be empty")
	@NotNull(message = "subgroupname cannot be null")
	@Column(name="SUBGROUPNAME")
	private String subgroupname;
	
	@Column(name="CREATEDBY")
	private String createdby;
	
	@Column(name="CREATEDDATE")
	private Date createdate;
	
	@Column(name="UPDATEDBY")
	private String updateby;
	
	@Column(name="UPDATEDDATE")
	private Date updatedate;
	 
	@Column(name="STATUS")
	private Integer status;

}
