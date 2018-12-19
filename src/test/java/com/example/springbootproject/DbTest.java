package com.example.springbootproject;

import com.example.springbootproject.mapper.TaskInfoMapper;
import com.example.springbootproject.model.TaskInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * Created by xp-zhao on 2018/12/19.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class DbTest
{
	@Autowired
	private TaskInfoMapper taskInfoMapper;

	@Test
	public void testSelect(){
		List<TaskInfo> taskInfos = taskInfoMapper.selectList(null);
		taskInfos.forEach(System.out::println);
	}
}
