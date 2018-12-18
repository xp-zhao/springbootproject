package com.example.springbootproject.zk.autoconfig;

import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Created by xp-zhao on 2018/12/17.
 */
@Configuration
@EnableConfigurationProperties(ZooKeeperConfig.class)
public class ZooKeeperAutoConfig
{
	private static final Logger logger = LoggerFactory.getLogger(ZooKeeperAutoConfig.class);

	@Bean
	@ConditionalOnMissingBean(ZooKeeper.class)
	public ZooKeeper init(ZooKeeperConfig config){
		ZooKeeper result = null;
		CountDownLatch connectedSemaphore = new CountDownLatch(1);
		try
		{
			result = new ZooKeeper(config.getHost() , config.getSessiontimeout() , event -> {
				if (Watcher.Event.KeeperState.SyncConnected == event.getState()) {
					connectedSemaphore.countDown();
				}
			});
			//如果正常结束,就返回zk连接,否则异常退出
			if(connectedSemaphore.await(config.getConnectionWaitTime(), TimeUnit.SECONDS))
			{
				return result;
			}
		}
		catch (Exception e)
		{
			logger.error("连接 zk 失败");
		}

		return result;
	}
}
