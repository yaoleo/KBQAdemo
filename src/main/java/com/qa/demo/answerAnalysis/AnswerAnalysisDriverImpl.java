package com.qa.demo.answerAnalysis;

import com.qa.demo.dataStructure.Answer;
import com.qa.demo.dataStructure.Question;

import java.util.HashSet;
import java.util.List;

public class AnswerAnalysisDriverImpl implements AnswerAnalysisDriver {

    /**
     * 分析答案类型是否合理，并修改question中候选answer的score；
     * @param q
     * @return
     */
    public Question typeInferenceJudge(Question q) {
        return null;
    }

    /**
     * 根据证据进行重排，并修改question中候选answer的score；
     * @param q
     * @return
     */
    public Question rankAnswerCandidate(Question q) {
        List<Answer> answers = q.getCandidateAnswer();
        answers = RerankAnswer.rank(answers);
        q.setCandidateAnswer(answers);
        return q;
    }

    /**
     * 确定最终答案，并修改question中的returnedAnswer；
     * @param q
     * @return
     */
    public Question returnAnswer(Question q) {
        List<Answer> answers = q.getCandidateAnswer();
        if(answers.size() >= 1){// union
            Answer answer = answers.get(0);
            HashSet<String> a_strings = new HashSet<>();
            a_strings.add(answer.getAnswerString());
            String a_string = "";
            double a_score = answer.getAnswerScore();
            for(int i = 1; i < answers.size(); i++) {
                if (answers.get(i).getAnswerScore() == a_score) {
                    a_strings.add(answers.get(i).getAnswerString());
                }
            }
            for(String s: a_strings){
                a_string+=(s+"\t\n");
            }
            answer.setAnswerString(a_string.trim());
            q.setReturnedAnswer(answer);
        }else{
            Answer answer = new Answer();
            q.setReturnedAnswer(answer);
        }
        return q;
    }
}
