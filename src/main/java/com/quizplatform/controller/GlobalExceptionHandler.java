package com.quizplatform.controller;
import com.quizplatform.util.SessionAuth; import jakarta.servlet.http.HttpSession; import org.springframework.ui.Model; import org.springframework.web.bind.annotation.*;
import com.quizplatform.exception.QuizNotFoundException;
import java.util.NoSuchElementException;
/** Converts application exceptions into suitable login redirects or error pages. */
@ControllerAdvice public class GlobalExceptionHandler {
 /** Sends unauthenticated users to the login page. */
 @ExceptionHandler(SessionAuth.UnauthenticatedException.class) public String unauthenticated(){return "redirect:/login";}
 /** Shows a forbidden response when a logged-in user lacks the required access. */
 @ExceptionHandler({SessionAuth.ForbiddenException.class,SecurityException.class}) @ResponseStatus(org.springframework.http.HttpStatus.FORBIDDEN) public String forbidden(Model m,HttpSession s){SessionAuth.addUser(m,s);m.addAttribute("message","You do not have permission to open this page.");return "error";}
 /** Shows expected validation, state, or missing-record errors to the user. */
 @ExceptionHandler({IllegalArgumentException.class,IllegalStateException.class,NoSuchElementException.class,QuizNotFoundException.class}) @ResponseStatus(org.springframework.http.HttpStatus.BAD_REQUEST) public String problem(Exception e,Model m,HttpSession s){SessionAuth.addUser(m,s);m.addAttribute("message",e.getMessage()==null?"Something went wrong while handling your request.":e.getMessage());return "error";}
 /** Provides a generic error page for exceptions not handled more specifically. */
 @ExceptionHandler(Exception.class) @ResponseStatus(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR) public String unexpected(Model m,HttpSession s){SessionAuth.addUser(m,s);m.addAttribute("message","An unexpected error occurred. Please try again.");return "error";}
}
