package com.yazhi1992.yazhilib.utils;

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by zengyazhi on 17/7/10.
 */

public class LibAESUtils {

    private LibAESUtils() {
    }

    private static final String SEC_NORMALIZE_ALG = "SHA-256";
    private static final String IV_STRING = "WisezoneFudaojun";
    private static final String charset = "UTF-8";

    /**
     * 加密
     */
    public static String encrypt(String key, String content) {
        try {
            byte[] contentBytes = content.getBytes(charset);
//		byte[] keyBytes = key.getBytes(charset);
            MessageDigest dig = MessageDigest.getInstance(SEC_NORMALIZE_ALG);
            byte[] keyBytes = dig.digest(key.getBytes(charset));
            byte[] encryptedBytes = aesEncryptBytes(contentBytes, keyBytes);
            return new String(Base64.encode(encryptedBytes, Base64.URL_SAFE), charset);
        } catch (Exception e) {
            LibUtils.myLog("LibAESUtils encrypt Exception " + e.toString()
                    + " content " + content
                    + " key " + key);
            return content;
        }
    }

    /**
     * 解密
     */
    public static String decrypt(String key, String content) {
        try {
            byte[] encryptedBytes = Base64.decode(content, Base64.URL_SAFE);
//	    byte[] keyBytes = key.getBytes(charset);

            MessageDigest dig = MessageDigest.getInstance(SEC_NORMALIZE_ALG);
            byte[] keyBytes = dig.digest(key.getBytes(charset));

            byte[] decryptedBytes = aesDecryptBytes(encryptedBytes, keyBytes);
            return new String(decryptedBytes, charset);
        } catch (Exception e) {
            LibUtils.myLog("LibAESUtils encrypt Exception " + e.toString()
                    + " content " + content
                    + " key " + key);
            return content;
        }
    }

    public static byte[] aesEncryptBytes(byte[] contentBytes, byte[] keyBytes) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
        return cipherOperation(contentBytes, keyBytes, Cipher.ENCRYPT_MODE);
    }

    public static byte[] aesDecryptBytes(byte[] contentBytes, byte[] keyBytes) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
        return cipherOperation(contentBytes, keyBytes, Cipher.DECRYPT_MODE);
    }

    private static byte[] cipherOperation(byte[] contentBytes, byte[] keyBytes, int mode) throws UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        SecretKeySpec secretKey = new SecretKeySpec(keyBytes, "AES");

        byte[] initParam = IV_STRING.getBytes(charset);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(initParam);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(mode, secretKey, ivParameterSpec);

        return cipher.doFinal(contentBytes);
    }
}
