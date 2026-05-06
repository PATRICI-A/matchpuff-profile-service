package com.matchpuff.profileservice.application.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PasswordHashingService {

    private final BCryptPasswordEncoder passwordEncoder;

    public PasswordHashingService() {
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    /**
     * Hashea una contraseña en plano usando BCrypt
     *
     * @param rawPassword la contraseña en plano
     * @return la contraseña hasheada
     */
    public String hashPassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    /**
     * Verifica si una contraseña en plano coincide con su hash
     *
     * @param rawPassword la contraseña en plano
     * @param hashedPassword el hash de la contraseña
     * @return true si coinciden, false en caso contrario
     */
    public boolean verifyPassword(String rawPassword, String hashedPassword) {
        return passwordEncoder.matches(rawPassword, hashedPassword);
    }
}
