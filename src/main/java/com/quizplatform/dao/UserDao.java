package com.quizplatform.dao;
import com.quizplatform.model.*; import org.springframework.jdbc.core.JdbcTemplate; import org.springframework.stereotype.Repository; import java.util.*;
/** Reads and writes user records and maps database roles to user subclasses. */
@Repository public class UserDao {
 private final JdbcTemplate jdbc; public UserDao(JdbcTemplate jdbc){this.jdbc=jdbc;}
 // The role column determines the concrete subclass, demonstrating runtime polymorphism.
 private User map(java.sql.ResultSet rs)throws java.sql.SQLException {long id=rs.getLong("id");String n=rs.getString("name"),e=rs.getString("email"),p=rs.getString("password_hash"),r=rs.getString("role");User user=switch(r){case "ADMIN"->new Admin(id,n,e,p);case "CREATOR"->new QuizCreator(id,n,e,p);default->new Participant(id,n,e,p);};user.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());return user;}
 /** Finds a user by email; the placeholder binds the value as a SQL parameter. */
 public Optional<User> findByEmail(String email){var rows=jdbc.query("SELECT * FROM users WHERE email=?",(rs,n)->map(rs),email);return rows.stream().findFirst();}
 /** Finds a user by database ID, returning an empty Optional when absent. */
 public Optional<User> findById(long id){var rows=jdbc.query("SELECT * FROM users WHERE id=?",(rs,n)->map(rs),id);return rows.stream().findFirst();}
 /** Inserts a user record and returns the database-generated ID. */
 public long create(String name,String email,String hash,String role){jdbc.update("INSERT INTO users(name,email,password_hash,role) VALUES(?,?,?,?)",name,email,hash,role);return jdbc.queryForObject("SELECT LAST_INSERT_ID()",Long.class);}
 /** Returns all users, with the newest accounts listed first. */
 public List<User> findAll(){return jdbc.query("SELECT * FROM users ORDER BY created_at DESC",(rs,n)->map(rs));}
}
