
package com.project.bsky.bean;

import lombok.Data;
/**
 * @author jayshree.moharana
 *
 */

@Data
public class CpdActionReportBean {

	private Double approve;
	private Double reject;
	private Double query;
	private Double dishonour;
	private Double myamount;
	private Double dishonouramount;
	private String date;
	private Double finalamount;
	private Long snaquery;
	private Long investigate;
	private Long Reverttocpd;
	private String userid;
	private String username;
	private String assigned;
	private String actiontaken;
	private String pendingatcpd;
	private String cpdquery7;
	private String patcpdfess;
	private String patcpdresettelment;
	
	private String totalRequest;
	private String totalApprove;
	private String totalAutoApprove;
	private String totalReject;
	private String totalAutoReject;
	private String hospitalcancel;
	private String expired;
	private String pending;
	
	
	
}
