package com.quizplatform.service;
import com.quizplatform.model.Question; import org.springframework.stereotype.Service; import java.util.*;
/** Calculates a quiz score from the points assigned to correctly answered questions. */
@Service public class DefaultScoringService implements ScoringService {
 // Responses are matched by question ID, so answers for one question cannot score another.
 @Override public int score(List<Question> questions,Map<Long,String> answers){return questions.stream().filter(q->q.getCorrectOption().equalsIgnoreCase(answers.getOrDefault(q.getId(),""))).mapToInt(Question::getPoints).sum();}
}
