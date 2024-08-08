package com.project.bsky.bean;

import java.util.List;

import lombok.Data;

@Data
public class Cpdspecialitymappingbean {

	private Long cpdId;
	private Long userId;
	private List<Cpdspecialitydocbean> speciality;
}
