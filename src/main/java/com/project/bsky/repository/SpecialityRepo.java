package com.project.bsky.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.bsky.model.Specialitydoctor;

public interface SpecialityRepo extends JpaRepository<Specialitydoctor, Integer> {

}
