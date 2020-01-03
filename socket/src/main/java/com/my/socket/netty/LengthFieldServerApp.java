package com.my.socket.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.util.CharsetUtil;
import org.apache.commons.lang3.StringUtils;

import java.io.FileInputStream;
import java.util.concurrent.TimeUnit;

/**
 * 自定义长度帧解码器
 * <pre>
 * （1） maxFrameLength - 发送的数据包最大长度；
 * （2） lengthFieldOffset - 长度域偏移量，指的是长度域位于整个数据包字节数组中的下标，通常为0；
 * （3） lengthFieldLength - 长度域的自己的字节数长度，一般长度为：int-4,long-8。
 * （4） lengthAdjustment – 长度域的偏移量矫正。 如果长度域的值，除了包含有效数据域的长度外，还包含了其他域（如长度域自身）长度，那么，就需要进行矫正。矫正的值为：包长 - 长度域的值 – 长度域偏移 – 长度域长。
 * （5） initialBytesToStrip – 丢弃的起始字节数。丢弃处于有效数据前面的字节数量。丢弃之后的字节全是正文数据域。
 * </pre>
 */
public class LengthFieldServerApp {
    private static final int LENGTH_128 = 128;
    private static String str = "Netty is a NIO client server framework which enables quick and easy development of network applications such as protocol servers and clients. It greatly simplifies and streamlines network programming such as TCP and UDP socket server.";
    private static String[] wordArr = str.replace(".", "").split(" ");

    public static void main(String[] args) throws InterruptedException {
        select();
    }


    private static void select() throws InterruptedException {
        final ServerHandler serverHandler = new ServerHandler();
        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup work = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(boss, work)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 512)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childHandler(new ChannelInitializer<io.netty.channel.socket.SocketChannel>() {
                    @Override
                    protected void initChannel(io.netty.channel.socket.SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(1024, 0, 4, 0, 4));
                        ch.pipeline().addLast(new StringDecoder(CharsetUtil.UTF_8));
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
            super.channelRegistered(ctx);
            System.out.println("channelRegistered: " + ctx.channel().remoteAddress());
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            super.channelActive(ctx);
            System.out.println("channelActive()");
            ctx.executor().schedule(new Runnable() {
                @Override
                public void run() {
                    String msg = String.format("Welcome!");
                    byte[] data = msg.getBytes(CharsetUtil.UTF_8);
                    ByteBuf buf = Unpooled.buffer();
                    buf.writeInt(data.length);
                    buf.writeBytes(data);
                    ctx.writeAndFlush(buf);
                }
            }, 500, TimeUnit.MILLISECONDS);
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            String str = (String) msg;
            System.out.println(String.format("channelRead: %s", str));
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
        }

        @Override
        public void handlerRemoved(ChannelHandlerContext ctx) {
            System.out.println(String.format("%s 已断开", ctx.channel().remoteAddress()));
        }
    }
}
