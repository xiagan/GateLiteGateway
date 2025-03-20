package edu.ustb.core.netty.processor;


import edu.ustb.core.context.HttpRequestWrapper;
/**
 * @author: ljr-YingWu
 * @date: 2023/10/23 12:57
 */
public interface NettyProcessor {

    void process(HttpRequestWrapper wrapper);

    void  start();

    void shutDown();
}
