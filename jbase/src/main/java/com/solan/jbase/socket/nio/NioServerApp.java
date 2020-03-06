package com.solan.jbase.socket.nio;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

public class NioServerApp {

    private static String str = "Netty is a NIO client server framework which enables quick and easy development of network applications such as protocol servers and clients. It greatly simplifies and streamlines network programming such as TCP and UDP socket server.";
    private static String[] wordArr = str.replace(".", "").split(" ");

    public static void main(String[] args) {
        select();
    }

    private static void select() {
        Selector selector = null;
        ServerSocketChannel ssc = null;
        try {
            selector = Selector.open();
            ssc = ServerSocketChannel.open();
            ssc.socket().bind(new InetSocketAddress(19901));
            ssc.configureBlocking(false);
            ssc.register(selector, SelectionKey.OP_ACCEPT);
            while (true) {
                if (selector.select(3000) == 0) {
                    continue;
                }
                Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
                while (iter.hasNext()) {
                    SelectionKey key = iter.next();
                    if (key.isAcceptable()) {
                        acceptHandler(key);
                    } else if (key.isReadable()) {
                        readHandler(key);
                    } else if (key.isWritable() && key.isValid()) {
                        writeHandler(key);
                    } else if (key.isConnectable()) {
                        System.out.println("isConnectable = true");
                    }
                    iter.remove();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ssc != null) {
                try {
                    ssc.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (selector != null) {
                try {
                    selector.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void acceptHandler(SelectionKey key) {
        ServerSocketChannel ssChannel = (ServerSocketChannel) key.channel();
        try {
            SocketChannel sChannel = ssChannel.accept();
            sChannel.configureBlocking(false);
            sChannel.register(key.selector(), SelectionKey.OP_READ, ByteBuffer.allocateDirect(1024));
            System.out.println(sChannel.getRemoteAddress() + "已连接");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void readHandler(SelectionKey key) throws IOException {
        SocketChannel sChannel = (SocketChannel) key.channel();
        ByteBuffer buf = (ByteBuffer) key.attachment();
        long bytesRead = 0;
        while ((bytesRead = sChannel.read(buf)) > -1) {
            buf.flip();
            while (buf.hasRemaining()) {
                System.out.println((char) buf.get());
            }
            buf.clear();
        }
        if (bytesRead == -1) {
            ByteBuffer buff = ByteBuffer.allocate(1024);
            buff.put("I'm SERVER.".getBytes("UTF-8"));
            buff.flip();
            sChannel.write(buff);
            buff.clear();
            sChannel.close();
        }
    }

    private static void writeHandler(SelectionKey key) throws IOException {
        ByteBuffer buf = (ByteBuffer) key.attachment();
        buf.flip();
        SocketChannel sc = (SocketChannel) key.channel();
        while (buf.hasRemaining()) {
            sc.write(buf);
        }
        buf.compact();
    }
}
