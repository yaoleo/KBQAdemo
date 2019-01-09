package com.qa.demo.controller;

import com.qa.demo.answerAnalysis.AnswerAnalysisDriverImpl;
import com.qa.demo.conf.Configuration;
import com.qa.demo.dataStructure.DataSource;
import com.qa.demo.dataStructure.QueryTuple;
import com.qa.demo.dataStructure.Question;
import com.qa.demo.query.*;
import com.qa.demo.questionAnalysis.Segmentation;
import com.qa.demo.systemController.FaqDemo;
import org.apache.log4j.PropertyConfigurator;
import org.nlpcn.commons.lang.util.logging.Log;
import org.nlpcn.commons.lang.util.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import com.alibaba.fastjson.JSONObject;
import static com.qa.demo.conf.FileConfig.LOG_PROPERTY;

/**
 * Created by hyh on 2017/8/14.
 */
@Controller
public class MainController {

    public static final Log LOG = LogFactory.getLog(FaqDemo.class);

    @RequestMapping(value = "/",method = RequestMethod.GET)
    public String Index()
    {
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

    /**
     * 创建日期:2018年4月6日<br/>
     * 功能描述:通过request的方式来获取到json数据<br/>
     * @param jsonParam 这个是阿里的 fastjson对象
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/json/data", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String getByJSON(@RequestParam  JSONObject jsonParam) {
        // 直接将json信息打印出来
        System.out.println(jsonParam.toJSONString());
        // get query
        JSONObject request = jsonParam.getJSONObject("request");
        JSONObject intent = request.getJSONObject("intent");
        String query = intent.getString("query");
        String slots = intent.getString("slots");

        System.out.println(query);
        System.out.println(slots);
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
        if (qstring.startsWith("问小问一个问题")){
            System.out.println("answer");
        }
        qstring = qstring.replace("问小问一个问题","");
        qstring = qstring.replace("问小问一下","");
        qstring = qstring.replace("让小问回答一下","");
        qstring = qstring.replace("让小问答一下","");
        q.setQuestionString(qstring);

        //从模板的同义词集合中查询，泛化主要功能；
        KbqaQueryDriver ALGQuerySynonymKBQADriver = new ALGQuerySynonymKBQA();
        q = ALGQuerySynonymKBQADriver.kbQueryAnswers(q);

        //从ES索引的模板库中匹配模板，并形成查询三元组，最终通过KG三元组匹配得到候选答案;
        KbqaQueryDriver esQuerySynonymKBQADriver = new ESQuerySynonymKBQA();
        q = esQuerySynonymKBQADriver.kbQueryAnswers(q);
        // 对答案进行排序
        AnswerAnalysisDriverImpl analysisDriver = new AnswerAnalysisDriverImpl();
        q = analysisDriver.rankAnswerCandidate(q);

        //生成答案并返回
        q = analysisDriver.returnAnswer(q);
        String answer = q.getReturnedAnswer().getAnswerString().trim();
        System.out.println("answer:"+answer);
        // 将获取的json数据封装一层，然后在给返回
        JSONObject response = new JSONObject();
        JSONObject to_speak = new JSONObject();
        to_speak.put("type",0);
        to_speak.put("text","test-小问:"+answer);
        response.put("to_speak",to_speak);

        JSONObject result = new JSONObject();
        result.put("version", "1.0");
        result.put("is_session_end", true);
        result.put("response", response);
        System.out.println(result.toJSONString());
        return result.toJSONString();
    }

    /**
     * 创建日期:2018年4月6日<br/>
     * 功能描述:通过request的方式来获取到json数据<br/>
     * @param jsonParam 这个是阿里的 fastjson对象
     * @return
     */

    @ResponseBody
    @RequestMapping(value = "/json/hupo", consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8",method = RequestMethod.POST)

    public String getByJSONAndReturn(@RequestBody JSONObject jsonParam) {

        // 直接将json信息打印出来
        System.out.println(jsonParam.toJSONString());
        // get query
        String QueryText = jsonParam.getString("text");
        System.out.println(QueryText);
        // answer
        String qstring = QueryText.replace(" ","");
        if (qstring == "" || qstring == null){
            return "noquestion";
        }
        for(String punctuation : Configuration.PUNCTUATION_SET)
        {
            qstring = qstring.replace(punctuation,"");
        }
        Question q = new Question(QueryText);
        if (qstring.startsWith("问小问一个问题")){
            System.out.println("answer");
        }
        qstring = qstring.replace("问小问一个问题","");
        qstring = qstring.replace("问小问一下","");
        qstring = qstring.replace("让小问回答一下","");
        qstring = qstring.replace("让小问答一下","");
        q.setQuestionString(qstring);

        //从模板的同义词集合中查询，泛化主要功能；
        KbqaQueryDriver ALGQuerySynonymKBQADriver = new ALGQuerySynonymKBQA();
        q = ALGQuerySynonymKBQADriver.kbQueryAnswers(q);

        //从ES索引的模板库中匹配模板，并形成查询三元组，最终通过KG三元组匹配得到候选答案;
        KbqaQueryDriver esQuerySynonymKBQADriver = new ESQuerySynonymKBQA();
        q = esQuerySynonymKBQADriver.kbQueryAnswers(q);
        // 对答案进行排序
        AnswerAnalysisDriverImpl analysisDriver = new AnswerAnalysisDriverImpl();
        q = analysisDriver.rankAnswerCandidate(q);

        //生成答案并返回
        q = analysisDriver.returnAnswer(q);
        String answer = q.getReturnedAnswer().getAnswerString().trim();
        System.out.println("answer:"+answer);


        // 将获取的json数据封装一层，然后在给返回
        JSONObject result = new JSONObject();
        JSONObject to_speak = new JSONObject();
        to_speak.put("text",answer);
        result.put("code",0);
        result.put("message", "返回错误！");
        result.put("data",to_speak);
        System.out.println(result.toJSONString());
        return result.toJSONString();
    }

}
