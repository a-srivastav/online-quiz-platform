package com.quizplatform.model;
import java.time.LocalDateTime;

public abstract class User {
    private long id;
    private String name;
    private String email;
    private String passwordHash;
    private LocalDateTime createdAt;
    protected User(long id, String name, String email, String passwordHash) { this.id=id; this.name=name; this.email=email; this.passwordHash=passwordHash; }
    public long getId(){return id;} public void setId(long id){this.id=id;}
    public String getName(){return name;} public void setName(String name){this.name=name;}
    public String getEmail(){return email;} public void setEmail(String email){this.email=email;}
    public String getPasswordHash(){return passwordHash;} public void setPasswordHash(String passwordHash){this.passwordHash=passwordHash;}
    public LocalDateTime getCreatedAt(){return createdAt;} public void setCreatedAt(LocalDateTime createdAt){this.createdAt=createdAt;}
    public abstract String getRole();
}
