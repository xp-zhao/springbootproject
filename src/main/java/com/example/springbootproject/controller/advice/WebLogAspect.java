package com.example.springbootproject.controller.advice;

import com.example.springbootproject.log.LogThreadLoacal;
import com.example.springbootproject.log.MyLogFactory;
import com.example.springbootproject.log.MyLogger;
import com.example.springbootproject.log.SimpleLog;
import com.example.springbootproject.model.req.BaseRequest;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.UUID;

/**
 * Created by xp-zhao on 2018/11/15.
 */
@Aspect
@Component
public class WebLogAspect
{
	private static final MyLogger logger = MyLogFactory.getLogger(WebLogAspect.class);

	@Around (value="execution(* com.example.springbootproject.controller.*Controller.*(..))")
	public Object servicePointcut(ProceedingJoinPoint joinPoint) throws Throwable
	{
		long starttime = System.currentTimeMillis();
		setLogId(joinPoint);
		try
		{
			//返回信息----执行方法
			Object result = joinPoint.proceed();
			//耗时
			long du = System.currentTimeMillis() - starttime;
			showlog(joinPoint,result,du);
			return result;
		}
		catch(Exception e)
		{
			long du = System.currentTimeMillis() - starttime;
			showlog(joinPoint,"接口处理异常",du);
			throw e;
		}
	}
	/**
	 * 设置logid，请求中没有传入 logid，则随机生成一个
	 * @param joinPoint
	 */
	private void setLogId(ProceedingJoinPoint joinPoint)
	{
		Object[] reqObjs = joinPoint.getArgs();
		String logid = null;
		if(null!=reqObjs)
		{
			for(Object obj:reqObjs)
			{
				if(obj instanceof BaseRequest)
				{
					logid = ((BaseRequest)obj).getLogId();
					break;
				}
			}
		}
		if(StringUtils.isBlank(logid))
		{
			logid = UUID.randomUUID().toString();
		}
		LogThreadLoacal.logid.set(logid);
	}

	/**
	 * 输出日志
	 * @param joinPoint
	 * @param result
	 * @param du
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 */
	private void showlog(ProceedingJoinPoint joinPoint,Object result,long du) throws NoSuchMethodException, SecurityException
	{
//		SimpleLog simpleLog = new SimpleLog("controll");
		String className = joinPoint.getSignature().getDeclaringTypeName();
		SimpleLog simpleLog = new SimpleLog(className.substring(className.lastIndexOf(".")+1));
		simpleLog.setMethodName(((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getServletPath());
		simpleLog.setResult(result);
		simpleLog.setArgs(joinPoint.getArgs());
		simpleLog.setDu(du);
		logger.info(simpleLog);
	}
}
