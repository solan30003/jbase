package com.my.socket.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.TooLongFrameException;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.util.CharsetUtil;

import java.io.FileInputStream;
import java.nio.charset.Charset;

/**
 * 自定义分隔符帧解码器
 */
public class DelimiterServerApp {
    private static int MAX_FRAME_LENGTH = 128;
    private static String separator = "$_";
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
                        ByteBuf delimiter = Unpooled.copiedBuffer(separator, Charset.forName("UTF-8"));
                        ch.pipeline().addLast(new DelimiterBasedFrameDecoder(MAX_FRAME_LENGTH, delimiter));
                        ch.pipeline().addLast(new StringDecoder(Charset.forName("UTF-8")));
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

    @ChannelHandler.Sharable
    public static class ServerHandler extends SimpleChannelInboundHandler<Object> {
        //        ChannelInboundHandlerAdapter s;
        @Override
        public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
            System.out.println(ctx.channel().remoteAddress());
            ByteBuf buf = Unpooled.copiedBuffer("Server." + separator, CharsetUtil.UTF_8);
            ctx.writeAndFlush(buf);
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            super.channelActive(ctx);
            String msg = String.format("Welcome!%s", separator);
            ByteBuf buf = Unpooled.copiedBuffer(msg, CharsetUtil.UTF_8);
            ctx.writeAndFlush(buf);
            System.out.println("channelActive()");
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            String str = (String) msg;
            System.out.println("channelRead: " + str);
            ByteBuf buf = Unpooled.copiedBuffer("RES" + separator, CharsetUtil.UTF_8);
            ctx.writeAndFlush(buf);
        }

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
            String str = (String) msg;
            System.out.println("channelRead0: " + str);
        }

        @Override
        public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
            ctx.flush();
            System.out.println("ReadComplete()");
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            ctx.close();
            if (cause instanceof TooLongFrameException) {
                System.out.println("接收消息超出所指定的长度");
            }
            cause.printStackTrace();
        }

        @Override
        public void handlerRemoved(ChannelHandlerContext ctx) {
            System.out.println(String.format("%s 已断开", ctx.channel().remoteAddress()));
        }
    }
}
