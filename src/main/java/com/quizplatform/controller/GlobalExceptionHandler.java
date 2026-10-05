package com.quizplatform.controller;
import com.quizplatform.util.SessionAuth; import jakarta.servlet.http.HttpSession; import org.springframework.ui.Model; import org.springframework.web.bind.annotation.*;
import com.quizplatform.exception.QuizNotFoundException;
import java.util.NoSuchElementException;
@ControllerAdvice public class GlobalExceptionHandler {
 @ExceptionHandler(SessionAuth.UnauthenticatedException.class) public String unauthenticated(){return "redirect:/login";}
 @ExceptionHandler({SessionAuth.ForbiddenException.class,SecurityException.class}) @ResponseStatus(org.springframework.http.HttpStatus.FORBIDDEN) public String forbidden(Model m,HttpSession s){SessionAuth.addUser(m,s);m.addAttribute("message","You do not have permission to open this page.");return "error";}
 @ExceptionHandler({IllegalArgumentException.class,IllegalStateException.class,NoSuchElementException.class,QuizNotFoundException.class}) @ResponseStatus(org.springframework.http.HttpStatus.BAD_REQUEST) public String problem(Exception e,Model m,HttpSession s){SessionAuth.addUser(m,s);m.addAttribute("message",e.getMessage()==null?"Something went wrong while handling your request.":e.getMessage());return "error";}
 @ExceptionHandler(Exception.class) @ResponseStatus(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR) public String unexpected(Model m,HttpSession s){SessionAuth.addUser(m,s);m.addAttribute("message","An unexpected error occurred. Please try again.");return "error";}
}
