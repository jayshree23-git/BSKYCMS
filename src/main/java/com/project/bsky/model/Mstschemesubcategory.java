/**
 * 
 */
package com.project.bsky.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 
 */
@Data
@Entity
@Table(name = "MSTSCHEMESUBCATEGORY")
public class Mstschemesubcategory implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "SCHEMESUBCATEGORYID")
	private Integer schemesubcategoryid;

	@Column(name = "SCHEMEID")
	private Integer schemeid;

	@Column(name = "SCHEMECATEGORYID")
	private Integer schemecategoryid;

	@Column(name = "SUBCATEGORYNAME")
	private String subcategoryname;

	@Column(name = "DESCRIPTION")
	private String description;

	@Column(name = "CREATEDON")
	private Date createdon;

	@Column(name = "CREATEDBY")
	private Long createdby;

	@Column(name = "UPDATEDON")
	private Date updatedon;

	@Column(name = "UPDATEDBY")
	private Long updatedby;

	@Column(name = "STATUSFLAG")
	private Integer statusflag;

}
