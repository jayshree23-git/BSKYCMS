/**
 * 
 */
package com.project.bsky.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.bsky.model.FoRemark;

/**
 * @author rajendra.sahoo
 *
 */
@Repository
public interface FoRemarkRepository extends JpaRepository<FoRemark, Long> {

	@Query("select count(*) from FoRemark where remark=:remark")
	Integer cheakduplicate(String remark);

	@Query("from FoRemark order by remark asc")
	List<FoRemark> getforemark();

	FoRemark findByremark(String remark);

	@Query("from FoRemark where status=0 order by remark")
	List<FoRemark> getactiveforemark();

}
