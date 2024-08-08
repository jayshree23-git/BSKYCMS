package com.project.bsky.bean;

import lombok.Data;

import java.util.List;

@Data
public class PreAuthGroupBean {

    private String userId;
    private Integer action;
    List<PreAuthBean> group;

}
