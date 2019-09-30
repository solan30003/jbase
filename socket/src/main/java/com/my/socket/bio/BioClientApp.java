package com.my.socket.bio;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class BioClientApp {
    private static String str = "Netty is a NIO client server framework which enables quick and easy development of network applications such as protocol servers and clients. It greatly simplifies and streamlines network programming such as TCP and UDP socket server.";
    private static String[] wordArr = str.replace(".", "").split(" ");

    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 19900);
            OutputStream outStream = socket.getOutputStream();
            outStream.write("hello".getBytes("UTF-8"));
            outStream.flush();
//            outStream.close();
            InputStream inStream = socket.getInputStream();
            int k = inStream.available();
            ByteArrayOutputStream outMemory = new ByteArrayOutputStream();
            if (k > 0) {
                byte[] buffer = new byte[1024];
                while ((k = inStream.read(buffer)) > -1) {
                    outMemory.write(buffer, 0, k);
                }
                String res = outMemory.toString("UTF-8");
                System.out.println("Client received: " + res);
            }
            inStream.close();
            outMemory.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
