package com.alex.demo.container;

import com.alex.demo.annotation.Topic;
import com.alex.demo.service.DeviceHandler;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Service
public class TopicContainer implements InitializingBean {

    @Resource
    private ApplicationContext applicationContext;

    private final Map<String, DeviceHandler> topicHandlerMap = new ConcurrentHashMap<>();

    public DeviceHandler getTopicHandler(String apiName) {
        return topicHandlerMap.get(apiName);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String, DeviceHandler> beansOfType = applicationContext.getBeansOfType(DeviceHandler.class);
        Collection<DeviceHandler> values = beansOfType.values();
        if (values.size() > 0) {
            values.forEach(deviceHandler -> {
                if (deviceHandler.getClass().isAnnotationPresent(Topic.class)) {
                    Topic topic = deviceHandler.getClass().getAnnotation(Topic.class);
                    if (!topicHandlerMap.containsKey(topic.value())) {
                        topicHandlerMap.put(topic.value(), deviceHandler);
                    }
                }
            });
        }
    }
}
