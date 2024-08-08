/**
 * 
 */
package com.project.bsky.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.bsky.model.InternalGrievance;
import com.project.bsky.model.UserDetails;

/**
 * @author priyanka.singh
 *
 */
@Repository
public interface InternalGrievanceRepository extends JpaRepository<InternalGrievance, Long> {
	
	@Query("from InternalGrievance  where   categoryType=:categoryType and priority=:priority")
	List<InternalGrievance> findGrievnceFilter(Integer categoryType, Integer priority);
	
	@Query("from InternalGrievance  where   categoryType=:categoryType")
	List<InternalGrievance> findGrievnceFiltercategoryType(Integer categoryType);
	
	@Query("from InternalGrievance  where   priority=:priority")
	List<InternalGrievance> findGrievnceFilterpriority( Integer priority);

//	@Query("from InternalGrievance order by createdOn asc,priority asc,categoryType asc")
//	List<InternalGrievance> findAlldata();
	
	@Query("from InternalGrievance order by grievanceId desc")
	List<InternalGrievance> findAlldata();

	@Query("from InternalGrievance where createdOn=:date")
	InternalGrievance findbycreatedOn(Date date);

	@Query("select count(*) from InternalGrievance where To_Date(createdOn,'DD-MM-RR')=To_Date(sysdate,'DD-MM-RR')")
	Integer checkCountDaate();

	List<InternalGrievance> findByStatusFlag(Integer status);
	List<InternalGrievance> findByPriorityAndStatusFlag(Integer priority,Integer status);
	List<InternalGrievance> findByCategoryTypeAndStatusFlag(Integer categoryType,Integer status);
	List<InternalGrievance> findByCategoryTypeAndPriorityAndStatusFlag(Integer categoryType,Integer priority,Integer status);

//	List<InternalGrievance> findByCategoryTypeAndPriorityAndStatusFlagAndCreatedOn(Integer categoryType,
//			Integer priority, Date fromDate, Date toDate, Integer status);
}
