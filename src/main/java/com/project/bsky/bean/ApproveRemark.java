package com.project.bsky.bean;

import lombok.Data;

@Data
public class ApproveRemark {

    private Integer aprv_id;
    private String remarks;
    private Long txnDetailsId;
    private Long aprvedAmount;

    @Override
    public String toString() {
        return "ApproveRemark{" +
                "aprv_id=" + aprv_id +
                ", remarks='" + remarks + '\'' +
                ", txnDetailsId='" + txnDetailsId + '\'' +
                ", aprvedAmount ='" + aprvedAmount + '\''+
                '}';
    }
}
