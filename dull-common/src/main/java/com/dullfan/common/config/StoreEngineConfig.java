package com.dullfan.common.config;

import com.dullfan.common.exception.ServiceException;
import com.dullfan.common.storage.StorageEngine;
import com.dullfan.common.storage.impl.FastDFSStorageEngine;
import com.dullfan.common.storage.impl.LocalStorageEngine;
import com.dullfan.common.storage.impl.OSSStorageEngine;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StoreEngineConfig {

    @Value("${storage.engine}")
    private String storageEngine;
    /**
     * 阿里OSS
     */
    private static final String OSS_ENGINE = "oss";
    /**
     * 本地存储
     */
    private static final String LOCAL_ENGINE = "local";

    /**
     * FastDFS存储
     */
    private static final String FASTDFS_ENGINE = "fastDFS";

    @Bean
    public StorageEngine storageEngine() {
        return switch (storageEngine) {
            case OSS_ENGINE -> new OSSStorageEngine();
            case LOCAL_ENGINE -> new LocalStorageEngine();
            case FASTDFS_ENGINE -> new FastDFSStorageEngine();
            default -> throw new ServiceException("未知存储引擎");
        };
    }

}
