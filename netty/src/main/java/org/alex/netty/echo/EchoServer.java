package org.alex.netty.echo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import lombok.extern.slf4j.Slf4j;

import javax.sound.midi.Sequence;

@Slf4j
public class EchoServer {
    private final int port;

    public EchoServer(int port)
    {
        this.port = port;
    }

    public static void main(String[] args) throws Exception
    {
        int port = 9999;
        EchoServer echoServer = new EchoServer(port);
        log.info("服务器即将启动");
        echoServer.start();
        log.info("服务器已启动");


    }

    private void start() throws InterruptedException {
        //事件循环处理器组（线程池），用于将channel（socket）分配给某个线程（某个eventLoop）
        EventLoopGroup eventExecutors = new NioEventLoopGroup();
        try{
            //创建服务器启动对象
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(eventExecutors)
                    .localAddress(port)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            //添加自定义处理器
                            ch.pipeline()
                                    .addLast(new EchoServerHandler());
                        }

                    });

            ChannelFuture channelFuture = bootstrap.bind().sync();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }finally {
            eventExecutors.shutdownGracefully().sync();
        }
    }
}
