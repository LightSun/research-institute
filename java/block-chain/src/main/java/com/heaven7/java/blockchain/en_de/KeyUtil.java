package com.heaven7.java.blockchain.en_de;

import sun.security.pkcs.PKCS8Key;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;
import sun.security.x509.X509Key;

import java.io.*;
import java.security.*;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;

/**
 * @author heaven7
 */
public class KeyUtil {

    public static KeyPair createKeyPairGenerator(String curveName){
        try{
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("EC");
            // curveName这里取值：secp256k1
            ECGenParameterSpec ecGenParameterSpec = new ECGenParameterSpec(curveName);
            keyPairGenerator.initialize(ecGenParameterSpec, new SecureRandom());
            return keyPairGenerator.generateKeyPair();
        }catch (InvalidAlgorithmParameterException | NoSuchAlgorithmException e){
            throw new RuntimeException(e);
        }
    }

    public static void savePublicKeyAsPEM(PublicKey publicKey, String name) throws Exception {
        String content = Base64.getEncoder().encodeToString(publicKey.getEncoded());
        File file = new File(name);
        if ( file.isFile() && file.exists() )
            throw new IOException("file already exists");
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw")) {
            randomAccessFile.write("-----BEGIN PUBLIC KEY-----\n".getBytes());
            int i = 0;
            for (; i<(content.length() - (content.length() % 64)); i+=64) {
                randomAccessFile.write(content.substring(i, i + 64).getBytes());
                randomAccessFile.write('\n');
            }

            randomAccessFile.write(content.substring(i, content.length()).getBytes());
            randomAccessFile.write('\n');

            randomAccessFile.write("-----END PUBLIC KEY-----".getBytes());
        }
    }

    public static void savePrivateKeyAsPEM(PrivateKey privateKey, String name) throws Exception {
        String content = Base64.getEncoder().encodeToString(privateKey.getEncoded());
        File file = new File(name);
        if ( file.isFile() && file.exists() )
            throw new IOException("file already exists");
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw")) {
            randomAccessFile.write("-----BEGIN PRIVATE KEY-----\n".getBytes());
            int i = 0;
            for (; i<(content.length() - (content.length() % 64)); i+=64) {
                randomAccessFile.write(content.substring(i, i + 64).getBytes());
                randomAccessFile.write('\n');
            }

            randomAccessFile.write(content.substring(i, content.length()).getBytes());
            randomAccessFile.write('\n');

            randomAccessFile.write("-----END PRIVATE KEY-----".getBytes());
        }
    }
    //pem
    public static PrivateKey loadECPrivateKey(String content,  String algorithm) throws Exception {
        String privateKeyPEM = content.replace("-----BEGIN PRIVATE KEY-----\n", "")
                .replace("-----END PRIVATE KEY-----", "").replace("\n", "");
        byte[] asBytes = Base64.getDecoder().decode(privateKeyPEM);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(asBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        return keyFactory.generatePrivate(spec);
    }

    //pem
    public static PublicKey loadECPublicKey(String content,  String algorithm) throws Exception {
        String strPublicKey = content.replace("-----BEGIN PUBLIC KEY-----\n", "")
                .replace("-----END PUBLIC KEY-----", "").replace("\n", "");
        byte[] asBytes = Base64.getDecoder().decode(strPublicKey);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(asBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        return keyFactory.generatePublic(spec);
    }


    // 读取公钥, encodedKey为从文件中读取到的byte[]数组
    public static PublicKey loadPublicKey(byte[] encodedKey, String algorithm)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encodedKey);
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        return keyFactory.generatePublic(keySpec);
    }

    // 读取私钥
    public static PrivateKey loadPrivateKey(byte[] encodedKey,  String algorithm)
            throws NoSuchAlgorithmException, InvalidKeySpecException{
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encodedKey);
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        return keyFactory.generatePrivate(keySpec);
    }

   /* PrivateKey privateKey1 = KeyUtil.loadPrivateKey(IOUtils.readBytes(
            new FileInputStream("privatekey.der")), "EC");*/

    // readBytes代码
    public static byte[] readBytes(final InputStream inputStream) throws IOException {
        final int BUFFER_SIZE = 1024;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int readCount;
        byte[] data = new byte[BUFFER_SIZE];
        while ((readCount = inputStream.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, readCount);
        }

        buffer.flush();
        return buffer.toByteArray();
    }

    public static void savePublicKey(PublicKey key, String filename) {
        try {
            byte[] encode = KeyFactory.getInstance("EC").translateKey(key).getEncoded();


            FileWriter fw = new FileWriter(filename);
            String content = Base64.getEncoder().encodeToString(key.getEncoded());
            fw.write(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void savePrivateKey(PrivateKey key, String filename) {
        try {
            FileWriter fw = new FileWriter(filename);
            String content = Base64.getEncoder().encodeToString(key.getEncoded());
            fw.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
