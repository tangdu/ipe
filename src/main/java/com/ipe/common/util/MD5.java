package com.ipe.common.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5工具类
 *
 * @author tangdu
 * @time 2013-5-11 下午7:49:06
 */
public class MD5 {
    protected static Logger log = LoggerFactory.getLogger(MD5.class);

    /**
     * 加密原始字符串
     * <p>
     * String source = "a"; String result = digest(source); //result =
     * "0cc175b9c0f1b6a831c399e269772661";
     * </p>
     *
     * @param inStr
     * @return
     * @throws java.security.NoSuchAlgorithmException
     */
    public static String digest(String inStr) throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        char[] charArray = inStr.toCharArray();
        byte[] byteArray = new byte[charArray.length];

        for (int i = 0; i < charArray.length; i++)
            byteArray[i] = (byte) charArray[i];

        byte[] md5Bytes = md5.digest(byteArray);

        StringBuffer hexValue = new StringBuffer();

        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16)
                hexValue.append("0");
            hexValue.append(Integer.toHexString(val));
        }

        return hexValue.toString();
    }

    /**
     * 可逆的加密算法 将加密过的字符串再加密
     *
     * @param inStr
     * @return
     */
    public static String KL(String inStr) {
        // String s = new String(inStr);
        char[] a = inStr.toCharArray();
        for (int i = 0; i < a.length; i++) {
            a[i] = (char) (a[i] ^ 't');
        }
        String s = new String(a);
        return s;
    }

    /**
     * 将二次加密后的字符串解密成一次加密的字符串
     *
     * @param inStr
     * @return
     */
    public static String JM(String inStr) {
        char[] a = inStr.toCharArray();
        for (int i = 0; i < a.length; i++) {
            a[i] = (char) (a[i] ^ 't');
        }
        String k = new String(a);
        return k;
    }

    public static void main(String args[]) {
        try {
            System.out.println(digest("admin"));
            System.out.println(digest("b"));
            System.out.println(digest("admin"));
            String s = new String("qin");
            System.out.println("原始：" + s);
            System.out.println("MD5后：" + digest(s));
            System.out.println("MD5后再加密：" + KL(digest(s)));
            System.out.println("解密为MD5后的：" + JM(KL(digest(s))));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}