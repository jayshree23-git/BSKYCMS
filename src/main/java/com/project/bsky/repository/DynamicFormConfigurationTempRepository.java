package com.project.bsky.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.bsky.entity.DynamicFormConfigurationTemp;
@Repository
public interface DynamicFormConfigurationTempRepository extends JpaRepository<DynamicFormConfigurationTemp, Integer> {

	@Query("from DynamicFormConfigurationTemp where itemId=:itemId and(:sectionId=0 or sectionId=:sectionId)")
	List<DynamicFormConfigurationTemp> getDataByItemId(Integer itemId, Integer sectionId);

	@Query("from DynamicFormConfigurationTemp where itemId=:itemId and deletedFlag=0")
	List<DynamicFormConfigurationTemp> findAllByItemId(Integer itemId);

	@Query(value="SELECT table_name FROM user_tables",nativeQuery = true)
	List<String> getAllTableName();

	@Query(value="desc = ?1 ",nativeQuery = true)
	List<Object[]> getTableDesc(String formTable);
	
	
	@Transactional
	@Modifying
	@Query(value="finalQuery",nativeQuery = true)
	void CreatingTable(String finalQuery);

	
	@Query(value="SELECT column_name FROM all_tab_cols  WHERE table_name=:formTable",nativeQuery=true)
	List<String> getColList(String formTable);

	@Transactional
	@Modifying
	@Query(value="UPDATE m_dyn_temp_form_configuration SET deletedFlag = 1 WHERE configurationId =:configurationId",nativeQuery = true)
	void deleteDataByConfigId(Integer configurationId);
	
	@Query(value="select * from m_dyn_temp_form_configuration where itemId=:itemId and sectionId=:sectionId",nativeQuery = true)
	DynamicFormConfigurationTemp getDataByItemIdAndSectionId(Integer itemId, Integer sectionId);
	

}