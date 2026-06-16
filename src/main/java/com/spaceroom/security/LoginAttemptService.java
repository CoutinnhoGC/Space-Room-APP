package com.spaceroom.security;

import com.spaceroom.exceptions.BusinessException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class LoginAttemptService {

    private static final int MAX_FAILURES = 5;
    private static final long BLOCK_MINUTES = 15;

    private final Map<String, AttemptState> attempts = new ConcurrentHashMap<>();

    public void ensureAllowed(String email, String clientIp) {
        AttemptState state = attempts.get(buildKey(email, clientIp));
        if (state == null || state.blockedUntil == null || state.blockedUntil.isBefore(LocalDateTime.now())) {
            return;
        }

        throw new BusinessException("Muitas tentativas de login. Tente novamente mais tarde.");
    }

    public void recordFailure(String email, String clientIp) {
        AttemptState state = attempts.computeIfAbsent(buildKey(email, clientIp), ignored -> new AttemptState());
        state.failures++;
        if (state.failures >= MAX_FAILURES) {
            state.blockedUntil = LocalDateTime.now().plusMinutes(BLOCK_MINUTES);
        }
    }

    public void recordSuccess(String email, String clientIp) {
        attempts.remove(buildKey(email, clientIp));
    }

    private String buildKey(String email, String clientIp) {
        return (email == null ? "unknown" : email) + "|" + (clientIp == null ? "unknown" : clientIp);
    }

    private static final class AttemptState {
        private int failures;
        private LocalDateTime blockedUntil;
    }
}
