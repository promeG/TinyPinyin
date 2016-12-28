package com.github.promeg.pinyinhelper;

import com.github.promeg.tinypinyin.lexicons.java.cncity.CnCityDict;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import org.apache.commons.lang3.RandomStringUtils;
import org.openjdk.jmh.annotations.Benchmark;

import java.util.Random;

/**
 * Created by guyacong on 2016/12/23.
 */
//CHECKSTYLE:OFF
public class PinyinDictBenchmark {
    static Random random = new Random();
    static HanyuPinyinOutputFormat format;
    static Pinyin pinyin = Pinyin.with(CnCityDict.getInstance()).build();

    static {
        format = new HanyuPinyinOutputFormat();
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        format.setCaseType(HanyuPinyinCaseType.UPPERCASE);
        format.setVCharType(HanyuPinyinVCharType.WITH_V);
    }

    //@Benchmark
    public void measureMy_toPinyin_no_dict() {
        Pinyin.with(null).build().toPinyin(genRandomString(), ",");
    }

    @Benchmark
    public void measureMy_toPinyin_one_dict() {
        pinyin.toPinyin(genRandomString(), ",");
    }


    //@Benchmark
    public void measurePinyin4j_toPinyin() throws BadHanyuPinyinOutputFormatCombination {
        PinyinHelper.toHanyuPinyinString(genRandomString(), format, ",");
    }

    //@Benchmark
    public void measureMy_toPinyin_with_dict() {
        Pinyin.with(null).build().toPinyin(genRandomString(), ",");
    }


    private String genRandomString() {
        int length = random.nextInt(100);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            if (random.nextBoolean()) {
                sb.append(randomChinese());
            } else {
                sb.append(RandomStringUtils.randomAscii(1));
            }
        }
        return sb.toString();
    }

    private static int chineseStart = Integer.parseInt(String.valueOf(0x4e00));
    private static int chineseEnd = Integer.parseInt(String.valueOf(0x9FA5));

    private static String randomChinese(){
        Random random = new Random();
        int position = random.nextInt(chineseEnd-chineseStart)+chineseStart;
        String code = Integer.toHexString(position);
        return decode("\\u"+code);
    }

    private static String decode(String unicodeStr) {
        if (unicodeStr == null) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        int maxLoop = unicodeStr.length();
        for (int i = 0; i <maxLoop; i++) {
            if (unicodeStr.charAt(i) == '\\') {
                if ((i <maxLoop - 5)
                        && ((unicodeStr.charAt(i + 1) == 'u') || (
                        unicodeStr.charAt(i + 1) == 'U')))
                    try {
                        sb.append((char) Integer.parseInt(unicodeStr.substring(i + 2, i + 6), 16));
                        i += 5;
                    } catch (NumberFormatException localNumberFormatException) {
                        sb.append(unicodeStr.charAt(i));
                    }
                else
                    sb.append(unicodeStr.charAt(i));
            } else {
                sb.append(unicodeStr.charAt(i));
            }
        }
        return sb.toString();
    }

}
//CHECKSTYLE:ON
