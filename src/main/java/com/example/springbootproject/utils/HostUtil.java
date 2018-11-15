package com.example.springbootproject.utils;


import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;

/**
 * Created by xp-zhao on 2018/6/25.
 */
public class HostUtil
{
	private static final Logger logger = LoggerFactory.getLogger(HostUtil.class);
	private volatile static String localIp;
	//获取本机ip
	public static String localIp()
	{
		if(StringUtils.isBlank(localIp))
		{
			synchronized (HostUtil.class)
			{
				if(StringUtils.isBlank(localIp))
				{
					try
					{
						InetAddress ia=InetAddress.getLocalHost();
						localIp=ia.getHostAddress();
					}
					catch (Exception e)
					{
						logger.error("获取本机IP失败",e);
						return "127.0.0.1";
					}
				}
			}
		}
		return localIp;
	}
}
