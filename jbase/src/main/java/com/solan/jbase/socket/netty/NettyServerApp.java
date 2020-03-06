package com.solan.jbase.socket.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.util.CharsetUtil;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

/**
 * 行分割帧解码器
 */
public class NettyServerApp {

    private static String str = "Netty is a NIO client server framework which enables quick and easy development of network applications such as protocol servers and clients. It greatly simplifies and streamlines network programming such as TCP and UDP socket server.";
    private static String[] wordArr = str.replace(".", "").split(" ");

    public static void main(String[] args) throws InterruptedException {
        select();
    }

    private static void select() throws InterruptedException {
        final ServerHandler serverHandler = new ServerHandler();
        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup work = new NioEventLoopGroup();
        FileInputStream stream;
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(boss, work)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 512)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childHandler(new ChannelInitializer<io.netty.channel.socket.SocketChannel>() {
                    @Override
                    protected void initChannel(io.netty.channel.socket.SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new LineBasedFrameDecoder(1024));
                        ch.pipeline().addLast(new StringDecoder(Charset.forName("UTF-8")));
                        ch.pipeline().addLast(new ReadTimeoutHandler(5, TimeUnit.MINUTES));
                        ch.pipeline().addLast(serverHandler);
                    }
                });
        ChannelFuture f = bootstrap.bind(19101).sync();
        if (f.isSuccess()) {
            System.out.println("Server startup.");
        } else {
            f.cause().printStackTrace();
            boss.shutdownGracefully();
            work.shutdownGracefully();
        }
        f.channel().closeFuture().sync();
    }

    @Sharable
    public static class ServerHandler extends SimpleChannelInboundHandler<Object> {
        //        ChannelInboundHandlerAdapter s;
        @Override
        public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
            System.out.println(ctx.channel().remoteAddress());
            ByteBuf buf = Unpooled.copiedBuffer("Server." + System.getProperty("line.separator"), CharsetUtil.UTF_8);
            ctx.writeAndFlush(buf);
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            super.channelActive(ctx);
            //ctx.fireChannelActive();
            System.out.println("Server channelActive()");
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            String str = (String) msg;
            System.out.println("Server channelRead: " + str);
        }

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
            String str = (String) msg;
            System.out.println("Server channelRead0: " + str);
        }

        @Override
        public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
            ctx.flush();
            System.out.println("Server ReadComplete()");
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            ctx.close();
            cause.printStackTrace();
        }
    }
}
