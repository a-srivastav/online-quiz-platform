package com.quizplatform.model;
public class QuizCreator extends User { public QuizCreator(long id,String name,String email,String hash){super(id,name,email,hash);} @Override public String getRole(){return "CREATOR";} }
