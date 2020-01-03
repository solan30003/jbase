package com.my.socket.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

public class NioClientApp {

  private static String str = "Netty is a NIO client server framework which enables quick and easy development of network applications such as protocol servers and clients. It greatly simplifies and streamlines network programming such as TCP and UDP socket server.";
  private static String[] wordArr = str.replace(".", "").split(" ");

  public static void main(String[] args) throws IOException {
    Selector selector = Selector.open();
    ByteBuffer buf = ByteBuffer.allocate(1024);
    SocketChannel sc = null;
    try {
      sc = SocketChannel.open();
      sc.configureBlocking(false);
      sc.register(selector, SelectionKey.OP_CONNECT);
      sc.connect(new InetSocketAddress("localhost", 19901));
      while (selector.select(500) > 0) {
        Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
        while (keys.hasNext()) {
          SelectionKey key = keys.next();
          if (key.isConnectable()) {
            SocketChannel channel = (SocketChannel) key.channel();
            channel.register(selector, SelectionKey.OP_READ);
            String msg = String.format("I'm from client.");
            buf.clear();
            buf.put(msg.getBytes("UTF-8"));
            buf.flip();
            while (buf.hasRemaining()) {
              System.out.println(channel.isConnected());
              channel.write(buf);
            }
          } else if (key.isReadable()) {
            buf.clear();
            SocketChannel channel = (SocketChannel) key.channel();
            long bytesRead = 0;
            while ((bytesRead = channel.read(buf)) > -1) {
              buf.flip();
              while (buf.hasRemaining()) {
                System.out.println((char) buf.get());
              }
              buf.clear();
            }
          }
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
