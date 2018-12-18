package com.example.springbootproject.zk.autoconfig;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by xp-zhao on 2018/12/17.
 */
@Data
@ConfigurationProperties (prefix = "spring.zookeeper")
public class ZooKeeperConfig
{
	/**
	 * zk连接地址
	 */
	private String host;
	/**
	 * zk连接过期时间
	 */
	private int sessiontimeout;
	/**
	 * 连接zookeeper时的超时时长,单位为秒，默认为30秒
	 */
	private int connectionWaitTime=30;
}
