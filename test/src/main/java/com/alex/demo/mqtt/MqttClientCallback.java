package com.alex.demo.mqtt;

import com.alex.demo.container.TopicContainer;
import com.alex.demo.service.DeviceHandler;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import javax.annotation.Resource;


@Slf4j
public class MqttClientCallback implements MqttCallbackExtended {

    @Resource
    private TopicContainer topicContainer;

    /**
     * 系统的mqtt客户端id
     */
    private final String mqttClientId;



    public MqttClientCallback(String mqttClientId) {
        this.mqttClientId = mqttClientId;
    }


    /**
     * MQTT 断开连接会执行此方法
     */
    @Override
    public void connectionLost(Throwable throwable) {
        log.error("断开了MQTT连接 ：{}", throwable.getMessage());
    }

    /**
     * publish发布成功后会执行到这里
     */
    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        log.debug("发布消息成功");
    }

    /**
     * subscribe订阅后得到的消息会执行到这里
     */
    @Override
    public void messageArrived(String topic, MqttMessage message) {

        try {
            DeviceHandler topicHandler = topicContainer.getTopicHandler(topic);
            topicHandler.handle(topic, message);
        } catch (Exception e) {
            log.error("处理消息失败" + e.getMessage());
        }
    }

    @Override
    public void connectComplete(boolean reconnect, String serverUri) {
        log.info("[MQTT] 连接成功，订阅主题...");
        MqttClientConnect clientConnect = MqttClientConnect.MQTT_CLIENTS.get(mqttClientId);
        try {
            String[] topics ={"thing/product/+/osd"};
            if (clientConnect != null) {

                if (clientConnect.isConnected()) {
                    clientConnect.sub(topics);
                } else {
                    clientConnect.setMqttClient(serverUri, null,"alex_user", "123456", true, new MqttClientCallback(mqttClientId));
                    clientConnect.sub(org.springframework.util.StringUtils.commaDelimitedListToStringArray(clientConnect.getInitTopic()));
                }
            } else {
                //重新建立连接

                String host = "tcp://myhost:3881";
                clientConnect = new MqttClientConnect();
                clientConnect.setMqttClientId("mqtt.client.id");
                clientConnect.setInitTopic("thing/product/+/osd");
                clientConnect.setMqttClient(host,
                        "mqtt.client.id",
                        "alex_user",
                        "123456",
                        true,
                        new MqttClientCallback("mqtt.client.id"));
                clientConnect.sub(topics);
            }
        } catch (MqttException e) {
            log.error("mqtt client connect sub failed, clientId: {}", mqttClientId, e);
        }
    }
}
