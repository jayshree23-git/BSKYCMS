package com.project.bsky.bean;

import java.util.ArrayList;

import com.project.bsky.bean.Detail;

import lombok.Data;


@Data
public class ApiUrldetail{
    public String url;
    public String method;
    public ArrayList<Detail> details;
    public ArrayList<AuthDetail> authDetails;
}
