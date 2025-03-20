package edu.ustb.core.disruptor;

/**
 * @author: ljr-YingWu
 * @date: 2023/11/13 19:57
 * 事件监听器
 * 监听接口
 */
public interface EventListener<E> {

    void onEvent(E event);

    /**
     *
     * @param ex
     * @param sequence 异常执行顺序
     * @param event
     */
    void onException(Throwable ex,long sequence,E event);

}
