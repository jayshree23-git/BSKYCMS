package com.project.bsky.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.bsky.model.TypeOfExpertiseModel;

/**
 * @author jayshree.moharana
 *
 */
@Repository
public interface TypeOfExpertiseRepository extends JpaRepository<TypeOfExpertiseModel, Long> {
	
	@Query("from TypeOfExpertiseModel order by typeofexpertiseid desc")
	List<TypeOfExpertiseModel> findAllbyorder();
	@Query ("update TypeOfExpertiseModel set status=1 where typeofexpertiseid=:typeofexpertiseid")
	void delete(long typeofexpertiseid);
	
	TypeOfExpertiseModel findBytypeofexpertisename(String typeofexpertisename);

	@Query("select count(*) from TypeOfExpertiseModel where typeofexpertisename=:typeofexpertisename and medexpertiseid.id=:medicalexpid"  )
	Long findtypeofexpertisename(String typeofexpertisename, long medicalexpid);

}
