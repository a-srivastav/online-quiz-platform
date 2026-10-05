package com.quizplatform.util;
import com.quizplatform.model.User; import jakarta.servlet.http.*; import org.springframework.ui.Model;
public final class SessionAuth {private SessionAuth(){} public static User user(HttpSession s){Object u=s.getAttribute("user");return u instanceof User?(User)u:null;} public static User require(HttpSession s,String role){User u=user(s);if(u==null)throw new UnauthenticatedException();if(!u.getRole().equals(role))throw new ForbiddenException();return u;} public static void addUser(Model m,HttpSession s){m.addAttribute("currentUser",user(s));}
 public static class UnauthenticatedException extends RuntimeException{} public static class ForbiddenException extends RuntimeException{}
}
