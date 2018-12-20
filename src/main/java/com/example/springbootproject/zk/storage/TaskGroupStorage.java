package com.example.springbootproject.zk.storage;

import com.example.springbootproject.zk.watcher.task.model.GroupInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xp-zhao on 2018/12/20.
 */
public enum TaskGroupStorage
{
	INSTANCE;
	private Map<String, GroupInfo>    groups = new HashMap<>();

	public void setGroup(String groupCode,GroupInfo groupInfo){
		groups.put(groupCode , groupInfo);
	}

	public void delGroup(String groupCode){
		groups.remove(groupCode);
	}

	public GroupInfo getGroup(String groupCode){
		return groups.get(groupCode);
	}

	public Map getGroups(){
		return groups;
	}
}
