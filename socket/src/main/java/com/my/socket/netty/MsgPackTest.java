package com.my.socket.netty;

import lombok.Data;
import org.msgpack.MessagePack;
import org.msgpack.annotation.Message;
import org.msgpack.template.Templates;

import java.io.IOException;

/**
 * TODO:
 *
 * @author: hyl
 * @date: 2019/12/24 18:01
 */
public class MsgPackTest {
    public static void main(String[] args) {
        System.out.println("Hello World!");
        MessagePack msgPack = new MessagePack();
        UserInfo userInfo = new UserInfo();
        userInfo.setAge(30);
        userInfo.setName("solan");
        try {
            byte[] buf = msgPack.write(userInfo);
            UserInfo user = msgPack.read(buf, UserInfo.class);
            System.out.println(user.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Message
    @Data
    public static class UserInfo {
        private int age;
        private String name;
    }
}
