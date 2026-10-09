package com.quizplatform.model;
import java.time.LocalDateTime;

/**
 * Shared data and behavior for every account type in the platform.
 *
 * <p>Role-specific subclasses inherit these common fields and provide their
 * own role name through {@link #getRole()}.</p>
 */
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
    /** Returns the role name used for authorization and role-specific pages. */
    public abstract String getRole();
}
