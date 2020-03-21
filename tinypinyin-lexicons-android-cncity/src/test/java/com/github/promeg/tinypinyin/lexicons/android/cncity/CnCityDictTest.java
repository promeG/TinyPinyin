package com.github.promeg.tinypinyin.lexicons.android.cncity;

import com.github.promeg.pinyinhelper.Pinyin;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Created by guyacong on 2016/12/23.
 */
//CHECKSTYLE:OFF
@RunWith(RobolectricTestRunner.class)
@Config(sdk = 23, application = TestApp.class)
public class CnCityDictTest {
    CnCityDict mDict;

    @Before
    public void setUp() {
        mDict =  CnCityDict.getInstance(RuntimeEnvironment.application);
    }

    @Test
    public void words() throws Exception {
        Set<String> words = mDict.mapping().keySet();

        assertThat(words.contains(null), is(false));
        assertThat(words.size(), is(97));
    }

    @Test
    public void toPinyin() throws Exception {
        Set<String> words = mDict.mapping().keySet();
        for (String word : words) {
            String[] pinyins = mDict.mapping().get(word);

            assertThat(word.length(), is(pinyins.length));
        }
    }

    @Test
    public void toPinyin_test_not_same_with_PinyinOrigin() throws Exception {
        Set<String> words = mDict.mapping().keySet();
        for (String word : words) {

            String[] originPinyins = new String[word.length()];


            for (int i = 0; i < word.length(); i++) {
                originPinyins[i] = Pinyin.toPinyin(word.charAt(i));
            }


            String[] pinyins = mDict.mapping().get(word);


            boolean hasDifferent = false;

            for (int i = 0; i < word.length(); i++) {
                if (!originPinyins[i].equalsIgnoreCase(pinyins[i])) {
                    hasDifferent = true;
                    break;
                }
            }

            assertThat(hasDifferent, is(true));
        }
    }

    // 字典返回的拼音应该只包含英文字母, fix issue 5
    @Test
    public void test_pinyin_only_contains_letters() throws Exception {
        Set<String> words = mDict.mapping().keySet();
        for (String word : words) {
            String[] pinyins = mDict.mapping().get(word);

            for (String pinyin : pinyins) {
                assertThat(pinyin.matches("[a-zA-Z]+"), is(true));
            }
        }
    }


}
//CHECKSTYLE:ON
