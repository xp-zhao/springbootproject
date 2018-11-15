package com.example.springbootproject.log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class MethodAOPLog
{
	private final static MyLogger logger = MyLogFactory.getLogger(MethodAOPLog.class);

	@Around (value="@annotation(logAnnotion)")
	public Object servicePointcut(ProceedingJoinPoint joinPoint,NeedLogAnnotation logAnnotion) throws Throwable
	{
		long starttime = System.currentTimeMillis();
		try
		{
			//返回信息----执行方法
			Object result = joinPoint.proceed();
			//耗时
			long du = System.currentTimeMillis() - starttime;
			showlog(joinPoint,logAnnotion,result,du);
			return result;
		}
		catch(Exception e)
		{
			long du = System.currentTimeMillis() - starttime;
			showlog(joinPoint,logAnnotion,"方法执行异常",du);
			throw e;
		}
	}
	
	/**
	 * 输出日志
	 * @param joinPoint
	 * @param result
	 * @param du
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 */
	private void showlog(ProceedingJoinPoint joinPoint,NeedLogAnnotation logAnnotion,Object result,long du) throws NoSuchMethodException, SecurityException
	{
		String className = joinPoint.getSignature().getDeclaringTypeName();
		SimpleLog logvo = new SimpleLog(className.substring(className.lastIndexOf(".")+1));
		logvo.setMethodName(joinPoint.getSignature().getName());
		logvo.setResult(result);
		logvo.setArgs(joinPoint.getArgs());
		logvo.setDu(du);
		if(LogAnnotionType.DEBUG == logAnnotion.logtype())
		{
			logger.debug(logvo);
		}
		else
		{
			logger.info(logvo);
		}
    }
}
