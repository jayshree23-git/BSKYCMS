package com.project.bsky.repository;

import java.sql.Clob;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.project.bsky.entity.MProcessName;
import com.project.bsky.entity.ProcessEntity;


@Repository
@Transactional
public interface ProcessRepository extends JpaRepository<ProcessEntity, Integer>{
	
	
	/*@Query(value = " SELECT a.intProcessId,a.vchProcessName,a.vchProcessCode,a.intPayment,a.intApproval,a.intDocument,a.viewAtCitizen, "
			+ " a.landUseExist, a.tinProjectAreaExist, a.dtmCreatedOn, a.intModuleId, a.tinStatus,a.vchProcessClass,a.tinAccessBy,a.tinPaymentTime,a.tinDemandNoteExist, "
			+ " a.txtOtherInfo, a.vchXyzs, a.intCreatedBy, a.stmUpdatedOn, a.intUpdatedBy, a.bitDeletedFlag, a.intBOQProcessId, a.tinProcessType, a.txtDynCtrlTblNm, a.vchSection ,"
			+ " a.vchSchemePoster, a.vchSchemeGuideline, a.m_process_namecol, a.intServiceMode, a.txtSchemeDescription, a.vchRedirectURL, a.vchAPIRedirectURLDtls, a.vchAPIReferenceURLDtls, "
			+ " a.vchAPIStatusURLDtls, a.intBaseType, a.vchProcessDtls, a.vchAPIURLDtls, a.vchConfigurationItemsDtls , a.tinPublishStatus, a.vchFormType, a.vchTableName, a.intAdminApplication, a.intWebsiteApplication, "
			+ " a.txtFormConfigDetails, a.vchredirectWindowType, a.formStatus, a.tinFormType, a.tinFinalSubmitStatus, a.tinGridType, b.vchModuleName "
			+ " FROM ProcessEntity a JOIN ModuleEntity b ON a.intModuleId=b.intModuleId ")*/
	
	@Query(value = " SELECT a.intProcessId,a.vchProcessName,a.vchProcessCode,a.intPayment,a.intApproval,a.intDocument,a.viewAtCitizen, "
			+ " a.landUseExist, a.tinProjectAreaExist, a.dtmCreatedOn, a.intModuleId, a.tinStatus,a.vchProcessClass,a.tinAccessBy,a.tinPaymentTime,a.tinDemandNoteExist, "
			+ " a.txtOtherInfo, a.vchXyzs, a.intCreatedBy, a.stmUpdatedOn, a.intUpdatedBy, a.bitDeletedFlag, a.intBOQProcessId, a.tinProcessType, a.txtDynCtrlTblNm, a.vchSection ,"
			+ " a.vchSchemePoster, a.vchSchemeGuideline, a.m_process_namecol, a.intServiceMode, a.txtSchemeDescription, a.vchRedirectURL, a.vchAPIRedirectURLDtls, a.vchAPIReferenceURLDtls, "
			+ " a.vchAPIStatusURLDtls, a.intBaseType, a.vchProcessDtls, a.vchAPIURLDtls, a.vchConfigurationItemsDtls , a.tinPublishStatus, a.vchFormType, a.vchTableName, a.intAdminApplication, a.intWebsiteApplication, "
			+ " a.txtFormConfigDetails, a.vchredirectWindowType, a.formStatus, a.tinFormType, a.tinFinalSubmitStatus, a.tinGridType, b.vchModuleName "
			+ " FROM m_process_name a LEFT JOIN m_module_name b ON a.intModuleId=b.intModuleId where a.bitDeletedFlag = 0 order by a.intProcessId DESC " ,nativeQuery = true)
	
	List<Object[]> findAllActiveProcess();
	
	@Query(value = " SELECT a.intProcessId,a.vchProcessName,a.vchProcessCode,a.intPayment,a.intApproval,a.intDocument,a.viewAtCitizen, "
			+ " a.landUseExist, a.tinProjectAreaExist, a.dtmCreatedOn, a.intModuleId, a.tinStatus,a.vchProcessClass,a.tinAccessBy,a.tinPaymentTime,a.tinDemandNoteExist, "
			+ " a.txtOtherInfo, a.vchXyzs, a.intCreatedBy, a.stmUpdatedOn, a.intUpdatedBy, a.bitDeletedFlag, a.intBOQProcessId, a.tinProcessType, a.txtDynCtrlTblNm, a.vchSection ,"
			+ " a.vchSchemePoster, a.vchSchemeGuideline, a.m_process_namecol, a.intServiceMode, a.txtSchemeDescription, a.vchRedirectURL, a.vchAPIRedirectURLDtls, a.vchAPIReferenceURLDtls, "
			+ " a.vchAPIStatusURLDtls, a.intBaseType, a.vchProcessDtls, a.vchAPIURLDtls, a.vchConfigurationItemsDtls , a.tinPublishStatus, a.vchFormType, a.vchTableName, a.intAdminApplication, a.intWebsiteApplication, "
			+ " a.txtFormConfigDetails, a.vchredirectWindowType, a.formStatus, a.tinFormType, a.tinFinalSubmitStatus, a.tinGridType, b.vchModuleName "
			+ " FROM m_process_name a LEFT JOIN m_module_name b ON a.intModuleId=b.intModuleId where a.intModuleId=:moduleId and a.intProcessId = :iteamId and a.bitDeletedFlag = 0 order by a.intProcessId DESC " ,nativeQuery = true)

	List<Object[]> findAllActiveProcessByModuleNameAndItemId(Integer moduleId, Integer iteamId);
	
	
	@Query(value = " SELECT a.intProcessId,a.vchProcessName,a.vchProcessCode,a.intPayment,a.intApproval,a.intDocument,a.viewAtCitizen, "
			+ " a.landUseExist, a.tinProjectAreaExist, a.dtmCreatedOn, a.intModuleId, a.tinStatus,a.vchProcessClass,a.tinAccessBy,a.tinPaymentTime,a.tinDemandNoteExist, "
			+ " a.txtOtherInfo, a.vchXyzs, a.intCreatedBy, a.stmUpdatedOn, a.intUpdatedBy, a.bitDeletedFlag, a.intBOQProcessId, a.tinProcessType, a.txtDynCtrlTblNm, a.vchSection ,"
			+ " a.vchSchemePoster, a.vchSchemeGuideline, a.m_process_namecol, a.intServiceMode, a.txtSchemeDescription, a.vchRedirectURL, a.vchAPIRedirectURLDtls, a.vchAPIReferenceURLDtls, "
			+ " a.vchAPIStatusURLDtls, a.intBaseType, a.vchProcessDtls, a.vchAPIURLDtls, a.vchConfigurationItemsDtls , a.tinPublishStatus, a.vchFormType, a.vchTableName, a.intAdminApplication, a.intWebsiteApplication, "
			+ " a.txtFormConfigDetails, a.vchredirectWindowType, a.formStatus, a.tinFormType, a.tinFinalSubmitStatus, a.tinGridType, b.vchModuleName "
			+ " FROM m_process_name a LEFT JOIN m_module_name b ON a.intModuleId=b.intModuleId where a.intModuleId = :moduleName and a.bitDeletedFlag = 0 order by a.intProcessId DESC " ,nativeQuery = true)

	List<Object[]> findAllActiveProcessByModuleName(Integer moduleName);
	
	/*@Query(value = " SELECT a.intProcessId,a.vchProcessName,a.vchProcessCode,a.intPayment,a.intApproval,a.intDocument,a.viewAtCitizen, "
			+ " a.landUseExist, a.tinProjectAreaExist, a.dtmCreatedOn, a.intModuleId, a.tinStatus,a.vchProcessClass,a.tinAccessBy,a.tinPaymentTime,a.tinDemandNoteExist, "
			+ " a.txtOtherInfo, a.vchXyzs, a.intCreatedBy, a.stmUpdatedOn, a.intUpdatedBy, a.bitDeletedFlag, a.intBOQProcessId, a.tinProcessType, a.txtDynCtrlTblNm, a.vchSection ,"
			+ " a.vchSchemePoster, a.vchSchemeGuideline, a.m_process_namecol, a.intServiceMode, a.txtSchemeDescription, a.vchRedirectURL, a.vchAPIRedirectURLDtls, a.vchAPIReferenceURLDtls, "
			+ " a.vchAPIStatusURLDtls, a.intBaseType, a.vchProcessDtls, a.vchAPIURLDtls, a.vchConfigurationItemsDtls , a.tinPublishStatus, a.vchFormType, a.vchTableName, a.intAdminApplication, a.intWebsiteApplication, "
			+ " a.txtFormConfigDetails, a.vchredirectWindowType, a.formStatus, a.tinFormType, a.tinFinalSubmitStatus, a.tinGridType, b.vchModuleName "
			+ " FROM m_process_name a JOIN m_module b ON a.intModuleId=b.intModuleId where a.intModuleId=:moduleId and a.intProcessId =:iteamId and  a.vchProcessName like :'vchProcessName%' " ,nativeQuery = true)

	List<Object[]> findAllActiveProcessByModuleNameAndItemIdAndProcessName(Integer moduleId, Integer iteamId,
			String vchProcessName);
	
	
	@Query(value = " SELECT a.intProcessId,a.vchProcessName,a.vchProcessCode,a.intPayment,a.intApproval,a.intDocument,a.viewAtCitizen, "
			+ " a.landUseExist, a.tinProjectAreaExist, a.dtmCreatedOn, a.intModuleId, a.tinStatus,a.vchProcessClass,a.tinAccessBy,a.tinPaymentTime,a.tinDemandNoteExist, "
			+ " a.txtOtherInfo, a.vchXyzs, a.intCreatedBy, a.stmUpdatedOn, a.intUpdatedBy, a.bitDeletedFlag, a.intBOQProcessId, a.tinProcessType, a.txtDynCtrlTblNm, a.vchSection ,"
			+ " a.vchSchemePoster, a.vchSchemeGuideline, a.m_process_namecol, a.intServiceMode, a.txtSchemeDescription, a.vchRedirectURL, a.vchAPIRedirectURLDtls, a.vchAPIReferenceURLDtls, "
			+ " a.vchAPIStatusURLDtls, a.intBaseType, a.vchProcessDtls, a.vchAPIURLDtls, a.vchConfigurationItemsDtls , a.tinPublishStatus, a.vchFormType, a.vchTableName, a.intAdminApplication, a.intWebsiteApplication, "
			+ " a.txtFormConfigDetails, a.vchredirectWindowType, a.formStatus, a.tinFormType, a.tinFinalSubmitStatus, a.tinGridType, b.vchModuleName "
			+ " FROM m_process_name a JOIN m_module b ON a.intModuleId=b.intModuleId where a.intModuleId=:moduleId and  a.vchProcessName like :'vchProcessName%' " ,nativeQuery = true)

	List<Object[]> findAllActiveProcessByModuleNameAndProcessName(Integer moduleId, String vchProcessName);
	*/
	
	@Query(value = " SELECT a.intProcessId,a.vchProcessName,a.vchProcessCode,a.intPayment,a.intApproval,a.intDocument,a.viewAtCitizen, "
			+ " a.landUseExist, a.tinProjectAreaExist, a.dtmCreatedOn, a.intModuleId, a.tinStatus,a.vchProcessClass,a.tinAccessBy,a.tinPaymentTime,a.tinDemandNoteExist, "
			+ " a.txtOtherInfo, a.vchXyzs, a.intCreatedBy, a.stmUpdatedOn, a.intUpdatedBy, a.bitDeletedFlag, a.intBOQProcessId, a.tinProcessType, a.txtDynCtrlTblNm, a.vchSection ,"
			+ " a.vchSchemePoster, a.vchSchemeGuideline, a.m_process_namecol, a.intServiceMode, a.txtSchemeDescription, a.vchRedirectURL, a.vchAPIRedirectURLDtls, a.vchAPIReferenceURLDtls, "
			+ " a.vchAPIStatusURLDtls, a.intBaseType, a.vchProcessDtls, a.vchAPIURLDtls, a.vchConfigurationItemsDtls , a.tinPublishStatus, a.vchFormType, a.vchTableName, a.intAdminApplication, a.intWebsiteApplication, "
			+ " a.txtFormConfigDetails, a.vchredirectWindowType, a.formStatus, a.tinFormType, a.tinFinalSubmitStatus, a.tinGridType, b.vchModuleName "
			+ " FROM m_process_name a LEFT JOIN m_module_name b ON a.intModuleId=b.intModuleId where a.intProcessId =:iteamId and a.bitDeletedFlag = 0 order by a.intProcessId DESC" ,nativeQuery = true)

	List<Object[]> findAllActiveProcessByItemId(Integer iteamId);

	@Query(value = " SELECT a.intProcessId,a.vchProcessName,a.vchProcessCode,a.intPayment,a.intApproval,a.intDocument,a.viewAtCitizen, "
			+ " a.landUseExist, a.tinProjectAreaExist, a.dtmCreatedOn, a.intModuleId, a.tinStatus,a.vchProcessClass,a.tinAccessBy,a.tinPaymentTime,a.tinDemandNoteExist, "
			+ " a.txtOtherInfo, a.vchXyzs, a.intCreatedBy, a.stmUpdatedOn, a.intUpdatedBy, a.bitDeletedFlag, a.intBOQProcessId, a.tinProcessType, a.txtDynCtrlTblNm, a.vchSection ,"
			+ " a.vchSchemePoster, a.vchSchemeGuideline, a.m_process_namecol, a.intServiceMode, a.txtSchemeDescription, a.vchRedirectURL, a.vchAPIRedirectURLDtls, a.vchAPIReferenceURLDtls, "
			+ " a.vchAPIStatusURLDtls, a.intBaseType, a.vchProcessDtls, a.vchAPIURLDtls, a.vchConfigurationItemsDtls , a.tinPublishStatus, a.vchFormType, a.vchTableName, a.intAdminApplication, a.intWebsiteApplication, "
			+ " a.txtFormConfigDetails, a.vchredirectWindowType, a.formStatus, a.tinFormType, a.tinFinalSubmitStatus, a.tinGridType, b.vchModuleName "
			+ " FROM m_process_name a LEFT JOIN m_module_name b ON a.intModuleId=b.intModuleId where a.vchProcessName like %:vchProcessName and a.bitDeletedFlag = 0 order by a.intProcessId DESC " ,nativeQuery = true)
	
	List<Object[]> findAllActiveProcessByProcessName(String vchProcessName);
	
	@Query(value = " SELECT a.intProcessId,a.vchProcessName,a.vchProcessCode,a.intPayment,a.intApproval,a.intDocument,a.viewAtCitizen, "
			+ " a.landUseExist, a.tinProjectAreaExist, a.dtmCreatedOn, a.intModuleId, a.tinStatus,a.vchProcessClass,a.tinAccessBy,a.tinPaymentTime,a.tinDemandNoteExist, "
			+ " a.txtOtherInfo, a.vchXyzs, a.intCreatedBy, a.stmUpdatedOn, a.intUpdatedBy, a.bitDeletedFlag, a.intBOQProcessId, a.tinProcessType, a.txtDynCtrlTblNm, a.vchSection ,"
			+ " a.vchSchemePoster, a.vchSchemeGuideline, a.m_process_namecol, a.intServiceMode, a.txtSchemeDescription, a.vchRedirectURL, a.vchAPIRedirectURLDtls, a.vchAPIReferenceURLDtls, "
			+ " a.vchAPIStatusURLDtls, a.intBaseType, a.vchProcessDtls, a.vchAPIURLDtls, a.vchConfigurationItemsDtls , a.tinPublishStatus, a.vchFormType, a.vchTableName, a.intAdminApplication, a.intWebsiteApplication, "
			+ " a.txtFormConfigDetails, a.vchredirectWindowType, a.formStatus, a.tinFormType, a.tinFinalSubmitStatus, a.tinGridType, b.vchModuleName "
			+ " FROM m_process_name a LEFT JOIN m_module_name b ON a.intModuleId=b.intModuleId where a.intProcessId =:iteamId and a.vchProcessName =:vchProcessName and a.bitDeletedFlag = 0 order by a.intProcessId DESC " ,nativeQuery = true)

	List<Object[]> findAllActiveProcessByItemIdAndProcessName(Integer iteamId, String vchProcessName);
	
	
	@Query(value = " SELECT a.intProcessId,a.vchProcessName,a.vchProcessCode,a.intPayment,a.intApproval,a.intDocument,a.viewAtCitizen, "
			+ " a.landUseExist, a.tinProjectAreaExist, a.dtmCreatedOn, a.intModuleId, a.tinStatus,a.vchProcessClass,a.tinAccessBy,a.tinPaymentTime,a.tinDemandNoteExist, "
			+ " a.txtOtherInfo, a.vchXyzs, a.intCreatedBy, a.stmUpdatedOn, a.intUpdatedBy, a.bitDeletedFlag, a.intBOQProcessId, a.tinProcessType, a.txtDynCtrlTblNm, a.vchSection ,"
			+ " a.vchSchemePoster, a.vchSchemeGuideline, a.m_process_namecol, a.intServiceMode, a.txtSchemeDescription, a.vchRedirectURL, a.vchAPIRedirectURLDtls, a.vchAPIReferenceURLDtls, "
			+ " a.vchAPIStatusURLDtls, a.intBaseType, a.vchProcessDtls, a.vchAPIURLDtls, a.vchConfigurationItemsDtls , a.tinPublishStatus, a.vchFormType, a.vchTableName, a.intAdminApplication, a.intWebsiteApplication, "
			+ " a.txtFormConfigDetails, a.vchredirectWindowType, a.formStatus, a.tinFormType, a.tinFinalSubmitStatus, a.tinGridType, b.vchModuleName "
			+ " FROM m_process_name a LEFT JOIN m_module_name b ON a.intModuleId=b.intModuleId where a.intModuleId =:moduleId and a.vchProcessName =:vchProcessName and a.bitDeletedFlag = 0  order by a.intProcessId DESC" ,nativeQuery = true)

	List<Object[]> findAllActiveProcessByModuleAndrocessName(Integer moduleId, String vchProcessName);

/*
	@Query(value = " SELECT a.intProcessId,a.vchProcessName,a.vchProcessCode,a.intPayment,a.intApproval,a.intDocument,a.viewAtCitizen, "
			+ " a.landUseExist, a.tinProjectAreaExist, a.dtmCreatedOn, a.intModuleId, a.tinStatus,a.vchProcessClass,a.tinAccessBy,a.tinPaymentTime,a.tinDemandNoteExist, "
			+ " a.txtOtherInfo, a.vchXyzs, a.intCreatedBy, a.stmUpdatedOn, a.intUpdatedBy, a.bitDeletedFlag, a.intBOQProcessId, a.tinProcessType, a.txtDynCtrlTblNm, a.vchSection ,"
			+ " a.vchSchemePoster, a.vchSchemeGuideline, a.m_process_namecol, a.intServiceMode, a.txtSchemeDescription, a.vchRedirectURL, a.vchAPIRedirectURLDtls, a.vchAPIReferenceURLDtls, "
			+ " a.vchAPIStatusURLDtls, a.intBaseType, a.vchProcessDtls, a.vchAPIURLDtls, a.vchConfigurationItemsDtls , a.tinPublishStatus, a.vchFormType, a.vchTableName, a.intAdminApplication, a.intWebsiteApplication, "
			+ " a.txtFormConfigDetails, a.vchredirectWindowType, a.formStatus, a.tinFormType, a.tinFinalSubmitStatus, a.tinGridType, b.vchModuleName "
			+ " FROM m_process_name a JOIN m_module b ON a.intModuleId=b.intModuleId where a.intProcessId =:iteamId and  a.vchProcessName like :'vchProcessName%' " ,nativeQuery = true)
	
	List<Object[]> findAllActiveProcessByItemIdAndProcessName(Integer iteamId, String vchProcessName);


	@Query(value = " SELECT a.intProcessId,a.vchProcessName,a.vchProcessCode,a.intPayment,a.intApproval,a.intDocument,a.viewAtCitizen, "
			+ " a.landUseExist, a.tinProjectAreaExist, a.dtmCreatedOn, a.intModuleId, a.tinStatus,a.vchProcessClass,a.tinAccessBy,a.tinPaymentTime,a.tinDemandNoteExist, "
			+ " a.txtOtherInfo, a.vchXyzs, a.intCreatedBy, a.stmUpdatedOn, a.intUpdatedBy, a.bitDeletedFlag, a.intBOQProcessId, a.tinProcessType, a.txtDynCtrlTblNm, a.vchSection ,"
			+ " a.vchSchemePoster, a.vchSchemeGuideline, a.m_process_namecol, a.intServiceMode, a.txtSchemeDescription, a.vchRedirectURL, a.vchAPIRedirectURLDtls, a.vchAPIReferenceURLDtls, "
			+ " a.vchAPIStatusURLDtls, a.intBaseType, a.vchProcessDtls, a.vchAPIURLDtls, a.vchConfigurationItemsDtls , a.tinPublishStatus, a.vchFormType, a.vchTableName, a.intAdminApplication, a.intWebsiteApplication, "
			+ " a.txtFormConfigDetails, a.vchredirectWindowType, a.formStatus, a.tinFormType, a.tinFinalSubmitStatus, a.tinGridType, b.vchModuleName "
			+ " FROM m_process_name a JOIN m_module b ON a.intModuleId=b.intModuleId where  a.vchProcessName like :'vchProcessName%' " ,nativeQuery = true)
	
	List<Object[]> findAllActiveProcessByProcessName(String vchProcessName); */
	



	

	//List<Object[]> findAllActiveProcess(Integer moduleId, Integer itemId, String vchProcessName);
	
//	List<Object[]> findAllActiveProcess(@Param("processId") int processId);

//	List<Object[]> findAllActiveProcess(ProcessEntity entity);
	
	
	@Query(value = " SELECT a.intProcessId,a.vchProcessName,a.intModuleId , b.vchModuleName "
			+ " FROM m_process_name a LEFT JOIN m_module_name b ON a.intModuleId=b.intModuleId where "
			+ "b.bitDeletedFlag = 0 and(:processId=0 or a.intProcessId=:processId) and (:moduleId=0 or a.intModuleId=:moduleId)  " ,nativeQuery = true)
	List<Object[]> findByProcessId(Integer processId, Integer moduleId);
	
	
//	@Query("From MProcessName where bitDeletedFlag = 0 and(:processId=0 or intProcessId=:processId) and (:moduleId=0 or intModuleId=:moduleId)")
//	List<MProcessName> getByProcessId(Integer processId, Integer moduleId);
	
	@Query(value = "select mdfc.configurationId,mdfc.itemId,mdfc.sectionId,mdfc.formDetails,mdfc.status,mpn.dtmCreatedOn,mpn.vchProcessName,mpn.tinFinalSubmitStatus,mpn.vchSection from m_process_name mpn inner join m_dyn_temp_form_configuration mdfc on mpn.intProcessId=mdfc.itemId where mpn.intProcessId=:intProcessId and(:sectionId=0 or mdfc.sectionId=:sectionId) and mdfc.deletedFlag=0 and (:finalsubmitStatus=-1 or mpn.tinFinalSubmitStatus=:finalsubmitStatus) order by mdfc.configurationId", nativeQuery = true)
	List<Object[]> getByProcessIdWithAndStatusFromTemp(Integer intProcessId, Integer sectionId, Integer finalsubmitStatus);

	@Query(value = "select mdfc.configurationId,mdfc.itemId,mdfc.sectionId,mdfc.formDetails,mdfc.status,mpn.dtmCreatedOn,mpn.vchProcessName,mpn.tinFinalSubmitStatus,mpn.vchSection from m_process_name mpn inner join m_dyn_form_configuration mdfc on mpn.intProcessId=mdfc.itemId where mpn.intProcessId=:intProcessId and(:sectionId=0 or mdfc.sectionId=:sectionId) and mdfc.deletedFlag=0 and (:finalsubmitStatus=-1 or mpn.tinFinalSubmitStatus=:finalsubmitStatus) order by mdfc.configurationId", nativeQuery = true)
	List<Object[]> getByProcessIdWithAndStatusFromMain(Integer intProcessId, Integer sectionId, Integer finalsubmitStatus);
	
	@Query("From MProcessName where intProcessId=:itemId")
	MProcessName getByProcessId(Integer itemId);

	@Query(value=" select HS.VCH_ORG_EMAIL_ID,HS.VCH_ORG_HD_NAME,HS.VCH_EMPANEL_TYPE,HS.VCH_ROHINI_ID,HS.VCH_HOSP_ADDRESS,HS.VCH_CITY_TOWN,HS.VCH_HOSP_PIN,HS.VCH_HOSP_REGDNO from DYN_BSKY_HOS_B_INFO HS join T_ONLINE_SERVICE_APPLICATION SP on HS.INTONLINESERVICEID = SP.INTONLINESERVICEID left join t_applicant_profile apt on apt.INTPROFILEID = SP.INTPROFILEID  where HS.BITDELETEDFLAG=0 and SP.BITDELETEDFLAG=0 and apt.INTPROFILEID =:profileId ",nativeQuery = true)
	List<Object[]> getProfileApplicationById(Integer profileId);
	
	
	@Modifying
	@Query(value="UPDATE m_process_name SET tinFinalSubmitStatus=:finalStatus,tinGridType=:gridType WHERE intProcessId=:itemId",nativeQuery = true)
	void setGridAndFinalStatus(Integer finalStatus, Integer gridType, Integer itemId);

	
	@Query("select intProcessId , txtSchemeDescription , vchProcessName , "
			+ "vchSchemePoster From ProcessEntity where bitDeletedFlag = 0 and "
			+ "tinFinalSubmitStatus = 1 and tinPublishStatus=1 and intWebsiteApplication=1")
	List<Object[]> getByProcessIdDetails();
	
	@Query(value="select vchSection , tinGridType , vchProcessName , vchTableName "
			+ "from m_process_name where bitDeletedFlag = 0  and intWebsiteApplication=1"
			+ "and intProcessId = ?1",nativeQuery = true)
	List<Object[]> getSectiondetails(Integer processid);


	ProcessEntity findByIntProcessId(Integer processId);

	@Query(value="select vchTableName from m_process_name where bitDeletedFlag = 0 and intProcessId = ?1 ",nativeQuery = true)
	String getDetailsByProcessId(Integer itemId);


	@Query(value="select tinGridType from m_process_name where bitDeletedFlag = 0 and intProcessId =:parseInt",nativeQuery = true)
	Byte getGridType(int parseInt);
	
	@Query(value="select FC.formDetails, pn.vchTableName from m_dyn_form_configuration FC join m_process_name pn on FC.itemId = pn.intProcessId where FC.deletedFlag =0 and pn.bitDeletedFlag = 0 and FC.itemId = ?1 ",nativeQuery = true)
	List<Object[]> getFormDetailsByProcessId(Integer itemId);
	
	@Query(value="select vchTableName from m_process_name where bitDeletedFlag = 0 and intProcessId =:processId",nativeQuery = true)
	String getTableName(Integer processId);
	
	@Query(value="select vchSection,tinGridType,vchProcessName,vchTableName from m_process_name where bitDeletedFlag = 0 and intWebsiteApplication=1 and intProcessId=:parseInt",nativeQuery = true)
	List<Object[]> getDataByProcessId(int parseInt);
	
	@Query(value="select intProcessId, vchprocessName from m_process_name",nativeQuery = true)
	List<Object[]> findAllById();
	
	@Query(value="select vchProcessName from m_process_name where bitDeletedFlag=0 and intProcessId=:intProcessId",nativeQuery = true)
	String findProcessNameByIntProcessId(Integer intProcessId);

	@Query(value="select vchSection from m_process_name where bitDeletedFlag=0 and intProcessId=:intProcessId",nativeQuery = true)
	Clob getVchSectionNameByFormId(Integer intProcessId);
	
	@Query(value="select * from m_process_name where bitDeletedFlag=0 and intProcessId=:intProcessId",nativeQuery = true)
	ProcessEntity getVchSectionNameByFormId1(Integer intProcessId);

	@Query(value="select JSONOPTIONTEXTDETAILS from DYN_JSOPTTEDTL_DATA WHERE INTID=:intId AND ONLINESERVICEID=:serviceId AND TABLENAME =:tableName",nativeQuery = true)
	Clob filterDataById(Integer intId, Integer serviceId, String tableName);


}
