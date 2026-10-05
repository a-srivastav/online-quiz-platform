package com.quizplatform.model;
import java.time.LocalDateTime;
public class Quiz {
    private long id, creatorId; private String title, description; private int durationMinutes; private boolean published; private LocalDateTime createdAt;
    public Quiz(long id,long creatorId,String title,String description,int durationMinutes,boolean published,LocalDateTime createdAt){this.id=id;this.creatorId=creatorId;this.title=title;this.description=description;this.durationMinutes=durationMinutes;this.published=published;this.createdAt=createdAt;}
    public long getId(){return id;} public void setId(long v){id=v;} public long getCreatorId(){return creatorId;} public void setCreatorId(long v){creatorId=v;}
    public String getTitle(){return title;} public void setTitle(String v){title=v;} public String getDescription(){return description;} public void setDescription(String v){description=v;}
    public int getDurationMinutes(){return durationMinutes;} public void setDurationMinutes(int v){durationMinutes=v;} public boolean isPublished(){return published;} public void setPublished(boolean v){published=v;} public LocalDateTime getCreatedAt(){return createdAt;} public void setCreatedAt(LocalDateTime v){createdAt=v;}
}
