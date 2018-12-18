package com.example.springbootproject.zk.watcher;

import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Created by xp-zhao on 2018/12/18.
 */
public class AsyncMaster implements Watcher
{
	ZooKeeper zk;
	String    host;
	String serverId = Integer.toHexString(new Random().nextInt());
	static boolean isLeader = false;
	AsyncCallback.StringCallback masterCreateCallBack = (rc , path , ctx , name) -> {
		switch(KeeperException.Code.get(rc)){
			case CONNECTIONLOSS:
				checkMaster();
				return;
			case OK:
				isLeader = true;
				break;
			default:
				isLeader = false;
		}
		System.out.println("I'm " + (isLeader ? "" : "not") + "the leader");
	};
	AsyncCallback.DataCallback masterCheckCallBack = (rc,path,ctx,data,stat) -> {
		switch(KeeperException.Code.get(rc)){
			case CONNECTIONLOSS:
				checkMaster();
				return;
			case NONODE:
				runForMaster();
				return;
		}
	} ;
	public AsyncMaster(String host){
		this.host = host;
	}

	public void runForMaster(){
		zk.create("/master" , serverId.getBytes() , ZooDefs.Ids.OPEN_ACL_UNSAFE , CreateMode.EPHEMERAL ,
			masterCreateCallBack , null);
	}

	public void checkMaster(){
		zk.getData("/master" , false , masterCheckCallBack , null);
	}

	public void startZK() throws IOException
	{
		zk = new ZooKeeper(host , 15000 , this);
	}

	public void stopZK() throws InterruptedException
	{
		zk.close();
	}

	@Override
	public void process(WatchedEvent event)
	{
		System.out.println(event);
	}

	public static void main(String[] args) throws Exception
	{
		AsyncMaster master = new AsyncMaster("47.98.49.140:2181,47.98.49.140:2182,47.98.49.140:2183");
		master.startZK();
		master.runForMaster();
		if(isLeader){
			System.out.println("I'm the leader");
			TimeUnit.SECONDS.sleep(6);
		}else{
			System.out.println("someone else is the leader");
		}
		Init init = new Init(master.zk);
		init.bootstrap();
		master.stopZK();
	}
}
