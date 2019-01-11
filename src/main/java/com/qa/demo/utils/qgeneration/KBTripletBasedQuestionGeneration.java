package com.qa.demo.utils.qgeneration;
/**
 *  Created time: 2017_09_05
 *  Author: Devin Hua
 *  Function description:
 *  To generate question-answer pairs from KB triplets.
 */

import com.qa.demo.conf.Configuration;
import com.qa.demo.conf.FileConfig;
import com.qa.demo.dataStructure.PredicateType;
import com.qa.demo.dataStructure.Triplet;
import com.qa.demo.utils.trainingcorpus.ExtractQuestionsFromText;
import com.qa.demo.utils.trainingcorpus.OrganizeQuestions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

public class KBTripletBasedQuestionGeneration {

    public static ArrayList<Triplet> generateTriplets(String filepath){

        ArrayList<String> lines = new ArrayList<String>();
        ArrayList<Triplet> triplets = new ArrayList<Triplet>();
        try {
            lines = ExtractQuestionsFromText.readLinesFromFile(filepath);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("读取文件失败！");
        }

        if(lines.size()>0)
        {
//            int count = 0;
            for(String temp:lines)
            {
                temp = temp.trim();
                if(temp==null||temp=="")
                    continue;
//                System.out.println("generateTriplet count: " + count++);
                String[] temps = temp.split(Configuration.SPLITSTRING);
                if(temps.length<5)
                    continue;
                else{
                    Triplet triplet = new Triplet(temp);
                    triplets.add(triplet);
                }
            }
        }
        return triplets;
    }


    public static HashSet<String> questionTemplates(String s, String p)
    {
//        HashSet<String> specialWordSet = Configuration.SPECIAL_WORD_SET;

        HashSet<String> questionSet = new HashSet<>();
        String question = "";

        //使用模板匹配属性和应该生成的问题；
//        if(specialWordSet.contains(p))
//        {
//            question = s + "属于哪个" + p + "？";
//            questionSet.add(question);
//            question = s + "属于什么" + p + "？";
//            questionSet.add(question);
//            question = s + "是什么" + p + "？";
//            questionSet.add(question);
//        }
        if(p.equalsIgnoreCase("关键词"))
        {
            question = s + "的" + p + "是？";
            questionSet.add(question);
            question = s + "的" + p + "都有什么？";
            questionSet.add(question);
            question = s + "的" + p + "是啥？";
            questionSet.add(question);
            question = s + "有什么" + p + "？";
            questionSet.add(question);
            question = s + "有哪些" + p + "？";
            questionSet.add(question);
        }
        else if(p.equalsIgnoreCase("拥有专长"))
        {
            question = s + "的专长是什么？";
            questionSet.add(question);
            question = s + "拥有什么专长？";
            questionSet.add(question);
            question = s + "的特长是什么？";
            questionSet.add(question);
            question = s + "拥有什么特长？";
            questionSet.add(question);
            question = s + "有什么特长？";
            questionSet.add(question);
            question = s + "有什么专长？";
            questionSet.add(question);
            question = s + "有什么特殊才能？";
            questionSet.add(question);
            question = s + "有什么独特的才能？";
            questionSet.add(question);
            question = s + "有什么独特的本领？";
            questionSet.add(question);
        }
        else if(p.equalsIgnoreCase("邮箱"))
        {
            question = s + "的" + p + "地址是什么？";
            questionSet.add(question);
            question = s + "的邮件地址是什么？";
            questionSet.add(question);
        }
        else if(p.equalsIgnoreCase("英文名")||
                p.equalsIgnoreCase("英文名称"))
        {
            question = s + "的英文名是什么？";
            questionSet.add(question);
            question = s + "的英文名称是什么？";
            questionSet.add(question);
            question = s + "的英文名字是什么？";
            questionSet.add(question);
            question = s + "的外文名是什么？";
            questionSet.add(question);
            question = s + "的外文名称是什么？";
            questionSet.add(question);
            question = s + "的外文名字是什么？";
            questionSet.add(question);
        }
        else if(p.equalsIgnoreCase("父元素"))
        {
            question = s + "的父亲元素是什么？";
            questionSet.add(question);
        }
        else if(p.equalsIgnoreCase("h系数"))
        {
            question = s + "的h因子是什么？";
            questionSet.add(question);
            question = s + "的h因素是什么？";
            questionSet.add(question);
        }
        else if(p.equalsIgnoreCase("总引用量"))
        {
            question = s + "的引用总量是什么？";
            questionSet.add(question);
            question = s + "的引用量是多少？";
            questionSet.add(question);
            question = s + "被引用了多少次？";
            questionSet.add(question);
            question = s + "被引用了几次？";
            questionSet.add(question);
            question = s + "的" + p + "是多少？";
            questionSet.add(question);
            question = "有多少篇文章引用了" + s + "？";
            questionSet.add(question);
            question = "引用了" + s + "的文章有多少篇？";
            questionSet.add(question);
            question = s + "被多少篇文章引用过？";
            questionSet.add(question);
            question = s + "被多少文章引用了？";
            questionSet.add(question);
        }
        else if(p.equalsIgnoreCase("中文名")||
                p.equalsIgnoreCase("中文名称"))
        {
            question = s + "的中文名是什么？";
            questionSet.add(question);
            question = s + "的中文名称是什么？";
            questionSet.add(question);
            question = s + "的中文名字是什么？";
            questionSet.add(question);
        }
        else if(p.equalsIgnoreCase("外文名")||
                p.equalsIgnoreCase("外文名称"))
        {
            question = s + "的外文名是什么？";
            questionSet.add(question);
            question = s + "的外文名称是什么？";
            questionSet.add(question);
            question = s + "的外文名字是什么？";
            questionSet.add(question);
        }
        else if(p.equalsIgnoreCase("工作"))
        {
            question = s + "是做什么工作的？";
            questionSet.add(question);
            question = s + "从事什么工作？";
            questionSet.add(question);
            question = s + "的" + p + "内容是什么？";
            questionSet.add(question);
            question = s + "的" + p + "职责是什么？";
            questionSet.add(question);
            question = "什么是" + s + "的" + p + "？";
            questionSet.add(question);
        }
        else if(p.equalsIgnoreCase("描述"))
        {
            question = s + "是什么？";
            questionSet.add(question);
            question = s + "是什么东西？";
            questionSet.add(question);
            question = s + "是啥？";
            questionSet.add(question);
            question = "什么是" + s + "？";
            questionSet.add(question);
            question = "怎么描述" + s + "？";
            questionSet.add(question);
            question = "如何描述" + s + "？";
            questionSet.add(question);
        }
        else if(p.equalsIgnoreCase("个人主页"))
        {
            question = s + "的个人主页地址是什么？";
            questionSet.add(question);
            question = s + "的个人主页链接是什么？";
            questionSet.add(question);
            question = s + "的主页超链接是什么？";
            questionSet.add(question);
            question = s + "的主页连接是什么？";
            questionSet.add(question);
        }
        else if(p.equalsIgnoreCase("ID"))
        {
            question = s + "的" + p + "是多少？";
            questionSet.add(question);
            question = s + "的" + p + "值是多少？";
            questionSet.add(question);
            question = s + "的" + p + "值是什么？";
            questionSet.add(question);
        }
        else if(p.equalsIgnoreCase("活动"))
        {
            question = s + "的主要" + p + "是什么？";
            questionSet.add(question);
        }
        else if(p.equalsIgnoreCase("文章"))
        {
            question = s + "的主要" + p + "是什么？";
            questionSet.add(question);
            question = s + "的" + p + "有哪些？";
            questionSet.add(question);
            question = s + "发表的" + p + "有哪些？";
            questionSet.add(question);
            question = s + "写作的" + p + "有哪些？";
            questionSet.add(question);
            question = s + "发表了哪些" + p + "？";
            questionSet.add(question);
            question = s + "发表了什么" + p + "？";
            questionSet.add(question);
            question = s + "有哪些著作？";
            questionSet.add(question);
            question = s + "有什么学术著作？";
            questionSet.add(question);
            question = s + "有哪些作品？";
            questionSet.add(question);
            question = s + "有什么学术作品？";
            questionSet.add(question);
            question = s + "发表了哪些作品？";
            questionSet.add(question);
            question = s + "出版了什么书？";
            questionSet.add(question);
            question = s + "出了什么书？";
            questionSet.add(question);
        }
        else if(p.equalsIgnoreCase("图片"))
        {
            question = s + "的" + p + "地址是什么？";
            questionSet.add(question);
            question = s + "的" + p + "链接是什么？";
            questionSet.add(question);
            question = s + "的" + p + "连接是什么？";
            questionSet.add(question);
            question = s + "的" + p + "超链接是什么？";
            questionSet.add(question);
        }
        else if(p.equalsIgnoreCase("中文简介")||p.equalsIgnoreCase("中文描述"))
        {
            question = s + "的中文简明介绍是什么？";
            questionSet.add(question);
            question = s + "的中文简单介绍是什么？";
            questionSet.add(question);
            question = "用中文简单介绍一下" + s + "是什么？";
            questionSet.add(question);
            question = "简明介绍一下" + s + "？";
            questionSet.add(question);
            question = "用中文简单描述一下" + s + "？";
            questionSet.add(question);
            question = s + "的基本介绍是什么？";
            questionSet.add(question);
            question = s + "的基本情况是什么？";
            questionSet.add(question);
            question = "对" + s + "做一个中文的概述？";
            questionSet.add(question);
            question = "对" + s + "做一个中文的简单介绍？";
            questionSet.add(question);
            question = "对" + s + "做一下中文的简单描述？";
            questionSet.add(question);
        }
        else if(p.equalsIgnoreCase("简介")||p.equalsIgnoreCase("描述"))
        {
            question = s + "的简明介绍是什么？";
            questionSet.add(question);
            question = s + "的简单介绍是什么？";
            questionSet.add(question);
            question = "用简单介绍一下" + s + "是什么？";
            questionSet.add(question);
            question = "简明介绍一下" + s + "？";
            questionSet.add(question);
            question = "用简单描述一下" + s + "？";
            questionSet.add(question);
            question = s + "的基本介绍是什么？";
            questionSet.add(question);
            question = s + "的基本情况是什么？";
            questionSet.add(question);
            question = "对" + s + "做一个概述？";
            questionSet.add(question);
            question = "对" + s + "做一个简单介绍？";
            questionSet.add(question);
            question = "对" + s + "做一下简单描述？";
            questionSet.add(question);
        }
        else if(p.equalsIgnoreCase("个人自传"))
        {
            question = s + "的自传是什么？";
            questionSet.add(question);
            question = s + "个人的自传是什么？";
            questionSet.add(question);
            question = s + "的自传？";
            questionSet.add(question);
        }
        else if(p.equalsIgnoreCase("子元素"))
        {
            question = s + "的孩子元素是什么？";
            questionSet.add(question);
        }
        else if(p.equalsIgnoreCase("类型"))
        {
            question = s + "是属于什么" + p + "的？";
            questionSet.add(question);
            question = s + "属于什么" + p + "？";
            questionSet.add(question);
            question = s + "分类于哪个类别？";
            questionSet.add(question);
            question = s + "属于什么类别？";
            questionSet.add(question);
            question = s + "是属于哪个" + p + "的？";
            questionSet.add(question);
        }
        else if(p.equalsIgnoreCase("国籍"))
        {
            question = s + "是哪国人？";
            questionSet.add(question);
            question = s + "来自哪个国家？";
            questionSet.add(question);
        }
        else if(p.equalsIgnoreCase("论文数量"))
        {
            question = s + "发表了多少文章？";
            questionSet.add(question);
            question = s + "发表了多少论文？";
            questionSet.add(question);
            question = s + "的" + p + "是多少？";
            questionSet.add(question);
            question = s + "发表的论文数量有多少？";
            questionSet.add(question);
            question = s + "发表的论文数目有多少？";
            questionSet.add(question);
            question = s + "发表了多少著作？";
            questionSet.add(question);
            question = s + "发表了多少学术著作？";
            questionSet.add(question);
            question = s + "发表了多少作品？";
            questionSet.add(question);
            question = s + "发表了多少学术作品？";
            questionSet.add(question);
        }
        else if(p.equalsIgnoreCase("产品供应商"))
        {
            question = s + "的" + p + "是哪个公司？";
            questionSet.add(question);
            question = s + "的" + p + "是哪家单位？";
            questionSet.add(question);
            question = s + "的" + p + "是什么机构？";
            questionSet.add(question);
        }
        else if(p.equalsIgnoreCase("机构"))
        {
            question = s + "的所属单位是哪家" + p + "？";
            questionSet.add(question);
            question = s + "属于哪个" + p + "？";
            questionSet.add(question);
            question = s + "的所属单位是什么？";
            questionSet.add(question);
            question = s + "属于哪所科研机构？";
            questionSet.add(question);
        }
        else if(p.equalsIgnoreCase("作者"))
        {
            question = s + "的" + p + "是谁？";
            questionSet.add(question);
            question = "谁创作了" + s + "？";
            questionSet.add(question);
            question = "谁写了" + s + "？";
            questionSet.add(question);
            question = "谁写作了" + s + "？";
            questionSet.add(question);
            question = "谁发表了" + s + "？";
            questionSet.add(question);
            question = "谁撰写了" + s + "？";
            questionSet.add(question);
            question = "谁撰文了" + s + "？";
            questionSet.add(question);
        }
        else if(p.equalsIgnoreCase("别名")||p.equalsIgnoreCase("别称"))
        {
            question = s + "的别名是什么？";
            questionSet.add(question);
            question = s + "的别称是什么？";
            questionSet.add(question);
            question = s + "有什么其他别名？";
            questionSet.add(question);
            question = s + "有什么其他别称？";
            questionSet.add(question);
            question = s + "有什么别名？";
            questionSet.add(question);
            question = s + "有什么别称？";
            questionSet.add(question);
            question = s + "有什么其他名字？";
            questionSet.add(question);
            question = s + "有什么其他名称？";
            questionSet.add(question);
            question = s + "又名什么？";
            questionSet.add(question);
        }
        else if(p.equalsIgnoreCase("名称")||p.equalsIgnoreCase("名字"))
        {
            question = s + "的名字是什么？";
            questionSet.add(question);
            question = s + "的名称是什么？";
            questionSet.add(question);
            question = s + "叫什么？";
            questionSet.add(question);
            question = s + "被称作什么？";
            questionSet.add(question);
            question = s + "被称为什么？";
            questionSet.add(question);
        }
        else if(p.equalsIgnoreCase("出生日期")||p.equalsIgnoreCase("出生年月"))
        {
            question = s + "的出生日期是什么？";
            questionSet.add(question);
            question = s + "的出生年月是什么？";
            questionSet.add(question);
            question = s + "的生日是什么？";
            questionSet.add(question);
            question = s + "是什么时候出生的？";
            questionSet.add(question);
            question = s + "生于什么时候？";
            questionSet.add(question);
            question = s + "出生于什么时候？";
            questionSet.add(question);
            question = s + "出生于哪年？";
            questionSet.add(question);
            question = s + "出生于哪天？";
            questionSet.add(question);
            question = s + "的出生时间是什么？";
            questionSet.add(question);
        }
        else if(p.equalsIgnoreCase("逝世日期")||p.equalsIgnoreCase("去世年月"))
        {
            question = s + "的逝世日期是什么？";
            questionSet.add(question);
            question = s + "的逝世时间是什么？";
            questionSet.add(question);
            question = s + "是什么时候逝世的？";
            questionSet.add(question);
            question = s + "的去世年月是什么？";
            questionSet.add(question);
            question = s + "的去世时间是什么？";
            questionSet.add(question);
            question = s + "是什么时候去世的？";
            questionSet.add(question);
            question = s + "逝于什么时候？";
            questionSet.add(question);
            question = s + "去世于哪年？";
            questionSet.add(question);
            question = s + "去世于哪天？";
            questionSet.add(question);
        }
        else if(p.equalsIgnoreCase("民族"))
        {
            question = s + "是什么" + p + "？";
            questionSet.add(question);
            question = s + "是什么" + p + "的？";
            questionSet.add(question);
            question = s + "是什么少数" + p + "的？";
            questionSet.add(question);
            question = s + "是什么少数" + p + "？";
            questionSet.add(question);
            question = s + "是哪个" + p + "？";
            questionSet.add(question);
            question = s + "是哪个" + p + "的？";
            questionSet.add(question);
            question = s + "属于哪个" + p + "？";
            questionSet.add(question);
        }
        else if(p.equalsIgnoreCase("出生地"))
        {
            question = s + "的" + p + "是哪里？";
            questionSet.add(question);
            question = s + "的" + p + "在哪里？";
            questionSet.add(question);
            question = s + "的" + p + "在哪？";
            questionSet.add(question);
            question = s + "的" + p + "在哪儿？";
            questionSet.add(question);
            question = s + "的" + p + "是哪？";
            questionSet.add(question);
            question = s + "的" + p + "是什么地方？";
            questionSet.add(question);
            question = s + "出生于哪里？";
            questionSet.add(question);
            question = s + "生于哪里？";
            questionSet.add(question);
            question = s + "是在哪里出生的？";
            questionSet.add(question);
        }
        else if(p.equalsIgnoreCase("地理位置"))
        {
            question = s + "的" + p + "是哪里？";
            questionSet.add(question);
            question = s + "的" + p + "在哪里？";
            questionSet.add(question);
            question = s + "的" + p + "在哪儿？";
            questionSet.add(question);
            question = s + "的" + p + "在哪？";
            questionSet.add(question);
            question = s + "位于哪里？";
            questionSet.add(question);
            question = s + "的" + p + "是哪？";
            questionSet.add(question);
            question = s + "的" + p + "在哪？";
            questionSet.add(question);
        }
        else if(p.equalsIgnoreCase("命名人")||
                p.equalsIgnoreCase("命名者"))
        {
            question = s + "的命名人是什么？";
            questionSet.add(question);
            question = s + "的命名人是谁？";
            questionSet.add(question);
            question = s + "的命名者是什么？";
            questionSet.add(question);
            question = s + "的命名者是谁？";
            questionSet.add(question);
            question = s + "的命名人？";
            questionSet.add(question);
            question = s + "的命名者？";
            questionSet.add(question);
            question = s + "由谁命名？";
            questionSet.add(question);
            question = s + "是谁命名的？";
            questionSet.add(question);
            question = "谁命名了" + s + "？";
            questionSet.add(question);
        }
        else if(p.equalsIgnoreCase("译者"))
        {
            question = s + "的" + p + "是谁？";
            questionSet.add(question);
            question = s + "是谁翻译的？";
            questionSet.add(question);
            question = s + "由谁翻译的？";
            questionSet.add(question);
            question = "谁翻译了" + s + "？";
            questionSet.add(question);
        }
        else if(p.equalsIgnoreCase("发现者"))
        {
            question = "谁发现了" + s + "？";
            questionSet.add(question);
            question = s + "是谁发现的？";
            questionSet.add(question);
            question = s + "由谁发现的？";
            questionSet.add(question);
            question = s + "的" + p + "是谁？";
            questionSet.add(question);
        }
        else if(p.equalsIgnoreCase("数量"))
        {
            question = s + "的" + p + "是多少？";
            questionSet.add(question);
            question = s + "的" + p + "有多少？";
            questionSet.add(question);
        }
        else if(p.equalsIgnoreCase("籍贯")||
                p.equalsIgnoreCase("政府驻地"))
        {
            question = s + "的" + p + "是哪里？";
            questionSet.add(question);
            question = s + "的" + p + "在哪里？";
            questionSet.add(question);
            question = s + "的" + p + "在哪？";
            questionSet.add(question);
            question = s + "的" + p + "在哪儿？";
            questionSet.add(question);
        }
        else if(p.equalsIgnoreCase("邮编区码")||
                p.equalsIgnoreCase("邮政区码"))
        {
            question = s + "的邮编区码是什么？";
            questionSet.add(question);
            question = s + "的邮政区码是什么？";
            questionSet.add(question);
            question = s + "的邮政编码是什么？";
            questionSet.add(question);
            question = s + "的邮编是什么？";
            questionSet.add(question);
            question = s + "的邮编码是什么？";
            questionSet.add(question);
        }
        else if(p.equalsIgnoreCase("祖籍"))
        {
            question = s + "的" + p + "在哪里？";
            questionSet.add(question);
            question = s + "的" + p + "在哪？";
            questionSet.add(question);
            question = s + "的" + p + "在哪儿？";
            questionSet.add(question);
        }
        else if(p.equalsIgnoreCase("发现命名"))
        {
            question = s + "的" + p + "人是谁？";
            questionSet.add(question);
            question = s + "的" + p + "者是谁？";
            questionSet.add(question);
            question = "谁" + p + "了" + s + "？";
            questionSet.add(question);
            question = "谁" + p + s + "？";
            questionSet.add(question);
            question = s + "被谁" + p + "？";
            questionSet.add(question);
            question = s + "是被谁" + p + "的？";
            questionSet.add(question);
            question = s + "被谁" + p + "了？";
            questionSet.add(question);
        }
        else if(p.equalsIgnoreCase("总部地点")||
                p.equalsIgnoreCase("总部所在地"))
        {
            question = s + "的总部地点是什么？";
            questionSet.add(question);
            question = s + "的总部地点是哪里？";
            questionSet.add(question);
            question = s + "的总部地点是哪？";
            questionSet.add(question);
            question = s + "的总部地点是哪儿？";
            questionSet.add(question);
            question = s + "的总部地点在哪里？";
            questionSet.add(question);
            question = s + "的总部所在地是什么？";
            questionSet.add(question);
            question = s + "的总部所在地是哪里？";
            questionSet.add(question);
            question = s + "的总部所在地在哪里？";
            questionSet.add(question);
            question = s + "的总部所在地在哪？";
            questionSet.add(question);
            question = s + "的总部地点在哪？";
            questionSet.add(question);
        }
        else if(p.equalsIgnoreCase("起源于"))
        {
            question = s + p + "什么？";
            questionSet.add(question);
            question = s + p + "？";
            questionSet.add(question);
            question = s + p + "什么时候？";
            questionSet.add(question);
            question = s + p + "哪个年代？";
            questionSet.add(question);
            question = s + p + "什么年代？";
            questionSet.add(question);
            question = s + p + "哪个时代？";
            questionSet.add(question);
            question = s + p + "什么时代？";
            questionSet.add(question);
            question = s + "的起源时间是什么？";
            questionSet.add(question);
        }
        else if(p.equalsIgnoreCase("创建地点"))
        {
            question = s + "的" + p + "是哪里？";
            questionSet.add(question);
            question = s + "的" + p + "在哪里？";
            questionSet.add(question);
            question = s + "的" + p + "在哪儿？";
            questionSet.add(question);
            question = s + "的" + p + "在哪？";
            questionSet.add(question);
            question = s + "创建于哪里？";
            questionSet.add(question);
            question = s + "是在哪里被创建的？";
            questionSet.add(question);
        }
        else if(p.equalsIgnoreCase("电话区号")||
                p.equalsIgnoreCase("电话区码"))
        {
            question = s + "的电话区号是什么？";
            questionSet.add(question);
            question = s + "的电话区码是什么？";
            questionSet.add(question);
            question = s + "的电话区号是多少？";
            questionSet.add(question);
            question = s + "的电话区码是多少？";
            questionSet.add(question);
        }
        else if(p.equalsIgnoreCase("命名者"))
        {
            question = s + "的" + p + "是谁？";
            questionSet.add(question);
            question = s + "被谁命名？";
            questionSet.add(question);
            question = s + "是被谁命名的？";
            questionSet.add(question);
            question = "谁命名了" + s + "？";
            questionSet.add(question);
        }
        else if(p.equalsIgnoreCase("产地")||
                p.equalsIgnoreCase("原产地")||
                p.equalsIgnoreCase("原产地区"))
        {
            question = s + "的产地是什么？";
            questionSet.add(question);
            question = s + "的原产地是什么？";
            questionSet.add(question);
            question = s + "的原产地区是什么？";
            questionSet.add(question);
            question = s + "的产地是哪里？";
            questionSet.add(question);
            question = s + "的产地是哪儿？";
            questionSet.add(question);
            question = s + "的产地是哪？";
            questionSet.add(question);
            question = s + "的原产地是哪里？";
            questionSet.add(question);
            question = s + "的原产地在哪里？";
            questionSet.add(question);
            question = s + "产于哪里？";
            questionSet.add(question);
            question = s + "原产于哪里？";
            questionSet.add(question);
            question = s + "产于什么地方？";
            questionSet.add(question);
            question = s + "原产于什么地方？";
            questionSet.add(question);
            question = s + "产于哪？";
            questionSet.add(question);
            question = s + "产自哪里？";
            questionSet.add(question);
            question = s + "原产自哪里？";
            questionSet.add(question);
            question = s + "产自什么地方？";
            questionSet.add(question);
            question = s + "原产自什么地方？";
            questionSet.add(question);
        }
        else if(p.equalsIgnoreCase("员工数"))
        {
            question = s + "的" + p + "是多少？";
            questionSet.add(question);
            question = s + "的" + p + "有多少？";
            questionSet.add(question);
            question = s + "有多少员工？";
            questionSet.add(question);
            question = s + "有几个员工？";
            questionSet.add(question);
        }
        else if(p.equalsIgnoreCase("逝世地"))
        {
            question = s + "的" + p + "是哪里？";
            questionSet.add(question);
            question = s + "的" + p + "是哪儿？";
            questionSet.add(question);
            question = s + "的" + p + "是哪？";
            questionSet.add(question);
            question = s + "的" + p + "在哪里？";
            questionSet.add(question);
            question = s + "逝于哪里？";
            questionSet.add(question);
            question = s + "逝世于哪里？";
            questionSet.add(question);
            question = s + "死于哪里？";
            questionSet.add(question);
            question = s + "逝于什么地方？";
            questionSet.add(question);
            question = s + "逝世于什么地方？";
            questionSet.add(question);
            question = s + "死于什么地方？";
            questionSet.add(question);
            question = s + "是在哪里逝世的？";
            questionSet.add(question);
            question = s + "是在哪里去世的？";
            questionSet.add(question);
        }
        else if(p.equalsIgnoreCase("创建时间"))
        {
            question = s + "创建于什么时间？";
            questionSet.add(question);
            question = s + "创建于什么时候？";
            questionSet.add(question);
            question = s + "是什么时候创建的？";
            questionSet.add(question);
            question = s + "创建于哪年的？";
            questionSet.add(question);
            question = s + "创建于哪天的？";
            questionSet.add(question);
        }
        else if(p.equalsIgnoreCase("释义"))
        {
            question = "如何解释" + s + "？";
            questionSet.add(question);
            question = "怎么解释" + s + "？";
            questionSet.add(question);
            question = s + "的含义是什么？";
            questionSet.add(question);
            question = s + "有什么含义？";
            questionSet.add(question);
        }
        else if(p.equalsIgnoreCase("笔画"))
        {
            question = s + "的" + p + "是多少？";
            questionSet.add(question);
            question = s + "有多少" + p + "？";
            questionSet.add(question);
            question = s + "有多少笔画？";
            questionSet.add(question);
            question = s + "有几画？";
            questionSet.add(question);
            question = s + "有多少画？";
            questionSet.add(question);
        }
        else if(p.equalsIgnoreCase("行业")) {
            question = s + "的所处" + p + "是什么？";
            questionSet.add(question);
            question = s + "是做哪一行业的？";
            questionSet.add(question);
            question = s + "是做什么行业的？";
            questionSet.add(question);
            question = s + "是做哪一行的？";
            questionSet.add(question);
        }
        else if(p.equalsIgnoreCase("页数")) {
            question = s + "的" + p + "是多少？";
            questionSet.add(question);
            question = s + "有多少页？";
            questionSet.add(question);
            question = s + "有几页？";
            questionSet.add(question);
        }
        else if(p.equalsIgnoreCase("价格"))
        {
            question = s + "的" + p + "是多少？";
            questionSet.add(question);
            question = s + "多少钱？";
            questionSet.add(question);
        }
        else if(p.equalsIgnoreCase("所处时代")||
                p.equalsIgnoreCase("所处年代"))
        {
            question = s + "处于哪个时代？";
            questionSet.add(question);
            question = s + "处于哪个年代？";
            questionSet.add(question);
            question = s + "的所处时代是什么？";
            questionSet.add(question);
            question = s + "的所处年代是什么？";
            questionSet.add(question);
            question = s + "的所处时代？";
            questionSet.add(question);
            question = s + "的所处年代？";
            questionSet.add(question);
        }
        else if(p.equalsIgnoreCase("成就")) {
            question = s + "有哪些" + p + "？";
            questionSet.add(question);
            question = s + "获得过哪些" + p + "？";
            questionSet.add(question);
        }
        else if(p.equalsIgnoreCase("单行本册数")) {
            question = s + "的" + p + "是多少？";
            questionSet.add(question);
            question = s + "有多少单行本？";
            questionSet.add(question);
            question = s + "的单行本的数量是多少？";
            questionSet.add(question);
            question = s + "的单行本的册数有多少？";
            questionSet.add(question);
            question = s + "的单行本发行了多少册？";
            questionSet.add(question);
            question = s + "的单行本发行册数是多少？";
            questionSet.add(question);
        }
        else if(p.equalsIgnoreCase("产品服务")) {
            question = s + "提供了什么" + p + "？";
            questionSet.add(question);
        }
        else if(p.equalsIgnoreCase("缺点"))
        {
            question = s + "的缺陷是什么？";
            questionSet.add(question);
            question = s + "的不足之处是什么？";
            questionSet.add(question);
            question = s + "的不足在哪里？";
            questionSet.add(question);
            question = s + "的哪里有不足？";
            questionSet.add(question);
            question = s + "有什么不好的地方？";
            questionSet.add(question);
            question = s + "有什么不好之处？";
            questionSet.add(question);
        }
        //问题的默认两种问法；
        question = s + "的" + p + "？";
        questionSet.add(question);
        question = s + "的" + p + "是什么？";
        questionSet.add(question);
        return questionSet;
    }

    //根据三元组，结合不同问法，形成一系列问题；
    private static ArrayList<String> generateQuestionAnswerPair(Triplet t)
    {
        ArrayList<String> results = new ArrayList<String>();
        String answer = t.getObjectName();
        String p = t.getPredicateName();
        String s = t.getSubjectName();

        HashSet<String> questionSet = questionTemplates(s,p);

        for(String qTemp:questionSet)
        {
            String output = "";
            output += "是什么"+ Configuration.SPLITSTRING+qTemp
                    + Configuration.SPLITSTRING+answer;
            //在答案之后加上映射到的三元组，对象属性为主语URI，谓语URI，宾语URI；
            if(t.getPredicateType().equals(PredicateType.OBJECTPROPERTY))
            {
                output += Configuration.SPLITSTRING + t.getSubjectURI()
                        + Configuration.SPLITSTRING + t.getPredicateURI()
                        + Configuration.SPLITSTRING + t.getObjectURI() + "\r\n";
            }
            //在答案之后加上映射到的三元组，数值属性为主语URI，谓语URI，宾语值；
            else if(t.getPredicateType().equals(PredicateType.DATATYPEPROPERTY))
            {
                output += Configuration.SPLITSTRING + t.getSubjectURI()
                        + Configuration.SPLITSTRING + t.getPredicateURI()
                        + Configuration.SPLITSTRING + t.getObjectName() + "\r\n";
            }
            results.add(output);
        }
        return results;
    }

    //将三元组list转换为问题-答案对的list；
    private static ArrayList<String> generateQuestionAnswerPairLists(ArrayList<Triplet> triplets){
        ArrayList<String> questionAnswerPairs = new ArrayList<String>();
        if(triplets.size()==0||triplets.isEmpty()||triplets==null)
            return questionAnswerPairs;
        else{
//            int count = 0;
            for(Triplet t:triplets)
            {
//                System.out.println("generateQuestionAnswerPair count: "+count++);
                ArrayList<String> qaPairs = generateQuestionAnswerPair(t);
                questionAnswerPairs.addAll(qaPairs);
            }
        }
        return questionAnswerPairs;
    }

    public static void mainDriver(){

        ArrayList<Triplet> triplets = generateTriplets(FileConfig.DATATYPE_PROPERTY_TRIPLETS_FILE);
        ArrayList<String> questionAnswerPairs = generateQuestionAnswerPairLists(triplets);
        triplets = generateTriplets(FileConfig.OBJECT_PROPERTY_TRIPLETS_FILE);
        ArrayList<String> data_questionAnswerPairs = generateQuestionAnswerPairLists(triplets);
        questionAnswerPairs.addAll(data_questionAnswerPairs);
        try {
            OrganizeQuestions.writeToFile(questionAnswerPairs, FileConfig.FILE_FAQ_T);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("写入文件失败！");
        }
    }

}
