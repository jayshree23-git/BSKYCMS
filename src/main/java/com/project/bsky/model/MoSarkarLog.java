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
@Table(name = "MO_SARKAR_LOG")
public class MoSarkarLog {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MO_SARKAR_LOG_id_gen")
    @SequenceGenerator(name = "MO_SARKAR_LOG_id_gen", sequenceName = "MO_SARKAR_LOG_SEQ", allocationSize = 1)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "API_ID", nullable = false)
    private Integer apiId;

    @Column(name = "API_NAME", nullable = false, length = 100)
    private String apiName;

    @Column(name = "START_TIME", nullable = false)
    private Date startTime;

    @Column(name = "END_TIME", nullable = false)
    private Date endTime;

    @Lob
    @Column(name = "INPUT_DATA")
    private String inputData;

    @Lob
    @Column(name = "OUTPUT_DATA")
    private String outputData;

    @Column(name = "DATA_SIZE", nullable = false)
    private Integer dataSize;

    @Lob
    @Column(name = "SUCCESS_DATA")
    private String successData;

    @Lob
    @Column(name = "FAILED_DATA")
    private String failedData;

    @Column(name = "FAILED_DATA_SIZE", nullable = false)
    private Integer failedDataSize;

    @Column(name = "SUCCESS_DATA_SIZE", nullable = false)
    private Integer successDataSize;

    @Column(name = "CREATED_ON", nullable = false)
    private Date createdOn;

    @Column(name = "CREATED_BY", nullable = false, length = 100)
    private String createdBy;

    @Column(name = "UPDATED_ON")
    private Date updatedOn;

    @Column(name = "UPDATED_BY", length = 100)
    private String updatedBy;

    @Column(name = "STATUS_FLAG", nullable = false)
    private Integer statusFlag;

}