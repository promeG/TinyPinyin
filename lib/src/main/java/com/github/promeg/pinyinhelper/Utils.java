package com.github.promeg.pinyinhelper;

import com.hankcs.algorithm.AhoCorasickDoubleArrayTrie;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by guyacong on 2016/12/28.
 */

final class Utils {

    private Utils() {
        //no instance
    }

    static AhoCorasickDoubleArrayTrie<String[]> dictsToTrie(List<PinyinDict> pinyinDicts) {
        Map<String, String[]> all = new HashMap<String, String[]>();

        if (pinyinDicts != null) {
            for (int i = pinyinDicts.size() - 1; i >= 0; i--) {
                PinyinDict dict = pinyinDicts.get(i);
                if (dict != null && dict.mapping() != null) {
                    all.putAll(dict.mapping());
                }
            }
            if (all.size() > 0) {
                AhoCorasickDoubleArrayTrie<String[]> trie
                        = new AhoCorasickDoubleArrayTrie<String[]>();
                trie.build(all);
                return trie;
            }
        }

        return null;
    }
}
