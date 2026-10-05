package com.quizplatform.model;
import org.junit.jupiter.api.Test; import static org.junit.jupiter.api.Assertions.*;
class UserRoleTest {
 @Test void subclassesExposeTheirRolesThroughTheBaseType(){User admin=new Admin(1,"Admin","a@example.com","hash");User creator=new QuizCreator(2,"Creator","c@example.com","hash");User participant=new Participant(3,"Learner","p@example.com","hash");assertEquals("ADMIN",admin.getRole());assertEquals("CREATOR",creator.getRole());assertEquals("PARTICIPANT",participant.getRole());}
}
