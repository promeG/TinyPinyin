package com.github.promeg.pinyinhelper;

import com.github.prome.tinypinyin.jmh.FullDiffDict;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.BenchmarkParams;

import java.util.concurrent.TimeUnit;

/**
 * Created by guyacong on 2016/12/23.
 */
//CHECKSTYLE:OFF
@State(Scope.Benchmark)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class PinyinDictBenchmark2 {
    static HanyuPinyinOutputFormat format;

    static String inputStr;

    static {
        format = new HanyuPinyinOutputFormat();
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        format.setCaseType(HanyuPinyinCaseType.UPPERCASE);
        format.setVCharType(HanyuPinyinVCharType.WITH_V);
    }

    @Setup(Level.Iteration)
    public void setUp(BenchmarkParams params) {
        inputStr = BenchmarkUtils.genRandomString(1000);
        Pinyin.init(Pinyin.newConfig().with(FullDiffDict.getInstance()));
    }

    @Benchmark
    public void Pinyin4j_StringToPinyin() throws BadHanyuPinyinOutputFormatCombination {
        PinyinHelper.toHanyuPinyinString(inputStr, format, ",");
    }

    @Benchmark
    public void TinyPinyin_StringToPinyin_With_Large_Dict() {
        Pinyin.toPinyin(inputStr, ",");
    }
}
//CHECKSTYLE:ON
