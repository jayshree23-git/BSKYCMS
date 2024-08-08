package com.project.bsky.bean;

import lombok.Data;

import java.util.List;

@Data
public class FPOverrideListBean {

    private String userId;

    public Integer getAction() {
        return action;
    }

    public void setAction(Integer action) {
        this.action = action;
    }

    private Integer action;
    List<overrideCodeBean> overrideCode;

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    


}
