# TinyPinyin
适用于Java和Android的快速、低内存占用的汉字转拼音库。

## 特性

1.	生成的拼音不包含声调，也不处理多音字，默认一个汉字对应一个拼音；
2.	拼音均为大写；
3.	无需初始化，执行效率很高(Pinyin4J的4倍)；
4.	很低的内存占用（小于30KB）。

## 使用

### API

```java
/**
 * 如果c为汉字，则返回大写拼音；如果c不是汉字，则返回String.valueOf(c)
 */
String Pinyin.toPinyin(char c)

/**
 * c为汉字，则返回true，否则返回false
 */
boolean Pinyin.isChinese(char c)
```

### 添加到工程

```groovy
buildscript {
  repositories {
    jcenter()
  }

  dependencies {
    compile 'com.github.promeg:tinypinyin:1.0.0' // ~80KB
  }
}
```

## 详细说明

### 1. 设计目标

#### Pinyin4J的问题

1. Jar文件较大，205KB；
2. Pinyin4J的PinyinHelper.toHanyuPinyinStringArray 在第一次调用时耗时非常长（~2000ms）；
3. 功能臃肿，许多情况下我们不需要声调、方言，（暂时）也不需要处理一字多音的情况；
4. 内存占用太高；

#### TinyPinyin特性

1. 转换后的结果**不**包含声调和方言，也**不**处理多音字，默认一个汉字仅对应一个拼音；
2. 无需初始化，保证多次调用时，有稳定的返回时间；
3. 尽可能低的内存占用；
4. 比Pinyin4J更快的转换速度。


### 2. Correctness

以Pinyin4J作为基准，确保对所有的字符（Character.MAX_VALUE ~ Character.MIN_VALUE），TinyPinyin与Pinyin4J有相同的返回结果。

（Pinyin4J采用无声调的输出，多音字取第一个拼音进行对比）

该部分请见PinyinTest.java

采用以下命令运行test： 

```groovy
./gradlew :lib:test
```

### 3. Effectiveness

#### 速度

使用[JMH](http://openjdk.java.net/projects/code-tools/jmh/)工具得到bechmark，对比TinyPinyin和Pinyin4J的运行速度。 

具体测例请见PinyinSampleBenchmark.java。

采用以下命令运行benchmark： 

```groovy
./gradlew :lib:jmh
```

生成的报告在 pinyinhelper/build/reports/jmh/ 中，运行一次约耗时 5min。

| Benchmark  | Mode | Samples | Score | Unit |
| -------------: | :-------------: | :------------: | ------:| :------: |
| TinyPinyin.isChinese       | thrpt  | 40 | 181 | ops/us |
| TinyPinyin.isChinese(内存优化后)       | thrpt  | 40 | 185 | ops/us |
| Pinyin4J.isChinese  | thrpt  | 40 | 39  | ops/us |
| TinyPinyin.toPinyin       | thrpt  | 40 | 174 | ops/us |
| TinyPinyin.toPinyin(内存优化后)       | thrpt  | 40 | 160 | ops/us |
| Pinyin4J.toPinyin  | thrpt  | 40 | 40| ops/us |

#### 内存占用

1. 3个static byte[7000] 存储所有汉字的拼音的低8位，占用7000 * 1 * 3 = 21KB 内存；
2. 3个static byte[7000/8] 存储所有汉字的拼音的第9位（最高位），占用7000 / 8 * 1 * 3 = 3KB 内存；
2. 一个String[408] 存储所有可能的拼音，占用 1.7KB 内存；

共占用 < 30KB.


### 4. 遇到的问题

1. 存储所有汉字拼音的数组长约21000，但硬编码到一个数组中，java编译会失败：code too large（[原因](http://stackoverflow.com/questions/2407912/code-too-large-compilation-error-in-java)）。 采用将数组分割为3个，并放置到三个类中即可解决。
2. 汉字中有一个异类：unicode 12295，出了它之外，剩余汉字均分布在19968 ~ 40869之间，为了尽可能的减小存储拼音的数组大小，对12295做单独处理，其它汉字用short[40869-19968]存储即可，offset为19968。

### 5. 下一步改进

**注：该项改进已于2015-9-30日完成**

由于汉字拼音共有407个，因此需要9位来表示一个拼音。Java中byte为8位，short为16位，因此目前采用short来表示一个拼音。

但使用short造成了较大的浪费，每个拼音编码浪费了16 - 9 = 7位，也就是说，理想情况下我们可以将存储所有汉字拼音的42KB内存优化到 42*9/16 = 24KB。

思路是使用byte[21000]存储每个汉字的低8位拼音编码，另外采用byte[21000/8]来存储每个汉字第9位（最高位）的编码，每个byte可存储8个汉字的第9位编码。 共耗用内存21KB + 3KB = 24KB。

实施上述内存优化后，Our.isChinese性能基本持平，Our.toPinyin速度下降了8%，仍能达到160 ops/us，是Pinyin4J的4倍，可以接受。 这里速度下降的原因是每次取拼音时均需进行一次offset的解码。