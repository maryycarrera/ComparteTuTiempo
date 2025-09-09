/*
 * Clase generada con GitHub Copilot Chat Extension
 */

package com.compartetutiempo.timebank.auth;

import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Iterator;
import org.springframework.scheduling.annotation.Scheduled;

@Component
public class JwtBlacklist {

    // token -> expiration timestamp (ms)
    private final Map<String, Long> blacklist = new ConcurrentHashMap<>();

    private static final Logger logger = LoggerFactory.getLogger(JwtBlacklist.class);

    /**
     * Añade un token a la blacklist con su tiempo de expiración (en ms).
     */
    public void add(String token, long expirationMillis) {
        logger.debug("[Blacklist] Añadiendo token: '" + token + "' con expiración: " + expirationMillis);
        blacklist.put(token, expirationMillis);
        cleanup();
    }

    /**
     * Verifica si el token está en la blacklist y no ha expirado.
     */
    public boolean contains(String token) {
        logger.debug("[Blacklist] Comprobando token: '" + token + "'");
        Long exp = blacklist.get(token);
        if (exp == null) {
            logger.debug("[Blacklist] Token NO encontrado");
            return false;
        }
        if (exp < System.currentTimeMillis()) {
            logger.debug("[Blacklist] Token expirado, eliminando");
            blacklist.remove(token);
            return false;
        }
        logger.debug("[Blacklist] Token ENCONTRADO y válido");
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
