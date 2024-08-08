package com.project.bsky.bean;

public class CPDCountReportBean {
	
	private Long userId;
	private String month;

	private String year;
	private String CASENO;
	private String URN;
	private String PATIENTNAME;
	private String ACTUALDATEOFADMISSION;
	private String ACTUALDATEOFDISCHARGE;
	private String CPDACTIONDATE;
//	private String UNPROCESSEDDATE;
	private String DATEUNOFPROCESSED;
	private String DISHONOURDATE;

	private String Hospital_Reclaim_Date;

	private String Non_compliance_date;
	private String CPD_QUERY_DATE;
	private String UNPROCESSED_TYPE;
	
	private String Assigned_Date;
	

	


	public String getAssigned_Date() {
		return Assigned_Date;
	}

	public void setAssigned_Date(String assigned_Date) {
		Assigned_Date = assigned_Date;
	}

	public String getDATEUNOFPROCESSED() {
		return DATEUNOFPROCESSED;
	}

	public void setDATEUNOFPROCESSED(String dATEUNOFPROCESSED) {
		DATEUNOFPROCESSED = dATEUNOFPROCESSED;
	}

	public String getDISHONOURDATE() {
		return DISHONOURDATE;
	}

	public void setDISHONOURDATE(String dISHONOURDATE) {
		DISHONOURDATE = dISHONOURDATE;
	}

//	public String getUNPROCESSEDDATE() {
//		return UNPROCESSEDDATE;
//	}
//
//	public void setUNPROCESSEDDATE(String uNPROCESSEDDATE) {
//		UNPROCESSEDDATE = uNPROCESSEDDATE;
//	}

	public String getHospital_Reclaim_Date() {
		return Hospital_Reclaim_Date;
	}

	public void setHospital_Reclaim_Date(String hospital_Reclaim_Date) {
		Hospital_Reclaim_Date = hospital_Reclaim_Date;
	}

	public String getNon_compliance_date() {
		return Non_compliance_date;
	}

	public void setNon_compliance_date(String non_compliance_date) {
		Non_compliance_date = non_compliance_date;
	}

	public String getCPD_QUERY_DATE() {
		return CPD_QUERY_DATE;
	}

	public void setCPD_QUERY_DATE(String cPD_QUERY_DATE) {
		CPD_QUERY_DATE = cPD_QUERY_DATE;
	}

	public String getUNPROCESSED_TYPE() {
		return UNPROCESSED_TYPE;
	}

	public void setUNPROCESSED_TYPE(String uNPROCESSED_TYPE) {
		UNPROCESSED_TYPE = uNPROCESSED_TYPE;
	}

	public String getCASENO() {
		return CASENO;
	}

	public void setCASENO(String cASENO) {
		CASENO = cASENO;
	}

	public String getURN() {
		return URN;
	}

	public void setURN(String uRN) {
		URN = uRN;
	}

	public String getPATIENTNAME() {
		return PATIENTNAME;
	}

	public void setPATIENTNAME(String pATIENTNAME) {
		PATIENTNAME = pATIENTNAME;
	}

	public String getACTUALDATEOFADMISSION() {
		return ACTUALDATEOFADMISSION;
	}

	public void setACTUALDATEOFADMISSION(String aCTUALDATEOFADMISSION) {
		ACTUALDATEOFADMISSION = aCTUALDATEOFADMISSION;
	}

	public String getACTUALDATEOFDISCHARGE() {
		return ACTUALDATEOFDISCHARGE;
	}

	public void setACTUALDATEOFDISCHARGE(String aCTUALDATEOFDISCHARGE) {
		ACTUALDATEOFDISCHARGE = aCTUALDATEOFDISCHARGE;
	}

	public String getCPDACTIONDATE() {
		return CPDACTIONDATE;
	}

	public void setCPDACTIONDATE(String cPDACTIONDATE) {
		CPDACTIONDATE = cPDACTIONDATE;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	@Override
	public String toString() {
		return "CPDCountReportBean [userId=" + userId + ", month=" + month + ", year=" + year + ", CASENO=" + CASENO
				+ ", URN=" + URN + ", PATIENTNAME=" + PATIENTNAME + ", ACTUALDATEOFADMISSION=" + ACTUALDATEOFADMISSION
				+ ", ACTUALDATEOFDISCHARGE=" + ACTUALDATEOFDISCHARGE + ", CPDACTIONDATE=" + CPDACTIONDATE + "]";
	}

	

	

}
