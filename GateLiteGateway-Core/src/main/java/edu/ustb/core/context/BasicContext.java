package edu.ustb.core.context;

import edu.ustb.common.config.Rule;
import io.netty.channel.ChannelHandlerContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;


/**
 * @author: ljr-YingWu
 * @date: 2023/10/23 19:57
 * Test类
 */
public class BasicContext implements IContext{

    /**
     * 转发协议
     */
    protected final String protocol;

    /**
     * 上下文状态
     */
    protected volatile int status  = RUNNING;
    /**
     * Netty上下文
     */
    protected final ChannelHandlerContext nettyCtx;

    /**
     * 上下文参数集合
     */
    protected  final Map<String,Object> attributes = new HashMap<String,Object>();

    /**
     * 请求过程中发生的异常
     */
    protected Throwable throwable;
    /**
     * 是否保持长连接
     */
    protected final boolean keepAlive;

    /**
     * 是否已经释放资源
     */
    protected final AtomicBoolean requestReleased = new AtomicBoolean(false);
    /**
     * 存放回调函数的集合
     */
    protected List<Consumer<IContext>> completedCallbacks;

    /**
     * 构造函数
     * @param protocol
     * @param nettyCtx
     * @param keepAlive
     */
    public BasicContext(String protocol, ChannelHandlerContext nettyCtx, boolean keepAlive) {
        this.protocol = protocol;
        this.nettyCtx = nettyCtx;
        this.keepAlive = keepAlive;
    }


    @Override
    public void running() {
        status = RUNNING;
    }

    @Override
    public void written() {
        status = WRITTEN;
    }

    @Override
    public void completed() {
        status = COMPLETED;
    }

    @Override
    public void terminated() {
        status = TERMINATED;
    }

    @Override
    public boolean isRunning() {
        return status == RUNNING;
    }

    @Override
    public boolean isWritten() {
        return  status == WRITTEN;
    }

    @Override
    public boolean isCompleted() {
        return status == COMPLETED;
    }

    @Override
    public boolean isTerminated() {
        return status == TERMINATED;
    }

    @Override
    public String getProtocol() {
        return this.protocol;
    }

    @Override
    public Rule getRule() {
        return null;
    }

    @Override
    public Object getRequest() {
        return null;
    }

    @Override
    public Object getResponse() {
        return null;
    }

    @Override
    public Throwable getThrowable() {
        return this.throwable;
    }

    @Override
    public Object getAttribute(Map<String, Object> key) {
        return attributes.get(key);
    }

    @Override
    public void setRule() {

    }

    @Override
    public void setResponse() {

    }

    @Override
    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

    @Override
    public void setAttribute(String key,Object obj) {
        attributes.put(key,obj);
    }

    @Override
    public ChannelHandlerContext getNettyCtx() {
        return this.nettyCtx;
    }

    @Override
    public boolean isKeepAlive() {
        return this.keepAlive;
    }

    @Override
    public void releaseRequest() {
        this.requestReleased.compareAndSet(false,true);
    }

    @Override
    public void setCompletedCallBack(Consumer<IContext> consumer) {
         if(completedCallbacks == null){
             completedCallbacks = new ArrayList<>();
         }
        completedCallbacks.add(consumer);
    }

    @Override
    public void invokeCompletedCallBack() {
        if(completedCallbacks == null){
            completedCallbacks.forEach(call->call.accept(this));
        }
    }
}
