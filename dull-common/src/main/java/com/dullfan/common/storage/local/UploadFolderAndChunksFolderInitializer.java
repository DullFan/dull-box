package com.dullfan.common.storage.local;

import com.dullfan.common.config.DullConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * 初始化上传文件根目录和文件分片存储根目录初始化
 */
@Component
@Slf4j
public class UploadFolderAndChunksFolderInitializer implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        FileUtils.forceMkdir(new File(DullConfig.getUploadPath()));
        FileUtils.forceMkdir(new File(DullConfig.getUploadStoreChunkPath()));

        log.info("LocalStorage初始化完成");
    }

}
