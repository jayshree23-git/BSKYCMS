/**
 * 
 */
package com.project.bsky.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author santanu.barad
 *
 */
@Entity
@Table(name = "OTP_CONFIGURATION_LOG")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OtpConfigLog {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "otp_configuration_log_ID_SEQ")
	@SequenceGenerator(name = "otp_configuration_log_ID_SEQ", sequenceName = "otp_configuration_log_ID_SEQ", allocationSize = 1)
	@Column(name = "CONFIG_LOG_ID")
	private Long logId;

	@Column(name = "CREATED_BY")
	private Integer createdBy;

	@Column(name = "CREATED_ON")
	private Date createdOn;

	@Column(name = "USERID")
	private Long userId;

	@Column(name = "IS_OTP_ALLOWED_STATUS")
	private Integer isOtp;

	@Column(name = "STATUS_FLAG")
	private Long statusFlag;

}
