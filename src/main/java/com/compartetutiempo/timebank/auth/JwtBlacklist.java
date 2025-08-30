/*
 * Clase generada con GitHub Copilot Chat Extension
 */

package com.compartetutiempo.timebank.auth;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Iterator;
import org.springframework.scheduling.annotation.Scheduled;

@Component
public class JwtBlacklist {
    // token -> expiration timestamp (ms)
    private final Map<String, Long> blacklist = new ConcurrentHashMap<>();

    /**
     * Añade un token a la blacklist con su tiempo de expiración (en ms).
     */
    public void add(String token, long expirationMillis) {
        blacklist.put(token, expirationMillis);
        cleanup();
    }

    /**
     * Verifica si el token está en la blacklist y no ha expirado.
     */
    public boolean contains(String token) {
        Long exp = blacklist.get(token);
        if (exp == null) return false;
        if (exp < System.currentTimeMillis()) {
            blacklist.remove(token);
            return false;
        }
        return true;
    }

    /**
     * Limpia tokens expirados (llamado manualmente o por scheduler).
     */
    @Scheduled(fixedDelay = 60000) // cada minuto
    public void cleanup() {
        long now = System.currentTimeMillis();
        Iterator<Map.Entry<String, Long>> it = blacklist.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Long> entry = it.next();
            if (entry.getValue() < now) {
                it.remove();
            }
        }
    }
}
