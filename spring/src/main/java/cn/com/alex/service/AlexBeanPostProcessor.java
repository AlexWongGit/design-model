package cn.com.alex.service;

import cn.com.alex.spring.BeanPostProcessor;
import cn.com.alex.spring.Component;

@Component
public class AlexBeanPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        return BeanPostProcessor.super.postProcessBeforeInitialization(bean, beanName);
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }
}
