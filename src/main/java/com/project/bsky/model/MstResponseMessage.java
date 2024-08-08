package com.project.bsky.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "MST_RESPONSE_MESSAGE")
public class MstResponseMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MST_RESPONSE_MESSAGE_id_gen")
    @SequenceGenerator(name = "MST_RESPONSE_MESSAGE_id_gen", sequenceName = "MST_RESPONSE_MESSAGE_SEQ", allocationSize = 1)
    @Column(name = "RESPONSE_ID", nullable = false)
    private Integer id;

    @Column(name = "RESPONSE_MESSAGE", nullable = false, length = 4000)
    private String responseMessage;

    @Column(name = "CREATED_BY", nullable = false)
    private Long createdBy;

    @Column(name = "CREATED_ON", nullable = false)
    private LocalDate createdOn;

    @Column(name = "UPDATED_BY")
    private Long updatedBy;

    @Column(name = "UPDATED_ON")
    private LocalDate updatedOn;

    @Column(name = "STATUS_FLAG", nullable = false)
    private Boolean statusFlag = false;
}