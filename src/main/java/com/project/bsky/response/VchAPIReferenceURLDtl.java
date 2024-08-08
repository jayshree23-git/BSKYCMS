package com.project.bsky.response;

import java.util.ArrayList;

import com.project.bsky.bean.AuthDetail;
import com.project.bsky.bean.Detail;

import lombok.Data;


@Data
public class VchAPIReferenceURLDtl{
    public String url;
    public Object method;
    public ArrayList<Detail> details;
    public ArrayList<AuthDetail> authDetails;
}
