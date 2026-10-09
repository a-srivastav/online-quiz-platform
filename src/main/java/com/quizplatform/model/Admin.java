package com.quizplatform.model;
/** Represents an administrator account as a specialized type of {@link User}. */
public class Admin extends User { public Admin(long id,String name,String email,String hash){super(id,name,email,hash);} @Override public String getRole(){return "ADMIN";} }
