package com.project.bsky.entity;


import java.math.BigDecimal;
import java.sql.Clob;
import java.time.Instant;
import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.Hibernate;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;



@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "T_APPLICANT_PROFILE")
public class TApplicantProfile {
    @Id
    
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="T_APPLICANT_PROFILE_SEQ")
	@SequenceGenerator(name="T_APPLICANT_PROFILE_SEQ", sequenceName="T_APPLICANT_PROFILE_SEQ", allocationSize=1)
    @Column(name = "INTPROFILEID")
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer INTPROFILEID;

    @Column(name = "VCHAPPLICANTPREFIX")
    private String VCHAPPLICANTPREFIX;

    @Column(name = "VCHAPPLICANTNAME")
    private String VCHAPPLICANTNAME;

    @Column(name = "VCHBUSINESSNAME")
    private String VCHBUSINESSNAME;

    @Column(name = "VCHMOBILENO")
    private String VCHMOBILENO;

    @Column(name = "VCHPHONENO")
    private String VCHPHONENO;

    @Column(name = "VCHEMAIL")
    private String VCHEMAIL;

    @Column(name = "VCHPASSWORD")
    private String VCHPASSWORD;

    @Column(name = "VCHUNIQUEID")
    private String VCHUNIQUEID;

    @Column(name = "TINCHKPASSSTATUS")
    private Integer TINCHKPASSSTATUS;

    @Column(name = "TINACTIVATIONSTATUS")
    private Integer TINACTIVATIONSTATUS;

    @Column(name = "DTMAPPLIEDON")
    private Date DTMAPPLIEDON;

    @Column(name = "DTMLASTLOGIN")
    private Date DTMLASTLOGIN;

    @Column(name = "DTMCREATEDON")
    private Date DTMCREATEDON;

    @Column(name = "STMUPDATEDON")
    private Date STMUPDATEDON;

    @Column(name = "INTCREATEDBY")
    private Long INTCREATEDBY;

    @Column(name = "INTUPDATEDBY")
    private Long INTUPDATEDBY;

    @Column(name = "BITDELETEDFLAG")
    private Boolean BITDELETEDFLAG = false;

    @Column(name = "VCHFATHERNAMEPREFIX")
    private String VCHFATHERNAMEPREFIX;

    @Column(name = "VCHFATHERNAME")
    private String VCHFATHERNAME;

    @Column(name = "INTCOUNTRYID")
    private Long INTCOUNTRYID;

    @Column(name = "INTSTATEID", length = 64)
    private String INTSTATEID;

    @Column(name = "VCHDISTRICT")
    private String VCHDISTRICT;

    @Column(name = "VCHADDRESSLN1")
    private String VCHADDRESSLN1;

    @Column(name = "VCHADDRESSLN2")
    private String VCHADDRESSLN2;

    @Column(name = "VCHADDRESSLN3")
    private String VCHADDRESSLN3;

    @Column(name = "VCHPINNO")
    private String VCHPINNO;

    @Column(name = "INTEDQUALIFICATION")
    private Integer INTEDQUALIFICATION;

    @Column(name = "INTSPECIALCATEGORY")
    private Integer INTSPECIALCATEGORY;

    @Column(name = "VCHCURRENTDESIGNATION")
    private String VCHCURRENTDESIGNATION;

    @Column(name = "VCHENTITYNAME")
    private String VCHENTITYNAME;

    @Column(name = "INTCONSTTYPE")
    private Integer INTCONSTTYPE;

    @Column(name = "VCHCOMPADDRESS")
    private String VCHCOMPADDRESS;

    @Column(name = "INTCOMPCOUNTRYID")
    private Integer INTCOMPCOUNTRYID;

    @Column(name = "INTCOMPSTATEID")
    private Integer INTCOMPSTATEID;

    @Column(name = "VCHCOMPFAX")
    private String VCHCOMPFAX;

    @Column(name = "VCHCOMPPAN")
    private String VCHCOMPPAN;

    @Column(name = "VCHCOMPCITYTOWN")
    private String VCHCOMPCITYTOWN;

    @Column(name = "VCHCOMPLANDLINENO")
    private String VCHCOMPLANDLINENO;

    @Column(name = "VCHCOMPWEBSITE")
    private String VCHCOMPWEBSITE;

    @Column(name = "VCHCOMPDISTRICT")
    private String VCHCOMPDISTRICT;

    @Column(name = "VCHCOMPPINNO")
    private String VCHCOMPPINNO;

    @Column(name = "VCHCOMPEMAIL")
    private String VCHCOMPEMAIL;

    @Column(name = "DTMCOMPINCORPDATE")
    private Date DTMCOMPINCORPDATE;

    @Column(name = "VCHBANKNAME")
    private String VCHBANKNAME;

    @Column(name = "VCHBANKBRANCHNAME")
    private String VCHBANKBRANCHNAME;

    @Column(name = "VCHBANKIFSCCODE")
    private String VCHBANKIFSCCODE;

    @Column(name = "VCHBANKACCNO")
    private String VCHBANKACCNO;

    @Column(name = "INTPROFILEUPDATESTATUS")
    private Boolean INTPROFILEUPDATESTATUS;

    @Column(name = "intSameAddressStatus")
    private Boolean intSameAddressStatus;

    @Column(name = "TINDRAFTSTATUS")
    private Byte TINDRAFTSTATUS	;

    @Column(name = "TINPAYMENTSTATUS")
    private Boolean TINPAYMENTSTATUS;

    @Column(name = "TINAPPLICATIONSTATUS")
    private Byte TINAPPLICATIONSTATUS;

    @Column(name = "DTMARCHIVEDON")
    private Date DTMARCHIVEDON;

    @Column(name = "DTMPASSWORDUPDATEDON")
    private Instant DTMPASSWORDUPDATEDON;

    @Column(name = "VCHPROFILEPIC")
    private String VCHPROFILEPIC;

    @Column(name = "DECFINALSCORE")
    private BigDecimal decfinalScore;

    @Column(name = "TINREVERTSTATUS")
    private Boolean TINREVERTSTATUS;

    @Lob
    @Column(name = "TXTPUBLICNOTEING")
    private Clob TXTPUBLICNOTEING;

    @Column(name = "DECFRONTAGE")
    private BigDecimal DECFRONTAGE;

    @Column(name = "TINTCORNER")
    private Byte TINTCORNER;

    @Column(name = "DECFRONTAGEPRICE")
    private BigDecimal DECFRONTAGEPRICE;

    @Column(name = "DECCORNERPRICE")
    private BigDecimal DECCORNERPRICE;

    @Column(name = "DECDPRSCORE")
    private BigDecimal DECDPRSCORE;

    @Column(name = "DECINTERVIEWSCORE")
    private BigDecimal DECINTERVIEWSCORE;

    @Column(name = "TINMOMSTATUS")
    private Byte TINMOMSTATUS;

    @Column(name = "DTMCURRENTLOGIN")
    private Instant DTMCURRENTLOGIN;

    @Column(name = "VCHOTHERSTATE")
    private String VCHOTHERSTATE;

    @Column(name = "INTPHASEID")
    private Integer INTPHASEID;

    @Column(name = "INTVENTURETYPE")
    private Integer INTVENTURETYPE;

    @Column(name = "TINAPPPROFILELOCK")
    private Byte TINAPPPROFILELOCK;

    @Column(name = "VCHFIRSTNAME")
    private String VCHFIRSTNAME;

    @Column(name = "VCHMIDDLENAME")
    private String VCHMIDDLENAME;

    @Column(name = "VCHLASTNAME")
    private String VCHLASTNAME;

    @Column(name = "VCHIDENTITYTYPE")
    private String VCHIDENTITYTYPE;

    @Column(name = "VCHIDENTITYNUM")
    private String VCHIDENTITYNUM;

    @Lob
    @Column(name = "VCHFCIMCODE")
    private Clob VCHFCIMCODE;

    @Lob
    @Column(name = "VCHDEVICEID")
    private Clob VCHDEVICEID;

    @Column(name = "VCHDEVICEPLATFORM")
    private String VCHDEVICEPLATFORM;

    @Column(name = "VCHDEVICEMODEL")
    private String VCHDEVICEMODEL;

    @Column(name = "VCHSIGN")
    private String VCHSIGN;

    @Column(name = "VCHOTP")
    private String VCHOTP;

    @Column(name = "VCHOTPSTATUS")
    private String VCHOTPSTATUS;

    @Column(name = "VCHSIGNDOC")
    private String VCHSIGNDOC;

    @Column(name = "VCHLOGINSESSION")
    private String VCHLOGINSESSION;

    @Column(name = "TINFAILURECTR")
    private Byte TINFAILURECTR;

    @Column(name = "DTMLASTFAILEDATTEMPT")
    private Date DTMLASTFAILEDATTEMPT;

    @Column(name = "TINLOGINSTATUS")
    private Integer TINLOGINSTATUS;

    @Column(name = "DTMLASTACTIVITYTIME")
    private Date DTMLASTACTIVITYTIME;

    @Column(name = "INTPROCESSID")
    private Integer INTPROCESSID;

    @Column(name = "TINTRANSFERAPPROVALSTATUS")
    private Integer TINTRANSFERAPPROVALSTATUS;

    @Column(name = "VCHLANGPREFER")
    private String VCHLANGPREFER;
    
    @Column(name = "DTMSTARTTIME")
    private Date DTMSTARTTIME;
    
    @Column(name = "DTMLASTTIME")
    private Date DTMLASTTIME;
    
    @Column(name = "INTCOUNT")
    private Integer INTCOUNT;
    
    @Column(name = "INTCOUNTOTP")
    private Integer INTCOUNTOTP;
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        TApplicantProfile that = (TApplicantProfile) o;
        return INTPROFILEID != null && Objects.equals(INTPROFILEID, that.INTPROFILEID);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}