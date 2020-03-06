package com.solan.jbase.socket.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class NettyClientApp {

    private static String str = "Netty is a NIO client server framework which enables quick and easy development of network applications such as protocol servers and clients. It greatly simplifies and streamlines network programming such as TCP and UDP socket server.";
    private static String[] wordArr = str.replace(".", "").split(" ");
    private static List<NettyClient> list = new ArrayList();

    public static void main(String[] args) throws InterruptedException {
        NettyClient client = new NettyClient("localhost", 19101);
        try {
            client.start();
            list.add(client);
            TimeUnit.MILLISECONDS.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        TimeUnit.MINUTES.sleep(5);
        list.forEach(c -> c.close());
    }


    public static class NettyClient {
        private String ip;
        private int port;
        private ChannelFuture f = null;

        public NettyClient(String ip, int port) {
            this.ip = ip;
            this.port = port;
        }

        public void start() throws InterruptedException {
            EventLoopGroup group = new NioEventLoopGroup();
            final ClientHandler clientHandler = new ClientHandler();
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_SNDBUF, 128)
                    .option(ChannelOption.SO_RCVBUF, 128)
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3 * 1000)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            System.out.println("connect。。。");
                            ch.pipeline().addLast(new LineBasedFrameDecoder(1024));
                            ch.pipeline().addLast(new StringDecoder(Charset.forName("UTF-8")));
                            ch.pipeline().addLast(clientHandler);
                        }
                    });
            f = b.connect(this.ip, this.port).sync();
            f.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) {
                    if (future.isSuccess()) {
                        System.out.println("connect done");
                    } else {
                        System.out.println("connect failure");
                    }
                }
            });
        }

        public void close() {
            if (f != null) {
                ChannelFuture cf = null;
                try {
                    cf = f.channel().close().sync();
                    cf.addListener(future -> {
                        if (future.isSuccess()) {
                            System.out.println("close done");
                        } else {
                            System.out.println("close failure");
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @ChannelHandler.Sharable
    public static class ClientHandler extends SimpleChannelInboundHandler {
        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            super.channelActive(ctx);
            for (int j = 0; j < 10; j++) {
                String str = "Hello " + j + System.getProperty("line.separator");
                ByteBuf buf = Unpooled.buffer(str.length());
                buf.writeBytes(str.getBytes(Charset.forName("UTF-8")));
                ctx.writeAndFlush(buf);
                try {
                    TimeUnit.MILLISECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
            String str = (String) msg;
            System.out.println("channelRead0: " + str);
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            ctx.close();
        }
        @Override
        public void handlerRemoved(ChannelHandlerContext ctx) {
            System.out.println(String.format("%s 已断开", ctx.channel().remoteAddress()));
        }
    }
}
