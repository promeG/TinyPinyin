package com.github.promeg.pinyinhelper;

import com.github.prome.tinypinyin.jmh.FullDiffDict;
import com.github.promeg.tinypinyin.lexicons.java.cncity.CnCityDict;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

import java.util.concurrent.TimeUnit;

/**
 * Created by guyacong on 2016/12/23.
 */
//CHECKSTYLE:OFF
@State(Scope.Benchmark)
@OutputTimeUnit(TimeUnit.SECONDS)
public class PinyinDictBenchmark1 {

    @Benchmark
    public void TinyPinyin_Init_With_Small_Dict() {
        Pinyin.init(null); //reset
        Pinyin.init(Pinyin.newConfig().with(CnCityDict.getInstance()));
    }

    @Benchmark
    public void TinyPinyin_Init_With_Large_Dict() {
        Pinyin.init(null); //reset
        Pinyin.init(Pinyin.newConfig().with(FullDiffDict.getInstance()));
    }
}
//CHECKSTYLE:ON
