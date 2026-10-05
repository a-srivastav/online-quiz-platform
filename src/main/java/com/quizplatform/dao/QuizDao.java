package com.quizplatform.dao;
import com.quizplatform.model.*; import org.springframework.jdbc.core.JdbcTemplate; import org.springframework.stereotype.Repository; import java.util.*;
@Repository public class QuizDao {
 private final JdbcTemplate jdbc; public QuizDao(JdbcTemplate jdbc){this.jdbc=jdbc;}
 private static final String SELECT="SELECT id,creator_id,title,description,duration_minutes,published,created_at FROM quizzes";
 private Quiz map(java.sql.ResultSet r)throws java.sql.SQLException{return new Quiz(r.getLong("id"),r.getLong("creator_id"),r.getString("title"),r.getString("description"),r.getInt("duration_minutes"),r.getBoolean("published"),r.getTimestamp("created_at").toLocalDateTime());}
 public List<Quiz> allPublished(){return jdbc.query(SELECT+" WHERE published=1 ORDER BY created_at DESC",(r,n)->map(r));}
 public List<Quiz> all(){return jdbc.query(SELECT+" ORDER BY created_at DESC",(r,n)->map(r));}
 public List<Quiz> byCreator(long id){return jdbc.query(SELECT+" WHERE creator_id=? ORDER BY created_at DESC",(r,n)->map(r),id);}
 public Optional<Quiz> find(long id){return jdbc.query(SELECT+" WHERE id=?",(r,n)->map(r),id).stream().findFirst();}
 public long create(long creator,String title,String description,int duration){jdbc.update("INSERT INTO quizzes(creator_id,title,description,duration_minutes) VALUES(?,?,?,?)",creator,title,description,duration);return jdbc.queryForObject("SELECT LAST_INSERT_ID()",Long.class);}
 public void update(long id,long creator,String title,String description,int duration,boolean published){jdbc.update("UPDATE quizzes SET title=?,description=?,duration_minutes=?,published=? WHERE id=? AND creator_id=?",title,description,duration,published,id,creator);}
 public void delete(long id,long creator){jdbc.update("DELETE FROM quizzes WHERE id=? AND creator_id=?",id,creator);}
}
