package com.dullfan.common.config;

import com.dullfan.common.constant.Constants;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.swing.*;

@Component
@Data
@ConfigurationProperties(prefix = "box.config")
public class BoxServiceConfig {

    /**
     * 文件分片的过期天数
     */
    private Integer chunkFileExpirationDays = Constants.ONE_INT;

    /**
     * 分享链接的前缀
     */
    private String sharePrefix = "http://127.0.0.1:5173/share/";

}
