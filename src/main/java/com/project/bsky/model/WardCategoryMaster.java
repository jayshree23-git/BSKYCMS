package com.project.bsky.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;


@Entity
@Table(name = "WARDCATEGORYMASTER")
public class WardCategoryMaster {
    @Id
    @Column(name = "WARDCATEGORYID", nullable = false)
    private Long id;

    @Column(name = "WARDNAME", length = 128)
    private String wardname;

    @Column(name = "IMPLANTCODE", length = 32)
    private String implantcode;

    @Column(name = "PREAUTHSTATUS")
    private Boolean preauthstatus;

    @Column(name = "WARDLEVEL")
    private Long wardlevel;

    @Column(name = "UNIT")
    private Long unit;

    @Column(name = "MAXIMUMUNIT")
    private Long maximumunit;

    @Column(name = "CREATEDBY")
    private Long createdby;

    @Column(name = "CREATEDON")
    private LocalDate createdon;

    @Column(name = "UPDATEDBY")
    private Long updatedby;

    @Column(name = "UPDATEDON")
    private LocalDate updatedon;

    @Column(name = "DELETEDFLAG")
    private Character deletedflag;

    @Override
    public String toString() {
        return "WardCategoryMaster{" +
                "id=" + id +
                ", wardname='" + wardname + '\'' +
                ", implantcode='" + implantcode + '\'' +
                ", preauthstatus=" + preauthstatus +
                ", wardlevel=" + wardlevel +
                ", unit=" + unit +
                ", maximumunit=" + maximumunit +
                ", createdby=" + createdby +
                ", createdon=" + createdon +
                ", updatedby=" + updatedby +
                ", updatedon=" + updatedon +
                ", deletedflag=" + deletedflag +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWardname() {
        return wardname;
    }

    public void setWardname(String wardname) {
        this.wardname = wardname;
    }

    public String getImplantcode() {
        return implantcode;
    }

    public void setImplantcode(String implantcode) {
        this.implantcode = implantcode;
    }

    public Boolean getPreauthstatus() {
        return preauthstatus;
    }

    public void setPreauthstatus(Boolean preauthstatus) {
        this.preauthstatus = preauthstatus;
    }

    public Long getWardlevel() {
        return wardlevel;
    }

    public void setWardlevel(Long wardlevel) {
        this.wardlevel = wardlevel;
    }

    public Long getUnit() {
        return unit;
    }

    public void setUnit(Long unit) {
        this.unit = unit;
    }

    public Long getMaximumunit() {
        return maximumunit;
    }

    public void setMaximumunit(Long maximumunit) {
        this.maximumunit = maximumunit;
    }

    public Long getCreatedby() {
        return createdby;
    }

    public void setCreatedby(Long createdby) {
        this.createdby = createdby;
    }

    public LocalDate getCreatedon() {
        return createdon;
    }

    public void setCreatedon(LocalDate createdon) {
        this.createdon = createdon;
    }

    public Long getUpdatedby() {
        return updatedby;
    }

    public void setUpdatedby(Long updatedby) {
        this.updatedby = updatedby;
    }

    public LocalDate getUpdatedon() {
        return updatedon;
    }

    public void setUpdatedon(LocalDate updatedon) {
        this.updatedon = updatedon;
    }

    public Character getDeletedflag() {
        return deletedflag;
    }

    public void setDeletedflag(Character deletedflag) {
        this.deletedflag = deletedflag;
    }
}