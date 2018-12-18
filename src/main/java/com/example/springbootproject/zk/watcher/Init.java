package com.example.springbootproject.zk.watcher;

import org.apache.zookeeper.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by xp-zhao on 2018/12/18.
 */
public class Init
{
	private static final Logger logger = LoggerFactory.getLogger(Init.class);

	ZooKeeper zk;

	public Init(ZooKeeper zk){
		this.zk = zk;
	}

	public void bootstrap(){
		createParent("/workers",new byte[0]);
		createParent("/assign",new byte[0]);
		createParent("/tasks",new byte[0]);
		createParent("/status",new byte[0]);
	}

	public void createParent(String path,byte[] data){
		zk.create(path , data , ZooDefs.Ids.OPEN_ACL_UNSAFE , CreateMode.PERSISTENT , createParentCallBack , data);
	}

	AsyncCallback.StringCallback createParentCallBack = (rc , path , ctx , name) -> {
		switch(KeeperException.Code.get(rc)){
			case CONNECTIONLOSS:
				createParent(path, (byte[]) ctx);
				break;
			case OK:
				logger.info("Parent created");
				break;
			case NODEEXISTS:
				logger.warn("Parent already registered: " + path);
				break;
			default:
				logger.error("Something went wrong: ",KeeperException.create(KeeperException.Code.get(rc),path));
		}
	};
}
