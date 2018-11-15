package com.example.springbootproject.log;

import com.alibaba.fastjson.JSON;
import com.example.springbootproject.utils.HostUtil;
import lombok.Data;

/**
 * Created by xp-zhao on 2018/11/15.
 */
@Data
public class SimpleLog implements LogFormater
{
	private String logtype;//日志类型
	private String logid;//logid
	private String lip;//机器IP

	private String methodName;
	private Object args;
	private Object result;
	private long du;

	public SimpleLog(String logtype)
	{
		this.logtype = logtype;
		lip = HostUtil.localIp();
		logid = LogThreadLoacal.logid.get();
	}

	@Override
	public String format()
	{
		return String.format("logid:%s||LIP:%s||type:%s||med:%s||req:%s||resp:%s||du:%d" , this.getLogid() ,
			this.getLip() , this.getLogtype() , this.getMethodName() , JSON.toJSONString(this.getArgs()) ,
			JSON.toJSONString(this.getResult()) , this.getDu());
	}
}
