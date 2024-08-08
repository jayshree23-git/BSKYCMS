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
@Table(name = "MST_API_SERVICE")
public class MstApi {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MST_API_SERVICE_id_gen")
    @SequenceGenerator(name = "MST_API_SERVICE_id_gen", sequenceName = "MST_API_SERVICE_SEQ", allocationSize = 1)
    @Column(name = "API_ID", nullable = false)
    private Long apiId;

    @Column(name = "API_NAME", nullable = false, length = 100)
    private String apiName;

    @Column(name = "API_DESCRIPTION", nullable = false, length = 100)
    private String apiDescription;

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