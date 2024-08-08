package com.project.bsky.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
@ToString
@Table(name = "HEALTH_DEPARTMENT_SERVICE_REPORT")
public class HealthDepartmentServiceReport {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HEALTH_DEPARTMENT_SERVICE_REPORT_id_gen")
    @SequenceGenerator(name = "HEALTH_DEPARTMENT_SERVICE_REPORT_id_gen", sequenceName = "HEALTH_DEPARTMENT_SERVICE_REPORT_SEQ", allocationSize = 1)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "SERVICE_STATUS", length = 200)
    private String serviceStatus;

    @Column(name = "START_DATE")
    private Date startDate;

    @Column(name = "END_DATE")
    private Date endDate;

    @Column(name = "RECORDS_FETCHED")
    private Long recordsFetched;

    @Column(name = "RECORDS_INSERTED")
    private Long recordsInserted;

    @Column(name = "RECORDS_UPDATED")
    private Long recordsUpdated;

    @Column(name = "RECORDS_FAILED")
    private Long recordsFailed;

    @Column(name = "CREATED_BY", length = 100)
    private String createdBy;

    @Column(name = "CREATED_ON")
    private Date createdOn;

    @Column(name = "UPDATED_BY", length = 100)
    private String updatedBy;

    @Column(name = "UPDATED_ON")
    private Date updatedOn;

    @Column(name = "STATUS_FLAG")
    private int statusFlag;

    @Column(name = "API_ID", length = 10)
    private Long apiId;
}