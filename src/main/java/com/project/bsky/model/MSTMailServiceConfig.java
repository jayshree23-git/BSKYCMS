package com.project.bsky.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@Setter
@Entity
@Table(name = "MST_MAIL_SERVICE_CONFIG")
public class MSTMailServiceConfig {
    @Id
    @Column(name = "MAIL_SERVICE_CONFIG_ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MST_MAIL_SERVICE_CONFIG_id_gen")
    @SequenceGenerator(name = "MST_MAIL_SERVICE_CONFIG_id_gen", sequenceName = "MST_MAIL_SERVICE_CONFIG_SEQ", allocationSize = 1)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "MAIL_SERVICE_ID", nullable = false)
    private MSTMailService mstMailService;

    @Column(name = "MAIL_SUBJECT", length = 4000)
    private String mailSubject;

    @Lob
    @Column(name = "MAIL_BODY")
    private String mailBody;

    @Column(name = "MAIL_CC_RECIPIENT", length = 4000)
    private String mailCcRecipient;

    @Column(name = "MAIL_BCC_RECIPIENT", length = 4000)
    private String mailBccRecipient;

    @Column(name = "MAIL_TIME", length = 10)
    private String mailTime;

    @Column(name = "MAIL_FREQUENCY", length = 10)
    private String mailFrequency;

    @Column(name = "MAIL_FREQUENCY_FROM")
    private Date mailFrequencyFrom;

    @Column(name = "MAIL_FREQUENCY_TO")
    private Date mailFrequencyTo;

    @Column(name = "CREATED_BY", nullable = false)
    private Integer createdBy;

    @Column(name = "CREATED_ON", nullable = false)
    private Date createdOn;

    @Column(name = "UPDATED_BY")
    private Integer updatedBy;

    @Column(name = "UPDATED_ON")
    private Date updatedOn;

    @Column(name = "STATUS_FLAG", nullable = false)
    private int statusFlag;

    public Map<String, Object> toMap() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", this.id);
        map.put("mstMailService", this.mstMailService);
        map.put("mailSubject", this.mailSubject);
        map.put("mailBody", this.mailBody);
        map.put("mailCcRecipient", this.mailCcRecipient);
        map.put("mailBccRecipient", this.mailBccRecipient);
        map.put("mailTime", this.mailTime);
        map.put("mailFrequency", this.mailFrequency);
        map.put("mailFrequencyFrom", this.mailFrequencyFrom);
        map.put("mailFrequencyTo", this.mailFrequencyTo);
        map.put("createdBy", this.createdBy);
        map.put("createdOn", this.createdOn);
        map.put("updatedBy", this.updatedBy);
        map.put("updatedOn", this.updatedOn);
        map.put("statusFlag", this.statusFlag);
        return map;
    }
}