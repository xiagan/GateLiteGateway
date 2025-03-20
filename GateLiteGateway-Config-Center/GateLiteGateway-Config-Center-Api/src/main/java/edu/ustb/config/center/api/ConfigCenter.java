package edu.ustb.config.center.api;





/**
 * @author: ljr-YingWu
 * @date: 2023/11/1 19:24
 * 配置中心接口方法
 */


public interface ConfigCenter {

    /**
     * 初始化配置中心配置
     * @param serverAddr  配置中心地址
     * @param env 环境
     */
    void init(String serverAddr, String env);


    /**
     * 订阅配置中心配置变更
     * @param listener  配置变更监听器
     */
    void subscribeRulesChange(RulesChangeListener listener);
}
