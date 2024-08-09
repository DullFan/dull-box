package com.dullfan.common.storage.fastdfs;

import com.alibaba.fastjson2.JSONObject;
import com.dullfan.common.exception.ServiceException;
import com.github.tobato.fastdfs.domain.conn.ConnectionPoolConfig;
import com.github.tobato.fastdfs.domain.conn.FdfsConnectionPool;
import com.github.tobato.fastdfs.domain.conn.PooledConnectionFactory;
import com.github.tobato.fastdfs.domain.conn.TrackerConnectionManager;
import com.google.common.collect.Lists;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.jmx.support.RegistrationPolicy;

import java.util.List;

@SpringBootConfiguration
@Data
@EnableMBeanExport(registration = RegistrationPolicy.IGNORE_EXISTING)
@ConfigurationProperties(prefix = "fastdfs.config")
@ComponentScan(value = {"com.github.tobato.fastdfs.service","com.github.tobato.fastdfs.domain"})
@Slf4j
public class FastDFSStorageEngineConfig {

    /**
     * 连接超时时间
     */
    private Integer connectTimeout = 600;

    /**
     * 跟踪服务器地址列表
     */
    private List<String> trackerList = Lists.newArrayList();

    /**
     * 组名称
     */
    private String group = "group1";

    @Bean
    public PooledConnectionFactory pooledConnectionFactory(){
        PooledConnectionFactory pooledConnectionFactory = new PooledConnectionFactory();
        pooledConnectionFactory.setConnectTimeout(getConnectTimeout());
        return pooledConnectionFactory;
    }

    @Bean
    public ConnectionPoolConfig connectionPoolConfig(){
        return new ConnectionPoolConfig();
    }

    @Bean
    public FdfsConnectionPool fdfsConnectionPool(ConnectionPoolConfig config,PooledConnectionFactory factory){
        return new FdfsConnectionPool(factory,config);
    }

    @Bean
    public TrackerConnectionManager trackerConnectionManager(FdfsConnectionPool fdfsConnectionPool){
        TrackerConnectionManager trackerConnectionManager = new TrackerConnectionManager(fdfsConnectionPool);
        if(CollectionUtils.isEmpty(getTrackerList())){
            log.error("TrackerConnectionManager加载失败");
            return null;
        }
        trackerConnectionManager.setTrackerList(getTrackerList());
        log.info("TrackerConnectionManager加载成功!");
        return trackerConnectionManager;
    }
}
