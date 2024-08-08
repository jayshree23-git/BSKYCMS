package com.project.bsky.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.bsky.model.ActionRemark;

/**
 * @author priyanka.singh
 *
 */
@Repository
public interface ActionRemarkRepository extends JpaRepository<ActionRemark, Long>{

//	@Query("select count(*) from ActionRemark where id=:id")
//	Integer checkRemark(Long id);
//
	@Query("select count(*) from ActionRemark where remarks=:remarks")
	Integer checkRemark(String remarks);

	
	@Query("SELECT count(*) FROM ActionRemark g WHERE g.remarks=:remarks")
	Integer countRowForCheckDuplicateType(String remarks);

//	@Query("FROM ActionRemark a WHERE a.statusFlag=1 and  order by  remarks desc")
//	List<ActionRemark> findCpdRemark();

	@Query("FROM ActionRemark a WHERE a.statusFlag=1")
	List<ActionRemark> findCpdRemarkByStatus();


	@Query("FROM ActionRemark a WHERE a.statusFlag=2")
	List<ActionRemark> findsnaRemarkByStatus();

	@Query("select count(*) from ActionRemark where remarks=:remarks")
	Integer checkRemarkSNA(String remarks);
	
	

//	Integer checkRemark(String remarks);

}
