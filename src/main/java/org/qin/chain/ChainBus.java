package org.qin.chain;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * @title: chainBus
 * @decription:
 * @author: liuqin
 * @date: 2020/8/17 14:07
 */

@Component
public class ChainBus implements ApplicationContextAware {

    private static ApplicationContext applicationContext;
    private static HashMap<ChainHandlerKey, OurChainHandler> ourChainHandlerList;


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ChainBus.applicationContext = applicationContext;
        ChainBus.getList();
    }


    public static HashMap<ChainHandlerKey, OurChainHandler> getList() {
        System.out.println("系统开始获取所有实现责任链模式接口的服务:");
        Collection<OurChainHandler> chainHandlerList = new LinkedList<>(applicationContext.getBeansOfType(OurChainHandler.class).values());
        ChainBus.ourChainHandlerList = new HashMap<>();

        for (OurChainHandler ourChainHandler : chainHandlerList) {
            System.out.println(ourChainHandler.getClass().getName());
            ChainHandlerKey chainHandlerKey = new ChainHandlerKey();
            chainHandlerKey.setGroupName(ourChainHandler.groupName());

            chainHandlerKey.setOrderNumber(ourChainHandler.orderNumber());
            chainHandlerKey.setServiceName(ourChainHandler.getServiceName());
            ChainBus.ourChainHandlerList.put(chainHandlerKey, ourChainHandler.getInstance());
        }

        return ChainBus.ourChainHandlerList;
    }


    public static void CreateChain() {

        List<String> groupList = new LinkedList<>();



        for (Object key : ChainBus.ourChainHandlerList.keySet()) {
            OurChainHandler ourChainHandler = ChainBus.ourChainHandlerList.get(key);
            if (groupList.size() == 0 || !groupList.contains(ourChainHandler.groupName())) {
                groupList.add(ourChainHandler.groupName());
            }
        }
        // 获取不同的分组
        if (groupList.size() > 0) {
            for (String groupStr : groupList) {
                List<OurChainHandler> list = new LinkedList<>();

                // 复杂查询语句
              //  LinkedHashMap<ChainHandlerKey, OurChainHandler> collect = ChainBus.ourChainHandlerList.entrySet().stream().filter(x -> x.getKey().groupName == groupStr).sorted(Comparator.comparingInt(k -> k.getKey().orderNumber)).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));

                for (Object key : ourChainHandlerList.keySet()) {
                    OurChainHandler ourChainHandler = ourChainHandlerList.get(key);
                    if (ourChainHandler.groupName() == groupStr) {
                        list.add(ourChainHandler);
                    }
                }
                list.sort((o1, o2) -> o1.orderNumber() - o2.orderNumber() > 0 ? 1 : 0);
                for (int i = 0; i < list.size() - 1; i++) {
                    if (list.get(i) != null && list.get(i + 1) != null) {
                        // 找到职责链的后一个节点
                        list.get(i).setSuccessor(list.get(i + 1));
                    }
                }
            }
        }

    }


    static class ChainHandlerKey {

        private String serviceName;
        private Integer orderNumber;
        private String groupName;


        public String getServiceName() {
            return serviceName;
        }


        public void setServiceName(String serviceName) {
            this.serviceName = serviceName;
        }


        public Integer getOrderNumber() {
            return orderNumber;
        }


        public void setOrderNumber(Integer orderNumber) {
            this.orderNumber = orderNumber;
        }


        public String getGroupName() {
            return groupName;
        }


        public void setGroupName(String groupName) {
            this.groupName = groupName;
        }

    }
}