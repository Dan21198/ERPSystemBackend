package osu.auth.service;

import osu.auth.repository.JwtBlacklistRepository;
import osu.auth.model.JwtBlacklist;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
public class TokenBlacklistService {

    private final JwtBlacklistRepository jwtBlacklistRepository;
    private final JwtService jwtService;

    public TokenBlacklistService(JwtBlacklistRepository jwtBlacklistRepository, JwtService jwtService) {
        this.jwtBlacklistRepository = jwtBlacklistRepository;
        this.jwtService = jwtService;
    }

    public boolean blacklistToken(String token) {
        if (jwtBlacklistRepository.existsByToken(token)) {
            return false;
        }

        LocalDateTime expirationTime = jwtService.extractExpiration(token).toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDateTime();

        JwtBlacklist blacklistedToken = new JwtBlacklist();
        blacklistedToken.setToken(token);
        blacklistedToken.setCreatedAt(LocalDateTime.now());
        blacklistedToken.setExpiresAt(expirationTime);

        jwtBlacklistRepository.save(blacklistedToken);
        return true;
    }
}
