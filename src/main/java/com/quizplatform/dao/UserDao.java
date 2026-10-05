package com.quizplatform.dao;
import com.quizplatform.model.*; import org.springframework.jdbc.core.JdbcTemplate; import org.springframework.stereotype.Repository; import java.util.*;
@Repository public class UserDao {
 private final JdbcTemplate jdbc; public UserDao(JdbcTemplate jdbc){this.jdbc=jdbc;}
 private User map(java.sql.ResultSet rs)throws java.sql.SQLException {long id=rs.getLong("id");String n=rs.getString("name"),e=rs.getString("email"),p=rs.getString("password_hash"),r=rs.getString("role");User user=switch(r){case "ADMIN"->new Admin(id,n,e,p);case "CREATOR"->new QuizCreator(id,n,e,p);default->new Participant(id,n,e,p);};user.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());return user;}
 public Optional<User> findByEmail(String email){var rows=jdbc.query("SELECT * FROM users WHERE email=?",(rs,n)->map(rs),email);return rows.stream().findFirst();}
 public Optional<User> findById(long id){var rows=jdbc.query("SELECT * FROM users WHERE id=?",(rs,n)->map(rs),id);return rows.stream().findFirst();}
 public long create(String name,String email,String hash,String role){jdbc.update("INSERT INTO users(name,email,password_hash,role) VALUES(?,?,?,?)",name,email,hash,role);return jdbc.queryForObject("SELECT LAST_INSERT_ID()",Long.class);}
 public List<User> findAll(){return jdbc.query("SELECT * FROM users ORDER BY created_at DESC",(rs,n)->map(rs));}
}
