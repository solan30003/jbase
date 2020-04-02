package com.solan.jbase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        Logger log = LoggerFactory.getLogger(App.class);
        try {
            //        log.debug("debug......{}", "abc");
            new User().test();
        } catch (Exception e) {
//            log.error("ex", e);
        }
        System.out.println("Hello World!");
    }

    public static class User {
        public void test() {
            int k = 0;
            int l = 100 / k;
        }
    }
}
