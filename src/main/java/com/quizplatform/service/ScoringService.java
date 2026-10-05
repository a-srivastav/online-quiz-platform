package com.quizplatform.service;
import com.quizplatform.model.Question; import java.util.List; import java.util.Map;
public interface ScoringService { int score(List<Question> questions, Map<Long,String> answers); }
