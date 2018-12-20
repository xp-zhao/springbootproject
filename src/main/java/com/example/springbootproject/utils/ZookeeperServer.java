package com.example.springbootproject.utils;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.example.springbootproject.model.TaskInfo;
import com.example.springbootproject.zk.storage.TaskGroupStorage;
import com.example.springbootproject.zk.storage.TaskStorage;
import com.example.springbootproject.zk.watcher.task.GroupWatcher;
import com.example.springbootproject.zk.watcher.task.RootWatcher;
import com.example.springbootproject.zk.watcher.task.TaskConstants;
import com.example.springbootproject.zk.watcher.task.TaskWatcher;
import com.example.springbootproject.zk.watcher.task.model.GroupInfo;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by xp-zhao on 2018/12/19.
 */
@Service
public class ZookeeperServer
{
	private static final Logger logger = LoggerFactory.getLogger(ZookeeperServer.class);

	@Autowired
	private ZooKeeper zk;

	public void initWatcher() throws KeeperException, InterruptedException
	{
//		Stat stat = zk.exists(TaskConstants.TASK_ROOTPATH , new RootWatcher(this));
//
//		if(null == stat){
//			createNode(TaskConstants.TASK_ROOTPATH , "" , CreateMode.PERSISTENT);
//		}
		List<String> groups = zk.getChildren(TaskConstants.TASK_ROOTPATH , new RootWatcher(this));
		logger.info("当前所有任务组："+groups);
		for(String group : groups)
		{
			// 为所有任务组添加监听
			List<String> tasks = zk.getChildren(TaskConstants.TASK_ROOTPATH + "/" + group , new GroupWatcher(this));
			for(String task : tasks)
			{
				// 为每个任务添加监听
				String taskInfoData = getData(TaskConstants.TASK_ROOTPATH + "/" + group + "/" + task , new TaskWatcher(this) ,
					null);
				TaskInfo taskInfo = JSON.parseObject(taskInfoData , TaskInfo.class);
				// 保存已经监听了的任务
				TaskStorage.INSTANCE.set(group+"|"+task , taskInfo);
			}
			GroupInfo groupInfo = new GroupInfo();
			groupInfo.setGroupCode(group);
			groupInfo.setTaksCode(tasks);
			// 保存已经添加了监听的任务组
			TaskGroupStorage.INSTANCE.setGroup(group,groupInfo);
		}
	}

	/**
	 * 删除指定的节点信息
	 * @param path
	 * @throws InterruptedException
	 * @throws KeeperException
	 */
	public void delNode(String path,boolean delChildren) throws InterruptedException, KeeperException
	{
		if(this.exists(path, false))
		{
			List<String> childrens = this.getChildren(path, null);
			if(CollectionUtils.isNotEmpty(childrens) && delChildren)
			{
				for(String children:childrens)
				{
					zk.delete(path+"/"+children, -1);
				}
			}
			zk.delete(path, -1);
		}
	}
	/**
	 * 删除指定节点下面的所有子节点
	 * @param path
	 * @throws InterruptedException
	 * @throws KeeperException
	 */
	public void delChildrensNodes(String path)  throws InterruptedException, KeeperException
	{
		if(this.exists(path, false))
		{
			List<String> childrens = this.getChildren(path, null);
			for(String children:childrens)
			{
				zk.delete(path+"/"+children, -1);
			}
		}
	}

	/**
	 * 获取指定节点数据内容
	 * @param path
	 * @param watcher
	 * @param stat
	 * @return
	 * @throws KeeperException
	 * @throws InterruptedException
	 */
	public String getData(String path,Watcher watcher,Stat stat) throws KeeperException, InterruptedException
	{
		return new String(zk.getData(path, watcher, stat));
	}

	/**
	 * 获取指定节点下面的子节点信息
	 * @param path
	 * @return
	 * @throws KeeperException
	 * @throws InterruptedException
	 */
	public List<String> getChildren(String path,Watcher watcher) throws KeeperException, InterruptedException
	{
		return zk.getChildren(path, watcher);
	}

	/**
	 * 更新节点数据
	 * @param path
	 * @param data
	 * @throws KeeperException
	 * @throws InterruptedException
	 */
	public void updateNodeData(String path,String data) throws KeeperException, InterruptedException
	{
		zk.setData(path, null!=data?data.getBytes():null, -1);
	}

	/**
	 * 创建一个持久节点
	 * @param path 节点路径
	 * @param data 节点内容
	 * @return
	 * @throws InterruptedException
	 * @throws KeeperException
	 */
	public String createPersistentNode(String path,String data) throws KeeperException, InterruptedException
	{
		return createNode(path,data,CreateMode.PERSISTENT);
	}

	/**
	 * 创建临时节点
	 * @param path
	 * @param data
	 * @return
	 * @throws KeeperException
	 * @throws InterruptedException
	 */
	public String createEphemeralNode(String path,String data) throws KeeperException, InterruptedException
	{
		return createNode(path , data , CreateMode.EPHEMERAL);
	}

	/**
	 * 创建节点
	 * @param path
	 * @param data
	 * @param createMode
	 * @return
	 * @throws KeeperException
	 * @throws InterruptedException
	 */
	public String createNode(String path,String data,CreateMode createMode)
		throws KeeperException, InterruptedException
	{
		return zk.create(path , data.getBytes() , ZooDefs.Ids.OPEN_ACL_UNSAFE , createMode);
	}

	/**
	 * 判断该节点是否存在
	 * @param path
	 * @return
	 * @throws KeeperException
	 * @throws InterruptedException
	 */
	public boolean exists(String path,boolean watch) throws KeeperException, InterruptedException
	{
		return null != zk.exists(path, watch);
	}

	/**
	 * 判断该节点是否存在
	 * @param path
	 * @return
	 * @throws KeeperException
	 * @throws InterruptedException
	 */
	public boolean exists(String path,Watcher watch) throws KeeperException, InterruptedException
	{
		return null != zk.exists(path,watch);
	}
}
