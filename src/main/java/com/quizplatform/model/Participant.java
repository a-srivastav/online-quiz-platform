package com.quizplatform.model;
public class Participant extends User { public Participant(long id,String name,String email,String hash){super(id,name,email,hash);} @Override public String getRole(){return "PARTICIPANT";} }
