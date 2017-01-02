package com.github.promeg.pinyinhelper;


import net.sourceforge.pinyin4j.PinyinHelper;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.BenchmarkParams;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Created by guyacong on 2015/9/28.
 */
//CHECKSTYLE:OFF
@State(Scope.Benchmark)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class PinyinSampleBenchmark {

    @Setup(Level.Iteration)
    public void setUp(BenchmarkParams params) {
        Pinyin.init(null);
    }

    @Benchmark
    public void TinyPinyin_IsChinese() {
        Pinyin.isChinese(genRandomChar());
    }

    @Benchmark
    public void Pinyin4j_IsChinese() {
        isChinesePinyin4j(genRandomChar());
    }

    @Benchmark
    public void TinyPinyin_CharToPinyin() {
        Pinyin.toPinyin(genRandomChar());
    }

    @Benchmark
    public void Pinyin4j_CharToPinyin() {
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
