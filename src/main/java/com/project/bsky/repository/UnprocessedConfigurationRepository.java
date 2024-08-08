/**
 * 
 */
package com.project.bsky.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.bsky.model.UnprocessedConfiguration;

/**
 * @author priyanka.singh
 *
 */
@Repository
public interface UnprocessedConfigurationRepository extends JpaRepository<UnprocessedConfiguration, Long> {
	
	@Query("FROM UnprocessedConfiguration order by  unprocessedId desc")
	List<UnprocessedConfiguration> findDetails();

	@Query("select count(*) from UnprocessedConfiguration where years=:years")
	Integer checkduplicateYear(Long years);

	@Query("select count(*) from UnprocessedConfiguration where months=:months and years=:years")
	Integer checkDuplicateMonth(Long months, Long years);

	UnprocessedConfiguration findByMonths(Long months);

	@Query("FROM UnprocessedConfiguration where months=:months and years=:years")
	List<UnprocessedConfiguration> findFilterUnprocess(Long years, Long months);

}
