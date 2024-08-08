package com.project.bsky.bean;

import lombok.Data;

@Data
public class OverrideRemark {
    private Integer aprv_id;
    private String remarks;

    @Override
    public String toString() {
        return "OverrideRemark{" +
                "aprv_id=" + aprv_id +
                ", remarks='" + remarks + '\'' +
                '}';
    }
}
