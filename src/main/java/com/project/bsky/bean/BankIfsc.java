package com.project.bsky.bean;

public class BankIfsc {
	private String branch;
	private String bank;
	public String getBranch() {
		return branch;
	}
	public void setBranch(String branch) {
		this.branch = branch;
	}
	public String getBank() {
		return bank;
	}
	public void setBank(String bank) {
		this.bank = bank;
	}
	@Override
	public String toString() {
		return "BankIfsc [branch=" + branch + ", bank=" + bank + "]";
	}

}
