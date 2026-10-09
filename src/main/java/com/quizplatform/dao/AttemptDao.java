package com.quizplatform.dao;
import com.quizplatform.model.QuizAttempt; import org.springframework.jdbc.core.JdbcTemplate; import org.springframework.stereotype.Repository; import java.util.*;
/** Stores quiz attempts, submitted answers, and report/result data. */
@Repository public class AttemptDao {
 private final JdbcTemplate jdbc;public AttemptDao(JdbcTemplate jdbc){this.jdbc=jdbc;}
 /** Creates an attempt and returns its database-generated ID. */
 public long create(long quiz,long user,String status){jdbc.update("INSERT INTO quiz_attempts(quiz_id,participant_id,status) VALUES(?,?,?)",quiz,user,status);return jdbc.queryForObject("SELECT LAST_INSERT_ID()",Long.class);}
 /** Saves one participant's selected option and the points awarded for it. */
 public void answer(long attempt,long question,String choice,boolean correct,int awarded){jdbc.update("INSERT INTO answers(attempt_id,question_id,selected_option,is_correct,points_awarded) VALUES(?,?,?,?,?)",attempt,question,choice,correct,awarded);}
 /** Records the final score, status, and submission time for an attempt. */
 public void finish(long id,int score,int total,String status){jdbc.update("UPDATE quiz_attempts SET score=?,total_points=?,status=?,submitted_at=CURRENT_TIMESTAMP WHERE id=?",score,total,status,id);}
 /** Marks an attempt expired only if it is still in progress. */
 public void expire(long id){jdbc.update("UPDATE quiz_attempts SET status='EXPIRED',submitted_at=CURRENT_TIMESTAMP WHERE id=? AND status='IN_PROGRESS'",id);}
 /** Stores a result row and calculates the percentage, avoiding division by zero. */
 public void addResult(long attempt,long score,int total){jdbc.update("INSERT INTO results(attempt_id,score,total_points,percentage) VALUES(?,?,?,?)",attempt,score,total,total==0?0.0:score*100.0/total);}
 // A submitted_at value can be null for an attempt that has not yet finished.
 private QuizAttempt map(java.sql.ResultSet r)throws java.sql.SQLException{return new QuizAttempt(r.getLong("id"),r.getLong("quiz_id"),r.getLong("participant_id"),r.getInt("score"),r.getInt("total_points"),r.getString("status"),r.getTimestamp("started_at").toLocalDateTime(),r.getTimestamp("submitted_at")==null?null:r.getTimestamp("submitted_at").toLocalDateTime());}
 private static final String Q="SELECT * FROM quiz_attempts";
 /** Lists one participant's attempts, newest first. */
 public List<QuizAttempt> byParticipant(long id){return jdbc.query(Q+" WHERE participant_id=? ORDER BY started_at DESC",(r,n)->map(r),id);}
 /** Finds an attempt by ID, or returns an empty Optional when absent. */
 public Optional<QuizAttempt> find(long id){return jdbc.query(Q+" WHERE id=?",(r,n)->map(r),id).stream().findFirst();}
 /** Locks an attempt row while its surrounding transaction processes it. */
 public Optional<QuizAttempt> findForUpdate(long id){return jdbc.query(Q+" WHERE id=? FOR UPDATE",(r,n)->map(r),id).stream().findFirst();}
 /** Returns attempt information across all quizzes for administrative reports. */
 public List<Map<String,Object>> report(){return jdbc.queryForList("SELECT qa.id attempt_id,q.title quiz_title,u.name participant,qa.score,qa.total_points,qa.status,qa.started_at,qa.submitted_at FROM quiz_attempts qa JOIN quizzes q ON q.id=qa.quiz_id JOIN users u ON u.id=qa.participant_id ORDER BY qa.started_at DESC");}
 /** Returns attempts for quizzes owned by the specified creator. */
 public List<Map<String,Object>> forCreator(long id){return jdbc.queryForList("SELECT qa.id attempt_id,q.title quiz_title,u.name participant,qa.score,qa.total_points,qa.status,qa.started_at FROM quiz_attempts qa JOIN quizzes q ON q.id=qa.quiz_id JOIN users u ON u.id=qa.participant_id WHERE q.creator_id=? ORDER BY qa.started_at DESC",id);}
 /** Retrieves the combined attempt, quiz title, and percentage for a result page. */
 public Map<String,Object> result(long attempt){return jdbc.queryForMap("SELECT qa.*,q.title quiz_title,r.percentage FROM quiz_attempts qa JOIN quizzes q ON q.id=qa.quiz_id JOIN results r ON r.attempt_id=qa.id WHERE qa.id=?",attempt);}
}
