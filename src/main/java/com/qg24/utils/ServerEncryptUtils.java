package com.qg24.utils;

import javax.crypto.SecretKey;
import java.security.PrivateKey;
import java.security.PublicKey;

public class ServerEncryptUtils {

    public static final String publicKeyStr = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAgTh8XPOs5hCMlpHFiJ4D3XvMFjGVlPE7fMC4IHjhVqWfcyJDtDRrfy1Uey7Qk+uzYfKfMRy/pkT1ARrz3UQc5IolD4nl3CH2Ag2a4MO7TG8dMqRL3vvwXVVH8OrwdXXIUEemo2dSApWxjVKxtsTGIGNt9i9UEjVBx9NpNu4w0yizeDAnkqalFD+lNZJnMw0v7wP2p8cHZ8iqxvbQ9W2i/WyL0E06vOtm/LUgVSMuz6r0H2FOiBvzSjaa6bHYJlx16AR2vLO7gJSOLDkCOye6oyA7b1ZOd510LH1VBsYPGbT9LOZU1lhFi9cGHFdC3IFBnPFloLlvwJvrzjNxCKUzwwIDAQAB";
    public static final String privateKeyStr = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCBOHxc86zmEIyWkcWIngPde8wWMZWU8Tt8wLggeOFWpZ9zIkO0NGt/LVR7LtCT67Nh8p8xHL+mRPUBGvPdRBzkiiUPieXcIfYCDZrgw7tMbx0ypEve+/BdVUfw6vB1dchQR6ajZ1IClbGNUrG2xMYgY232L1QSNUHH02k27jDTKLN4MCeSpqUUP6U1kmczDS/vA/anxwdnyKrG9tD1baL9bIvQTTq862b8tSBVIy7PqvQfYU6IG/NKNprpsdgmXHXoBHa8s7uAlI4sOQI7J7qjIDtvVk53nXQsfVUGxg8ZtP0s5lTWWEWL1wYcV0LcgUGc8WWguW/Am+vOM3EIpTPDAgMBAAECggEATjNN57672rsXKaPsJCJkemwJ9UBqoqlec7/5bgVZcWNn3HhW+r5ic6OOJcIODgatF1CKEGGyhb/jeZjDDhvDVHdZuE6eoZqX02RCX1V0iuwPgm81Tyuuqf0LPXwgqyTtZdAGw4qqcc//lGQAEniA4mbpy/uWaDJ8rZ5+ZPS+7Xoux6bE6ABQyw2MDblkx3RWq1cmHsHYvGVkT6g80VT9Gwf542Iq3Olopz/udCrldjKoIvxgrNyzIeNSvPOYhcNsw7BTOcw2D6rB6WljFO5Hg+hfAMcvadOh1IEmHP8uw5Z3orsXXfwjUqXz5cIbvDAJCqwZUmfZw0A1kxbViiPS6QKBgQC+qeNoKD4662/MOfrPvdON6wFKNY+YkxnYJa4wMagFNVRC6MUAfHkvmnaw25jXVKkU/89nGtRzKYu0OzGdU9VGPReKj5f4lQd1X7xyECLnI5czOnube7kNkdp10hyKVD8wAg1zax2JDPWm3Fi+/FaP/Qh8w3HMsiw9DuS3NcRRPQKBgQCtgHcmygoRTtw1KSCQS0653iyUvr5kIj+NUa/tDj9gAo35UnKOuVP95Ng21y7XnLnVvZCaM4UrNKry3ckw0LVLkhSr0m+3+UZoXWSKwcduvtmm4aiNI9O/9/IPh/icEwS1BodA9FZFJKEAkwITMAGo6aW99Op0rBezHX3ud+3o/wKBgFX/WssIG8mHmA2NuUsqW9mPPi2yquour2GG3WJvg1GWFEN5qiHmAuMt0ItFAVNepEUMfQgMWfb3rkPEz0QfI/tiYtPkoK9GyO5wGBd6n8ORkglRP5+J1pYAS4EJHEZoaXfF9Tsx/UhryAFTMdQzDPg1MTbVEOc7FikUJqVcYwghAoGAHtQt19Gcmli13ihRpfILqhElQMTn1akUO8shBHE8p5Wsnnmc62lY2Etac9wmcZ9ike0eDZhL+FJicccg4bCkFip9y84XlcwQS6kUnWczRaVZzUJZjcKo9HpdI49YiHsEohnn3I3N6nlItm5otvoXwWjKz4b+Rf7yMxxxbPcYagcCgYA7WPFVXXoVEQa/0J+Rdb1Uaj2rqTz5/YO0Bwa9DkCy8nIHFQbqO1cR2+2sjf2joBo+mQ1mtRsMrNMafEVYqrV6kXspK0VFolyGCAOhAQMrlQZ9WZz3dJV5W7zEJ9I5yZPtQZ94iVLZyeQzvI9zD22sb2hrkUKGaGMz4SqqoK83QA==";
    public static final String aesKeyStr = "Ky01tCGl8Zvh3oVlQFapMw==";

    /**
     * 服务端解密
     * @param content
     * @return
     * @throws Exception
     */
    public static String UnEncryptText(String content) throws Exception {
        //将Base64编码后的私钥转换成PrivateKey对象
        PrivateKey privateKey = RSAUtil.string2PrivateKey(privateKeyStr);
        //获取公钥
        PublicKey publicKey = RSAUtil.string2PublicKey(publicKeyStr);
        byte[] publicEncrypt = RSAUtil.publicEncrypt(aesKeyStr.getBytes(), publicKey);
        //用私钥解密,得到aesKey
        byte[] aesKeyStrBytes = RSAUtil.privateDecrypt(publicEncrypt, privateKey);
        //解密后的aesKey
        String aesKeyStr2 = new String(aesKeyStrBytes);
        //将Base64编码后的AES秘钥转换成SecretKey对象
        SecretKey aesKey2 = AESUtil.loadKeyAES(aesKeyStr2);
        //AES秘钥加密后的内容(Base64编码)，进行Base64解码
        byte[] encryptAES2 = AESUtil.base642Byte(content);
        //用AES秘钥解密实际的内容
        byte[] decryptAES = AESUtil.decryptAES(encryptAES2, aesKey2);
        //解密后的实际内容

        return new String(decryptAES);
    }


}
