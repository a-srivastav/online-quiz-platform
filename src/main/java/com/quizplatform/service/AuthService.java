package com.quizplatform.service;
import com.quizplatform.dao.UserDao; import com.quizplatform.model.*; import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; import org.springframework.stereotype.Service;
/** Authenticates existing accounts and validates and stores new user accounts. */
@Service public class AuthService {private final UserDao users;private final BCryptPasswordEncoder encoder;public AuthService(UserDao u,BCryptPasswordEncoder e){users=u;encoder=e;}
 /** Normalizes the email before lookup and checks the password against its stored BCrypt hash. */
 public User login(String email,String password){User u=users.findByEmail(email.trim().toLowerCase()).orElseThrow(()->new IllegalArgumentException("Email or password is incorrect."));if(!encoder.matches(password,u.getPasswordHash()))throw new IllegalArgumentException("Email or password is incorrect.");return u;}
 /** Validates registration details and stores the password in hashed form. */
 public void register(String name,String email,String password,String role){if(name==null||name.isBlank()||email==null||!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"))throw new IllegalArgumentException("Enter a valid name and email.");if(password==null||password.length()<8)throw new IllegalArgumentException("Password must contain at least 8 characters.");if(!role.equals("CREATOR")&&!role.equals("PARTICIPANT"))throw new IllegalArgumentException("Choose a valid account type.");if(users.findByEmail(email.trim().toLowerCase()).isPresent())throw new IllegalArgumentException("An account already uses that email.");users.create(name.trim(),email.trim().toLowerCase(),encoder.encode(password),role);}
}
