package edu.ustb.client.api;

/**
 * @author: ljr-YingWu
 * @date: 2023/10/29 17:07
 * ApiProtocol类
 *
 * 这里提供对http和dubbo的支持
 */
public enum ApiProtocol {
    HTTP("http", "http协议"),
    DUBBO("dubbo", "dubbo协议");

    private String protocol;

    private String desc;

    ApiProtocol(String protocol, String desc) {
        this.protocol = protocol;
        this.desc = desc;
    }

    public String getProtocol() {
        return protocol;
    }

    public String getDesc() {
        return desc;
    }
}
