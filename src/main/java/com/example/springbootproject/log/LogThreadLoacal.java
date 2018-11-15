package com.example.springbootproject.log;

/**
 * Created by xp-zhao on 2018/11/15.
 */
public class LogThreadLoacal
{
	public static final ThreadLocal<String> logid = new ThreadLocal<>();
}
