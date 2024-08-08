package com.project.bsky.model;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Accessors(chain = true)
@ToString
@Table(name = "HOSPITALCATEGORYMASTER")
public class HospitalCategoryMaster implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "HOSPITALCATEGORYID")
    @GenericGenerator(name = "catInc", strategy = "increment")
    @GeneratedValue(generator = "catInc") //for oracle sequence generate
    private Long hospitalCategoryId;

    @NotEmpty
    @NotBlank
    @NotNull
    @Column(name = "HOSPITALCATEGORYNAME")
    private String hospitalCategoryName;

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

    public Long getHospitalCategoryId() {
        return hospitalCategoryId;
    }

    public void setHospitalCategoryId(Long hospitalCategoryId) {
        this.hospitalCategoryId = hospitalCategoryId;
    }

    public String getHospitalCategoryName() {
        return hospitalCategoryName;
    }

    public void setHospitalCategoryName(String hospitalCategoryName) {
        this.hospitalCategoryName = hospitalCategoryName;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Integer getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Integer updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Date getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(Date updatedOn) {
        this.updatedOn = updatedOn;
    }

    public Integer getDeletedFlag() {
        return deletedFlag;
    }

    public void setDeletedFlag(Integer deletedFlag) {
        this.deletedFlag = deletedFlag;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

}