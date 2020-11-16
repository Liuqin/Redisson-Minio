package org.qin.chain;

/**
 * @title: AbstractHandler
 * @decription:
 * @author: liuqin
 * @date: 2020/8/17 10:24
 */
public interface OurChainHandler {

    /**
     * 示意处理请求的方法，虽然这个示意方法是没有传入参数的
     * 但实际是可以传入参数的，根据具体需要来选择是否传递参数
     */
    public abstract void handleRequest();

    /**
     * 业务组类型
     */
    abstract String groupName();

    /**
     * 执行顺序
     */
    abstract Integer orderNumber();

    /**
     * 服务名称
     */
    String getServiceName();

    /**
     * 实例
     */
    OurChainHandler getInstance();

    /**
     * 持有后继的责任对象
     */
    ThreadLocal<OurChainHandler> successor = new ThreadLocal<>();

    /**
     * 取值方法
     */
    public default OurChainHandler getSuccessor() {
        return successor.get();
    }

    /**
     * 赋值方法，设置后继的责任对象
     */
    public default void setSuccessor(OurChainHandler successor) {
        OurChainHandler.successor.set(successor);
    }
}


