package com.project.bsky.model;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name ="VW_HEALTH_DEPT_MEM_DET")
@NamedQuery(name = "findByUrn",
        query = "select e.urn,e.aadharNumber, e.fullNameEnglish, e.memberId, e.gender, e.age from HealthCardSample e where e.urn = ?1")
@NamedQuery(name = "findByAdhara",
        query = "select e.urn, e.aadharNumber, e.fullNameEnglish, e.memberId, e.gender, e.age from HealthCardSample e where e.aadharNumber = ?1")
@NamedQuery(name = "findByFullNameEnglish",
        query = "select e.urn, e.aadharNumber, e.fullNameEnglish, e.memberId, e.gender, e.age from HealthCardSample e where e.fullNameEnglish = ?1")
public class HealthCardSample extends AbstractPersistable<Long> {

    //@Id
//    @GenericGenerator(name = "catInc", strategy = "increment")
//    @GeneratedValue(generator = "catInc")
    @Column(name = "HEALTHMEMEBERSLNO")
    private Long id;

    @Column(name="URN")
    private  String urn;

    @Column(name="MEMBERID")
    private  String memberId;

    @Column(name="FULLNAMEENGLISH")
    private  String fullNameEnglish;

    @Column(name="FULLNAMEODIYA")
    private  String fullNameOdiya;

    @Column(name="AADHARNUMBER")
    private  String aadharNumber;

    @Column(name="GENDER")
    private  String gender;

    @Column(name="DATEOFBIRTH")
    private  Date dateOfBirth;

    @Column(name="AGE")
    private  Integer age;

    @Column(name="RELATIONWITHFAMILYHEAD")
    private  String relationWithFamilyHead;

    @Column(name="SCHEMETYPE")
    private  String schemeType;

    @Column(name="MOBILENUMBER")
    private  String mobileNumber;

    @Column(name="STATUS")
    private Integer status;

    @Column(name="ADDITIONDELETIONSTATUS")
    private  String additionDeletionStatus;

    @Column(name="EXPORTDATE")
    private Date exportDate;

    @Column(name="RATIONCARDTYPE")
    private String rationCardType;

    @Column(name="STATE_CODE")
    private String stateCode;

    @Column(name="STATE_NAME")
    private String stateName;

    @Column(name="DISTRICT")
    private String district;

    @Column(name="DISTRICT_CODE")
    private Integer districtCode;

    @Column(name="BLOCK_ULB")
    private String blockUlb;

    @Column(name="BLOCK_CODE")
    private Integer blockCode;

    @Column(name="GP_WARD")
    private String gpWard;

    @Column(name="PANCHAYT_TOWN_CODE")
    private Integer panchayatTownCode;

    @Column(name="LOCALITY_VILLAGE")
    private String localityVillage;

    @Column(name="VILLAGE_CODE")
    private Integer villageCode;

    @Column(name="FPSNAME")
    private String fpsName;

    @Column(name="STATUS_BASIC")
    private Integer statusBasic;

    @Column(name="ADDITIONDELETIONSTATUS_BASIC")
    private String additionDeletionStatusBasic;

    @Column(name="EXPORTDATE_BASIC")
    private Date exportDateBasic;

    @Column(name="HEAD_MEMBERID")
    private  String headMemberId;

    @Column(name="HEAD_MEMBERNAME")
    private  String headMemberName;

}
