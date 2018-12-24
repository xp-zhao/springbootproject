package com.example.springbootproject;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.Condition;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import com.baomidou.mybatisplus.extension.api.R;
import com.example.springbootproject.mapper.TaskGroupMapper;
import com.example.springbootproject.mapper.TaskInfoMapper;
import com.example.springbootproject.model.TaskGroup;
import com.example.springbootproject.model.TaskInfo;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by xp-zhao on 2018/12/19.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class DbTest
{
	private static final Logger logger = LoggerFactory.getLogger(DbTest.class);

	@Autowired
	private TaskInfoMapper taskInfoMapper;
	@Autowired
	private TaskGroupMapper taskGroupMapper;
	@Autowired
	private ZooKeeper zk;

	@Test
	public void testSelect(){
		QueryWrapper wrapper = new QueryWrapper();
		wrapper.eq("status" , 1);
		wrapper.eq("allowConcurrent" , "true");
//		List<TaskInfo> taskInfos = taskInfoMapper.selectList(wrapper);
		List<TaskInfo> taskInfos = taskInfoMapper.selectList(null);
		Map<String, List<TaskInfo>> taskByGroup = taskInfos.stream()
			.collect(Collectors.groupingBy(TaskInfo::getGroupCode));
		List<String> collect = taskInfos.stream().map(TaskInfo::getGroupCode).distinct().collect(Collectors.toList());
		List<TaskGroup> taskGroups = taskGroupMapper.selectList(null);
//		noExistsAndCreate("/path");
		// 创建任务组节点
//		for(TaskGroup taskGroup : taskGroups)
//		{
//			try {
//				zk.create("/task/"+taskGroup.getGroupcode(), JSON.toJSONString(taskGroup).getBytes(),
//					ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
//			}
//			catch (Exception e)
//			{
//				logger.error("创建节点异常！",e);
//			}
//		}
		// 创建任务节点
		for(TaskInfo taskInfo : taskInfos)
		{
			try {
				String result = zk.create("/task/"+taskInfo.getGroupCode()+"/"+taskInfo.getTaskCode(), JSON.toJSONString(taskInfo).getBytes(),
					ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
				System.out.println("创建节点成功，" + result);
			}
			catch (Exception e)
			{
				logger.error("创建节点异常！",e);
			}
		}
//		for(TaskInfo taskInfo : taskInfos)
//		{
//			try
//			{
//				zk.create("/task/"+taskInfo.getGroupCode()+"/"+taskInfo.getTaskCode(),"".getBytes(),
//					ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
//			}
//			catch (Exception e)
//			{
//				logger.error("创建节点异常！",e);
//			}
//		}
		taskInfos.forEach(System.out::println);
	}

	public void noExistsAndCreate(String path){
		zk.exists(path , watcher , existsCallBack , null);
	}

	Watcher watcher = (event) -> {

	};

	AsyncCallback.StatCallback existsCallBack = (rc , path , ctx , stat) -> {
		switch(KeeperException.Code.get(rc)){
			case NONODE:
				createNode(path,"");
				break;
		}
	};

	public void createNode(String path,String data){
		try {
			String result = zk.create(path, data.getBytes(),
				ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
			System.out.println("创建节点成功，" + result);
		}
		catch (Exception e)
		{
			logger.error("创建节点异常！",e);
		}
	}
}
