package com.project.bsky.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@ToString
@Entity
@Table(name = "API_SERVICE_LOG")
public class APIServiceLog {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "API_SERVICE_LOG_id_gen")
    @SequenceGenerator(name = "API_SERVICE_LOG_id_gen", sequenceName = "API_SERVICE_LOG_SEQ", allocationSize = 1)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "API_ID", nullable = false)
    private Integer apiId;

    @Column(name = "API_NAME", nullable = false, length = 100)
    private String apiName;

    @Column(name = "API_HIT_TIME", nullable = false)
    private Date apiHitTime;

    @Column(name = "API_END_TIME", nullable = false)
    private Date apiEndTime;

    @Lob
    @Column(name = "INPUT_DATA ")
    private String inputData;

    @Lob
    @Column(name = "OUTPUT_DATA")
    private String outputData;

    @Column(name = "DATA_SIZE")
    private Integer dataSize;

    @Column(name = "CREATED_ON", nullable = false)
    private Date createdOn;

    @Column(name = "CREATED_BY", nullable = false, length = 100)
    private String createdBy;

    @Column(name = "UPDATED_ON")
    private Date updatedOn;

    @Column(name = "UPDATED_BY", length = 100)
    private String updatedBy;

    @Column(name = "STATUS_FLAG", nullable = false)
    private int statusFlag;

}