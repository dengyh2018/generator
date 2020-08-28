-- --------------------------------------------------------
-- 主机:                           10.249.12.80
-- 服务器版本:                        5.7.24-log - Source distribution
-- 服务器OS:                        Linux
-- HeidiSQL 版本:                  10.2.0.5599
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

-- Dumping structure for table o2o_generate.config
CREATE TABLE IF NOT EXISTS `config` (
  `id` bigint(32) NOT NULL AUTO_INCREMENT,
  `datasource_id` bigint(32) DEFAULT NULL COMMENT '数据源id',
  `name` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '模块名称',
  `tables` varchar(500) COLLATE utf8_bin DEFAULT NULL COMMENT '数据表',
  `ignore_words` varchar(500) COLLATE utf8_bin DEFAULT NULL COMMENT '忽略表名部分',
  `models` varchar(500) COLLATE utf8_bin DEFAULT NULL COMMENT '实体名称',
  `xml_project` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT 'xml所在项目/精确到包路径(不包含包路径)',
  `xml_package` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT 'xml包路径',
  `model_project` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT 'entity所在项目/精确到包路径(不包含包路径)',
  `model_package` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT 'entity包路径',
  `client_project` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT 'dao所在项目/精确到包路径(不包含包路径)',
  `client_package` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT 'dao包路径',
  `service_project` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT 'service所在项目/精确到包路径(不包含包路径)',
  `service_package` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT 'service包路径',
  `creator` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updator` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='路径配置';

-- Data exporting was unselected.

-- Dumping structure for table o2o_generate.datasource
CREATE TABLE IF NOT EXISTS `datasource` (
  `id` bigint(32) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) COLLATE utf8_bin NOT NULL,
  `type` varchar(100) COLLATE utf8_bin NOT NULL,
  `url` varchar(500) COLLATE utf8_bin NOT NULL,
  `username` varchar(50) COLLATE utf8_bin NOT NULL,
  `pwd` varchar(50) COLLATE utf8_bin NOT NULL,
  `status` enum('1','0') COLLATE utf8_bin NOT NULL DEFAULT '1',
  `creator` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updator` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='数据源';

-- Data exporting was unselected.

-- Dumping structure for table o2o_generate.project
CREATE TABLE IF NOT EXISTS `project` (
  `id` bigint(32) NOT NULL AUTO_INCREMENT,
  `config_id` bigint(32) DEFAULT NULL COMMENT '模块id',
  `ip` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT 'ip地址',
  `old_address` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '原项目地址',
  `new_address` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '新项目地址',
  `creator` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updator` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='项目目录';

-- Data exporting was unselected.

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
