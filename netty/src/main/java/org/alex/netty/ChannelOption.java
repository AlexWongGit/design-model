package org.alex.netty;

import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.MessageSizeEstimator;
import io.netty.channel.RecvByteBufAllocator;
import io.netty.channel.WriteBufferWaterMark;
import io.netty.util.AbstractConstant;
import io.netty.util.ConstantPool;

import java.net.InetAddress;
import java.net.NetworkInterface;

/**
 * 带**的属性属于了解即可
 * @param <T>
 */
public class ChannelOption<T> extends AbstractConstant<ChannelOption<T>> {
    private static final ConstantPool<ChannelOption<Object>> pool = new ConstantPool<ChannelOption<Object>>() {
        @Override
        protected ChannelOption<Object> newConstant(int id, String name) {
            return new ChannelOption(id, name);
        }
    };
    public static final ChannelOption<ByteBufAllocator> ALLOCATOR = valueOf("ALLOCATOR");
    // **Netty参数，用于Channel分配接受Buffer的分配器，默认值为AdaptiveRecvByteBufAllocator.DEFAULT，是一个自适应的接受缓冲区分配器，能根据接受到的数据自动调节大小。
    // **可选值为FixedRecvByteBufAllocator，固定大小的接受缓冲区分配器。
    public static final ChannelOption<RecvByteBufAllocator> RCVBUF_ALLOCATOR = valueOf("RCVBUF_ALLOCATOR");
    // **Netty参数，消息大小估算器，默认为DefaultMessageSizeEstimator.DEFAULT。
    // **估算ByteBuf、ByteBufHolder和FileRegion的大小，其中ByteBuf和ByteBufHolder为实际大小，FileRegion估算值为0。
    // **该值估算的字节数在计算水位时使用，FileRegion为0可知FileRegion不影响高低水位。
    public static final ChannelOption<MessageSizeEstimator> MESSAGE_SIZE_ESTIMATOR = valueOf("MESSAGE_SIZE_ESTIMATOR");
    // Netty参数，连接超时毫秒数，默认值30000毫秒即30秒。
    public static final ChannelOption<Integer> CONNECT_TIMEOUT_MILLIS = valueOf("CONNECT_TIMEOUT_MILLIS");
    /**
     * @deprecated
     */
    // **Netty参数，一次Loop读取的最大消息数，对于ServerChannel或者NioByteChannel，默认值为16，其他Channel默认值为1。
    // **默认值这样设置，是因为：ServerChannel需要接受足够多的连接，保证大吞吐量，NioByteChannel可以减少不必要的系统调用select。
    @Deprecated
    public static final ChannelOption<Integer> MAX_MESSAGES_PER_READ = valueOf("MAX_MESSAGES_PER_READ");
    // **Netty参数，一个Loop写操作执行的最大次数，默认值为16。
    // **也就是说，对于大数据量的写操作至多进行16次，如果16次仍没有全部写完数据，此时会提交一个新的写任务给EventLoop，任务将在下次调度继续执行。
    // **这样，其他的写请求才能被响应不会因为单个大数据量写请求而耽误
    public static final ChannelOption<Integer> WRITE_SPIN_COUNT = valueOf("WRITE_SPIN_COUNT");
    /**
     * @deprecated
     */
    // **Netty参数，写高水位标记，默认值64KB。如果Netty的写缓冲区中的字节超过该值，Channel的isWritable()返回False。
    @Deprecated
    public static final ChannelOption<Integer> WRITE_BUFFER_HIGH_WATER_MARK = valueOf("WRITE_BUFFER_HIGH_WATER_MARK");
    /**
     * @deprecated
     */
    // **Netty参数，写低水位标记，默认值32KB。
    // **当Netty的写缓冲区中的字节超过高水位之后若下降到低水位，则Channel的isWritable()返回True。写高低水位标记使用户可以控制写入数据速度，从而实现流量控制。
    // **推荐做法是：每次调用channl.write(msg)方法首先调用channel.isWritable()判断是否可写。
    @Deprecated
    public static final ChannelOption<Integer> WRITE_BUFFER_LOW_WATER_MARK = valueOf("WRITE_BUFFER_LOW_WATER_MARK");
    public static final ChannelOption<WriteBufferWaterMark> WRITE_BUFFER_WATER_MARK = valueOf("WRITE_BUFFER_WATER_MARK");
    public static final ChannelOption<Boolean> ALLOW_HALF_CLOSURE = valueOf("ALLOW_HALF_CLOSURE");
    // **Netty参数，一个连接的远端关闭时本地端是否关闭，默认值为False。
    // **值为False时，连接自动关闭；为True时，触发ChannelInboundHandler的userEventTriggered()方法，事件为ChannelInputShutdownEvent。
    public static final ChannelOption<Boolean> AUTO_READ = valueOf("AUTO_READ");
    /**
     * @deprecated
     */
    @Deprecated
    public static final ChannelOption<Boolean> AUTO_CLOSE = valueOf("AUTO_CLOSE");
    // **Socket参数，设置广播模式。
    public static final ChannelOption<Boolean> SO_BROADCAST = valueOf("SO_BROADCAST");
    // ChannelOption.SO_KEEPALIVE参数对应于套接字选项中的SO_KEEPALIVE，该参数用于设置TCP连接，
    // 当设置该选项后，TCP会主动探测空闲连接的有效性。这个选项用于可能长时间没有数据交流的连接。
    // 当设置该选项后，如果在两小时内没有数据的通信时，TCP会自动发送一个活动探测数据报文。
    // 但是由于时间间隔太大，实际项目中一般都会实现自己的心跳机制。
    public static final ChannelOption<Boolean> SO_KEEPALIVE = valueOf("SO_KEEPALIVE");
    // ChannelOption.SO_SNDBUF参数对应于套接字选项中的SO_SNDBUF。
    // ChannelOption.SO_RCVBUF参数对应于套接字选项中的SO_RCVBUF。
    // 这两个参数用于设置接收缓冲区和发送缓冲区的大小，接收缓冲区用于保存网络协议站内收到的数据，直至应用程序读取成功，发送缓冲区用于保存发送数据，直至发送成功。
    public static final ChannelOption<Integer> SO_SNDBUF = valueOf("SO_SNDBUF");
    public static final ChannelOption<Integer> SO_RCVBUF = valueOf("SO_RCVBUF");
    // ChannelOption.SO_REUSEADDR对应于套接字选项中的SO_REUSEADDR，这个参数表示允许重复使用本地地址和端口。
    // 举个栗子：某个服务器进程占用了TCP的80端口进行监听，此时再次监听该端口就会返回错误，使用该参数就可以解决问题，该参数允许共用该端口，这个在服务器程序中比较常见
    // 比如某个程序非正常退出，改程序占用的端口可能要被占用一段时间才能允许其他进程使用，而且程序死掉之后，内核需要一定的时间才能释放该端口，不设置SO_REUSEADDR就无法正常使用该端口。
    public static final ChannelOption<Boolean> SO_REUSEADDR = valueOf("SO_REUSEADDR");
    // ChannelOption.SO_LINGER参数对应于套接字选项中的SO_LINGER。Linux内核默认处理方式是当用户调用close()方法的时候，函数返回，
    // 在可能的情况下，尽量发送数据，不一定保证会发送完剩余的数据，造成了数据的不确定性，使用SO_LINGER可以阻塞close()的调用时间，直至数据完全发送。
    // Netty对底层Socket参数的简单封装，关闭Socket的延迟时间，默认值为-1，表示禁用该功能。
    // -1以及所有<0的数表示socket.close()方法立即返回，但OS底层会将发送缓冲区全部发送到对端。
    // 0表示socket.close()方法立即返回，OS放弃发送缓冲区的数据直接向对端发送RST包，对端收到复位错误。
    // 非0整数值表示调用socket.close()方法的线程被阻塞直到延迟时间到或发送缓冲区中的数据发送完毕，若超时，则对端会收到复位错误。
    public static final ChannelOption<Integer> SO_LINGER = valueOf("SO_LINGER");
    // ChannelOption.SO_BACKLOG对应的是tcp/ip协议listen函数中的backlog参数，
    // 函数listen(int socketfd, int backlog)用来初始化服务端可连接队列。
    // 服务端处理客户端的连接请求是顺序处理的，所以同一时间只能处理一个客户端连接
    // 多个客户端请求的时候，服务端将不能处理的客户端连接请求放在队列中等待处理，backlog参数指定了队列的大小。默认值。Windows 200，其余128。
    public static final ChannelOption<Integer> SO_BACKLOG = valueOf("SO_BACKLOG");
    // **这个参数设定的是HTTP连接成功后，等待读取数据或者写数据的最大超时时间，单位为毫秒，如果设置为0，则表示永远不会超时。
    public static final ChannelOption<Integer> SO_TIMEOUT = valueOf("SO_TIMEOUT");
    // **IP参数，设置IP头部的Type-of-Service字段，用于描述IP包的优先级和QoS选项。
    public static final ChannelOption<Integer> IP_TOS = valueOf("IP_TOS");
    // **对应IP参数IP_MULTICAST_IF，设置对应地址的网卡为多播模式。
    public static final ChannelOption<InetAddress> IP_MULTICAST_ADDR = valueOf("IP_MULTICAST_ADDR");
    // **对应IP参数IP_MULTICAST_IF2，同上但支持IPV6。
    public static final ChannelOption<NetworkInterface> IP_MULTICAST_IF = valueOf("IP_MULTICAST_IF");
    // **IP参数，多播数据报的time-to-live即存活跳数。
    public static final ChannelOption<Integer> IP_MULTICAST_TTL = valueOf("IP_MULTICAST_TTL");
    // **对应IP参数IP_MULTICAST_LOOP，设置本地回环接口的多播功能。由于IP_MULTICAST_LOOP返回True表示关闭，所以Netty加上后缀_DISABLED防止歧义。
    public static final ChannelOption<Boolean> IP_MULTICAST_LOOP_DISABLED = valueOf("IP_MULTICAST_LOOP_DISABLED");
    // ChannelOption.TCP_NODELAY参数对应于套接字选项中的TCP_NODELAY，该参数的使用与Nagle算法有关，Nagle算法是将小的数据包组装为更大的帧进行发送，
    // 而不是输入一次发送一次，因此在数据包不足的时候会等待其他数据。虽然该方式有效提高网络的有效负载，但是造成了延时。
    // TCP_NODELAY参数的作用就是禁用Nagle算法，适用于小数据即时传输，与TCP_NODELAY相对应的是TCP_CORK，该选项是需要等到发送的数据量最大的时候，一次性发送数据，适用于文件传输。
    public static final ChannelOption<Boolean> TCP_NODELAY = valueOf("TCP_NODELAY");
    /**
     * @deprecated
     */
    @Deprecated
    public static final ChannelOption<Boolean> DATAGRAM_CHANNEL_ACTIVE_ON_REGISTRATION = valueOf("DATAGRAM_CHANNEL_ACTIVE_ON_REGISTRATION");
    public static final ChannelOption<Boolean> SINGLE_EVENTEXECUTOR_PER_GROUP = valueOf("SINGLE_EVENTEXECUTOR_PER_GROUP");

    public static <T> ChannelOption<T> valueOf(String name) {
        return (ChannelOption) pool.valueOf(name);
    }

    public static <T> ChannelOption<T> valueOf(Class<?> firstNameComponent, String secondNameComponent) {
        return (ChannelOption) pool.valueOf(firstNameComponent, secondNameComponent);
    }

    public static boolean exists(String name) {
        return pool.exists(name);
    }

    public static <T> ChannelOption<T> newInstance(String name) {
        return (ChannelOption) pool.newInstance(name);
    }

    private ChannelOption(int id, String name) {
        super(id, name);
    }

    /**
     * @deprecated
     */
    @Deprecated
    protected ChannelOption(String name) {
        this(pool.nextId(), name);
    }

    public void validate(T value) {
        if (value == null) {
            throw new NullPointerException("value");
        }
    }
}
