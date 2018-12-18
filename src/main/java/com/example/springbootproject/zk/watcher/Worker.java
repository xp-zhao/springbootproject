package com.example.springbootproject.zk.watcher;

import org.apache.zookeeper.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Created by xp-zhao on 2018/12/18.
 */
public class Worker implements Watcher
{
	private static final Logger logger = LoggerFactory.getLogger(Worker.class);
	ZooKeeper zk;
	String    host;
	String serverId = Integer.toHexString(new Random().nextInt());
	String status;
	String name;

	public Worker(String host){
		this.host = host;
	}

	public void startZK() throws IOException
	{
		zk = new ZooKeeper(host , 15000 , this);
	}

	@Override 
	public void process(WatchedEvent event)
	{
		logger.info(event.toString()+","+host);
	}

	public void register(){
		zk.create("/workers/worker-" + serverId , "Idle".getBytes() , ZooDefs.Ids.OPEN_ACL_UNSAFE ,
			CreateMode.EPHEMERAL , createWorkerCallBack , null);
	}

	AsyncCallback.StringCallback createWorkerCallBack = (rc,path,ctx,name) -> {
		switch(KeeperException.Code.get(rc)){
			case CONNECTIONLOSS:
				register();
				break;
			case OK:
				logger.info("Registered successfully: "+ serverId);
				break;
			case NODEEXISTS:
				logger.warn("Already registered: "+serverId);
				break;
			default:
				logger.error("Something went wrong: ",KeeperException.create(KeeperException.Code.get(rc),path));
		}
	};

	AsyncCallback.StatCallback statusUpdateCallBack = (rc,path,ctx,stat) -> {
		switch(KeeperException.Code.get(rc)){
			case CONNECTIONLOSS:

		}
	};

	synchronized private void updateStatus(String status){
		if(status == this.status){
			zk.setData("/workers/" + name , status.getBytes() , -1 , statusUpdateCallBack , status);
		}
	}

	public void setStatus(String status){
		this.status = status;
		updateStatus(status);
	}

	public static void main(String[] args) throws Exception
	{
		Worker worker = new Worker("47.98.49.140:2181,47.98.49.140:2182,47.98.49.140:2183");
		worker.startZK();
		worker.register();
		TimeUnit.SECONDS.sleep(30);
	}
}
