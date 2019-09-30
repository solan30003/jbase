package com.my.socket.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.TimeUnit;

public class NioClientApp {
    private static String str = "Netty is a NIO client server framework which enables quick and easy development of network applications such as protocol servers and clients. It greatly simplifies and streamlines network programming such as TCP and UDP socket server.";
    private static String[] wordArr = str.replace(".", "").split(" ");

    public static void main(String[] args) {
        ByteBuffer buf = ByteBuffer.allocate(1024);
        SocketChannel sc = null;
        try {
            sc = SocketChannel.open();
            sc.configureBlocking(false);
            sc.connect(new InetSocketAddress("localhost", 19901));
            if (sc.finishConnect()) {
                int k = 0;
                while (true) {
                    String msg = String.format("I'm from client. %s", k);
                    buf.clear();
                    buf.put(msg.getBytes("UTF-8"));
                    buf.flip();
                    while (buf.hasRemaining()) {
                        sc.write(buf);
                    }
                    TimeUnit.SECONDS.sleep(1);
                    k++;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (sc != null) {
                try {
                    sc.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
