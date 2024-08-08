/**
 * 
 */
package com.project.bsky.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.bsky.model.EnableHospitalDischargeLogModel;

/**
 * @author jayshree.moharana
 *
 */
@Repository
public interface EnableHospitalDischargeLog extends JpaRepository<EnableHospitalDischargeLogModel, Long> {

}
