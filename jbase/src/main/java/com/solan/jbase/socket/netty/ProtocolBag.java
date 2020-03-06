package com.solan.jbase.socket.netty;

import lombok.Data;

/**
 * 私有协议包
 *
 * @author: hyl
 * @date: 2019/12/27 14:31
 */
@Data
public class ProtocolBag {
    /**
     * 1个字节业务类型，以十六进制表示，最多表示15种业务，其中0为默认：未知类型
     */
    private byte bizType = 0x0;
    /**
     * 1个字节，可表示业务类型的子类型标记，最多表示15类，其中0为默认：未知标记
     */
    private byte flag = 0x0;
    /**
     * 数据域(content)长度，占4个字节
     */
    private int length = 0;
    /**
     * 数据域
     */
    private String content;

    public ProtocolBag() {

    }

    public ProtocolBag(byte bizType, byte flag, int length) {
        this.bizType = bizType;
        this.flag = flag;
        this.length = length;
    }

    public ProtocolBag(byte bizType, byte flag, int length, String content) {
        this(bizType, flag, length);
        this.content = content;
    }

    public static class Builder {
        /**
         * 1个字节业务类型，以十六进制表示，最多表示15种业务，其中0为默认：未知类型
         */
        private byte bizType = 0x0;
        /**
         * 1个字节，可表示业务类型的子类型标记，最多表示15类，其中0为默认：未知标记
         */
        private byte flag = 0x0;
        /**
         * 数据域(content)长度，占4个字节
         */
        private int length = 0;
        /**
         * 数据域
         */
        private String content;

        public Builder() {

        }

        public Builder bizType(byte bizType) {
            this.bizType = bizType;
            return this;
        }

        public Builder flag(byte flag) {
            this.flag = flag;
            return this;
        }

        public Builder length(byte length) {
            this.length = length;
            return this;
        }

        public Builder content(String content) {
            this.content = content;
            return this;
        }

        public ProtocolBag build() {
            return new ProtocolBag(bizType, flag, length, content);
        }
    }
}
