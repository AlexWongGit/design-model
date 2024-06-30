package com.alex.demo.service;

import org.eclipse.paho.client.mqttv3.MqttMessage;

public interface DeviceHandler {

    void handle( String topic, MqttMessage message);
}
