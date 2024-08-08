package com.project.bsky.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.project.bsky.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
	User findByUserName(String username);

	User findByUserNameIgnoreCase(String username);

	User findByUserId(int userId);


	@Query("from User where groupId=6")
	List<User> getdclist();

}
