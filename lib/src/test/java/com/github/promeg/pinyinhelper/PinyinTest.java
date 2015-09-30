package com.github.promeg.pinyinhelper;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * Assert that our pinyin helper produce the same result for all Characters as Pinyin4J.
 *
 * Created by guyacong on 2015/9/28.
 */
public class PinyinTest {

    @Test
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

    @Test
    public void testToPinyin() throws BadHanyuPinyinOutputFormatCombination {
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

    private static char[] allChars() {
        char[] allChars = new char[Character.MAX_VALUE - Character.MIN_VALUE + 1];
        int length = allChars.length;
        for (int i = 0; i < length; i++) {
            allChars[i] = (char) (Character.MIN_VALUE + i);
        }
        return allChars;
    }
}
