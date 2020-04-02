package com.solan.jbase.log;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: hyl
 * @date: 2020/3/30 15:51
 */
public enum OptType {
    SEARCH(1, "查询"),
    CREATE(2, "创建"),
    UPDATE(3, "更新"),
    DELETE(4, "删除"),
    SPECIAL(5, "特殊操作");

    public final int type;
    public final String name;
    protected static final Map<Integer, OptType> map;

    private OptType(int value, String name) {
        this.type = value;
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public int getType() {
        return this.type;
    }

    static {
        Map<Integer, OptType> tempMap = new HashMap();
        OptType[] var1 = values();
        int var2 = var1.length;

        for (int var3 = 0; var3 < var2; ++var3) {
            OptType value = var1[var3];
            tempMap.put(value.type, value);
        }

        map = Collections.unmodifiableMap(tempMap);
    }
}
