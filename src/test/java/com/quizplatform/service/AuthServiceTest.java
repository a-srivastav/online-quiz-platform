package com.quizplatform.service;

import com.quizplatform.dao.UserDao;
import com.quizplatform.model.Admin;
import com.quizplatform.model.Participant;
import com.quizplatform.model.QuizCreator;
import com.quizplatform.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AuthServiceTest {
    private static final String SEEDED_HASH = "$2a$10$VQxEIQ465O1pXyZ.lkN7.OE.0mCqrQf9xSWq56fZiKIvP5rTTsu6.";

    @Test
    void seededAccountsAuthenticateWithDemoPassword() {
        UserDao users = mock(UserDao.class);
        Map<String, User> accounts = Map.of(
                "admin@quizplatform.local", new Admin(1, "System Administrator", "admin@quizplatform.local", SEEDED_HASH),
                "creator@quizplatform.local", new QuizCreator(2, "Aarav Sharma", "creator@quizplatform.local", SEEDED_HASH),
                "participant@quizplatform.local", new Participant(3, "Ananya Patel", "participant@quizplatform.local", SEEDED_HASH));
        accounts.forEach((email, user) -> when(users.findByEmail(email)).thenReturn(Optional.of(user)));

        AuthService auth = new AuthService(users, new BCryptPasswordEncoder());

        accounts.forEach((email, expected) -> assertEquals(expected.getRole(), auth.login(email, "password").getRole()));
    }
}
