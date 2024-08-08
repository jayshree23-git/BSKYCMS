package com.project.bsky.bean;

import java.util.List;

import lombok.Data;


@Data
public class AuthDetail{
    public List<Param> params;
    public String authType;
}
