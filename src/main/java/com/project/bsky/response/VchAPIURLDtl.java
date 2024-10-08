package com.project.bsky.response;

import java.util.List;

import com.project.bsky.bean.AuthDetail;
import com.project.bsky.bean.Detail;

import lombok.Data;

@Data
public class VchAPIURLDtl{
    public String url;
    public Object method;
    public List<Detail> details;
    public List<AuthDetail> authDetails;
}
