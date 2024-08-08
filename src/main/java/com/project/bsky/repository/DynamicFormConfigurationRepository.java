package com.project.bsky.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.bsky.entity.DynamicFormConfiguration;

@Repository
@Transactional
public interface DynamicFormConfigurationRepository extends JpaRepository<DynamicFormConfiguration, Integer> {

	
	@Modifying
	@Query(value=":finalQuery",nativeQuery=true)
	void alterTable(String finalQuery);

	@Query("From DynamicFormConfiguration where itemId=:itemId and deletedFlag=0")
	List<DynamicFormConfiguration> getDataBySectionIdAndItemId(Integer itemId);
	
	@Modifying
	@Query(value="UPDATE M_DYN_FORM_CONFIGURATION SET DELETEDFLAG=1 where CONFIGURATIONID=:configurationId and DELETEDFLAG=0",nativeQuery = true)
	void updateData(Integer configurationId);

}
