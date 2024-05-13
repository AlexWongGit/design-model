package org.alex.netty;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

public class NettyDemo {
    public static void main(String[] args) {

        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        try{
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new HttpServerInitializer());
            serverBootstrap.bind(new InetSocketAddress(6667)).sync();
        }catch (InterruptedException e){
            e.printStackTrace();
        }finally {
bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
