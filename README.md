# TinyPinyin

适用于Java和Android的快速、低内存占用的汉字转拼音库。

当前稳定版本：2.0.3

## 特性

1. 生成的拼音不包含声调，均为大写；
2. 支持自定义词典，支持简体中文、繁体中文；
3. 执行效率很高(Pinyin4J的4~16倍)；
4. 很低的内存占用（不添加词典时小于30KB）。

## 原理介绍

[打造最好的Java拼音库TinyPinyin（一）：单字符转拼音的极致优化](https://github.com/promeG/promeg.github.io/blob/master/_posts/2017-03-19-tinypinyin-part-1.markdown)

[打造最好的Java拼音库TinyPinyin（二）：多音字快速处理方案](https://github.com/promeG/promeg.github.io/blob/master/_posts/2017-03-20-tinypinyin-part-2.markdown)

[打造最好的Java拼音库TinyPinyin（三）：API设计和测试实践](https://github.com/promeG/promeg.github.io/blob/master/_posts/2017-03-22-tinypinyin-part-3.markdown)

## 使用

### 汉字转拼音API

```java
/**
 * 如果c为汉字，则返回大写拼音；如果c不是汉字，则返回String.valueOf(c)
 */
String Pinyin.toPinyin(char c)

/**
 * c为汉字，则返回true，否则返回false
 */
boolean Pinyin.isChinese(char c)

/**
 * 将输入字符串转为拼音，转换过程中会使用之前设置的用户词典，以字符为单位插入分隔符
 */
String toPinyin(String str, String separator)
```

### 词典API

```java
// 添加中文城市词典
Pinyin.init(Pinyin.newConfig().with(CnCityDict.getInstance());

// 添加自定义词典
Pinyin.init(Pinyin.newConfig()
            .with(new PinyinMapDict() {
                @Override
                public Map<String, String[]> mapping() {
                    HashMap<String, String[]> map = new HashMap<String, String[]>();
                    map.put("重庆",  new String[]{"CHONG", "QING"});
                    return map;
                }
            }));
```

### 添加到工程

```groovy
buildscript {
  repositories {
    maven {
      url 'https://maven.aliyun.com/repository/public'
    }
    mavenCentral()
  }

  dependencies {
    compile 'com.github.promeg:tinypinyin:2.0.3' // TinyPinyin核心包，约80KB

    compile 'com.github.promeg:tinypinyin-lexicons-android-cncity:2.0.3' // 可选，适用于Android的中国地区词典

    compile 'com.github.promeg:tinypinyin-lexicons-java-cncity:2.0.3' // 可选，适用于Java的中国地区词典
  }
}
```

## 详细说明

### 1\. 设计目标

#### Pinyin4J的问题

1. Jar文件较大，205KB；
2. Pinyin4J的PinyinHelper.toHanyuPinyinStringArray 在第一次调用时耗时非常长（~2000ms）；
3. 功能臃肿，许多情况下我们不需要声调、方言；
4. 无法添加自定义词典，进而无法有效处理多音字
5. 内存占用太高；

#### TinyPinyin特性

1. 转换后的结果**不**包含声调和方言；
2. 支持自定义词典，方便处理多音字；
3. 尽可能低的内存占用；
4. 比Pinyin4J更快的转换速度;

### 2\. Correctness

以Pinyin4J作为基准，确保对所有的字符（Character.MAX_VALUE ~ Character.MIN_VALUE），TinyPinyin与Pinyin4J有相同的返回结果。

（Pinyin4J采用无声调的输出，多音字取第一个拼音进行对比）

该部分请见PinyinTest.java

繁体中文的测试请见：PinyinTest.testToPinyin_traditional_chars()

采用以下命令运行test：

```groovy
./gradlew clean build :lib:test :tinypinyin-lexicons-android-cncity:test :tinypinyin-android-asset-lexicons:test :android-sample:connectedAndroidTest
```

### 3\. Effectiveness

#### 速度

使用[JMH](http://openjdk.java.net/projects/code-tools/jmh/)工具得到bechmark，对比TinyPinyin和Pinyin4J的运行速度。

具体测例请见lib/src/jmh/中的性能测试代码。

采用以下命令运行benchmark：

```groovy
./gradlew jmh
```

生成的报告在 pinyinhelper/build/reports/jmh/ 中。

性能测试结果简要说明：单个字符转拼音的速度是Pinyin4j的**四倍**，添加字典后字符串转拼音的速度是Pinyin4j的**16倍**。

详细测试结果：

Benchmark | Mode  | Samples | Score |  Unit
-------------------------- | --- | ----- | ---- | ----
TinyPinyin_Init_With_Large_Dict（初始化大词典）| thrpt | 200 | 66.131 | ops/s
TinyPinyin_Init_With_Small_Dict（初始化小词典）  | thrpt | 200 | 35408.045 | ops/s
TinyPinyin_StringToPinyin_With_Large_Dict（添加大词典后进行String转拼音） | thrpt | 200 | 16.268 | ops/ms
Pinyin4j_StringToPinyin（Pinyin4j的String转拼音） | thrpt | 200 | 1.033 | ops/ms
TinyPinyin_CharToPinyin（字符转拼音） | thrpt | 200 | 14.285 | ops/us
Pinyin4j_CharToPinyin（Pinyin4j的字符转拼音）| thrpt | 200 | 4.460 | ops/us
TinyPinyin_IsChinese（字符是否为汉字） | thrpt | 200 | 15.552 | ops/us
Pinyin4j_IsChinese（Pinyin4j的字符是否为汉字） | thrpt | 200 | 4.432 | ops/us

#### 内存占用

##### 1. 不添加词典时

+ 3个static byte[7000] 存储所有汉字的拼音的低8位，占用7000 _1_ 3 = 21KB 内存；
+ 3个static byte[7000/8] 存储所有汉字的拼音的第9位（最高位），占用7000 / 8 _1_ 3 = 3KB 内存；
+ 一个String[408] 存储所有可能的拼音，占用 1.7KB 内存；

共占用 < 30KB.

##### 2. 添加词典时

使用‘com.github.promeg:tinypinyin-lexicons-java-cncity:2.0.0’时，额外消耗约43KB内存。

## Todo

+ ~~支持繁体中文~~
+ 支持姓氏拼音
+ 压缩词库
+ 词库生成工具
