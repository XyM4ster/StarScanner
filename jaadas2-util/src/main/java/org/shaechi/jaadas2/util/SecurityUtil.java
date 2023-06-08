package org.shaechi.jaadas2.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SecurityUtil {

    public static boolean validateFilePath(String filepath) {
        if (filepath.contains(".."))
            return false;
        if (!filepath.endsWith(".apk") || !filepath.endsWith(".jar") || !filepath.endsWith(".dex"))
            return false;
        if (filepath.contains("//"))
            return false;
        return true;
    }

    public static String calcFileHash(String filepath) throws IOException, NoSuchAlgorithmException {
        File file = new File(filepath);
        byte[] bytes = new byte[(int) file.length()];
        FileInputStream stream = new FileInputStream(file);
        stream.read(bytes);
        stream.close();
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(bytes,0,bytes.length);
        BigInteger i = new BigInteger(1,md.digest());
        String md5hash = String.format("%1$032X", i);
        return md5hash;
    }
}
