package com.example.springbootproject.model.resp;


/**
 * Created by xp-zhao on 2018/11/15.
 */
public enum  ReturnInfo
{
	/**
	 *code:000000<br>
	 *info:操作成功
	 */
	SUCCESS("000000","操作成功"),
	/**
	 *code:199990<br>
	 *info:数据库操作失败
	 */
	FAILED("199999","操作失败");

	private final String code;
	private final String info;

	ReturnInfo(String code,String info){
		this.code = code;
		this.info = info;
	}
	public String getCode() {
		return code;
	}
	public String getInfo() {
		return info;
	}
}
