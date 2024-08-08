/**
 * 
 */
package com.project.bsky.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.bsky.model.MobileUserModel;

/**
 * @author preetam.mishra
 *
 */
public interface MobileUserRepository extends JpaRepository<MobileUserModel, Integer>{
	MobileUserModel findByUserName(String username);
}
