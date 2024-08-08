/**
 * 
 */
package com.project.bsky.bean;

/**
 * @author priyanka.singh
 *
 */
public class FoFloatReportBean {
	public String floateId;
	public String floateno;
	public String amount;
	public String createby;
	public String createon;
	public String getFloateId() {
		return floateId;
	}
	public void setFloateId(String floateId) {
		this.floateId = floateId;
	}
	public String getFloateno() {
		return floateno;
	}
	public void setFloateno(String floateno) {
		this.floateno = floateno;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getCreateby() {
		return createby;
	}
	public void setCreateby(String createby) {
		this.createby = createby;
	}
	public String getCreateon() {
		return createon;
	}
	public void setCreateon(String createon) {
		this.createon = createon;
	}
	@Override
	public String toString() {
		return "FoFloatReportBean [floateId=" + floateId + ", floateno=" + floateno + ", amount=" + amount
				+ ", createby=" + createby + ", createon=" + createon + "]";
	}
	
	
}
