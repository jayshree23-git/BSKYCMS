package com.project.bsky.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.bsky.model.CallResponsecategory;


@Repository

public interface CallResponseCategoryRepository extends JpaRepository<CallResponsecategory, Long> {
	@Query("FROM CallResponsecategory where statusId=:statusId order by categoryName")
	List<CallResponsecategory> findByCategoryName(Integer statusId);
//	 @Query("FROM MOBILENOACTIVESTATUS where status=:status")
//	    List<CallResponsecategory> findDetails(String status);

}
