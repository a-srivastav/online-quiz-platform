package com.quizplatform.service;
import com.quizplatform.model.Question; import org.junit.jupiter.api.Test; import java.util.*; import static org.junit.jupiter.api.Assertions.*;
/** Checks that quiz scoring awards points only for valid correct responses. */
class DefaultScoringServiceTest {
 private final ScoringService scoring=new DefaultScoringService();
 // One answer is correct for two points and one is incorrect, so only two points should be awarded.
 @Test void addsPointsForCorrectAnswersOnly(){List<Question> questions=List.of(new Question(1,1,"First","a","b","c","d","B",2),new Question(2,1,"Second","a","b","c","d","D",3));Map<Long,String> answers=Map.of(1L,"B",2L,"A");assertEquals(2,scoring.score(questions,answers));}
 // An omitted response must not accidentally match a question's correct answer.
 @Test void ignoresMissingAnswers(){Question q=new Question(1,1,"First","a","b","c","d","A",1);assertEquals(0,scoring.score(List.of(q),Map.of()));}
}
