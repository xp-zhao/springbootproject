package com.example.springbootproject.zk.storage;

import com.example.springbootproject.model.TaskInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xp-zhao on 2018/12/20.
 */
public enum TaskStorage
{
	INSTANCE;
	private Map<String, TaskInfo> map = new HashMap<>();

	public void set(String taskCode,TaskInfo taskInfo){
		map.put(taskCode , taskInfo);
	}

	public void del(String taskCode){
		map.remove(taskCode);
	}
}
