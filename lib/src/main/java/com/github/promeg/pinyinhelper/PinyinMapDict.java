package com.github.promeg.pinyinhelper;

import java.util.Map;
import java.util.Set;

/**
 * Created by guyacong on 2016/12/23.
 */

public abstract class PinyinMapDict implements PinyinDict {

    public abstract Map<String, String[]> mapping();


    @Override
    public Set<String> words() {
        return mapping() != null ? mapping().keySet() : null;
    }

    @Override
    public String[] toPinyin(String word) {
        return mapping() != null ? mapping().get(word) : null;
    }
}
