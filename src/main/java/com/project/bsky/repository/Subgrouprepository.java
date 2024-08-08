/**
 * 
 */
package com.project.bsky.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.bsky.model.Subgroup;

/**
 * @author rajendra.sahoo
 *
 */
@Repository
public interface Subgrouprepository extends JpaRepository<Subgroup, Long> {

	
	@Transactional
	@Modifying
	@Query ("update Subgroup set status=1 where subgroupid=:subgroupid")
	void delete(long subgroupid);


	@Query("select count(*) from Subgroup where subgroupname=:subgroupname")
	Long findsubgroupname(String subgroupname);

	@Query("from Subgroup order by subgroupid desc")
	List<Subgroup> findAllbyorder();

//	@Query("from Subgroup where subgroupname=:subgroupname")
	Subgroup findBysubgroupname(String subgroupname);

}
