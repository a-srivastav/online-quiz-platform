package com.quizplatform.model;
public class Question {
    private long id,quizId; private String text,optionA,optionB,optionC,optionD,correctOption; private int points;
    public Question(long id,long quizId,String text,String optionA,String optionB,String optionC,String optionD,String correctOption,int points){this.id=id;this.quizId=quizId;this.text=text;this.optionA=optionA;this.optionB=optionB;this.optionC=optionC;this.optionD=optionD;this.correctOption=correctOption;this.points=points;}
    public long getId(){return id;} public void setId(long v){id=v;} public long getQuizId(){return quizId;} public void setQuizId(long v){quizId=v;} public String getText(){return text;} public void setText(String v){text=v;}
    public String getOptionA(){return optionA;} public void setOptionA(String v){optionA=v;} public String getOptionB(){return optionB;} public void setOptionB(String v){optionB=v;} public String getOptionC(){return optionC;} public void setOptionC(String v){optionC=v;} public String getOptionD(){return optionD;} public void setOptionD(String v){optionD=v;} public String getCorrectOption(){return correctOption;} public void setCorrectOption(String v){correctOption=v;} public int getPoints(){return points;} public void setPoints(int v){points=v;}
}
