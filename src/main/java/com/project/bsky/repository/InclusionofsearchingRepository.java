/**
 * 
 */
package com.project.bsky.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.bsky.bean.PackageMaster;
import com.project.bsky.model.Inclusionofsearching;

/**
 * @author hrusikesh.mohanty
 *
 */
public interface InclusionofsearchingRepository extends JpaRepository<PackageMaster, Long> {

}
