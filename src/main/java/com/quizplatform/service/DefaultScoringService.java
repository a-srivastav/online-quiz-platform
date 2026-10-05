package com.quizplatform.service;
import com.quizplatform.model.Question; import org.springframework.stereotype.Service; import java.util.*;
@Service public class DefaultScoringService implements ScoringService {
 @Override public int score(List<Question> questions,Map<Long,String> answers){return questions.stream().filter(q->q.getCorrectOption().equalsIgnoreCase(answers.getOrDefault(q.getId(),""))).mapToInt(Question::getPoints).sum();}
}
