package edu.ustb.core;

import edu.ustb.core.netty.NettyHttpClient;
import edu.ustb.core.netty.NettyHttpServer;
import edu.ustb.core.netty.processor.DisruptorNettyCoreProcessor;
import edu.ustb.core.netty.processor.NettyCoreProcessor;
import edu.ustb.core.netty.processor.NettyProcessor;
import lombok.extern.slf4j.Slf4j;

import static edu.ustb.common.constant.GatewayConst.BUFFER_TYPE_PARALLEL;

/**
 * @author: ljr-YingWu
 * @date: 2023/10/23 19:57
 */
@Slf4j
public class Container implements LifeCycle {
    private final Config config;

    private NettyHttpServer nettyHttpServer;

    private NettyHttpClient nettyHttpClient;

    private NettyProcessor nettyProcessor;

    public Container(Config config) {
        this.config = config;
        init();
    }

    @Override
    public void init() {

        NettyCoreProcessor nettyCoreProcessor = new NettyCoreProcessor();
        //如果启动要使用多生产者多消费组 那么我们读取配置
        if (BUFFER_TYPE_PARALLEL.equals(config.getParallelBufferType())) {
            //开启配置的情况下使用Disruptor
            this.nettyProcessor = new DisruptorNettyCoreProcessor(config, nettyCoreProcessor);
        } else {
            this.nettyProcessor = nettyCoreProcessor;
        }

        this.nettyHttpServer = new NettyHttpServer(config, nettyProcessor);

        this.nettyHttpClient = new NettyHttpClient(config,
                nettyHttpServer.getEventLoopGroupWoker());
    }

    @Override
    public void start() {
        nettyProcessor.start();
        nettyHttpServer.start();
        nettyHttpClient.start();
        log.info("api gateway started!");
    }

    @Override
    public void shutdown() {
        nettyProcessor.shutDown();
        nettyHttpServer.shutdown();
        nettyHttpClient.shutdown();
    }
}
