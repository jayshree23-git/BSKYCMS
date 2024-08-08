package com.project.bsky.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UnprocessedCountData {
	private Integer discharge;
	private Integer claim;
	private Integer freshCpd;
	private Integer cpdResettlement;
	private Integer hosPending;
	private String state;
	private String district;
	private String hospital;

}
