package com.project.bsky.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLocation {
private String distId;
private String stateId;
public String getDistId() {
	return distId;
}
public void setDistId(String distId) {
	this.distId = distId;
}
public String getStateId() {
	return stateId;
}
public void setStateId(String stateId) {
	this.stateId = stateId;
}
@Override
public String toString() {
	return "UserLocation [distId=" + distId + ", stateId=" + stateId + "]";
}

}
