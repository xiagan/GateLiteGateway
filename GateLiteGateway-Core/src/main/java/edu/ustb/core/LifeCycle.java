package edu.ustb.core;

/**
 * @author: ljr-YingWu
 * @date: 2023/10/23 19:57
 * LifeCycle 生命周期
 */
public interface LifeCycle {

    /**
     * 初始化
     */
    void init();

    /**
     * 启动
     */
    void start();

    /**
     * 关闭
     */
    void shutdown();
}
