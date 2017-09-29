package com.e_eduspace.forms.utils;

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * Created by Administrator on 2017-08-08.
 */

public class RSAHelper {
    private final String RSA = "RSA";
    private final int RSA_KEY_SIZE = 1024;
    private final String RSA_ECB_PKCS1PADDING = "RSA/ECB/PKCS1Padding";
    private KeyPair mKeyPair;

    public RSAHelper() {
        mKeyPair = generateKeyPair();
    }
    /**
     * 生成密钥对
     */
    private KeyPair generateKeyPair() {
        try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance(RSA);
            kpg.initialize(RSA_KEY_SIZE, new SecureRandom());
            return kpg.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取公钥
     */
    public String getPublicKey() {
        return mKeyPair == null ? "" : new String(Base64.encode(mKeyPair.getPublic().getEncoded(), Base64.DEFAULT));
    }

    /**
     * 获取私钥
     */
    public String getPrivateKey() {
        return mKeyPair == null ? "" : new String(Base64.encode(mKeyPair.getPrivate().getEncoded(), Base64.DEFAULT));
    }

    /**
     * 加密
     * @param data
     * @param secretKey
     * @return
     */
    private String encrypt(byte[] data, String secretKey, boolean pubKey){
        try {
            byte[] decodeKey = Base64.decode(secretKey, Base64.DEFAULT);
            EncodedKeySpec keySpec = pubKey ? new X509EncodedKeySpec(decodeKey) : new PKCS8EncodedKeySpec(decodeKey);
            KeyFactory keyFactory = KeyFactory.getInstance(RSA);
            Key key = pubKey ? keyFactory.generatePublic(keySpec) : keyFactory.generatePrivate(keySpec);
            Cipher cipher = Cipher.getInstance(RSA_ECB_PKCS1PADDING);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] bytes = cipher.doFinal(data);
            return new String(Base64.encode(bytes,Base64.DEFAULT));
        } catch (NoSuchAlgorithmException | IllegalBlockSizeException | BadPaddingException | InvalidKeyException | NoSuchPaddingException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解密
     */
    private byte[] decrypt(String data, String secretKey, boolean pubKey) {
        try {
            byte[] decodeKey = Base64.decode(secretKey, Base64.DEFAULT);
            EncodedKeySpec keySpec = pubKey ? new X509EncodedKeySpec(decodeKey) : new PKCS8EncodedKeySpec(decodeKey);
            KeyFactory keyFactory = KeyFactory.getInstance(RSA);
            Key key = pubKey ? keyFactory.generatePublic(keySpec) : keyFactory.generatePrivate(keySpec);
            byte[] bytes = Base64.decode(data.getBytes("utf-8"), Base64.DEFAULT);
            Cipher cipher = Cipher.getInstance(RSA_ECB_PKCS1PADDING);
            cipher.init(Cipher.DECRYPT_MODE, key);
            bytes = cipher.doFinal(bytes);
            return bytes;
        } catch (NoSuchAlgorithmException | IllegalBlockSizeException | BadPaddingException | UnsupportedEncodingException | InvalidKeyException | NoSuchPaddingException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    public String encryptByPublic(byte[] data, String secretKey){
        return encrypt(data,secretKey,true);
    }

    public String encryptByPrivate(byte[] data, String secretKey){
        return encrypt(data,secretKey,false);
    }

    public byte[] decryptByPublic(String data, String secretKey){
        return decrypt(data, secretKey, true);
    }

    public byte[] decryptByPrivate(String data, String secretKey){
        return decrypt(data, secretKey, false);
    }
}
