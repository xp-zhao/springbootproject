package com.example.springbootproject.model.resp;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by xp-zhao on 2018/11/15.
 */
@Data
@NoArgsConstructor
public class BaseResponse
{
	/**
	 * 返回码
	 */
	private String code;
	/**
	 * 返回描述
	 */
	private String info;

	private Object data;

	public BaseResponse(ReturnInfo returnInfo)
	{
		this.code = returnInfo.getCode();
		this.info = returnInfo.getInfo();
	}

	public BaseResponse(ReturnInfo returnInfo, Object data)
	{
		this.code = returnInfo.getCode();
		this.info = returnInfo.getInfo();
		this.data = data;
	}
	public BaseResponse(String code)
	{
		this.code = code;
	}
	public BaseResponse(String code, String info)
	{
		this.code = code;
		this.info = info;
	}
}
