package com.alex.demo.mqtt;


import ch.qos.logback.classic.util.LogbackMDCAdapter;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Slf4j
@Component
public class MqttClientConnect {

    public static final ConcurrentMap<String, MqttClientConnect> MQTT_CLIENTS = new ConcurrentHashMap<>();

    private MqttClient mqttClient;

    /*  *
     * 系统的mqtt客户端id
     */
    private String mqttClientId;

    private String initTopic;


    /* *
     * 客户端connect连接mqtt服务器
     *
     * @param clientId     客户端id，环境变量-客户端id-4位随机数
     * @param userName     用户名
     * @param passWord     密码
     * @param mqttCallback 回调函数
     **/
    public void setMqttClient(String host, String clientId, String userName, String passWord, boolean cleanSession, MqttCallback mqttCallback) throws MqttException {
        MqttConnectOptions options = mqttConnectOptions(host, clientId, userName, passWord, cleanSession);
        mqttClient.setCallback(Objects.requireNonNullElseGet(mqttCallback, () -> new MqttClientCallback(mqttClientId)));
        mqttClient.connect(options);
    }

    /*    *
     * 判断是否连接成功
     **/
    public boolean isConnected() throws MqttException {
        return mqttClient.isConnected();
    }

    /*
     *
     * MQTT连接参数设置
     * <p>
     * MqttConnectOptions可用于覆盖默认连接选项
     * MqttAsyncClient
     */

    private MqttConnectOptions mqttConnectOptions(String host, String clientId, String userName, String passWord, boolean cleanSession) throws MqttException {
        //
        mqttClient = new MqttClient(host, clientId, new MemoryPersistence());
        MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName(userName);
        options.setPassword(passWord.toCharArray());
        ///默认：30
        options.setConnectionTimeout(5);
        //默认：false
        options.setAutomaticReconnect(true);
        options.setMaxReconnectDelay(5);
        //默认：true
/*
            false：
               即使网络、客户端或服务器出现故障，消息传递也将与发布消息时指定的服务质量相匹配
               当客户端未连接时，服务器将代表客户端存储活动订阅的消息。服务器将在下次连接时将这些消息传递给客户端。
            true:
               存储在客户端和与客户端相关的服务器上的任何状态都将在连接完全启动之前被清除。先前会话的订阅将被取消订阅，并且先前会话中仍在进行中的任何消息都将被删除。
               当客户端由于应用程序请求断开连接或网络故障而断开连接时，与客户端相关的状态将被清除，就像在连接时一样。
               如果在传递消息时保持连接，则仅将消息传递到发布时请求的服务质量*/

        options.setCleanSession(cleanSession);
        //设置保活间隔
        //默认：60
        options.setKeepAliveInterval(60);
        //订立遗嘱
        //options.setWill();
        return options;
    }

    /*
     *
     * 关闭MQTT连接
     */

    public void close() throws MqttException {
        log.info("关闭MQTT连接,{}", mqttClient.getClientId());
        mqttClient.disconnect();
        mqttClient.close();
    }

    /*    *
     * 向某个主题发布消息 默认qos：1
     *
     * @param topic:发布的主题
     * @param msg：发布的消息*/

    public void pub(String topic, String msg) throws MqttException {
        MqttMessage mqttMessage = new MqttMessage();
        mqttMessage.setPayload(msg.getBytes());
        MqttTopic mqttTopic = mqttClient.getTopic(topic);
        MqttDeliveryToken token = mqttTopic.publish(mqttMessage);
        //MqttAsyncClient它提供了一个非阻塞接口，其中方法在请求的操作完成之前返回。waitForCompletion阻塞直到操作完成。
        token.waitForCompletion();
    }

    /*    *
     * 向某个主题发布消息
     *
     * @param topic: 发布的主题
     * @param msg:   发布的消息
     * @param qos:   消息质量    Qos：0、1、2*/

    public void pub(String topic, String msg, int qos) throws MqttException {
        MqttMessage mqttMessage = new MqttMessage();
        mqttMessage.setQos(qos);
        mqttMessage.setPayload(msg.getBytes());
        MqttTopic mqttTopic = mqttClient.getTopic(topic);
        MqttDeliveryToken token = mqttTopic.publish(mqttMessage);
        token.waitForCompletion();
    }

    /*    *
     * 订阅多个主题 ，此方法默认的的Qos等级为：1
     *
     * @param topic 主题*/

    public void sub(String[] topic) throws MqttException {
        mqttClient.subscribe(topic);
    }

    /*
     *
     * 订阅某一个主题，可携带Qos
     *
     * @param topic 所要订阅的主题
     * @param qos   消息质量：0、1、2
     */

    public void sub(String topic, int qos) throws MqttException {
        mqttClient.subscribe(topic, qos);
    }

    public String getMqttClientId() {
        return mqttClientId;
    }

    public void setMqttClientId(String mqttClientId) {
        this.mqttClientId = mqttClientId;
    }

    public String getInitTopic() {
        return initTopic;
    }

    public void setInitTopic(String initTopic) {
        this.initTopic = initTopic;
    }
}
