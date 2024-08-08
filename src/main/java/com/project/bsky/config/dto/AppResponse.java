package com.project.bsky.config.dto;


import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@ToString
@Accessors(chain = true)

public class AppResponse<T> {
	private String status;
	private T result;
	private String msg;
	public static <T> AppResponse<T> success1(String msg) {
		return new AppResponse<T>().setStatus("200").setMsg(msg);
	}
	public static <T> AppResponse<T> success(T result) {
		return new AppResponse<T>().setStatus("200").setResult(result);
	}

	public static <T> AppResponse<T> failed(String msg) {
		return new AppResponse<T>().setMsg(msg).setStatus("300");
	}

	enum Status {
		SUCCESS, FAILED;
	}

}
