package com.github.promeg.tinypinyin.lexicons.android.cncity;

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
@RunWith(RobolectricTestRunner.class)
@Config(sdk = 23, constants = BuildConfig.class, application = TestApp.class)
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
        assertThat(words.size(), is(2594));
    }

    @Test
    public void toPinyin() throws Exception {
        Set<String> words = mDict.mapping().keySet();
        for (String word : words) {
            String[] pinyins = mDict.mapping().get(word);

            assertThat(word.length(), is(pinyins.length));
        }
    }
}