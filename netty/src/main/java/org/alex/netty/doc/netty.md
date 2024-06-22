# Netty笔记
## 概述
+ Netty 是一个异步事件驱动的网络通信框架，它提供了一种基于事件驱动的、非阻塞的、高性能的IO通信模型。
+ Netty 提供了多种传输协议，包括TCP、UDP、HTTP、HTTPS、FTP等。Netty 提供了多种编解码器，包括JSON、XML、Protobuf、Avro等。Netty 提供了多种编解码器，包括JSON、XML、Protobuf、Avro等。Netty 提供了多种编解码器，包括JSON、XML、Protobuf、Avro等。Netty 提供了多种编解码器，包括JSON、XML、Proto
## netty经典设计思想
### 责任链模式：Netty 的核心设计思想之一就是责任链模式。 Netty 的核心组件是 ChannelHandler，它实现了 ChannelInboundHandler 和 ChannelOutboundHandler 接口，分别用于处理入站和出站事件。ChannelHandler 可以通过链式调用的方式实现事件处理逻辑的解耦。ChannelHandler在ChannelPipeline中注册，ChannelPipeline是ChannelHandler的集合，ChannelPipeline中的ChannelHandler按照注册顺序执行。
### 适配器模式：Netty 的核心设计思想之二就是适配器模式。Netty 的核心组件是 ChannelHandler，它实现了 ChannelInboundHandler 和 ChannelOutboundHandler 接口，分别用于处理入站和出站事件。ChannelHandler 可以通过适配器模式实现事件处理逻辑的解耦。
### 上下文模式：Netty 的核心设计思想之三就是上下文模式，它实现了 ChannelHandlerContext 接口，用于管理 ChannelHandler 的上下文信息。


## 笔记
### ChannelHandlerMask里面定义了哪些是出站事件和入站事件。
static final int MASK_ONLY_INBOUND =  MASK_CHANNEL_REGISTERED |

MASK_CHANNEL_UNREGISTERED | MASK_CHANNEL_ACTIVE | MASK_CHANNEL_INACTIVE | MASK_CHANNEL_READ |
MASK_CHANNEL_READ_COMPLETE | MASK_USER_EVENT_TRIGGERED | MASK_CHANNEL_WRITABILITY_CHANGED;

private static final int MASK_ALL_INBOUND = MASK_EXCEPTION_CAUGHT | MASK_ONLY_INBOUND;

static final int MASK_ONLY_OUTBOUND =  MASK_BIND | MASK_CONNECT | MASK_DISCONNECT |
MASK_CLOSE | MASK_DEREGISTER | MASK_READ | MASK_WRITE | MASK_FLUSH;
private static final int MASK_ALL_OUTBOUND = MASK_EXCEPTION_CAUGHT | MASK_ONLY_OUTBOUND;

### ChannelPipeline和ChannelHandlerContext
+ ChannelPipeline提供了ChannelHandler的注册和移除，ChannelHandlerContext提供了ChannelHandler的调用。
+ ChannelPipeline是一个双向链表，定义了用于在该链上出战和入站事件流的api，可以分辨出站和入站事件。
+ 出站事件和入站事件分属不同的handler，一般情况下在业务没有要求的情况下可以不考虑出站事件和入站事件之间的顺序。
+ 而同属一个方向的handler是有顺序的，因为上一个handler处理结果往往是下一个handler的要求的输入。
+ ChannelHandlerContext是ChannelPipeline和ChannelHandler的桥梁(关联prev,next)，它提供了对ChannelHandler的调用，比如传递handler之间的事件，获取ChannelHandler前一个和后一个ChannelHandler。
+ 如果写数据的时候是用channel.write()或者channel.pipeline().write()，那么事件就会流经整个ChannelPipeline，如果用context.write()，那么事件就会从当前的写入的context对应的channelHandler开始流经ChannelPipeline。
![img.png](img.png)
+ fire开头的方法，会把事件从当前ChannelHandler开始，传递到下一个ChannelHandler。

### Channelhandler
+ 出站事件继承ChannelOutboundHandler，入站事件继承ChannelInboundHandler，实现需要用到的方法。
+ channelOutboundHandler.read()方法，是一个业务方（如网卡）要求一个读动作，也就是触发了订阅的op_read，netty将读这个"要求读动作"打包成一个入站事件，然后传入ChannelPipeline。
+ 对一个channel最多读16次，所以满16此次或者读不到数据了就停止发送上述事件。
+ 共享handler用注解 @ChannelHandler.Sharable。

### buffer
+ netty会在pipeline里加一个head和tail上下文，在这两个上下文自动释放了buffer。
+ simpleChannelInboundHandler.channelRead()方法，将数据要不传递要不释放。自己实现也要注意，否则会造成内存泄露。![img_1.png](img_1.png)