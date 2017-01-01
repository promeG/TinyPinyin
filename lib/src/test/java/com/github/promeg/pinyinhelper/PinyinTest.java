package com.github.promeg.pinyinhelper;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.nullValue;
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

    @Test
    public void testInit_no_dict() {
        Pinyin pinyin = Pinyin.with(null).build();

        assertThat(pinyin.mTrieDict, nullValue());
    }

    @Test
    public void testInit_with_null_dict() {
        Pinyin pinyin = Pinyin.with(new PinyinMapDict() {
                @Override
                public Map<String, String[]> mapping() {
                    return null;
                }
            }).with(new PinyinMapDict() {
                @Override
                public Map<String, String[]> mapping() {
                    return null;
                }
            }).build();

        assertThat(pinyin.mTrieDict, nullValue());
    }

    @Test
    public void testInit_with_nonnull_nokey_dict() {
        Pinyin pinyin = Pinyin.with(new PinyinMapDict() {
                @Override
                public Map<String, String[]> mapping() {
                    return new HashMap<String, String[]>();
                }
            }).build();

        assertThat(pinyin.mTrieDict, nullValue());
    }

    @Test
    public void testInit_with_nonnull_haskey_dict() {
        Pinyin pinyin = Pinyin.with(new PinyinMapDict() {
                @Override
                public Map<String, String[]> mapping() {
                    Map<String, String[]> map = new HashMap<String, String[]>();
                    map.put("1", new String[]{});
                    return map;
                }
            }).build();

        assertThat(pinyin.mTrieDict.containsMatch("1"), is(true));
    }

    @Test
    public void testInit_with_multi_haskey_dict() {
        Pinyin pinyin = Pinyin.with(new PinyinMapDict() {
                @Override
                public Map<String, String[]> mapping() {
                    Map<String, String[]> map = new HashMap<String, String[]>();
                    map.put("1", new String[]{});
                    return map;
                }
            }).with(new PinyinMapDict() {
                @Override
                public Map<String, String[]> mapping() {
                    Map<String, String[]> map = new HashMap<String, String[]>();
                    map.put("2", new String[]{});
                    return map;
                }
            }).build();

        assertThat(pinyin.mTrieDict.containsMatch("1"), is(true));
        assertThat(pinyin.mTrieDict.containsMatch("2"), is(true));
    }

    @Test
    public void testInit_with_multi_hasduplicatekey_dict() {
        Pinyin pinyin = Pinyin.with(new PinyinMapDict() {
                @Override
                public Map<String, String[]> mapping() {
                    Map<String, String[]> map = new HashMap<String, String[]>();
                    map.put("1", new String[]{"Hello"});
                    return map;
                }
            }).with(new PinyinMapDict() {
                @Override
                public Map<String, String[]> mapping() {
                    Map<String, String[]> map = new HashMap<String, String[]>();
                    map.put("1", new String[]{"world"});
                    return map;
                }
            }).build();

        assertThat(pinyin.mTrieDict.containsMatch("1"), is(true)); // first one in wins
    }


    @Test
    public void testToPinyin_Str_no_dict() {
        Pinyin pinyin = Pinyin.with(null).build();

        String str = "一个测试重庆test,中英文符号；;《>。";
        String expected = "YI GE CE SHI ZHONG QING t e s t , ZHONG YING WEN FU HAO ； ; 《 > 。";

        assertThat(pinyin.toPinyin(str, " "), is(expected));

    }

    @Test
    public void testToPinyin_Str_empty_dict() {
        Pinyin pinyin = Pinyin.with(new PinyinMapDict() {
                @Override
                public Map<String, String[]> mapping() {
                    return null;
                }
            }).build();

        String str = "一个测试重庆test,中英文符号；;《>。";
        String expected = "YI GE CE SHI ZHONG QING t e s t , ZHONG YING WEN FU HAO ； ; 《 > 。";

        assertThat(pinyin.toPinyin(str, " "), is(expected));

    }

    @Test
    public void testToPinyin_Str_one_dict() {

        Pinyin pinyin = Pinyin.with(new PinyinMapDict() {
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

    @Test
    public void testToPinyin_Str_multi_dict() {
        Pinyin pinyin = Pinyin.with(new PinyinMapDict() {
                @Override
                public Map<String, String[]> mapping() {
                    Map<String, String[]> map = new HashMap<String, String[]>();
                    map.put("重庆", new String[]{"CHONG", "QING"});
                    return map;
                }
            }).with(new PinyinMapDict() {
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

    @Test
    public void testToPinyin_Str_overlap() {
        Pinyin pinyin = Pinyin.with(new PinyinMapDict() {
            @Override
            public Map<String, String[]> mapping() {
                Map<String, String[]> map = new HashMap<String, String[]>();
                map.put("中国人", new String[]{"SHOULD", "NOT", "MATCH"});
                map.put("中国人民", new String[]{"ZHONG", "GUO", "REN", "MIN"});
                return map;
            }
        }).build();

        String str = "一个测试中国人民很赞";
        String expected = "YI GE CE SHI ZHONG GUO REN MIN HEN ZAN";

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
}
