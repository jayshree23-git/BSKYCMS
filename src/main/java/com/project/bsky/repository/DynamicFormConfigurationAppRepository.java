package com.project.bsky.repository;

import java.sql.Clob;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.bsky.entity.DynamicFormConfigurationApp;

@Repository
public interface DynamicFormConfigurationAppRepository extends JpaRepository<DynamicFormConfigurationApp, Integer> {

	@Query("from DynamicFormConfigurationApp where itemId=:itemId and(:sectionId=0 or sectionId=:sectionId) and deletedFlag = 0")
	List<DynamicFormConfigurationApp> getDataByItemIdApp(Integer itemId, Integer sectionId);

	@Query(value = "select :listval from :tablenam where (:onlineserviceid=0 or intOnlineServiceId=:onlineserviceid) and bitDeletedFlag =0 ", nativeQuery = true)
	List<Object[]> getcombineddetails(String listval, Integer onlineserviceid, String tablenam);

	@Query(value = "select formDetails from m_dyn_form_configuration where sectionId=:parseInt and deletedFlag = 0 and itemId=:processId", nativeQuery = true)
	Clob getFormDetailsBySectionId(Integer parseInt, Integer processId);

	@Query("From DynamicFormConfigurationApp where itemId=:parseInt and deletedFlag = 0")
	DynamicFormConfigurationApp getFormDataSecListNotPre(int parseInt);

	@Query("From DynamicFormConfigurationApp where deletedFlag=0 and itemId=:processId and sectionId=:sectionId")
	DynamicFormConfigurationApp getDataByProcessIdAndSectionId(Integer processId, Integer sectionId);

	@Query("From DynamicFormConfigurationApp where deletedFlag=0 and itemId=:parseInt and sectionId=:string")
	DynamicFormConfigurationApp getDataByProcessId(int parseInt, Integer string);

	@Query("From DynamicFormConfigurationApp where deletedFlag=0 and itemId=:parseInt")
	DynamicFormConfigurationApp getDataByProcessId(int parseInt);
	@Query("From DynamicFormConfigurationApp where deletedFlag=0 and itemId=:parseInt")
	List<DynamicFormConfigurationApp> getDataByIntProcessId(int parseInt);

	@Query(value="select vchSectionWiseTableName from m_dyn_form_configuration where deletedFlag = 0 and sectionId=:sectionId and itemId=:processid",nativeQuery=true)
	String getTableNameSectionWise(int sectionId, int processid);

}