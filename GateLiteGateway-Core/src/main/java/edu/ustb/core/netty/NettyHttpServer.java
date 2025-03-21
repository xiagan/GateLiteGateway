package edu.ustb.core.netty;

import edu.ustb.common.utils.RemotingUtil;
import edu.ustb.core.Config;
import edu.ustb.core.LifeCycle;
import edu.ustb.core.netty.processor.NettyProcessor;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.HttpServerExpectContinueHandler;
import io.netty.util.concurrent.DefaultThreadFactory;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;


import java.net.InetSocketAddress;

/**
 * @author: ljr-YingWu
 * @date: 2023/10/24 19:23
 * Netty的Server端实现
 */


// 类注解和作者信息
@Slf4j
public class NettyHttpServer implements LifeCycle {
    // 服务器配置对象，用于获取如端口号等配置信息
    private final Config config;
    // 自定义的Netty处理器接口，用于定义如何处理接收到的请求
    private final NettyProcessor nettyProcessor;
    // 服务器引导类，用于配置和启动Netty服务
    private ServerBootstrap serverBootstrap;
    // boss线程组，用于处理新的客户端连接
    private EventLoopGroup eventLoopGroupBoss;
    // worker线程组，用于处理已经建立的连接的后续操作
    @Getter
    private EventLoopGroup eventLoopGroupWoker;

    // 构造方法，用于创建Netty服务器实例
    public NettyHttpServer(Config config, NettyProcessor nettyProcessor) {
        this.config = config;
        this.nettyProcessor = nettyProcessor;
        init(); // 初始化服务器
    }

    // 初始化服务器，设置线程组和选择线程模型
    @Override
    public void init() {
        this.serverBootstrap = new ServerBootstrap();
        // 判断是否使用Epoll模型，这是Linux系统下的高性能网络通信模型
        if (useEpoll()) {
            this.eventLoopGroupBoss = new EpollEventLoopGroup(config.getEventLoopGroupBossNum(),
                    new DefaultThreadFactory("epoll-netty-boss-nio"));
            this.eventLoopGroupWoker = new EpollEventLoopGroup(config.getEventLoopGroupWokerNum(),
                    new DefaultThreadFactory("epoll-netty-woker-nio"));
        } else {
            // 否则使用默认的NIO模型
            this.eventLoopGroupBoss = new NioEventLoopGroup(config.getEventLoopGroupBossNum(),
                    new DefaultThreadFactory("default-netty-boss-nio"));
            this.eventLoopGroupWoker = new NioEventLoopGroup(config.getEventLoopGroupWokerNum(),
                    new DefaultThreadFactory("default-netty-woker-nio"));
        }
    }

    // 检测是否使用Epoll优化性能
    public boolean useEpoll() {
        return RemotingUtil.isLinuxPlatform() && Epoll.isAvailable();
    }

    // 启动Netty服务器
    @Override
    public void start() {
        // 配置服务器参数，如端口、TCP参数等
        this.serverBootstrap
                .group(eventLoopGroupBoss, eventLoopGroupWoker)
                .channel(useEpoll() ? EpollServerSocketChannel.class : NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 1024)            // TCP连接的最大队列长度
                .option(ChannelOption.SO_REUSEADDR, true)          // 允许端口重用
                .option(ChannelOption.SO_KEEPALIVE, true)          // 保持连接检测
                .childOption(ChannelOption.TCP_NODELAY, true)      // 禁用Nagle算法，适用于小数据即时传输
                .childOption(ChannelOption.SO_SNDBUF, 65535)       // 设置发送缓冲区大小
                .childOption(ChannelOption.SO_RCVBUF, 65535)       // 设置接收缓冲区大小
                .localAddress(new InetSocketAddress(config.getPort())) // 绑定监听端口
                .childHandler(new ChannelInitializer<Channel>() {   // 定义处理新连接的管道初始化逻辑
                    @Override
                    protected void initChannel(Channel ch) throws Exception {
                        // 配置管道中的处理器，如编解码器和自定义处理器
                        ch.pipeline().addLast(
                                new HttpServerCodec(), // 处理HTTP请求的编解码器
                                new HttpObjectAggregator(config.getMaxContentLength()), // 聚合HTTP请求
                                new HttpServerExpectContinueHandler(), // 处理HTTP 100 Continue请求
                                new NettyHttpServerHandler(nettyProcessor), // 自定义的处理器
                                new NettyServerConnectManagerHandler() // 连接管理处理器
                        );
                    }
                });

        // 绑定端口并启动服务，等待服务端关闭
        try {
            this.serverBootstrap.bind().sync();
            //也可以用这种方法进行netty端口监听的绑定
            //this.serverBootstrap.bind(config.getPort()).sync();
            log.info("server startup on port {}", this.config.getPort());
        } catch (Exception e) {
            throw new RuntimeException("启动服务器时发生异常", e);
        }
    }

    // 关闭Netty服务器，释放资源
    @Override
    public void shutdown() {
        if (eventLoopGroupBoss != null) {
            eventLoopGroupBoss.shutdownGracefully(); // 优雅关闭boss线程组
        }

        if (eventLoopGroupWoker != null) {
            eventLoopGroupWoker.shutdownGracefully(); // 优雅关闭worker线程组
        }
    }
}
