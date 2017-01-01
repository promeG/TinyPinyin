package com.github.promeg.pinyinhelper;

import java.util.Set;

/**
 * Created by guyacong on 2016/12/23.
 */

public interface PinyinDict {
    Set<String> words();

    String[] toPinyin(String word);
}
