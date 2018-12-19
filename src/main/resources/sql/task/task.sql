CREATE TABLE `task_group` (
  `groupcode` varchar(100) NOT NULL COMMENT '定时任务组代码',
  `groupname` varchar(200) NOT NULL COMMENT '定时任务组名称',
  `groupdesc` varchar(512) DEFAULT NULL COMMENT '定时任务组描榜',
  `createuid` varchar(100) NOT NULL COMMENT '定时任务创建者',
  `createtime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '定时任务组创建时间',
  PRIMARY KEY (`groupcode`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `task_taskinfo` (
  `groupCode` varchar(100) NOT NULL COMMENT '定时任务组代码',
  `taskCode` varchar(100) NOT NULL COMMENT '定时任务代码',
  `taskName` varchar(200) NOT NULL COMMENT '定时任务名称',
  `status` varchar(1) NOT NULL DEFAULT '0' COMMENT '定时任务状态0暂停，1等待执行，2执行中',
  `taskDesc` varchar(250) DEFAULT NULL COMMENT '定时任务描述',
  `cronExpression` varchar(100) NOT NULL COMMENT '任务执行策略,定时任务执行的时间 quartz时间表达式:秒 分 时 日 月 周 年',
  `allowConcurrent` varchar(10) NOT NULL DEFAULT 'true' COMMENT '是否可以重复执行:true:前一次未执行完,时间到达后再次触发执行,false:前一次未执行完,时间到达后不触发执行',
  `scheduleType` varchar(1) NOT NULL DEFAULT '1' COMMENT '策略类型 1:通过mq进行通知',
  `createUid` varchar(100) NOT NULL COMMENT '定时任务创建者',
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '定时任务创建时间',
  PRIMARY KEY (`taskCode`),
  KEY `groupCode` (`groupCode`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
