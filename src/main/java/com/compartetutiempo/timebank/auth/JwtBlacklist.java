/*
 * Clase generada con GitHub Copilot Chat Extension
 */

package com.compartetutiempo.timebank.auth;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Iterator;
import org.springframework.scheduling.annotation.Scheduled;

@Component
public class JwtBlacklist {
    @Autowired
    private Environment environment;
    // token -> expiration timestamp (ms)
    private final Map<String, Long> blacklist = new ConcurrentHashMap<>();

    /**
     * Añade un token a la blacklist con su tiempo de expiración (en ms).
     */
    public void add(String token, long expirationMillis) {
        if (!isProd()) {
            System.out.println("[Blacklist] Añadiendo token: '" + token + "' con expiración: " + expirationMillis);
        }
        blacklist.put(token, expirationMillis);
        cleanup();
    }

    /**
     * Verifica si el token está en la blacklist y no ha expirado.
     */
    public boolean contains(String token) {
        if (!isProd()) {
            System.out.println("[Blacklist] Comprobando token: '" + token + "'");
        }
        Long exp = blacklist.get(token);
        if (exp == null) {
            if (!isProd()) {
                System.out.println("[Blacklist] Token NO encontrado");
            }
            return false;
        }
        if (exp < System.currentTimeMillis()) {
            if (!isProd()) {
                System.out.println("[Blacklist] Token expirado, eliminando");
            }
            blacklist.remove(token);
            return false;
        }
        if (!isProd()) {
            System.out.println("[Blacklist] Token ENCONTRADO y válido");
        }
        return true;
    }

    private boolean isProd() {
        if (environment == null) return false;
        for (String profile : environment.getActiveProfiles()) {
            if (profile.equalsIgnoreCase("prod")) {
                return true;
            }
        }
        return false;
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
