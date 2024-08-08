package com.project.bsky.bean;

import java.util.List;

import lombok.Data;

@Data
public class Submitspecialitybean {
    public String packageheadercode;
    public String packagesubcategor;
    public String procedurecode;
    public List<Long> dataIdArray;
    public String[] procedurecodeArray;
    public String type;
    public Long userid;
}
