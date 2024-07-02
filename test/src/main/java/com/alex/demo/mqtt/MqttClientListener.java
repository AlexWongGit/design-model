package com.alex.demo.mqtt;


import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
public class MqttClientListener implements ApplicationListener<ContextRefreshedEvent> {

    private final AtomicBoolean isInit = new AtomicBoolean(false);
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        try {
            //防止重复触发
            if (!isInit.compareAndSet(false, true)) {
                return;
            }
            log.info("初始化MQTT 连接…………………………………………start");
            try {
                MqttClientConnect mqttClientConnect = new MqttClientConnect();
                mqttClientConnect.setMqttClientId("mqtt.client.id");
                String host = "tcp://" + "url" + ":" + "1833";
                mqttClientConnect.setInitTopic("thing/product/1/osd");
                mqttClientConnect.setMqttClient(host,
                        "mqttx_8f2cae71",
                        "alex_user",
                        "123456",
                        true,
                        new MqttClientCallback("mqttx_8f2cae71"));
                MqttClientConnect.MQTT_CLIENTS.put("mqttx_8f2cae71", mqttClientConnect);
            } catch (Exception e) {
                log.error("初始化MQTT异常：{}",e.getMessage());
            }
            log.info("成功连接mqtt");
            log.info("初始化MQTT 连接…………………………………………end");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

    }
}
