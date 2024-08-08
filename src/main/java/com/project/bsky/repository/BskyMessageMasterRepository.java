/**
 * 
 */
package com.project.bsky.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.bsky.model.BskyMessageMaster;

/**
 * @author rajendra.sahoo
 *
 */
public interface BskyMessageMasterRepository extends JpaRepository<BskyMessageMaster, Long> {

}
