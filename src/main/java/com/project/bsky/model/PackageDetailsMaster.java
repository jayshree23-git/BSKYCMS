package com.project.bsky.model;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.lang.model.element.Name;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Table(name = "MSTPACKAGEDETAILS")
public class PackageDetailsMaster implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	@GenericGenerator(name = "catInc", strategy = "increment")
	@GeneratedValue(generator = "catInc") // for oracle sequence generate
	private Long id;

	@Column(name = "PACKAGEHEADERCODE")
	private String packageHeaderCode;

	@ManyToOne
	@JoinColumn(name = "PACKAGESUBCATAGORYID")
//    @Column(name = "PACKAGESUBCATAGORYID")
	private PackageSubCategory packageSubcatagory;

	@Column(name = "PACKAGESUBCODE")
	private String packageSubcode;

	@Column(name = "PROCEDURECODE")
	private String procedureCode;

	@Column(name = "PROCEDUREDESCRIPTION")
	private String procedureDescription;

//    @ManyToOne
//    @JoinColumn(name="HOSPITALCATEGORYID")
//    @Column(name = "HOSPITALCATEGORYID")
//    private HospitalCategoryMaster hospitalCategory;

//    @Column(name = "AMOUNT")
//    private Long amount;

	@Column(name = "CREATEDBY")
	private Integer createdBy;

	@CreationTimestamp
	@Column(name = "CREATEDON")
	private Date createdOn;

	@Column(name = "UPDATEDBY")
	private Integer updatedBy;

	@Column(name = "UPDATEDON")
	private Date updatedOn;

	@Column(name = "DELETEDFLAG")
	private Integer deletedFlag;

	@Column(name = "PACKAGECATAGORYTYPE")
	private String packageCatagoryType;

	@Column(name = "MAXIMUMDAYS")
	private Long maximumDays;

	@Column(name = "MULTIPROCEDURE")
	private String multiProcedure;

	@Column(name = "MANDATORYPREAUTH")
	private String mandatoryPreauth;

	@Column(name = "STAYTYPE")
	private String stayType;

	@Column(name = "DAYCARE")
	private String dayCare;

	@Column(name = "PREAUTHDOCS")
	private String preauthDocs;

	@Column(name = "CLAIMPROCESSDOCS")
	private String claimProcessDocs;

	@Column(name = "PACKAGEMODE")
	private Long packagemode;

	@Column(name = "PACKAGEEXTENTION")
	private String packageExtention;

	@Column(name = "PRICEEDITABLE")
	private String priceEditable;

	@Column(name = "PACKAGEEXCEPTIONFLAG")
	private String ispackageException;

	@Column(name = "ISSURGICAL")
	private String isSurgical;

}