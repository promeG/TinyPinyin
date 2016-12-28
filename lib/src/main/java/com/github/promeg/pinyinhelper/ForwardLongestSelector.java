package com.github.promeg.pinyinhelper;

import com.hankcs.algorithm.AhoCorasickDoubleArrayTrie;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 正向最大匹配
 *
 * Created by guyacong on 2016/12/28.
 */

final class ForwardLongestSelector implements SegmentationSelector<String[]> {

    static final Engine.HitComparator HIT_COMPARATOR = new Engine.HitComparator();

    @Override
    public List<AhoCorasickDoubleArrayTrie<String[]>.Hit<String[]>> select(
            final List<AhoCorasickDoubleArrayTrie<String[]>.Hit<String[]>> hits) {
        if (hits == null) {
            return hits;
        }

        List<AhoCorasickDoubleArrayTrie<String[]>.Hit<String[]>> results = new ArrayList<AhoCorasickDoubleArrayTrie<java.lang.String[]>.Hit<String[]>>(hits);

        Collections.sort(hits, HIT_COMPARATOR);

        int endValueToRemove = -1;

        for (AhoCorasickDoubleArrayTrie.Hit hit : hits) {
            if (hit.begin > endValueToRemove && hit.end > endValueToRemove) {
                endValueToRemove = hit.end;
            } else {
                results.remove(hit);
            }
        }

        return results;
    }
}
