package com.github.promeg.pinyinhelper;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by guyacong on 2017/1/1.
 */
public class PinyinMapDictTest {

    @Test
    public void words_null_map_return_null() throws Exception {
        PinyinMapDict dict = new PinyinMapDict() {
            @Override
            public Map<String, String[]> mapping() {
                return null;
            }
        };

        assertThat(dict.words(), nullValue());
    }

    @Test
    public void words_nonnull_map_return_keyset() throws Exception {
        final Map<String, String[]> map = new HashMap<String, String[]>();
        map.put("1", null);

        PinyinMapDict dict = new PinyinMapDict() {
            @Override
            public Map<String, String[]> mapping() {
                return map;
            }
        };

        assertThat(dict.words(), is(map.keySet()));
    }

    @Test
    public void toPinyin_null_map_return_null() throws Exception {
        PinyinMapDict dict = new PinyinMapDict() {
            @Override
            public Map<String, String[]> mapping() {
                return null;
            }
        };

        assertThat(dict.toPinyin("1"), nullValue());
    }

    @Test
    public void toPinyin_nonnull_map_return_value() throws Exception {
        final Map<String, String[]> map = new HashMap<String, String[]>();
        map.put("1", new String[]{"ONE"});

        PinyinMapDict dict = new PinyinMapDict() {
            @Override
            public Map<String, String[]> mapping() {
                return map;
            }
        };

        assertThat(dict.toPinyin("1"), is(map.get("1")));
    }

    @Test
    public void toPinyin_nonnull_map_nokey_return_null() throws Exception {
        final Map<String, String[]> map = new HashMap<String, String[]>();
        map.put("1", new String[]{"ONE"});

        PinyinMapDict dict = new PinyinMapDict() {
            @Override
            public Map<String, String[]> mapping() {
                return map;
            }
        };

        assertThat(dict.toPinyin("2"), nullValue());
    }

}
