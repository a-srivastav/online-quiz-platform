package com.quizplatform.dao;
import com.quizplatform.model.Question; import org.springframework.jdbc.core.JdbcTemplate; import org.springframework.stereotype.Repository; import java.util.*;
@Repository public class QuestionDao {
 private final JdbcTemplate jdbc;public QuestionDao(JdbcTemplate jdbc){this.jdbc=jdbc;}
 private Question map(java.sql.ResultSet r)throws java.sql.SQLException{return new Question(r.getLong("id"),r.getLong("quiz_id"),r.getString("question_text"),r.getString("option_a"),r.getString("option_b"),r.getString("option_c"),r.getString("option_d"),r.getString("correct_option"),r.getInt("points"));}
 public List<Question> byQuiz(long id){return jdbc.query("SELECT * FROM questions WHERE quiz_id=? ORDER BY id",(r,n)->map(r),id);}
 public Optional<Question> find(long id){return jdbc.query("SELECT * FROM questions WHERE id=?",(r,n)->map(r),id).stream().findFirst();}
 public void create(long quiz,String text,String a,String b,String c,String d,String correct,int points){jdbc.update("INSERT INTO questions(quiz_id,question_text,option_a,option_b,option_c,option_d,correct_option,points) VALUES(?,?,?,?,?,?,?,?)",quiz,text,a,b,c,d,correct,points);}
 public void update(long id,long quiz,String text,String a,String b,String c,String d,String correct,int points){jdbc.update("UPDATE questions SET question_text=?,option_a=?,option_b=?,option_c=?,option_d=?,correct_option=?,points=? WHERE id=? AND quiz_id=?",text,a,b,c,d,correct,points,id,quiz);}
 public void delete(long id,long quiz){jdbc.update("DELETE FROM questions WHERE id=? AND quiz_id=?",id,quiz);}
}
