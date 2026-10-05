package com.quizplatform.model;
public class Admin extends User { public Admin(long id,String name,String email,String hash){super(id,name,email,hash);} @Override public String getRole(){return "ADMIN";} }
