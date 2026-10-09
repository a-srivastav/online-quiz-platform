package com.quizplatform.dao;
import com.quizplatform.model.Question; import org.springframework.jdbc.core.JdbcTemplate; import org.springframework.stereotype.Repository; import java.util.*;
/** Performs database operations for quiz questions. */
@Repository public class QuestionDao {
 private final JdbcTemplate jdbc;public QuestionDao(JdbcTemplate jdbc){this.jdbc=jdbc;}
 // Maps the result-set columns to the corresponding fields of a Question object.
 private Question map(java.sql.ResultSet r)throws java.sql.SQLException{return new Question(r.getLong("id"),r.getLong("quiz_id"),r.getString("question_text"),r.getString("option_a"),r.getString("option_b"),r.getString("option_c"),r.getString("option_d"),r.getString("correct_option"),r.getInt("points"));}
 /** Returns a quiz's questions in their database ID order. */
 public List<Question> byQuiz(long id){return jdbc.query("SELECT * FROM questions WHERE quiz_id=? ORDER BY id",(r,n)->map(r),id);}
 /** Finds a question by ID, or returns an empty Optional when it is absent. */
 public Optional<Question> find(long id){return jdbc.query("SELECT * FROM questions WHERE id=?",(r,n)->map(r),id).stream().findFirst();}
 /** Inserts a new question and its four choices into the database. */
 public void create(long quiz,String text,String a,String b,String c,String d,String correct,int points){jdbc.update("INSERT INTO questions(quiz_id,question_text,option_a,option_b,option_c,option_d,correct_option,points) VALUES(?,?,?,?,?,?,?,?)",quiz,text,a,b,c,d,correct,points);}
 /** Updates a question only when it belongs to the specified quiz. */
 public void update(long id,long quiz,String text,String a,String b,String c,String d,String correct,int points){jdbc.update("UPDATE questions SET question_text=?,option_a=?,option_b=?,option_c=?,option_d=?,correct_option=?,points=? WHERE id=? AND quiz_id=?",text,a,b,c,d,correct,points,id,quiz);}
 /** Deletes a question only when it belongs to the specified quiz. */
 public void delete(long id,long quiz){jdbc.update("DELETE FROM questions WHERE id=? AND quiz_id=?",id,quiz);}
}
