package com.github.promeg.tinypinyin.lexicons.java.cncity;

import com.github.promeg.pinyinhelper.PinyinMapDict;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by guyacong on 2016/12/23.
 */
public final class CnCityDict extends PinyinMapDict {

    static volatile CnCityDict singleton = null;

    final Map<String, String[]> mDict;

    private CnCityDict() {
        mDict = new HashMap<String, String[]>();
        init();
    }

    public static CnCityDict getInstance() {
        if (singleton == null) {
            synchronized (CnCityDict.class) {
                if (singleton == null) {
                    singleton = new CnCityDict();
                }
            }
        }
        return singleton;
    }


    @Override
    public Map<String, String[]> mapping() {
        return mDict;
    }

    private void init() {
        BufferedReader reader = null;
        try {
            ClassLoader classloader = Thread.currentThread().getContextClassLoader();

            InputStream is = classloader.getResourceAsStream("tinypinyin/cncity.txt");
            reader = new BufferedReader(new InputStreamReader(is, "utf-8"));
            String line;
            while ((line = reader.readLine()) != null) {
                // process the line.
                String[] keyAndValue = line.split("\\s+");
                if (keyAndValue != null && keyAndValue.length == 2) {
                    String[] pinyinStrs = keyAndValue[0].split("'");
                    mDict.put(keyAndValue[1], pinyinStrs);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
