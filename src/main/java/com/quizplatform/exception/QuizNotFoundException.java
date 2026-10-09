package com.quizplatform.exception;
/** Indicates that a requested quiz could not be found or is unavailable. */
public class QuizNotFoundException extends RuntimeException { public QuizNotFoundException(String message){super(message);} }
