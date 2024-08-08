/**
 * 
 */
package com.project.bsky.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.bsky.model.PackageSchemeMapping;

/**
 * @author santanu.barad
 *
 */
@Repository
public interface PackageSchemeMappingRepository extends JpaRepository<PackageSchemeMapping, Long> {
	public List<PackageSchemeMapping> findAllByProcedureCode(String procedureCode);
}
