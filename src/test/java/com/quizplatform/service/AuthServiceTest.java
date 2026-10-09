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

/** Verifies that seeded accounts can authenticate using their shared demo password. */
class AuthServiceTest {
    // BCrypt hash for the seeded demo password used by all sample accounts.
    private static final String SEEDED_HASH = "$2a$10$VQxEIQ465O1pXyZ.lkN7.OE.0mCqrQf9xSWq56fZiKIvP5rTTsu6.";

    @Test
    void seededAccountsAuthenticateWithDemoPassword() {
        // Mock the data-access layer so each test account is returned without a database.
        UserDao users = mock(UserDao.class);
        Map<String, User> accounts = Map.of(
                "admin@quizplatform.local", new Admin(1, "System Administrator", "admin@quizplatform.local", SEEDED_HASH),
                "creator@quizplatform.local", new QuizCreator(2, "Aarav Sharma", "creator@quizplatform.local", SEEDED_HASH),
                "participant@quizplatform.local", new Participant(3, "Ananya Patel", "participant@quizplatform.local", SEEDED_HASH));
        // Configure the mock lookup to return the matching role-specific user for each email.
        accounts.forEach((email, user) -> when(users.findByEmail(email)).thenReturn(Optional.of(user)));

        AuthService auth = new AuthService(users, new BCryptPasswordEncoder());

        // The demo password must authenticate every role and preserve each account's role.
        accounts.forEach((email, expected) -> assertEquals(expected.getRole(), auth.login(email, "password").getRole()));
    }
}
