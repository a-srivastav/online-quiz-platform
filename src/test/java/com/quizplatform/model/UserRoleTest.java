package com.quizplatform.model;
import org.junit.jupiter.api.Test; import static org.junit.jupiter.api.Assertions.*;
/** Confirms that each User subclass reports its own role through the common base type. */
class UserRoleTest {
 // Assigning different subclasses to User references checks runtime polymorphism via getRole().
 @Test void subclassesExposeTheirRolesThroughTheBaseType(){User admin=new Admin(1,"Admin","a@example.com","hash");User creator=new QuizCreator(2,"Creator","c@example.com","hash");User participant=new Participant(3,"Learner","p@example.com","hash");assertEquals("ADMIN",admin.getRole());assertEquals("CREATOR",creator.getRole());assertEquals("PARTICIPANT",participant.getRole());}
}
