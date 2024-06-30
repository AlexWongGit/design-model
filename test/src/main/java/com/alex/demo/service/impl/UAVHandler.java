package com.alex.demo.service.impl;

import com.alex.demo.annotation.Topic;
import com.alex.demo.service.DeviceHandler;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.stereotype.Service;

@Service
@Topic("thing/product/1/osd")
public class UAVHandler implements DeviceHandler {
    @Override
    public void handle(String topic, MqttMessage message) {

    }
}
