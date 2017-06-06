package com.github.promeg.pinyinhelper;

import org.ahocorasick.trie.Emit;
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
        List<Emit> list = new ArrayList<Emit>();
        list.add(new Emit(0, 4, null));

        List<Emit> result = mSelector.select(list);

        assertThat(result.size(), is(1));
        assertThat(result.get(0).getStart(), is(0));
        assertThat(result.get(0).getEnd(), is(4));

    }

    @Test
    public void select_multi_hit_no_overlap() throws Exception {
        List<Emit> list = new ArrayList<Emit>();
        list.add(new Emit(0, 5, null));
        list.add(new Emit(7, 8, null));
        list.add(new Emit(9, 10, null));

        List<Emit> result = mSelector.select(list);

        assertThat(result.size(), is(3));
        assertThat(result.get(0).getStart(), is(0));
        assertThat(result.get(0).getEnd(), is(5));

        assertThat(result.get(1).getStart(), is(7));
        assertThat(result.get(1).getEnd(), is(8));

        assertThat(result.get(2).getStart(), is(9));
        assertThat(result.get(2).getEnd(), is(10));

    }

    @Test
    public void select_multi_hit_with_overlap() throws Exception {
        List<Emit> list = new ArrayList<Emit>();
        list.add(new Emit(0, 4, null));
        list.add(new Emit(0, 4, null));
        list.add(new Emit(0, 5, null));
        list.add(new Emit(2, 3, null));
        list.add(new Emit(2, 10, null));
        list.add(new Emit(5, 7, null));
        list.add(new Emit(7, 8, null));
        list.add(new Emit(8, 9, null));

        List<Emit> result = mSelector.select(list);

        assertThat(result.size(), is(2));
        assertThat(result.get(0).getStart(), is(0));
        assertThat(result.get(0).getEnd(), is(5));

        assertThat(result.get(1).getStart(), is(7));
        assertThat(result.get(1).getEnd(), is(8));

    }

    @Test
    public void select_null_return_null() throws Exception {

        List<Emit> result = mSelector.select(null);

        assertThat(result, nullValue());

    }

}
//CHECKSTYLE:ON
