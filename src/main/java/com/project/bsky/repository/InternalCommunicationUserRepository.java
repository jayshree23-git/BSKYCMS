/**
 * 
 */
package com.project.bsky.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.bsky.model.InternalCommunication_user;

/**
 * @author rajendra.sahoo
 *
 */
@Repository
public interface InternalCommunicationUserRepository extends JpaRepository<InternalCommunication_user, Long> {

	@Query("from InternalCommunication_user where statusflag=0")
	List<InternalCommunication_user> getallactiveuser();

	@Query("from InternalCommunication_user where statusflag=0 and Userid=:towhom")
	InternalCommunication_user getuser(Long towhom);

}
