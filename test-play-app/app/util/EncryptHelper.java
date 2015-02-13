package util;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.RandomStringUtils;



/**
 * Created by aabramov on 12/6/14.
 */
public class EncryptHelper {

    private static final String ENCRYPT_SECRET = "ashdfWER234p8kwQ";

    private static String encrypt(String input, String key) {

        try {
            String ivstr = RandomStringUtils.randomAlphanumeric(16);
            IvParameterSpec iv = new IvParameterSpec(ivstr.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
            byte[] ar = cipher.doFinal(input.getBytes());
            //merge the encrypted string with the initialization vector
            String ivstrX64 = Base64.encodeBase64String(ivstr.getBytes("UTF-8"));
            String encrypted = Base64.encodeBase64String(ar);
            return ivstrX64 + "_"+ encrypted;
        } catch (Exception ex) {
            throw new IllegalArgumentException("Could not encrypt", ex);
        }
    }

    public static String encrypt(String input) {
        return EncryptHelper.encrypt(input, ENCRYPT_SECRET);
    }

    private static String decrypt(String input, String key) {

        try {

            //pull the iv
            String[] ar = input.split("_");
            if (ar.length != 2) throw new IllegalArgumentException("Invalid format of encrypted string");
            String ivstr = ar[0];
            String in = ar[1];

            byte[] ivstrAr = Base64.decodeBase64(ivstr);
            IvParameterSpec iv = new IvParameterSpec(ivstrAr);

            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] original = cipher.doFinal(Base64.decodeBase64(in));

            return new String(original, "UTF-8");
        } catch (Exception ex) {
            throw new IllegalArgumentException("Could not decrypt", ex);
        }

    }

    public static String decrypt(String input) {
        return EncryptHelper.decrypt(input, ENCRYPT_SECRET);
    }


    public static String hash(String input, String key) {
        try {

            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(secretKey);
            byte[] hmacData = mac.doFinal(input.getBytes("UTF-8"));
            return new String(Base64.encodeBase64URLSafe(hmacData), "UTF-8");

        }
        catch (NoSuchAlgorithmException e) {
            throw new ApiException("Failed to hash input string", e);
        }
        catch (InvalidKeyException e) {
            throw new ApiException("Failed to hash input string", e);
        }
        catch (UnsupportedEncodingException e) {
            throw new ApiException("Failed to hash input string", e);
        }
    }
}
