package com.example.springbootproject;

import com.baomidou.mybatisplus.annotation.TableName;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by xp-zhao on 2018/12/19.
 */
@RunWith (SpringRunner.class)
@SpringBootTest
public class ZKTest
{
	private static final Logger logger = LoggerFactory.getLogger(ZKTest.class);

	@Autowired
	private ZooKeeper zk;

	@Test
	public void testCreate(){
		String stat = null;
		try
		{
			stat = zk.create("/controller", "hellozk".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		}
		catch (Exception e)
		{
			logger.error("创建节点出错！",e);
		}
		System.out.println("created path is -->: " + stat);
	}
}
