package com.qa.demo.questionAnalysis;

/**
 * Created by Devin Hua on 2017/10/06.
 * Function description:
 * To tokenize the sentence and get relevant POS.
 */

import com.qa.demo.conf.FileConfig;
import org.ansj.domain.Result;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.nlpcn.commons.lang.tire.domain.Forest;
import org.nlpcn.commons.lang.tire.domain.Value;
import org.nlpcn.commons.lang.tire.library.Library;

import java.util.*;

public class Segmentation {

    private static List<String> tokens;
    private static List<Map<String, String>> tokenPOSList;

    public static List<Map<String, String>> getTokenPOSList() {
        return tokenPOSList;
    }

    public static void setTokenPOSList(List<Map<String, String>> tokenPOSList) {
        Segmentation.tokenPOSList = tokenPOSList;
    }

    public static List<String> getTokens() {
        return tokens;
    }

    public static void setTokens(ArrayList<String> tokens) {
        Segmentation.tokens = tokens;
    }

    //输入一个字符串，对其进行分词后输出；
    public static void segmentation(String sentence) {

        tokens = new ArrayList<>();
        tokenPOSList = new ArrayList<>();
        Forest forest = null;
        //官方预设的自定分词词典;
        try {
            forest = Library.makeForest(FileConfig.DICTIONARY_FILE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Forest forest1 = new Forest();
        //为分词词典增加停用词，最后一个参数的分值越大， 越按照这个词来分词；
        //比如说“可以”的词频为1000，“都可以”的词频为1001，那么都可以的三个字就不会分开；
        //第二个参数指词性；
        Library.insertWord(forest1, new Value("最高职务", "n", "1000"));
        Library.insertWord(forest1, new Value("又名", "n", "1000"));
        Library.insertWord(forest1, new Value("校区", "n", "1000"));
        Library.insertWord(forest1, new Value("命名人", "n", "1000"));
        Library.insertWord(forest1, new Value("现居地", "n", "1000"));
        Library.insertWord(forest1, new Value("逝世地", "n", "1000"));
        Library.insertWord(forest1, new Value("其他名", "n", "1000"));
        Library.insertWord(forest1, new Value("现在居住地", "n", "1000"));
        Library.insertWord(forest1, new Value("等级", "n", "1000"));
        Library.insertWord(forest1, new Value("博士点", "n", "1000"));
        Library.insertWord(forest1, new Value("外文名", "n", "1000"));
        Library.insertWord(forest1, new Value("下辖地区", "n", "1000"));
        Library.insertWord(forest1, new Value("名人", "n", "1000"));
        Library.insertWord(forest1, new Value("出品人", "n", "1000"));
        Library.insertWord(forest1, new Value("发现者", "n", "1000"));
        Library.insertWord(forest1, new Value("学历", "n", "1000"));
        Library.insertWord(forest1, new Value("别名", "n", "1000"));
        Library.insertWord(forest1, new Value("别称", "n", "1000"));
        Library.insertWord(forest1, new Value("属于", "v", "1000"));
        Library.insertWord(forest1, new Value("原作者", "n", "1000"));
        Library.insertWord(forest1, new Value("长江学者", "n", "1000"));
        Library.insertWord(forest1, new Value("硕士点", "n", "1000"));
        Library.insertWord(forest1, new Value("外号", "n", "1000"));
        Library.insertWord(forest1, new Value("官方网站", "n", "1000"));
        Library.insertWord(forest1, new Value("命名者", "n", "1000"));
        Library.insertWord(forest1, new Value("政治面貌", "n", "1000"));
        Library.insertWord(forest1, new Value("地点", "n", "1000"));
        Library.insertWord(forest1, new Value("完成时间", "n", "1000"));
        Library.insertWord(forest1, new Value("中文学名", "n", "1000"));
        Library.insertWord(forest1, new Value("安全术语", "n", "1000"));
        Library.insertWord(forest1, new Value("通用名", "n", "1000"));
        Library.insertWord(forest1, new Value("叫什么", "v", "1000"));
        Library.insertWord(forest1, new Value("拼音名", "n", "1000"));
        Library.insertWord(forest1, new Value("哪天", "n", "1000"));
        Library.insertWord(forest1, new Value("哪年", "n", "1000"));
        Library.insertWord(forest1, new Value("拥有专长", "n", "1000"));
        Library.insertWord(forest1, new Value("父元素", "n", "1000"));
        Library.insertWord(forest1, new Value("子元素", "n", "1000"));
        Library.insertWord(forest1, new Value("h系数", "n", "1000"));
        Library.insertWord(forest1, new Value("h因子", "n", "1000"));
        Library.insertWord(forest1, new Value("h因素", "n", "1000"));
        Library.insertWord(forest1, new Value("总引用量", "n", "1000"));
        Library.insertWord(forest1, new Value("id", "n", "1000"));


//        Result terms = ToAnalysis.parse(sentence, forest1);
//        Result terms = ToAnalysis.parse(sentence, forest);
        Result terms = ToAnalysis.parse(sentence, forest, forest1);
//        System.out.println(terms);

        HashSet<String> stopwords = MoveStopwords.getInstance().getStopwordSet();
        for (int i = 0; i < terms.size(); i++) {
            String s = terms.get(i).toString();
            if (s.contains("/") && (!s.endsWith("/"))) {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put(s.split("/")[0], s.split("/")[1]);
                String token = s.split("/")[0];
                if (!stopwords.isEmpty()) {
                    if (!stopwords.contains(token)) {
                        tokens.add(token);
                        tokenPOSList.add(map);
                    }
                }
            }
        }
    }
}

