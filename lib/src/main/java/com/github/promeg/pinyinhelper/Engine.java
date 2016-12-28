package com.github.promeg.pinyinhelper;

import com.hankcs.algorithm.AhoCorasickDoubleArrayTrie;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by guyacong on 2016/12/23.
 */

final class Engine {

    static final HitComparator HIT_COMPARATOR = new HitComparator();

    private Engine() {
        //no instance
    }

    static String toPinyin(final String inputStr, final AhoCorasickDoubleArrayTrie<String[]> trie,
            final String separator, final SegmentationSelector<String[]> selector) {

        if (trie == null || trie.size() == 0 || selector == null) {
            // 没有提供字典或选择器，按单字符转换输出
            StringBuffer resultPinyinStrBuf = new StringBuffer();
            for (int i = 0; i < inputStr.length(); i++) {
                resultPinyinStrBuf.append(Pinyin.toPinyin(inputStr.charAt(i)));
                if (i != inputStr.length() - 1) {
                    resultPinyinStrBuf.append(separator);
                }
            }
            return resultPinyinStrBuf.toString();
        }

        List<AhoCorasickDoubleArrayTrie<String[]>.Hit<String[]>> selectedHits = selector.select(trie.parseText(inputStr));

        Collections.sort(selectedHits, HIT_COMPARATOR);

        StringBuffer resultPinyinStrBuf = new StringBuffer();

        int nextHitIndex = 0;

        for (int i = 0; i < inputStr.length();) {
            // 首先确认是否有以第i个字符作为begin的hit
            if (nextHitIndex < selectedHits.size() && i == selectedHits.get(nextHitIndex).begin) {
                // 有以第i个字符作为begin的hit
                String[] fromDicts = selectedHits.get(nextHitIndex).value;
                for (int j = 0; j < fromDicts.length; j++) {
                    resultPinyinStrBuf.append(fromDicts[j].toUpperCase());
                    if (j != fromDicts.length - 1) {
                        resultPinyinStrBuf.append(separator);
                    }
                }

                i = i + (selectedHits.get(nextHitIndex).end - selectedHits.get(nextHitIndex).begin);
                nextHitIndex++;
            } else {
                // 将第i个字符转为拼音
                resultPinyinStrBuf.append(Pinyin.toPinyin(inputStr.charAt(i)));
                i++;
            }

            if (i != inputStr.length()) {
                resultPinyinStrBuf.append(separator);
            }
        }

        return resultPinyinStrBuf.toString();
    }

    static final class HitComparator implements Comparator<AhoCorasickDoubleArrayTrie<java.lang.String[]>.Hit<String[]>> {

        @Override
        public int compare(AhoCorasickDoubleArrayTrie<String[]>.Hit<String[]> o1,
                AhoCorasickDoubleArrayTrie<String[]>.Hit<String[]> o2) {
            if (o1.begin == o2.begin) {
                // 起点相同时，更长的排前面
                int o1Length = o1.end - o1.begin;
                int o2Length = o2.end - o2.begin;
                return (o1Length < o2Length) ? 1 : ((o1Length == o2Length) ? 0 : -1);
            } else {
                // 起点小的放前面
                return (o1.begin < o2.begin) ? -1 : ((o1.begin == o2.begin) ? 0 : 1);
            }
        }
    }

}
