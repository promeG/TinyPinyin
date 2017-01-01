package com.github.promeg.pinyinhelper;

import org.ahocorasick.trie.Trie;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by guyacong on 2015/9/28.
 */
public final class Pinyin {

    final Trie mTrieDict;
    final SegmentationSelector mSelector;
    final List<PinyinDict> mPinyinDicts;

    private Pinyin(List<PinyinDict> pinyinDicts, SegmentationSelector selector) {
        mPinyinDicts = Collections.unmodifiableList(pinyinDicts);
        mTrieDict = Utils.dictsToTrie(pinyinDicts);
        mSelector = selector;
    }

    public static Builder with(PinyinDict dict) {
        return new Builder(dict);
    }

    public String toPinyin(String str, String separator) {
        return Engine.toPinyin(str, mTrieDict, mPinyinDicts, separator, mSelector);
    }

    /**
     * return pinyin if c is chinese in uppercase, String.valueOf(c) otherwise.
     */
    public static String toPinyin(char c) {
        if (isChinese(c)) {
            if (c == PinyinData.CHAR_12295) {
                return PinyinData.PINYIN_12295;
            } else {
                return PinyinData.PINYIN_TABLE[getPinyinCode(c)];
            }
        } else {
            return String.valueOf(c);
        }
    }

    /**
     * return whether c is chinese
     */
    public static boolean isChinese(char c) {
        return (PinyinData.MIN_VALUE <= c && c <= PinyinData.MAX_VALUE
                && getPinyinCode(c) > 0)
                || PinyinData.CHAR_12295 == c;
    }

    private static int getPinyinCode(char c) {
        int offset = c - PinyinData.MIN_VALUE;
        if (0 <= offset && offset < PinyinData.PINYIN_CODE_1_OFFSET) {
            return decodeIndex(PinyinCode1.PINYIN_CODE_PADDING, PinyinCode1.PINYIN_CODE, offset);
        } else if (PinyinData.PINYIN_CODE_1_OFFSET <= offset
                && offset < PinyinData.PINYIN_CODE_2_OFFSET) {
            return decodeIndex(PinyinCode2.PINYIN_CODE_PADDING, PinyinCode2.PINYIN_CODE,
                    offset - PinyinData.PINYIN_CODE_1_OFFSET);
        } else {
            return decodeIndex(PinyinCode3.PINYIN_CODE_PADDING, PinyinCode3.PINYIN_CODE,
                    offset - PinyinData.PINYIN_CODE_2_OFFSET);
        }
    }

    private static short decodeIndex(byte[] paddings, byte[] indexes, int offset) {
        //CHECKSTYLE:OFF
        int index1 = offset / 8;
        int index2 = offset % 8;
        short realIndex;
        realIndex = (short) (indexes[offset] & 0xff);
        //CHECKSTYLE:ON
        if ((paddings[index1] & PinyinData.BIT_MASKS[index2]) != 0) {
            realIndex = (short) (realIndex | PinyinData.PADDING_MASK);
        }
        return realIndex;
    }

    public static final class Builder {

        SegmentationSelector mSelector = null;

        List<PinyinDict> mPinyinDicts = null;

        private Builder(PinyinDict dict) {
            mPinyinDicts = new ArrayList<PinyinDict>();
            if (dict != null) {
                mPinyinDicts.add(dict);
            }
        }

        public Builder with(PinyinDict dict) {
            if (dict != null) {
                mPinyinDicts.add(dict);
            }
            return this;
        }

        // 暂不公开此API
        /*public*/ Builder selector(SegmentationSelector selector) {
            if (selector != null) {
                mSelector = selector;
            }
            return this;
        }

        public Pinyin build() {
            // mSelector为null时，默认使用ForwardLongestSelector
            return new Pinyin(mPinyinDicts, mSelector == null ? new ForwardLongestSelector() : mSelector);
        }
    }
}
