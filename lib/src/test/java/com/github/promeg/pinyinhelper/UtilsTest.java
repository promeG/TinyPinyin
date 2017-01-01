package com.github.promeg.pinyinhelper;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by guyacong on 2017/1/1.
 */
public class UtilsTest {

    @Test
    public void dictsToTrie_null_should_return_null() throws Exception {
        assertThat(Utils.dictsToTrie(null), nullValue());
    }

    @Test
    public void dictsToTrie_empty_list_should_return_null() throws Exception {
        List<PinyinDict> dicts = new ArrayList<PinyinDict>();

        assertThat(Utils.dictsToTrie(dicts), nullValue());
    }

    @Test
    public void dictsToTrie_empty_dicts_should_return_null() throws Exception {
        List<PinyinDict> dicts = new ArrayList<PinyinDict>();
        dicts.add(new PinyinMapDict() {
            @Override
            public Map<String, String[]> mapping() {
                return null;
            }
        });

        assertThat(Utils.dictsToTrie(dicts), nullValue());
    }

    @Test
    public void dictsToTrie_null_item__should_return_null() throws Exception {
        List<PinyinDict> dicts = new ArrayList<PinyinDict>();
        dicts.add(null);

        assertThat(Utils.dictsToTrie(dicts), nullValue());
    }

    @Test
    public void dictsToTrie_nonnull_should_return_trie() throws Exception {
        List<PinyinDict> dicts = new ArrayList<PinyinDict>();
        dicts.add(new PinyinMapDict() {
            @Override
            public Map<String, String[]> mapping() {
                Map<String, String[]> map = new HashMap<String, String[]>();
                map.put("1", null);
                return map;
            }
        });

        assertThat(Utils.dictsToTrie(dicts), notNullValue());
    }

}
