package me.joshmendiola.JoServer.service.utils;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;

public class Utility
{
    private static final int FILENAME_LENGTH = 32;

    public static String generateUniqueFilename()
    {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[FILENAME_LENGTH];
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().encodeToString(bytes);
    }
}
