package com.github.promeg.tinypinyin.android_sample;

import com.github.promeg.pinyinhelper.Pinyin;
import com.github.promeg.tinypinyin.lexicons.android.cncity.CnCityDict;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends Activity {

    TextView mTvText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTvText = (TextView) findViewById(R.id.mTvText);

        Pinyin.init(Pinyin.newConfig().with(CnCityDict.getInstance(MainActivity.this)));

        mTvText.setText(Pinyin.toPinyin("中国重庆", ""));
    }
}
