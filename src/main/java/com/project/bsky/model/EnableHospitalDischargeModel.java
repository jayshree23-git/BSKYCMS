package com.project.bsky.model;

import java.io.Serializable;
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
@Table(name = "TBL_HOSPITALAPVBYSNA")

public class EnableHospitalDischargeModel implements Serializable  {
	    @Id
	    @GenericGenerator(name = "catInc", strategy = "increment")
	    @GeneratedValue(generator = "catInc")
	    @Column(name = "ID")
	    private Long id;
	   
	   @Column(name = "APPROVEFLAG")
	    private Character approveflag;
 
	   @Column(name = "CREATEDBY")
	    private Long createdBy;

	    @Column(name = "CREATEDON")
	    private  Date createdOn;

	    @Column(name = "DISTRICTCODE")
	    private  String districtcode;

	    @Column(name = "HOSPITALCODE")
	    private String hospitalcode;

	    @Column(name = "STATECODE")
	    private String statecode;

	    @Column(name = "STATUSFLAG")
	    private Integer statusflag;

	    @Column(name = "UPDATEDBY")
	    private Long updatedBy;

	    @Column(name = "UPDATEDON")
	    private Date updatedOn;
}
