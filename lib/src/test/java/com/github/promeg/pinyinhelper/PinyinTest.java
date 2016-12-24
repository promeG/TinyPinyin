package com.github.promeg.pinyinhelper;

import com.github.promeg.tinypinyin.lexicons.java.cncity.CnCityDict;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * Assert that our pinyin helper produce the same result for all Characters as Pinyin4J.
 *
 * Created by guyacong on 2015/9/28.
 */
public class PinyinTest {

    //@Test
    public void testIsChinese() throws BadHanyuPinyinOutputFormatCombination {
        char[] allChars = allChars();
        final int allCharsLength = allChars.length;
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        format.setCaseType(HanyuPinyinCaseType.UPPERCASE);
        format.setVCharType(HanyuPinyinVCharType.WITH_V);

        for (int i = 0; i < allCharsLength; i++) {
            char targetChar = allChars[i];
            String[] pinyins = PinyinHelper.toHanyuPinyinStringArray(targetChar, format);
            if (pinyins != null && pinyins.length > 0) {
                // is chinese
                assertThat(Pinyin.isChinese(targetChar), is(true));
            } else {
                // not chinese
                assertThat(Pinyin.isChinese(targetChar), is(false));
            }
        }
    }

    //@Test
    public void testToPinyin_char() throws BadHanyuPinyinOutputFormatCombination {
        char[] allChars = allChars();
        final int allCharsLength = allChars.length;
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        format.setCaseType(HanyuPinyinCaseType.UPPERCASE);
        format.setVCharType(HanyuPinyinVCharType.WITH_V);

        int chineseCount = 0;
        for (int i = 0; i < allCharsLength; i++) {
            char targetChar = allChars[i];
            String[] pinyins = PinyinHelper.toHanyuPinyinStringArray(targetChar, format);
            if (pinyins != null && pinyins.length > 0) {
                // is chinese
                chineseCount++;
                assertThat(Pinyin.toPinyin(targetChar), equalTo(pinyins[0]));
            } else {
                // not chinese
                assertThat(Pinyin.toPinyin(targetChar), equalTo(String.valueOf(targetChar)));
            }
        }

        //CHECKSTYLE:OFF
        int expectedChineseCount = 20378;
        //CHECKSTYLE:ON

        assertThat(chineseCount, is(expectedChineseCount));
    }

    //@Test
    public void testInit_no_dict() {
        Pinyin pinyin = Pinyin.with(null).build();

        assertThat(pinyin.mPinyinDicts.size(), is(0));
    }

    //@Test
    public void testInit_with_dict() {
        Pinyin pinyin = Pinyin.with(new PinyinDict() {
                @Override
                public Map<String, String[]> mapping() {
                    return null;
                }
            }).with(new PinyinDict() {
                @Override
                public Map<String, String[]> mapping() {
                    return null;
                }
            }).build();

        assertThat(pinyin.mPinyinDicts.size(), is(2));
    }


    //@Test
    public void testToPinyin_Str_no_dict() {
        Pinyin pinyin = Pinyin.with(null).build();

        String str = "一个测试重庆test,中英文符号；;《>。";
        String expected = "YI GE CE SHI ZHONG QING t e s t , ZHONG YING WEN FU HAO ； ; 《 > 。";

        assertThat(pinyin.toPinyin(str, " "), is(expected));

    }

    //@Test
    public void testToPinyin_Str_empty_dict() {
        Pinyin pinyin = Pinyin.with(new PinyinDict() {
                @Override
                public Map<String, String[]> mapping() {
                    return null;
                }
            }).build();

        String str = "一个测试重庆test,中英文符号；;《>。";
        String expected = "YI GE CE SHI ZHONG QING t e s t , ZHONG YING WEN FU HAO ； ; 《 > 。";

        assertThat(pinyin.toPinyin(str, " "), is(expected));

    }

    //@Test
    public void testToPinyin_Str_one_dict() {
        Pinyin pinyin = Pinyin.with(new PinyinDict() {
                @Override
                public Map<String, String[]> mapping() {
                    Map<String, String[]> map = new HashMap<String, String[]>();
                    map.put("重庆", new String[]{"CHONG", "QING"});
                    return map;
                }
            }).build();

        String str = "一个测试重庆test,中英文符号；;《>。";
        String expected = "YI GE CE SHI CHONG QING t e s t , ZHONG YING WEN FU HAO ； ; 《 > 。";

        assertThat(pinyin.toPinyin(str, " "), is(expected));
    }

    //@Test
    public void testToPinyin_Str_multi_dict() {
        Pinyin pinyin = Pinyin.with(new PinyinDict() {
                @Override
                public Map<String, String[]> mapping() {
                    Map<String, String[]> map = new HashMap<String, String[]>();
                    map.put("重庆", new String[]{"CHONG", "QING"});
                    return map;
                }
            }).with(new PinyinDict() {
                @Override
                public Map<String, String[]> mapping() {
                    Map<String, String[]> map = new HashMap<String, String[]>();
                    map.put("重庆", new String[]{"NOT", "MATCH"});
                    map.put("长安", new String[]{"CHANG", "AN"});
                    return map;
                }
            }).build();

        String str = "一个测试重庆和长安test,中英文符号；;《>。";
        String expected = "YI GE CE SHI CHONG QING HE CHANG AN t e s t , ZHONG YING WEN FU HAO ； ; 《 > 。";

        assertThat(pinyin.toPinyin(str, " "), is(expected));
    }

    private static char[] allChars() {
        char[] allChars = new char[Character.MAX_VALUE - Character.MIN_VALUE + 1];
        int length = allChars.length;
        for (int i = 0; i < length; i++) {
            allChars[i] = (char) (Character.MIN_VALUE + i);
        }
        return allChars;
    }

    @Test
    public void temp() {
        CnCityDict cityDict = CnCityDict.getInstance();

        System.out.println(cityDict.mapping().size());
    }
}
