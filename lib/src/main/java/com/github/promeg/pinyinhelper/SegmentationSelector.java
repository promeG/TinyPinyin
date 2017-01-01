package com.github.promeg.pinyinhelper;

import org.ahocorasick.trie.Emit;

import java.util.Collection;
import java.util.List;

/**
 * 分词选择算法应实现的接口
 *
 * Created by guyacong on 2016/12/28.
 */

public interface SegmentationSelector {
    List<Emit> select(Collection<Emit> emits);
}
