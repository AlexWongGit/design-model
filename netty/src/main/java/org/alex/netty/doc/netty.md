# Netty
## 概述
+ Netty 是一个异步事件驱动的网络通信框架，它提供了一种基于事件驱动的、非阻塞的、高性能的IO通信模型。
+ Netty 提供了多种传输协议，包括TCP、UDP、HTTP、HTTPS、FTP等。Netty 提供了多种编解码器，包括JSON、XML、Protobuf、Avro等。Netty 提供了多种编解码器，包括JSON、XML、Protobuf、Avro等。Netty 提供了多种编解码器，包括JSON、XML、Protobuf、Avro等。Netty 提供了多种编解码器，包括JSON、XML、Proto
## netty经典设计思想
### 责任链模式：Netty 的核心设计思想之一就是责任链模式。 Netty 的核心组件是 ChannelHandler，它实现了 ChannelInboundHandler 和 ChannelOutboundHandler 接口，分别用于处理入站和出站事件。ChannelHandler 可以通过链式调用的方式实现事件处理逻辑的解耦。ChannelHandler在ChannelPipeline中注册，ChannelPipeline是ChannelHandler的集合，ChannelPipeline中的ChannelHandler按照注册顺序执行。
### 适配器模式：Netty 的核心设计思想之二就是适配器模式。Netty 的核心组件是 ChannelHandler，它实现了 ChannelInboundHandler 和 ChannelOutboundHandler 接口，分别用于处理入站和出站事件。ChannelHandler 可以通过适配器模式实现事件处理逻辑的解耦。
### 观察者模式：Netty 的核心设计思想之三就是观察者模式。
### 模板方法模式：Netty 的核心设计思想之四就是模板方法模式。
### 策略模式：Netty 的核心设计思想之五就是策略模式。
### 状态模式：Netty 的核心设计思想之六就是状态模式。
### 享元模式：Netty 的核心设计思想之七就是享元模式。