package com.dullfan.common.storage.oss;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSClientBuilder;
import com.dullfan.common.exception.ServiceException;
import com.dullfan.common.utils.StringUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * OSS文件存储引擎配置类
 */
@Component
@Data
@Slf4j
@ConfigurationProperties(prefix = "oss.config")
public class OssStorageEngineConfig {

    /**
     * 节点
     */
    private String endpoint;
    /**
     * KeyId
     */
    private String accessKeyId;
    /**
     * 密钥
     */
    private String accessKeySecret;
    /**
     * bucket名称
     */
    private String bucketName;

    /**
     * 注入OSS操作客户端对象
     */
    @Bean(destroyMethod = "shutdown")
    public OSS ossClient(){
        if(StringUtils.isAnyBlank(getEndpoint(),getAccessKeyId(),getAccessKeySecret(),getBucketName())){
            throw new ServiceException("OSS配置失败");
        }
        log.info("OSS配置成功");
        return new OSSClientBuilder().build(getEndpoint(), getAccessKeyId(), getAccessKeySecret());
    }

}
