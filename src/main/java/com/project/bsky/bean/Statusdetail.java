package com.project.bsky.bean;

import java.util.ArrayList;

import lombok.Data;

@Data
public class Statusdetail{
    public String url;
    public String method;
    public ArrayList<Detail> details;
    public ArrayList<AuthDetail> authDetails;
}
