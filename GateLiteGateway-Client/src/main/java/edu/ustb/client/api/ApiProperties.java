package edu.ustb.client.api;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author: ljr-YingWu
 * @date: 2023/10/29 17:36
 * ApiProperties类
 * 存放环境和注册中心地址
 */


@Data
@ConfigurationProperties(prefix = "api")
public class ApiProperties {
    /**
     * 注册中心地址
     */
    private String registerAddress;
    /**
     * 环境
     */
    private String env = "dev";
    /**
     * 是否灰度发布
     */
    private boolean gray;
}
