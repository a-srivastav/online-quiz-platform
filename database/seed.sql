-- Use the application database populated by schema.sql.
USE quiz_platform;

-- Demo accounts share the password "password"; use only for local/testing environments.
-- Password for each account: password (demo credentials only; change for real deployments).
-- BCrypt hash generated with BCrypt strength 10 and verified by Spring Security's encoder.
-- Insert or refresh the demo accounts while keeping their email addresses unique.
INSERT INTO users(name,email,password_hash,role) VALUES
('System Administrator','admin@quizplatform.local','$2a$10$VQxEIQ465O1pXyZ.lkN7.OE.0mCqrQf9xSWq56fZiKIvP5rTTsu6.','ADMIN'),
('Anant Srivastava','creator@quizplatform.local','$2a$10$VQxEIQ465O1pXyZ.lkN7.OE.0mCqrQf9xSWq56fZiKIvP5rTTsu6.','CREATOR'),
('Ananya Patel','participant@quizplatform.local','$2a$10$VQxEIQ465O1pXyZ.lkN7.OE.0mCqrQf9xSWq56fZiKIvP5rTTsu6.','PARTICIPANT')
ON DUPLICATE KEY UPDATE name=VALUES(name), password_hash=VALUES(password_hash), role=VALUES(role);

-- Add one published sample quiz for the creator, but only if it is not already present.
INSERT INTO quizzes(creator_id,title,description,duration_minutes,published)
SELECT id,'Java and Object-Oriented Programming','A short introduction to core Java and OOP concepts.',20,TRUE FROM users WHERE email='creator@quizplatform.local'
AND NOT EXISTS (SELECT 1 FROM quizzes WHERE title='Java and Object-Oriented Programming');

-- Add sample questions to the sample quiz.
-- The derived seed table lists the question text, four choices, and correct option.
-- The NOT EXISTS condition makes this sample-data insertion safe to run repeatedly.
INSERT INTO questions(quiz_id,question_text,option_a,option_b,option_c,option_d,correct_option,points)
SELECT q.id, seed.question_text,seed.option_a,seed.option_b,seed.option_c,seed.option_d,seed.correct_option,1
FROM quizzes q JOIN (
 SELECT 'Which keyword is used to inherit a class in Java?' question_text,'implements' option_a,'extends' option_b,'inherits' option_c,'superclass' option_d,'B' correct_option
 UNION ALL SELECT 'Which OOP principle hides internal object details?','Inheritance','Polymorphism','Encapsulation','Compilation','C'
 UNION ALL SELECT 'What is the return type of a Java constructor?','void','The class type','int','A constructor has no return type','D'
 UNION ALL SELECT 'Which collection preserves insertion order and allows duplicates?','HashSet','ArrayList','TreeSet','Map','B'
) seed ON q.title='Java and Object-Oriented Programming'
WHERE NOT EXISTS (SELECT 1 FROM questions WHERE quiz_id=q.id);
