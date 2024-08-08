/**
 * 
 */
package com.project.bsky.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.bsky.model.Mstschedular;

/**
 * @author rajendra.sahoo
 *
 */
@Repository
public interface MstschedulerRepository extends JpaRepository<Mstschedular, Integer> {

	@Query("from Mstschedular where status=0")
	List<Mstschedular> getAll();

	@Query("from Mstschedular order by id desc")
	List<Mstschedular> findAlldata();

}
