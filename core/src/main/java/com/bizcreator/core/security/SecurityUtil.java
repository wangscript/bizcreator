package com.bizcreator.core.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SecurityUtil {

    public static String getDigest(String p_password) {
        String result = null;
        MessageDigest messageDigest;

        try {
            messageDigest = MessageDigest.getInstance("SHA-1");
            messageDigest.reset();
            messageDigest.update(p_password.getBytes());
        }
        catch (NoSuchAlgorithmException e) {
            // Can't happen...  SHA-1 is a known algorithm
            messageDigest = null;
        }

        if (messageDigest != null) {
            byte[] tampon = messageDigest.digest();

            StringBuffer sb = new StringBuffer();

            for (int i = 0; i < tampon.length; i++) {
                sb.append(new Byte(tampon[i]));
                sb.append('.');
            }

            result = sb.toString();
        }

        return result;
    }
}
