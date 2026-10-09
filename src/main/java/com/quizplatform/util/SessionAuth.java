package com.quizplatform.util;
import com.quizplatform.model.User; import jakarta.servlet.http.*; import org.springframework.ui.Model;
/** Provides helpers for reading the logged-in user and enforcing role checks. */
public final class SessionAuth {private SessionAuth(){} /** Returns the session user, or null when no valid user is logged in. */ public static User user(HttpSession s){Object u=s.getAttribute("user");return u instanceof User?(User)u:null;} /** Requires a logged-in user with the given role; otherwise throws a handled authorization exception. */ public static User require(HttpSession s,String role){User u=user(s);if(u==null)throw new UnauthenticatedException();if(!u.getRole().equals(role))throw new ForbiddenException();return u;} /** Adds the current session user to the MVC model for the page template. */ public static void addUser(Model m,HttpSession s){m.addAttribute("currentUser",user(s));}
 // Separate exception types allow the global handler to redirect login requests and render access-denied pages.
 public static class UnauthenticatedException extends RuntimeException{} public static class ForbiddenException extends RuntimeException{}
}
