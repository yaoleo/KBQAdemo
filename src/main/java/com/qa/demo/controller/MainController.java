package com.qa.demo.controller;

import com.alibaba.fastjson.JSON;
import com.qa.demo.answerAnalysis.AnswerAnalysisDriverImpl;
import com.qa.demo.conf.Configuration;
import com.qa.demo.dataStructure.DataSource;
import com.qa.demo.dataStructure.QueryTuple;
import com.qa.demo.dataStructure.Question;
import com.qa.demo.query.*;
import com.qa.demo.questionAnalysis.Segmentation;
import com.qa.demo.systemController.FaqDemo;
import org.apache.jena.base.Sys;
import org.apache.log4j.PropertyConfigurator;
import org.nlpcn.commons.lang.util.logging.Log;
import org.nlpcn.commons.lang.util.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletRequest;

import static com.qa.demo.conf.FileConfig.LOG_PROPERTY;

/**
 * Created by hyh on 2017/8/14.
 */
@Controller
public class MainController {



    private String _runTask(Question q){
        long beginTime=System.currentTimeMillis();
        //任务执行准备
        //如下为任务算法执行
        while((System.currentTimeMillis()-beginTime<150)){
            LOG.info(String.valueOf((System.currentTimeMillis()-beginTime)));
            //执行循环体内的任务片段和算法
            KbqaQueryDriver esQuerySynonymKBQADriver = new ESQuerySynonymKBQA();
            q = esQuerySynonymKBQADriver.kbQueryAnswers(q);

            KbqaQueryDriver ALGQuerySynonymKBQADriver = new ALGQuerySynonymKBQA();
            q = ALGQuerySynonymKBQADriver.kbQueryAnswers(q);
            // 对答案进行排序
            AnswerAnalysisDriverImpl analysisDriver = new AnswerAnalysisDriverImpl();
            q = analysisDriver.rankAnswerCandidate(q);

            //生成答案并返回
            q = analysisDriver.returnAnswer(q);
            String answer = q.getReturnedAnswer().getAnswerString().trim();
            return answer;
        }
        return "我还得再想想，换一个问题吧";
    }

    public static final Log LOG = LogFactory.getLog(FaqDemo.class);

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String Index() {
        return "index";
    }
    @RequestMapping(value = "/question", method = RequestMethod.POST)
    public String IndexSearch(@RequestParam("question") String questionstring, Model model) throws IOException
    {
        PropertyConfigurator.configure(LOG_PROPERTY);

        System.out.println(questionstring);

        Scanner scanner = new Scanner(questionstring);

        String input = scanner.next();

        for(String punctuation : Configuration.PUNCTUATION_SET)
        {
            input = input.replace(punctuation,"");
        }

        Question question = new Question(input);

        //从模板的同义词集合中查询，泛化主要功能；
        KbqaQueryDriver ALGQuerySynonymKBQADriver = new ALGQuerySynonymKBQA();
        question = ALGQuerySynonymKBQADriver.kbQueryAnswers(question);

        //从ES索引的模板库中匹配模板，并形成查询三元组，最终通过KG三元组匹配得到候选答案;
        KbqaQueryDriver esQuerySynonymKBQADriver = new ESQuerySynonymKBQA();
        question = esQuerySynonymKBQADriver.kbQueryAnswers(question);

        //对答案进行排序
        AnswerAnalysisDriverImpl analysisDriver = new AnswerAnalysisDriverImpl();
        question = analysisDriver.rankAnswerCandidate(question);

        //生成答案并返回
        question = analysisDriver.returnAnswer(question);
        System.out.println(question.getReturnedAnswer().getAnswerString().trim());
        model.addAttribute("answer",question.getReturnedAnswer().getAnswerString().trim());

        if(question.getReturnedAnswer().getAnswerString().trim().contains
                ("我还得再想想"))
        {
            LOG.error("[error] 用户输入的问题为： " + input);
            LOG.error("[error] 问题无法回答");
            for(QueryTuple t : question.getQueryTuples())
            {
                LOG.error("[error] 返回模板为：");
                LOG.error(t.toString());
            }
            LOG.error("[error] 处理完成");
        }
        else{
            LOG.info("[info] 用户输入的问题为： " + input);
            LOG.info("[info] 系统作答：");
            LOG.info("[info] " + question.getReturnedAnswer().getAnswerString().trim());
            LOG.info("[info] 处理完成");
        }
        return "result";
    }

    @ResponseBody
    @RequestMapping(value = "/json/data", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String getByRequest(HttpServletRequest r) {
        JSONObject jsonParam = this.getJSONParam(r);

        // 将获取的json数据封装一层，然后在给返回

        JSONObject request = jsonParam.getJSONObject("request");
        JSONObject intent = request.getJSONObject("intent");
        JSONObject session = jsonParam.getJSONObject("session");
        String wrongTimes="";
        if(session.containsKey("attributes")) {
            JSONObject session_attributes = session.getJSONObject("attributes");
            if (session_attributes.containsKey("wrongTimes")) {
                wrongTimes = session_attributes.getString("wrongTimes");
            }else{
                LOG.info("9999");
                wrongTimes ="-1";
            }
        }

        JSONObject attributes = new JSONObject();

        if(request.containsKey("type")) {
            Integer type = request.getIntValue("type");
            if(type.equals(2)) {
                if (wrongTimes.equals("2") ||wrongTimes.equals("-1")){
                    JSONObject response = new JSONObject();
                    JSONObject to_speak = new JSONObject();
                    to_speak.put("type", 0);
                    to_speak.put("text", "小问已为您退出,下次再问");

                    response.put("to_speak", to_speak);
                    response.put("open_mic", false);

                    JSONObject result = new JSONObject();
                    result.put("version", "1.0");
                    result.put("is_session_end", true);
                    result.put("response", response);
                    System.out.println(result.toJSONString());
                    LOG.info("8888");
                    return result.toJSONString();
                }
                JSONObject response = new JSONObject();
                JSONObject to_speak = new JSONObject();
                to_speak.put("type", 0);
                to_speak.put("text", "您问了什么没听清,可以问一些学术问题哦");

                response.put("to_speak", to_speak);
                response.put("open_mic", true);

                JSONObject result = new JSONObject();
                result.put("version", "1.0");
                result.put("is_session_end", false);
                result.put("response", response);
                wrongTimes = ""+(Integer.parseInt(wrongTimes)+1);
                attributes.put("wrongTimes",wrongTimes);
                result.put("session_attributes",attributes);
                System.out.println(result.toJSONString());
                LOG.info("7777");
                return result.toJSONString();
            }
        }
        String query = intent.getString("query");
        String slots = intent.getString("slots");

        System.out.println(jsonParam);
        System.out.println(slots);
        // answer
        String qstring = query.replace(" ","");
        for(String punctuation : Configuration.PUNCTUATION_SET)
        {
            qstring = qstring.replace(punctuation,"");
        }
        if (qstring.equals("退出小问") || qstring.equals("退出")){

            JSONObject response = new JSONObject();
            JSONObject to_speak = new JSONObject();
            to_speak.put("type",0);
            to_speak.put("text","已为您退出,下次再问");

            response.put("to_speak",to_speak);
            response.put("open_mic",true);

            JSONObject result = new JSONObject();
            result.put("version", "1.0");
            result.put("is_session_end", false);
            result.put("response", response);
            wrongTimes = ""+(Integer.parseInt(wrongTimes)+1);
            attributes.put("wrongTimes",wrongTimes);
            result.put("session_attributes",attributes);
            System.out.println(result.toJSONString());
            LOG.info("6666");
            return result.toJSONString();
        }
        if (qstring.equals("") || qstring == null){

            if (wrongTimes.equals("2")||wrongTimes.equals("-1")){
                JSONObject response = new JSONObject();
                JSONObject to_speak = new JSONObject();
                to_speak.put("type", 0);
                to_speak.put("text", "小问还是没听清,已为您退出,下次再问");

                response.put("to_speak", to_speak);
                response.put("open_mic", false);

                JSONObject result = new JSONObject();
                result.put("version", "1.0");
                result.put("is_session_end", true);
                result.put("response", response);
                System.out.println(result.toJSONString());
                LOG.info("5555");
                return result.toJSONString();
            }

            JSONObject response = new JSONObject();
            JSONObject to_speak = new JSONObject();
            to_speak.put("type",0);
            to_speak.put("text",",没听清,可以问一些学术问题哦");

            response.put("to_speak",to_speak);
            response.put("open_mic",true);

            JSONObject result = new JSONObject();
            result.put("version", "1.0");
            result.put("is_session_end", false);
            result.put("response", response);
            wrongTimes = ""+(Integer.parseInt(wrongTimes)+1);
            attributes.put("wrongTimes",wrongTimes);
            result.put("session_attributes",attributes);
            System.out.println(result.toJSONString());
            LOG.info("4444");
            return result.toJSONString();
        }
        if(qstring.equals("打开小问")||qstring.equals("进入小问")) {

            JSONObject response = new JSONObject();
            JSONObject to_speak = new JSONObject();
            to_speak.put("type",0);
            to_speak.put("text","已进入小问,可以问一些学术问题,比如:策略迭代对应的英文是什么？说退出小问,就可以退出");
            attributes.put("wrongTimes","0");

            response.put("to_speak",to_speak);
            response.put("open_mic",true);

            JSONObject result = new JSONObject();
            result.put("version", "1.0");
            result.put("is_session_end", false);
            result.put("response", response);
            result.put("session_attributes",attributes);
            System.out.println(result.toJSONString());
            LOG.info("3333");
            return result.toJSONString();
        }
        for(String punctuation : Configuration.PUNCTUATION_SET)
        {
            qstring = qstring.replace(punctuation,"");
        }
        Question q = new Question(query);
        if (qstring.startsWith("问小问一个问题")){
            System.out.println("answer");
        }
        qstring = qstring.replace("问小问一个问题","");
        qstring = qstring.replace("问小问一下","");
        qstring = qstring.replace("让小问回答一下","");
        qstring = qstring.replace("让小问答一下","");
        q.setQuestionString(qstring);

        String answer = _runTask(q);

        System.out.println("answer:"+answer);
        if(answer.contains("我还得再想想")){

            if(jsonParam.containsKey("session")) {
                if (wrongTimes.equals("2")){
                    JSONObject response = new JSONObject();
                    JSONObject to_speak = new JSONObject();
                    to_speak.put("type", 0);
                    to_speak.put("text", "小问还是不会,已为您退出,下次再问");

                    response.put("to_speak", to_speak);
                    response.put("open_mic", false);

                    JSONObject result = new JSONObject();
                    result.put("version", "1.0");
                    result.put("is_session_end", true);
                    result.put("response", response);
                    System.out.println(result.toJSONString());
                    LOG.info("2222");
                    return result.toJSONString();
                }
            }
            // 将获取的json数据封装一层，然后在给返回
            JSONObject response = new JSONObject();
            JSONObject to_speak = new JSONObject();
            to_speak.put("type",0);
            to_speak.put("text","答案是:"+answer);

            wrongTimes = ""+(Integer.parseInt(wrongTimes)+1);
            attributes.put("wrongTimes",wrongTimes);

            response.put("to_speak",to_speak);
            response.put("open_mic",true);
            JSONObject result = new JSONObject();
            result.put("version", "1.0");
            result.put("is_session_end", false);
            result.put("response", response);
            result.put("session_attributes",attributes);
            System.out.println(result.toJSONString());
            LOG.info("1111");
            return result.toJSONString();
        }

        // 将获取的json数据封装一层，然后在给返回
        JSONObject response = new JSONObject();
        JSONObject to_speak = new JSONObject();
        to_speak.put("type",0);
        to_speak.put("text","答案是:"+answer);
        response.put("to_speak",to_speak);
        response.put("open_mic",true);
        JSONObject result = new JSONObject();
        result.put("version", "1.0");
        result.put("is_session_end", false);
        result.put("response", response);
        wrongTimes = "0";
        attributes.put("wrongTimes",wrongTimes);
        result.put("session_attributes",attributes);
        System.out.println(result.toJSONString());
        LOG.info("0000");
        return result.toJSONString();
    }


    @ResponseBody
    @RequestMapping(value = "/json/hupo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String getByRequestHupo(HttpServletRequest r) {
        JSONObject jsonParam = this.getJSONParam(r);

        // 将获取的json数据封装一层，然后在给返回

        String query = jsonParam.getString("text");


        System.out.println(query);
        // answer
        String qstring = query.replace(" ","");
        if (qstring == "" || qstring == null){
            return "noquestion";
        }

        for(String punctuation : Configuration.PUNCTUATION_SET)
        {
            qstring = qstring.replace(punctuation,"");
        }
        Question q = new Question(query);

        q.setQuestionString(qstring);

        KbqaQueryDriver esQuerySynonymKBQADriver = new ESQuerySynonymKBQA();
        q = esQuerySynonymKBQADriver.kbQueryAnswers(q);

        KbqaQueryDriver ALGQuerySynonymKBQADriver = new ALGQuerySynonymKBQA();
        q = ALGQuerySynonymKBQADriver.kbQueryAnswers(q);
        // 对答案进行排序
        AnswerAnalysisDriverImpl analysisDriver = new AnswerAnalysisDriverImpl();
        q = analysisDriver.rankAnswerCandidate(q);

        //生成答案并返回
        q = analysisDriver.returnAnswer(q);
        String answer = q.getReturnedAnswer().getAnswerString().trim();
        System.out.println("answer:"+answer);
        // 将获取的json数据封装一层，然后在给返回
        JSONObject data = new JSONObject();

        data.put("text", answer);

        JSONObject result = new JSONObject();
        result.put("code", "0");
        result.put("message", "sthwrong");

        System.out.println(result.toJSONString());
        return result.toJSONString();
    }
    public JSONObject getJSONParam(HttpServletRequest request){
        JSONObject jsonParam = null;
        try {
            // 获取输入流
            BufferedReader streamReader = new BufferedReader(new InputStreamReader(request.getInputStream(), "UTF-8"));

            // 写入数据到Stringbuilder
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = streamReader.readLine()) != null) {
                sb.append(line);
            }
            jsonParam = JSONObject.parseObject(sb.toString());
            // 直接将json信息打印出来
            System.out.println(jsonParam.toJSONString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonParam;
    }
}