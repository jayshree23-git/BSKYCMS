/**
 * 
 */
package com.project.bsky.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author priyanka.singh
 *
 */
@Entity
@Table(name = "PAYMENT_MODE")
public class PaymentMode implements Serializable{
	
	@Id
	@Column(name = "PAYMENT_MODE_ID")
	private Long paymentModeId;
	
	@Column(name = "PAYMENT_TYPE")
	private String paymentType;

	
	
	
	public Long getPaymentModeId() {
		return paymentModeId;
	}

	public void setPaymentModeId(Long paymentModeId) {
		this.paymentModeId = paymentModeId;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	@Override
	public String toString() {
		return "PaymentMode [paymentModeId=" + paymentModeId + ", paymentType=" + paymentType + "]";
	}

	
	

}
