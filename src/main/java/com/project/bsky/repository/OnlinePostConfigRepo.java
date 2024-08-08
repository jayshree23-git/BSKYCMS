package com.project.bsky.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.bsky.model.OnlinePostConfigModel;
@Repository
public interface OnlinePostConfigRepo extends JpaRepository<OnlinePostConfigModel, Long>{

}
