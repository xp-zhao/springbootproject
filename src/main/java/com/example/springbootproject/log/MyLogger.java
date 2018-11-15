package com.example.springbootproject.log;

import org.slf4j.Logger;

/**
 * Created by xp-zhao on 2018/11/15.
 */
public class MyLogger
{
	private Logger logger;

	protected MyLogger(Logger logger)
	{
		this.logger = logger;
	}

	public void info(LogFormater obj)
	{
		logger.info(obj.format());
	}

	public void info(String msg)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("logid:").append(LogThreadLoacal.logid.get()).append("||").append(msg);
		logger.info(sb.toString());
	}

	public void info(String format,Object... arguments)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("logid:").append(LogThreadLoacal.logid.get()).append("||").append(format);
		logger.info(sb.toString(), arguments);
	}


	public void debug(LogFormater obj)
	{
		if(logger.isDebugEnabled())
		{
			logger.debug(obj.format());
		}
	}

	public void debug(String msg)
	{
		if(logger.isDebugEnabled())
		{
			StringBuilder sb = new StringBuilder();
			sb.append("logid:").append(LogThreadLoacal.logid.get()).append("||").append(msg);
			logger.debug(sb.toString());
		}
	}

	public void debug(String format,Object... arguments)
	{
		if(logger.isDebugEnabled())
		{
			StringBuilder sb = new StringBuilder();
			sb.append("logid:").append(LogThreadLoacal.logid.get()).append("||").append(format);
			logger.debug(sb.toString(), arguments);
		}
	}

	public void error(String msg)
	{
		if(logger.isErrorEnabled())
		{
			StringBuilder sb = new StringBuilder();
			sb.append("logid:").append(LogThreadLoacal.logid.get()).append("||").append(msg);
			logger.error(sb.toString());
		}
	}

	public void error(String format,Object... arguments)
	{
		if(logger.isErrorEnabled())
		{
			StringBuilder sb = new StringBuilder();
			sb.append("logid:").append(LogThreadLoacal.logid.get()).append("||").append(format);
			logger.error(sb.toString(), arguments);
		}
	}

	public void error(String msg,Throwable t)
	{
		if(logger.isErrorEnabled())
		{
			StringBuilder sb = new StringBuilder();
			sb.append("logid:").append(LogThreadLoacal.logid.get()).append("||");
			sb.append("msg:").append(msg).append("||");
			sb.append("desc:").append(t.getMessage()).append("||");

			StackTraceElement[] stackEl = t.getStackTrace();
			for(StackTraceElement stack:stackEl)
			{
				sb.append("\tat "+stack);

			}
			logger.error(sb.toString());
		}
	}

	public void error(Throwable t)
	{
		if(logger.isErrorEnabled())
		{
			StringBuilder sb = new StringBuilder();
			sb.append("logid:").append(LogThreadLoacal.logid.get()).append("||");
			sb.append("desc:").append(t.getMessage()).append("||");
			StackTraceElement[] stackEl = t.getStackTrace();
			for(StackTraceElement stack:stackEl)
			{
				sb.append("\tat "+stack);

			}
			logger.error(sb.toString());
		}
	}
}
