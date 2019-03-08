package com.heaven7.java.blockchain;

import com.heaven7.java.blockchain.en_de.KeyUtil;
import org.junit.Assert;
import org.junit.Test;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;

/**
 * @author heaven7
 */
public class Secp256k1Test {


    public static byte[] signData(String algorithm, byte[] data, PrivateKey key) throws Exception {
        Signature signer = Signature.getInstance(algorithm);
        signer.initSign(key);
        signer.update(data);
        return (signer.sign());
    }

    public static boolean verifySign(String algorithm, byte[] data, PublicKey key, byte[] sig) throws Exception {
        Signature signer = Signature.getInstance(algorithm);
        signer.initVerify(key);
        signer.update(data);
        return (signer.verify(sig));
    }

    @Test
    public void testPublicAndPrivateKey(){
        KeyPair keyPair = KeyUtil.createKeyPairGenerator("secp256k1");
        try {
           // KeyUtil.savePublicKey(keyPair.getPublic(), "publickey.der");
           // KeyUtil.savePrivateKey(keyPair.getPrivate(), "privatekey.der");
            KeyUtil.savePublicKeyAsPEM(keyPair.getPublic(), "publickey.pem");
            KeyUtil.savePrivateKeyAsPEM(keyPair.getPrivate(), "privatekey.pem");
        } catch (Exception e) {
            e.printStackTrace();
        }
        /*
         * pem:
         # 打印pem公钥
           openssl ec -in publickey.pem -pubin -text -noout
            //私钥
           openssl ec -in privatekey.pem -text -noout
         */
        //openssl pkey -inform DER -pubin -in publickey.der -text
        //openssl pkey -inform DER -in privatekey.der -text
    }

    @Test
    public void testSignVerify() throws Exception {
        // 需要签名的数据
        byte[] data = new byte[1000];
        for (int i=0; i<data.length; i++)
            data[i] = 0xa;

        // 生成秘钥，在实际业务中，应该加载秘钥
        KeyPair keyPair = KeyUtil.createKeyPairGenerator("secp256k1");
        PublicKey publicKey1 = keyPair.getPublic();
        PrivateKey privateKey1 = keyPair.getPrivate();

        // 生成第二对秘钥，用于测试
        keyPair = KeyUtil.createKeyPairGenerator("secp256k1");
        PublicKey publicKey2 = keyPair.getPublic();
        PrivateKey privateKey2 = keyPair.getPrivate();

        // 计算签名
        byte[] sign1 = signData("SHA256withECDSA", data, privateKey1);
        byte[] sign2 = signData("SHA256withECDSA", data, privateKey1);

        // sign1和sign2的内容不同，因为ECDSA在计算的时候，加入了随机数k，因此每次的值不一样
        // 随机数k需要保密，并且每次不同


        // 用对应的公钥验证签名，必须返回true
        Assert.assertTrue(verifySign("SHA256withECDSA", data, publicKey1, sign1));
        // 数据被篡改，返回false
        data[1] = 0xb;
        Assert.assertFalse(verifySign("SHA256withECDSA", data, publicKey1, sign1));
        data[1] = 0xa;

        Assert.assertTrue(verifySign("SHA256withECDSA", data, publicKey1, sign1));
        // 签名被篡改，返回false
        // 签名为DER格式，前三个字节是标识和数据长度，如果修改了这三个会抛出异常，无效签名格式
        sign1[20] = (byte)~sign1[20];
        Assert.assertFalse(verifySign("SHA256withECDSA", data, publicKey1, sign1));

        // 使用其他公钥验证，返回false
        Assert.assertFalse(verifySign("SHA256withECDSA", data, publicKey2, sign1));
    }
}
