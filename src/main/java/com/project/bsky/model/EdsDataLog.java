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
@Table(name = "EDS_DATA_LOG")
public class EdsDataLog {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "EDS_DATA_LOG_id_gen")
    @SequenceGenerator(name = "EDS_DATA_LOG_id_gen", sequenceName = "EDS_DATA_LOG_SEQ", allocationSize = 1)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "API_ID", nullable = false)
    private Integer apiId;

    @Column(name = "API_NAME", nullable = false)
    private String apiName;

    @Lob
    @Column(name = "INPUT_DATA", nullable = false)
    private String inputData;

    @Lob
    @Column(name = "BSKY_DATA", nullable = false)
    private String bskyData;

    @Lob
    @Column(name = "BSKY_INSERTED_DATA", nullable = false)
    private String bskyInsertedData;

    @Lob
    @Column(name = "BSKY_UPDATED_DATA", nullable = false)
    private String bskyUpdatedData;

    @Column(name = "START_TIME", nullable = false)
    private Date startTime;

    @Column(name = "END_TIME", nullable = false)
    private Date endTime;

    @Lob
    @Column(name = "BSKY_FAILED_DATA", nullable = false)
    private String bskyFailedData;

    @Column(name = "RECORDS_FETCH")
    private Long recordsFetch;

    @Column(name = "RECORDS_INSERTED")
    private Long recordsInserted;

    @Column(name = "RECORDS_UPDATED")
    private Long recordsUpdated;

    @Column(name = "RECORDS_FAILED")
    private Long recordsFailed;

    @Column(name = "CREATED_ON", nullable = false)
    private Date createdDate;

    @Column(name = "UPDATED_ON")
    private Date updatedDate;

    @Column(name = "CREATED_BY", nullable = false, length = 50)
    private String createdBy;

    @Column(name = "UPDATED_BY", length = 50)
    private String updatedBy;

    @Column(name = "STATUS_FLAG", nullable = false)
    private Long statusFlag = 0L;

}