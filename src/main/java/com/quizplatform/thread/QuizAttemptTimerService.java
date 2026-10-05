package com.quizplatform.thread;
import com.quizplatform.dao.AttemptDao; import jakarta.annotation.PreDestroy; import org.springframework.stereotype.Service; import java.util.concurrent.*;
@Service public class QuizAttemptTimerService {
 private final ScheduledExecutorService scheduler=Executors.newScheduledThreadPool(1,r->{Thread t=new Thread(r,"quiz-attempt-timer");t.setDaemon(true);return t;});
 private final ConcurrentMap<Long,ScheduledFuture<?>> active=new ConcurrentHashMap<>(); private final AttemptDao attempts;
 public QuizAttemptTimerService(AttemptDao attempts){this.attempts=attempts;}
 public void start(long attemptId,int minutes){ScheduledFuture<?> task=scheduler.schedule(()->expire(attemptId),minutes,TimeUnit.MINUTES);ScheduledFuture<?> old=active.putIfAbsent(attemptId,task);if(old!=null)task.cancel(false);}
 private synchronized void expire(long id){if(active.remove(id)!=null)attempts.expire(id);}
 public void cancel(long id){ScheduledFuture<?> task=active.remove(id);if(task!=null)task.cancel(false);}
 @PreDestroy public void shutdown(){scheduler.shutdownNow();}
}
