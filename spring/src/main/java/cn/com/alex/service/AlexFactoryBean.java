package cn.com.alex.service;

import cn.com.alex.spring.Component;
import org.springframework.beans.factory.FactoryBean;

@Component
public class AlexFactoryBean implements FactoryBean {

    @Override
    public Object getObject() throws Exception {
        return new SimpleBean();
    }

    @Override
    public Class<?> getObjectType() {
        return null;
    }
}
