package com.project.bsky.bean;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HospitalGroupAuthBean {

	List<HospitalGroupBean> group;
}
