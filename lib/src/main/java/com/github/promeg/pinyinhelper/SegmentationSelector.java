package com.github.promeg.pinyinhelper;

import com.hankcs.algorithm.AhoCorasickDoubleArrayTrie;

import java.util.List;

/**
 * 分词选择算法应实现的接口
 *
 * Created by guyacong on 2016/12/28.
 */

public interface SegmentationSelector<T> {
    List<AhoCorasickDoubleArrayTrie<T>.Hit<T>> select(List<AhoCorasickDoubleArrayTrie<T>.Hit<T>> hits);
}
