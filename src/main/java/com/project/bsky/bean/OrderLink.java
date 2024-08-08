/**
 * 
 */
package com.project.bsky.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author santanu.barad
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderLink {
	private Long id;
	private Integer orderId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

}
