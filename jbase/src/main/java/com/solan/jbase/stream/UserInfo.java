package com.solan.jbase.stream;

import cn.hutool.core.util.RandomUtil;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.awt.image.AreaAveragingScaleFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author hyl
 * @Package com.my.stream
 * @date 2020/1/6 10:03
 */
@Data
@AllArgsConstructor
@ToString
public class UserInfo {
    private int id;
    private String name;
    private String address;
    private float salary;
    private int deptId;

    /**
     * 创建爱你模拟数据
     *
     * @param total 总数
     * @return
     */
    public static List<UserInfo> build(Integer total) {
        int max = total == null ? 10 : total;
        List<UserInfo> list = Lists.newArrayList();
        for (int i = 1; i < max + 1; i++) {
            list.add(new UserInfo(i,
                    "solan" + i,
                    "江苏省南京市",
                    (float) RandomUtil.randomDouble(1000, 3000),
                    RandomUtil.randomInt(1, 5)));
        }
        return list;
    }
}
