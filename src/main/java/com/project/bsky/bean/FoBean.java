package com.project.bsky.bean;



public class FoBean {
	public String id;
	public String amount;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	@Override
	public String toString() {
		return "FoBean [id=" + id + ", amount=" + amount + "]";
	}
	


}
