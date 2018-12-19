package com.example.springbootproject.zk.watcher.task;

import com.example.springbootproject.utils.ZookeeperServer;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by xp-zhao on 2018/12/19.
 */
public class RootWatcher implements Watcher
{
	private static final Logger logger = LoggerFactory.getLogger(RootWatcher.class);

	private ZookeeperServer zkServer;

	public RootWatcher(ZookeeperServer zkServer){
		this.zkServer = zkServer;
	}

	@Override
	public void process(WatchedEvent event)
	{
		try
		{
			if(event.getState() == Event.KeeperState.SyncConnected){
				if(event.getType() == Event.EventType.NodeCreated){
					logger.info("Node( " + event.getPath() + " ) Created");
					zkServer.exists(event.getPath() , this);
				}else if(event.getType() == Event.EventType.NodeDeleted){
					logger.info("Node( " + event.getPath() + " ) Deleted");
					zkServer.exists(event.getPath() , this);
				}else if(event.getType() == Event.EventType.NodeDataChanged){
					logger.info("Node( " + event.getPath() + " ) DataChanged");
					zkServer.exists(event.getPath() , this);
				}else if(event.getType() == Event.EventType.NodeChildrenChanged){
					logger.info("Node( " + event.getPath() + " ) Created");
					zkServer.exists(TaskConstants.TASK_ROOTPATH , this);
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
