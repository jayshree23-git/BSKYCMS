package com.project.bsky.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.*;

@Getter
@Setter
@Entity
@Table(name = "MST_MAIL_SERVICE")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class MSTMailService {
    @Id
    @Column(name = "MAIL_SERVICE_ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MST_MAIL_SERVICE_id_gen")
    @SequenceGenerator(name = "MST_MAIL_SERVICE_id_gen", sequenceName = "MST_MAIL_SERVICE_SEQ", allocationSize = 1)
    private int id;

    @Column(name = "MAIL_SERVICE_NAME", nullable = false, length = 100)
    private String mailServiceName;

    @Column(name = "MAIL_DESCRIPTION", length = 4000)
    private String mailDescription;

    @Column(name = "CREATED_BY", nullable = false, length = 10)
    private String createdBy;

    @Column(name = "CREATED_ON", nullable = false)
    private Date createdOn;

    @Column(name = "UPDATED_BY", length = 10)
    private String updatedBy;

    @Column(name = "UPDATED_ON")
    private Date updatedOn;

    @Column(name = "STATUS_FLAG", nullable = false)
    private int statusFlag;

    public Map<String, Object> toMap() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", this.id);
        map.put("mailServiceName", this.mailServiceName);
        map.put("mailDescription", this.mailDescription);
        map.put("createdBy", this.createdBy);
        map.put("createdOn", this.createdOn);
        map.put("updatedBy", this.updatedBy);
        map.put("updatedOn", this.updatedOn);
        map.put("statusFlag", this.statusFlag);
        return map;
    }
}