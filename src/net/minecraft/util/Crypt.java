package net.minecraft.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Base64.Encoder;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Crypt {
   private static final String SYMMETRIC_ALGORITHM = "AES";
   private static final int SYMMETRIC_BITS = 128;
   private static final String ASYMMETRIC_ALGORITHM = "RSA";
   private static final int ASYMMETRIC_BITS = 1024;
   private static final String BYTE_ENCODING = "ISO_8859_1";
   private static final String HASH_ALGORITHM = "SHA-1";
   public static final String SIGNING_ALGORITHM = "SHA256withRSA";
   public static final int SIGNATURE_BYTES = 256;
   private static final String PEM_RSA_PRIVATE_KEY_HEADER = "-----BEGIN RSA PRIVATE KEY-----";
   private static final String PEM_RSA_PRIVATE_KEY_FOOTER = "-----END RSA PRIVATE KEY-----";
   public static final String RSA_PUBLIC_KEY_HEADER = "-----BEGIN RSA PUBLIC KEY-----";
   private static final String RSA_PUBLIC_KEY_FOOTER = "-----END RSA PUBLIC KEY-----";
   public static final String MIME_LINE_SEPARATOR = "\n";
   public static final Encoder MIME_ENCODER = Base64.getMimeEncoder(76, "\n".getBytes(StandardCharsets.UTF_8));
   public static final Codec<PublicKey> PUBLIC_KEY_CODEC = Codec.STRING.comapFlatMap(rsaString -> {
      try {
         return DataResult.success(stringToRsaPublicKey(rsaString));
      } catch (CryptException var2) {
         return DataResult.error(var2::getMessage);
      }
   }, Crypt::rsaPublicKeyToString);
   public static final Codec<PrivateKey> PRIVATE_KEY_CODEC = Codec.STRING.comapFlatMap(rsaString -> {
      try {
         return DataResult.success(stringToPemRsaPrivateKey(rsaString));
      } catch (CryptException var2) {
         return DataResult.error(var2::getMessage);
      }
   }, Crypt::pemRsaPrivateKeyToString);

   public static SecretKey generateSecretKey() throws CryptException {
      try {
         KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
         keyGenerator.init(128);
         return keyGenerator.generateKey();
      } catch (Exception var1) {
         throw new CryptException(var1);
      }
   }

   public static KeyPair generateKeyPair() throws CryptException {
      try {
         KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
         generator.initialize(1024);
         return generator.generateKeyPair();
      } catch (Exception var1) {
         throw new CryptException(var1);
      }
   }

   public static byte[] digestData(final String serverId, final PublicKey publicKey, final SecretKey sharedKey) throws CryptException {
      try {
         return digestData(serverId.getBytes("ISO_8859_1"), sharedKey.getEncoded(), publicKey.getEncoded());
      } catch (Exception var4) {
         throw new CryptException(var4);
      }
   }

   private static byte[] digestData(final byte[]... inputs) throws Exception {
      MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");

      for (byte[] input : inputs) {
         messageDigest.update(input);
      }

      return messageDigest.digest();
   }

   private static <T extends Key> T rsaStringToKey(String input, final String header, final String footer, final Crypt$ByteArrayToKeyFunction<T> byteArrayToKey) throws CryptException {
      int begin = input.indexOf(header);
      if (begin != -1) {
         begin += header.length();
         int end = input.indexOf(footer, begin);
         input = input.substring(begin, end + 1);
      }

      try {
         return byteArrayToKey.apply(Base64.getMimeDecoder().decode(input));
      } catch (IllegalArgumentException var6) {
         throw new CryptException(var6);
      }
   }

   public static PrivateKey stringToPemRsaPrivateKey(final String rsaString) throws CryptException {
      return rsaStringToKey(rsaString, "-----BEGIN RSA PRIVATE KEY-----", "-----END RSA PRIVATE KEY-----", Crypt::byteToPrivateKey);
   }

   public static PublicKey stringToRsaPublicKey(final String rsaString) throws CryptException {
      return rsaStringToKey(rsaString, "-----BEGIN RSA PUBLIC KEY-----", "-----END RSA PUBLIC KEY-----", Crypt::byteToPublicKey);
   }

   public static String rsaPublicKeyToString(final PublicKey publicKey) {
      if (!"RSA".equals(publicKey.getAlgorithm())) {
         throw new IllegalArgumentException("Public key must be RSA");
      } else {
         return "-----BEGIN RSA PUBLIC KEY-----\n" + MIME_ENCODER.encodeToString(publicKey.getEncoded()) + "\n-----END RSA PUBLIC KEY-----\n";
      }
   }

   public static String pemRsaPrivateKeyToString(final PrivateKey privateKey) {
      if (!"RSA".equals(privateKey.getAlgorithm())) {
         throw new IllegalArgumentException("Private key must be RSA");
      } else {
         return "-----BEGIN RSA PRIVATE KEY-----\n" + MIME_ENCODER.encodeToString(privateKey.getEncoded()) + "\n-----END RSA PRIVATE KEY-----\n";
      }
   }

   private static PrivateKey byteToPrivateKey(final byte[] keyData) throws CryptException {
      try {
         EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyData);
         KeyFactory keyFactory = KeyFactory.getInstance("RSA");
         return keyFactory.generatePrivate(keySpec);
      } catch (Exception var3) {
         throw new CryptException(var3);
      }
   }

   public static PublicKey byteToPublicKey(final byte[] keyData) throws CryptException {
      try {
         EncodedKeySpec keySpec = new X509EncodedKeySpec(keyData);
         KeyFactory keyFactory = KeyFactory.getInstance("RSA");
         return keyFactory.generatePublic(keySpec);
      } catch (Exception var3) {
         throw new CryptException(var3);
      }
   }

   public static SecretKey decryptByteToSecretKey(final PrivateKey privateKey, final byte[] keyData) throws CryptException {
      byte[] key = decryptUsingKey(privateKey, keyData);

      try {
         return new SecretKeySpec(key, "AES");
      } catch (Exception var4) {
         throw new CryptException(var4);
      }
   }

   public static byte[] encryptUsingKey(final Key key, final byte[] input) throws CryptException {
      return cipherData(1, key, input);
   }

   public static byte[] decryptUsingKey(final Key key, final byte[] input) throws CryptException {
      return cipherData(2, key, input);
   }

   private static byte[] cipherData(final int cipherOpMode, final Key key, final byte[] input) throws CryptException {
      try {
         return setupCipher(cipherOpMode, key.getAlgorithm(), key).doFinal(input);
      } catch (Exception var4) {
         throw new CryptException(var4);
      }
   }

   private static Cipher setupCipher(final int cipherOpMode, final String algorithm, final Key key) throws Exception {
      Cipher cipher = Cipher.getInstance(algorithm);
      cipher.init(cipherOpMode, key);
      return cipher;
   }

   public static Cipher getCipher(final int opMode, final Key key) throws CryptException {
      try {
         Cipher cip = Cipher.getInstance("AES/CFB8/NoPadding");
         cip.init(opMode, key, new IvParameterSpec(key.getEncoded()));
         return cip;
      } catch (Exception var3) {
         throw new CryptException(var3);
      }
   }
}
