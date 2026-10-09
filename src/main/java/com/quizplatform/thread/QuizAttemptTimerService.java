package com.quizplatform.thread;
import com.quizplatform.dao.AttemptDao; import jakarta.annotation.PreDestroy; import org.springframework.stereotype.Service; import java.util.concurrent.*;
/**
 * Schedules quiz-attempt expiration independently of web request threads.
 *
 * <p>A single daemon scheduler runs timeout tasks, while a concurrent map tracks
 * the scheduled task for each active attempt.</p>
 */
@Service public class QuizAttemptTimerService {
 private final ScheduledExecutorService scheduler=Executors.newScheduledThreadPool(1,r->{Thread t=new Thread(r,"quiz-attempt-timer");t.setDaemon(true);return t;});
 private final ConcurrentMap<Long,ScheduledFuture<?>> active=new ConcurrentHashMap<>(); private final AttemptDao attempts;
 public QuizAttemptTimerService(AttemptDao attempts){this.attempts=attempts;}
 /** Schedules an expiration task after the quiz's allotted number of minutes. */
 public void start(long attemptId,int minutes){ScheduledFuture<?> task=scheduler.schedule(()->expire(attemptId),minutes,TimeUnit.MINUTES);ScheduledFuture<?> old=active.putIfAbsent(attemptId,task);if(old!=null)task.cancel(false);}
 // Removing the task first prevents a cancelled or already handled task expiring the attempt again.
 private synchronized void expire(long id){if(active.remove(id)!=null)attempts.expire(id);}
 /** Cancels and removes an attempt's timer after normal submission. */
 public void cancel(long id){ScheduledFuture<?> task=active.remove(id);if(task!=null)task.cancel(false);}
 /** Stops the scheduler when Spring closes the application context. */
 @PreDestroy public void shutdown(){scheduler.shutdownNow();}
}
