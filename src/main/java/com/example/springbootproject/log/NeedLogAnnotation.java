package com.example.springbootproject.log;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于日志输出的标签,只有加在受Spring管理类的方法上才有效果
 * @author carryx
 *
 */
@Target({ElementType.METHOD})  
@Retention(RetentionPolicy.RUNTIME)
public @interface NeedLogAnnotation 
{
	LogAnnotionType logtype() default LogAnnotionType.INFO;
}
