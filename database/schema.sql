-- Create the database with Unicode support, then select it for the table definitions below.
CREATE DATABASE IF NOT EXISTS quiz_platform CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE quiz_platform;

-- Account records shared by administrators, quiz creators, and participants.
-- The unique email prevents duplicate accounts; role limits values to supported account types.
CREATE TABLE IF NOT EXISTS users (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(120) NOT NULL,
  email VARCHAR(190) NOT NULL UNIQUE,
  password_hash VARCHAR(100) NOT NULL,
  role ENUM('ADMIN','CREATOR','PARTICIPANT') NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Quizzes belong to a creator. Deleting that user also removes their quizzes.
-- The duration check keeps quizzes within the application's allowed time range.
CREATE TABLE IF NOT EXISTS quizzes (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  creator_id BIGINT NOT NULL,
  title VARCHAR(180) NOT NULL,
  description TEXT,
  duration_minutes INT NOT NULL DEFAULT 20,
  published BOOLEAN NOT NULL DEFAULT FALSE,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_quiz_creator FOREIGN KEY (creator_id) REFERENCES users(id) ON DELETE CASCADE,
  CONSTRAINT chk_quiz_duration CHECK (duration_minutes BETWEEN 1 AND 180)
);

-- Each question belongs to one quiz and provides four multiple-choice options.
-- Foreign-key cascading removes questions with their quiz; checks validate the answer key and points.
CREATE TABLE IF NOT EXISTS questions (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  quiz_id BIGINT NOT NULL,
  question_text TEXT NOT NULL,
  option_a VARCHAR(500) NOT NULL, option_b VARCHAR(500) NOT NULL,
  option_c VARCHAR(500) NOT NULL, option_d VARCHAR(500) NOT NULL,
  correct_option CHAR(1) NOT NULL,
  points INT NOT NULL DEFAULT 1,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_question_quiz FOREIGN KEY (quiz_id) REFERENCES quizzes(id) ON DELETE CASCADE,
  CONSTRAINT chk_correct_option CHECK (correct_option IN ('A','B','C','D')),
  CONSTRAINT chk_question_points CHECK (points > 0)
);

-- One row represents a participant's attempt, including its score and lifecycle status.
-- Quiz and participant foreign keys keep attempts connected to existing records.
CREATE TABLE IF NOT EXISTS quiz_attempts (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  quiz_id BIGINT NOT NULL,
  participant_id BIGINT NOT NULL,
  score INT NOT NULL DEFAULT 0,
  total_points INT NOT NULL DEFAULT 0,
  status ENUM('IN_PROGRESS','SUBMITTED','EXPIRED') NOT NULL DEFAULT 'IN_PROGRESS',
  started_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  submitted_at TIMESTAMP NULL,
  CONSTRAINT fk_attempt_quiz FOREIGN KEY (quiz_id) REFERENCES quizzes(id) ON DELETE CASCADE,
  CONSTRAINT fk_attempt_participant FOREIGN KEY (participant_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Stores the selected option and awarded points for each question in an attempt.
-- The unique pair prevents the same question from being recorded twice for one attempt.
CREATE TABLE IF NOT EXISTS answers (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  attempt_id BIGINT NOT NULL,
  question_id BIGINT NOT NULL,
  selected_option CHAR(1) NULL,
  is_correct BOOLEAN NOT NULL DEFAULT FALSE,
  points_awarded INT NOT NULL DEFAULT 0,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_answer_attempt FOREIGN KEY (attempt_id) REFERENCES quiz_attempts(id) ON DELETE CASCADE,
  CONSTRAINT fk_answer_question FOREIGN KEY (question_id) REFERENCES questions(id) ON DELETE CASCADE,
  CONSTRAINT uq_attempt_question UNIQUE (attempt_id, question_id)
);

-- Stores the final percentage and point totals for a completed attempt.
-- A unique attempt_id enforces one result record per attempt.
CREATE TABLE IF NOT EXISTS results (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  attempt_id BIGINT NOT NULL UNIQUE,
  score INT NOT NULL,
  total_points INT NOT NULL,
  percentage DECIMAL(5,2) NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_result_attempt FOREIGN KEY (attempt_id) REFERENCES quiz_attempts(id) ON DELETE CASCADE
);
