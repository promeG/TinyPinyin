package com.github.promeg.pinyinhelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;

/**
 * Created by guyacong on 2017/1/2.
 */

final class BenchmarkUtils {

    private static String SAMPLE_STR = null;

    static String genRandomString(int length) {
        Random random = new Random();
        int start = random.nextInt(fullSampleStr().length() - length);

        return fullSampleStr().substring(start, start + length);
    }

    private static String fullSampleStr() {
        if (SAMPLE_STR == null) {
            synchronized (BenchmarkUtils.class) {
                if (SAMPLE_STR == null) {
                    StringBuilder sb = new StringBuilder();
                    BufferedReader reader = null;
                    try {
                        ClassLoader classloader = Thread.currentThread().getContextClassLoader();

                        InputStream is = classloader.getResourceAsStream("sample.txt");
                        reader = new BufferedReader(new InputStreamReader(is));
                        String line;
                        while ((line = reader.readLine()) != null) {
                            sb.append(line);
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

                    SAMPLE_STR = sb.toString();
                }
            }
        }
        return SAMPLE_STR;

    }

    private BenchmarkUtils() {
        //no instance
    }


}
