package com.project.bsky.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.bsky.model.MedicalExpertiseModel;

/**
 * @author jayshree.moharana
 *
 */
@Repository
public interface EmpanelmentMasterRepository extends JpaRepository<MedicalExpertiseModel, Long>{
	

@Query("select count(*) from MedicalExpertiseModel where medexpertisename=:medexpertisename")
Integer checkduplicatemedicalexpertise(String medexpertisename);
	
@Query("FROM MedicalExpertiseModel order by id desc")
List<MedicalExpertiseModel> getmedicalexpertiseData();
	
MedicalExpertiseModel findBymedexpertisename(String medexpertisename);

@Query("from MedicalExpertiseModel where statusFlag=0")
List<MedicalExpertiseModel> findAllactivedata ();





}
