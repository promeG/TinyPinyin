package com.github.promeg.pinyinhelper;

import com.hankcs.algorithm.AhoCorasickDoubleArrayTrie;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

/**
 * Created by guyacong on 2016/12/28.
 */
//CHECKSTYLE:OFF
public class ForwardLongestSelectorTest {

    ForwardLongestSelector mSelector = new ForwardLongestSelector();

    @Test
    public void select_single_hit() throws Exception {
        List<AhoCorasickDoubleArrayTrie<String[]>.Hit<String[]>> list = new ArrayList<AhoCorasickDoubleArrayTrie<java.lang.String[]>.Hit<String[]>>();
        list.add(new AhoCorasickDoubleArrayTrie<String[]>().new Hit<String[]>(0, 4, null));

        List<AhoCorasickDoubleArrayTrie<String[]>.Hit<String[]>> result = mSelector.select(list);

        assertThat(result.size(), is(1));
        assertThat(result.get(0).begin, is(0));
        assertThat(result.get(0).end, is(4));

    }

    @Test
    public void select_multi_hit_no_overlap() throws Exception {
        List<AhoCorasickDoubleArrayTrie<String[]>.Hit<String[]>> list = new ArrayList<AhoCorasickDoubleArrayTrie<java.lang.String[]>.Hit<String[]>>();
        list.add(new AhoCorasickDoubleArrayTrie<String[]>().new Hit<String[]>(0, 5, null));
        list.add(new AhoCorasickDoubleArrayTrie<String[]>().new Hit<String[]>(7, 8, null));
        list.add(new AhoCorasickDoubleArrayTrie<String[]>().new Hit<String[]>(9, 10, null));

        List<AhoCorasickDoubleArrayTrie<String[]>.Hit<String[]>> result = mSelector.select(list);

        assertThat(result.size(), is(3));
        assertThat(result.get(0).begin, is(0));
        assertThat(result.get(0).end, is(5));

        assertThat(result.get(1).begin, is(7));
        assertThat(result.get(1).end, is(8));

        assertThat(result.get(2).begin, is(9));
        assertThat(result.get(2).end, is(10));

    }

    @Test
    public void select_multi_hit_with_overlap() throws Exception {
        List<AhoCorasickDoubleArrayTrie<String[]>.Hit<String[]>> list = new ArrayList<AhoCorasickDoubleArrayTrie<java.lang.String[]>.Hit<String[]>>();
        list.add(new AhoCorasickDoubleArrayTrie<String[]>().new Hit<String[]>(0, 4, null));
        list.add(new AhoCorasickDoubleArrayTrie<String[]>().new Hit<String[]>(0, 4, null));
        list.add(new AhoCorasickDoubleArrayTrie<String[]>().new Hit<String[]>(0, 5, null));
        list.add(new AhoCorasickDoubleArrayTrie<String[]>().new Hit<String[]>(2, 3, null));
        list.add(new AhoCorasickDoubleArrayTrie<String[]>().new Hit<String[]>(2, 10, null));
        list.add(new AhoCorasickDoubleArrayTrie<String[]>().new Hit<String[]>(5, 7, null));
        list.add(new AhoCorasickDoubleArrayTrie<String[]>().new Hit<String[]>(7, 8, null));
        list.add(new AhoCorasickDoubleArrayTrie<String[]>().new Hit<String[]>(8, 9, null));

        List<AhoCorasickDoubleArrayTrie<String[]>.Hit<String[]>> result = mSelector.select(list);

        assertThat(result.size(), is(2));
        assertThat(result.get(0).begin, is(0));
        assertThat(result.get(0).end, is(5));

        assertThat(result.get(1).begin, is(7));
        assertThat(result.get(1).end, is(8));

    }

    @Test
    public void select_null_return_null() throws Exception {

        List<AhoCorasickDoubleArrayTrie<String[]>.Hit<String[]>> result = mSelector.select(null);

        assertThat(result, nullValue());

    }

}
//CHECKSTYLE:ON
