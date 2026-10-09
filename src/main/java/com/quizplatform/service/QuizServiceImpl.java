package com.quizplatform.service;
import com.quizplatform.dao.*; import com.quizplatform.exception.QuizNotFoundException; import com.quizplatform.model.*; import com.quizplatform.thread.QuizAttemptTimerService;
import org.springframework.stereotype.Service; import org.springframework.transaction.annotation.Transactional; import java.util.*;
/** Implements quiz availability, attempt lifecycle, and answer submission rules. */
@Service public class QuizServiceImpl implements QuizService {
 private final QuizDao quizzes;private final QuestionDao questions;private final AttemptDao attempts;private final ScoringService scoring;private final QuizAttemptTimerService timer;
 public QuizServiceImpl(QuizDao q,QuestionDao qs,AttemptDao a,ScoringService s,QuizAttemptTimerService t){quizzes=q;questions=qs;attempts=a;scoring=s;timer=t;}
 /** Returns only quizzes that creators have published. */
 public List<Quiz> available(){return quizzes.allPublished();}
 /** Returns a quiz's questions, after confirming that the quiz exists. */
 public List<Question> questions(long id){if(quizzes.find(id).isEmpty())throw new QuizNotFoundException("Quiz not found.");return questions.byQuiz(id);}
 /** Creates a timed attempt for an available quiz containing at least one question. */
 @Transactional public long start(long quizId,long participant){Quiz q=quizzes.find(quizId).filter(Quiz::isPublished).orElseThrow(()->new QuizNotFoundException("This quiz is not available."));if(questions.byQuiz(quizId).isEmpty())throw new IllegalStateException("This quiz has no questions yet.");long id=attempts.create(quizId,participant,"IN_PROGRESS");timer.start(id,q.getDurationMinutes());return id;}
 /**
  * Validates and scores answers, saves each answer and the final result, then
  * cancels the timer. The transaction keeps these database updates together.
  */
 @Transactional public long submit(long attemptId,long participant,Map<Long,String> answers){QuizAttempt a=attempts.findForUpdate(attemptId).filter(x->x.getParticipantId()==participant&&x.getStatus().equals("IN_PROGRESS")).orElseThrow(()->new IllegalStateException("Attempt is unavailable, expired, or already submitted."));List<Question> qs=questions.byQuiz(a.getQuizId());Map<Long,String> safe=new HashMap<>();for(Question q:qs){String value=answers.get(q.getId());if(value!=null&&value.matches("[ABCD]"))safe.put(q.getId(),value);}int score=scoring.score(qs,safe),total=qs.stream().mapToInt(Question::getPoints).sum();for(Question q:qs){String choice=safe.get(q.getId());boolean correct=choice!=null&&q.getCorrectOption().equalsIgnoreCase(choice);attempts.answer(attemptId,q.getId(),choice,correct,correct?q.getPoints():0);}attempts.finish(attemptId,score,total,"SUBMITTED");attempts.addResult(attemptId,score,total);timer.cancel(attemptId);return attemptId;}
}
