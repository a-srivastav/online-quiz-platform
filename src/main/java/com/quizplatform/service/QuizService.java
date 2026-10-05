package com.quizplatform.service;
import com.quizplatform.model.*; import java.util.*;
public interface QuizService { List<Quiz> available(); List<Question> questions(long quizId); long start(long quizId,long participantId); long submit(long attemptId,long participantId,Map<Long,String> answers); }
