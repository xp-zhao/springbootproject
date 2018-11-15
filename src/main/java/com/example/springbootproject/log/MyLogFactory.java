package com.example.springbootproject.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by xp-zhao on 2018/11/15.
 */
public class MyLogFactory
{
	public static MyLogger getLogger(Class<?> clazz)
	{
		Logger logger = LoggerFactory.getLogger(clazz);
		MyLogger myLogger = new MyLogger(logger);
		return myLogger;
	}
}
