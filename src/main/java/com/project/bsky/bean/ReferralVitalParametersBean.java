package com.project.bsky.bean;

import com.project.bsky.model.Referral;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

public class ReferralVitalParametersBean {

//    private Long referalVitalId;

//    @Column(name = "REFERRALID")
//    private Long referralId;

    public String getVital() {
        return vital;
    }

    public void setVital(String vital) {
        this.vital = vital;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    private String vital;

    private String value;

//   private Date uploadDate;
//
//    private Integer createdBy;
//
//    private Date createdOn;
//
//    private Integer updatedBy;
//
//    private Date updatedOn;
//
//    private Integer deletedFlag;

//    @ManyToOne
//    @JoinColumn(name="REFERRALID")
//    private Referral referral;

}

