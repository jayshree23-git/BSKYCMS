package com.project.bsky.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.bsky.model.Mstschemesubcategory;
@Repository
public interface Mstschemesubcategoryrepository extends JpaRepository<Mstschemesubcategory, Integer>{

}
