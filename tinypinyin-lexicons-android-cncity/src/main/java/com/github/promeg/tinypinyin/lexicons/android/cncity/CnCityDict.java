package com.github.promeg.tinypinyin.lexicons.android.cncity;

import com.github.promeg.pinyinhelper.PinyinMapDict;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by guyacong on 2016/12/23.
 */
public final class CnCityDict extends PinyinMapDict {

    static volatile CnCityDict singleton = null;

    final Context mContext;

    final Map<String, String[]> mDict;

    private CnCityDict(Context context) {
        mContext = context.getApplicationContext();
        mDict = new HashMap<>();
        init();
    }

    public static CnCityDict getInstance(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("context == null");
        }
        if (singleton == null) {
            synchronized (CnCityDict.class) {
                if (singleton == null) {
                    singleton = new CnCityDict(context);
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
            reader = new BufferedReader(new InputStreamReader(mContext.getAssets().open("cncity.txt")));
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
