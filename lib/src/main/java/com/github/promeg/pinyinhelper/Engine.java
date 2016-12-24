package com.github.promeg.pinyinhelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guyacong on 2016/12/23.
 */

final class Engine {

    private Engine() {
        //no instance
    }

    //取词的最大长度，必须大于0
    static final int WORD_MAX_LENGTH = 6;

    public static String toPinyin(String inputStr, List<PinyinDict> pinyinDictSet, String separator) {
        if (pinyinDictSet == null || pinyinDictSet.size() == 0) {
            // 没有提供字典，按单字符转换输出
            StringBuffer resultPinyinStrBuf = new StringBuffer();
            for (int i = 0; i < inputStr.length(); i++) {
                resultPinyinStrBuf.append(Pinyin.toPinyin(inputStr.charAt(i)));
                if (i != inputStr.length() - 1) {
                    resultPinyinStrBuf.append(separator);
                }
            }
            return resultPinyinStrBuf.toString();
        }

        List<String> segWords = new ArrayList<String>();

        String word;
        int wordLength;
        int position;
        int segLength = 0;

        // 开始分词，循环以下操作，直到全部完成
        while (segLength < inputStr.length()) {
            if ((inputStr.length() - segLength) < WORD_MAX_LENGTH) {
                wordLength = inputStr.length() - segLength;
            } else {
                wordLength = WORD_MAX_LENGTH;
            }

            position = segLength;
            word = inputStr.substring(position, position + wordLength);

            while (!dictSetContains(word, pinyinDictSet)) {
                if (word.length() == 1) {
                    break;
                }

                word = word.substring(0, word.length() - 1);
            }

            segWords.add(word);
            segLength += word.length();
        }

        StringBuffer resultPinyinStrBuf = new StringBuffer();
        for (int i = 0; i < segWords.size(); i++) {
            String wordStr = segWords.get(i);

            if (wordStr.length() == 1) {
                resultPinyinStrBuf.append(Pinyin.toPinyin(wordStr.charAt(0)));
            } else {
                String[] fromDicts = pinyinFromDict(wordStr, pinyinDictSet);
                for (int j = 0; j < fromDicts.length; j++) {
                    resultPinyinStrBuf.append(fromDicts[j].toUpperCase());
                    if (j != fromDicts.length - 1) {
                        resultPinyinStrBuf.append(separator);
                    }
                }
            }

            if (i != segWords.size() - 1) {
                resultPinyinStrBuf.append(separator);
            }
        }
        return resultPinyinStrBuf.toString();
    }

    static boolean  dictSetContains(String word, List<PinyinDict> pinyinDictSet) {
        if (pinyinDictSet != null) {
            for (PinyinDict dict : pinyinDictSet) {
                if (dict != null && dict.mapping() != null
                        && dict.mapping().containsKey(word)) {
                    return true;
                }
            }
        }
        return false;
    }

    static String[] pinyinFromDict(String wordInDict, List<PinyinDict> pinyinDictSet) {
        if (pinyinDictSet != null) {
            for (PinyinDict dict : pinyinDictSet) {
                if (dict != null && dict.mapping() != null
                        && dict.mapping().containsKey(wordInDict)) {
                    return dict.mapping().get(wordInDict);
                }
            }
        }
        throw new IllegalArgumentException("No pinyin dict contains word: " + wordInDict);
    }

}
