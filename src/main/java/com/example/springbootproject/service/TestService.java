package com.example.springbootproject.service;

import com.example.springbootproject.log.NeedLogAnnotation;
import org.springframework.stereotype.Service;

/**
 * Created by xp-zhao on 2018/11/15.
 */
@Service
public class TestService
{
	@NeedLogAnnotation
	public String get(String id,String name){
		return id +"-"+ name;
	}
}
