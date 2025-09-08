package com.compartetutiempo.util;

import org.springframework.core.env.Environment;

public class ProfileUtils {
    private ProfileUtils() {}

    public static boolean isProd(Environment env) {
        if (env == null) return false;
        for (String profile : env.getActiveProfiles()) {
            if (profile.equalsIgnoreCase("prod")) {
                return true;
            }
        }
        return false;
    }
}
