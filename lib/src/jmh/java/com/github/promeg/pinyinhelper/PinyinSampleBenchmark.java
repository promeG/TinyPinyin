package com.github.promeg.pinyinhelper;


import net.sourceforge.pinyin4j.PinyinHelper;

import java.util.Random;

/**
 * Created by guyacong on 2015/9/28.
 */
//CHECKSTYLE:OFF
public class PinyinSampleBenchmark {

    //@Benchmark
    public void measureMyIsChinese() {
        Pinyin.isChinese(genRandomChar());
    }

    //@Benchmark
    public void measurePinyin4jIsChinese() {
        isChinesePinyin4j(genRandomChar());
    }

    //@Benchmark
    public void measureMyToPinyin() {
        Pinyin.toPinyin(genRandomChar());
    }

    //@Benchmark
    public void measurePinyin4jToPinyin() {
        PinyinHelper.toHanyuPinyinStringArray(genRandomChar());
    }

    private boolean isChinesePinyin4j(char c) {
        String[] pinyins = PinyinHelper.toHanyuPinyinStringArray(c);
        if (pinyins != null && pinyins.length > 0) {
            return true;
        }
        return false;
    }


    private char genRandomChar() {
        Random random = new Random();
        return (char) (Character.MIN_VALUE + random.nextInt(Character.MAX_VALUE - Character.MIN_VALUE));
    }
}
//CHECKSTYLE:ON
