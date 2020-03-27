package com.solan.jbase.jieba;

import com.huaban.analysis.jieba.JiebaSegmenter;
import com.huaban.analysis.jieba.SegToken;

import java.util.List;

/**
 *
 * @author: hyl
 * @date: 2020/3/19 10:30
 */
public class JiebaApp {
    public static void main(String[] args) {
        JiebaSegmenter segmenter = new JiebaSegmenter();
        String txt = "有幸福里小区由于爆管导致有燃气发生泄-漏";
        List<String> list = segmenter.sentenceProcess(txt);
        System.out.println(list);
        //[有, 幸福, 里, 小区, 由于, 爆管, 导致, 有, 燃气, 发生, 泄, -, 漏]
        List<SegToken> tokens = segmenter.process(txt, JiebaSegmenter.SegMode.INDEX);
        System.out.println(tokens);


    }
}
