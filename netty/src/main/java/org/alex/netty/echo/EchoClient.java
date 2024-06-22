package org.alex.netty.echo;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;

import java.net.InetSocketAddress;

public class EchoClient {

    private final String host;

    private final Integer port;
    public EchoClient(String host, Integer port) {
        this.host = host;
        this.port = port;
    }

    public static void main(String[] args) throws InterruptedException {
        String host = "127.0.0.1";
        Integer port = 9999;

        // 创建线程池
        EventLoopGroup eventExecutors = new NioEventLoopGroup();

        try {
            // 和服务器不同的点1
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(eventExecutors)
                    // 和服务器不同的点2
                    .remoteAddress(new InetSocketAddress(host, port))
                    // 和服务器不同的点3
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new EchoClientHandler());
                        }
                    });
            // 和服务器不同的点4 连接到服务器
            ChannelFuture future = bootstrap.connect().sync();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }finally {
            eventExecutors.shutdownGracefully().sync();
        }
    }
}
