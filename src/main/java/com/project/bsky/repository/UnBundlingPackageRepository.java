/**
 * 
 */
package com.project.bsky.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.bsky.model.UnBundlingPackage;

/**
 * @author rajendra.sahoo
 *
 */
@Repository
public interface UnBundlingPackageRepository extends JpaRepository<UnBundlingPackage, Long> {

	@Query("from UnBundlingPackage order by unboundlingid desc")
	List<UnBundlingPackage> getall();

}
