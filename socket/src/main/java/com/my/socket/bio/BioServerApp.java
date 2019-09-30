package com.my.socket.bio;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class BioServerApp {
    private static String str = "Netty is a NIO client server framework which enables quick and easy development of network applications such as protocol servers and clients. It greatly simplifies and streamlines network programming such as TCP and UDP socket server.";
    private static String[] wordArr = str.replace(".", "").split(" ");

    public static void main(String[] args) {
        try {
            ServerSocket server = new ServerSocket(19900);
            while (true) {
                Socket clntSock = server.accept();
                SocketAddress clientAddress = clntSock.getRemoteSocketAddress();
                System.out.println("Handling client at " + clientAddress);
                InputStream in = clntSock.getInputStream();
                int k = in.available();
                if (k > 0) {
                    byte[] buffer = new byte[k];
                    k = in.read(buffer);
                    if (k > 0) {
                        String req = new String(buffer);
                        System.out.println("Server received: " + req);
                    }
                }
                OutputStream out = clntSock.getOutputStream();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HHmmss");
                byte[] buf = sdf.format(new Date()).getBytes("UTF-8");
                out.write(buf, 0, buf.length);
                out.flush();
//                out.close();
                TimeUnit.SECONDS.sleep(3); //等客户端接收
                clntSock.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
