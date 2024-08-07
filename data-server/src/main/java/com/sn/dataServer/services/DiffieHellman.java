package com.sn.dataServer.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import javax.crypto.*;
import javax.crypto.interfaces.DHPrivateKey;
import javax.crypto.interfaces.DHPublicKey;
import javax.crypto.spec.*;
import java.io.*;
import java.math.BigInteger;
import java.security.*;
import java.security.spec.*;
import java.util.Base64;
import java.nio.charset.StandardCharsets;

import static ch.qos.logback.core.encoder.ByteArrayUtil.hexStringToByteArray;
import static ch.qos.logback.core.encoder.ByteArrayUtil.toHexString;


@Service
public class DiffieHellman {

    @Autowired
    private ResourceLoader resourceLoader;

    // function to set the parameter specs of the Diffie-Hellman Key Exchange method
    public DHParameterSpec modp15() {
        final BigInteger p =
                new BigInteger(
                                "ffffffffffffffffc90fdaa22168c234c4c6628b80dc1cd129024e088a67cc74020bbea63b139b22514a08798e3404ddef9519b3cd3a431b302b0a6df25f14374fe1356d6d51c245e485b576625e7ec6f44c42e9a637ed6b0bff5cb6f406b7edee386bfb5a899fa5ae9f24117c4b1fe649286651ece45b3dc2007cb8a163bf0598da48361c55d39a69163fa8fd24cf5f83655d23dca3ad961c62f356208552bb9ed529077096966d670c354e4abc9804f1746c08ca18217c32905e462e36ce3be39e772c180e86039b2783a2ec07a28fb5c55df06f4c52c9de2bcbf6955817183995497cea956ae515d2261898fa051015728e5a8aaac42dad33170d04507a33a85521abdf1cba64ecfb850458dbef0a8aea71575d060c7db3970f85a6e1e4c7abf5ae8cdb0933d71e8c94e04a25619dcee3d2261ad2ee6bf12ffa06d98a0864d87602733ec86a64521f2b18177b200cbbe117577a615d6c770988c0bad946e208e24fa074e5ab3143db5bfce0fd108e4b82d120a93ad2caffffffffffffffff",
                        16);
        final BigInteger g = new BigInteger("2");
        return new DHParameterSpec(p, g);
    }


    // verify signature of the data received
    public boolean verify(String message, String signatureBase64, String publicKeyBase64) throws NoSuchAlgorithmException, InvalidKeySpecException,
            InvalidKeyException, SignatureException, IOException {
        System.out.println(publicKeyBase64);
        byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyBase64);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(publicKeyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(spec);
        Signature sig = Signature.getInstance("SHA256withRSA");
        sig.initVerify(publicKey);
        sig.update(message.getBytes());
        byte[] signatureBytes = Base64.getDecoder().decode(signatureBase64);
        return sig.verify(signatureBytes);
    }


    // create public private keys for the server
    public void createKeys() throws NoSuchAlgorithmException, IOException, InvalidAlgorithmParameterException {

        DHParameterSpec modp15 = modp15();
        KeyPairGenerator serverKeyGen = KeyPairGenerator.getInstance("DH");
        serverKeyGen.initialize(modp15);
        KeyPair serverKeyPair = serverKeyGen.generateKeyPair();
        byte[] serverPublicKeyBytes = ((DHPublicKey)serverKeyPair.getPublic()).getY().toByteArray();

        byte[] serverPrivateBytes = ((DHPrivateKey)serverKeyPair.getPrivate()).getX().toByteArray();

        FileWriter file = new FileWriter("/Users/shashi/projects/Distributed-SN/data-server/src/main/resources/static/public.txt");
        file.write(toHexString(serverPublicKeyBytes));
        file.close();

        file = new FileWriter("/Users/shashi/projects/Distributed-SN/data-server/src/main/resources/static/private.txt");
        file.write(toHexString(serverPrivateBytes));
        file.close();

    }


    // send the public key of the server when requested
    public String sendPDSKey() throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {

        return readFile("public.txt");

    }


    // generate the secret key
    public byte[] generateSecretKey(String clientPublicKey) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException, InvalidKeyException {

        DHParameterSpec modp15 = modp15();

        BigInteger clientPublicKeyInt = new BigInteger(clientPublicKey, 16);

        KeyFactory keyFactory = KeyFactory.getInstance("DH");
        DHPublicKeySpec clientPublicKeySpecs = new DHPublicKeySpec(
                clientPublicKeyInt,
                modp15.getP(),
                modp15.getG());
        DHPublicKey clientPKey = (DHPublicKey)keyFactory.generatePublic(clientPublicKeySpecs); // Public key done

        // now get server private key

//        BufferedReader reader = new BufferedReader(new FileReader("/Users/shashi/projects/Distributed-SN/data-server/src/main/resources/static/private.txt"));
        String line = readFile("private.txt");

        byte[] serverPrivateKeyBytes = hexStringToByteArray(line);
        BigInteger serverPrivateInt = new BigInteger(serverPrivateKeyBytes);
        DHPrivateKeySpec serverPrivateKeySpecs = new DHPrivateKeySpec(
                serverPrivateInt,
                modp15.getP(),
                modp15.getG());

        DHPrivateKey serverPrivateKey = (DHPrivateKey)keyFactory.generatePrivate(serverPrivateKeySpecs); // server private done

        KeyAgreement keyAgreement = KeyAgreement.getInstance("DH");
        keyAgreement.init(serverPrivateKey);

        keyAgreement.doPhase(clientPKey, true);
        byte[] sharedSecretKey = keyAgreement.generateSecret();

        System.out.println(toHexString(sharedSecretKey));

        return sharedSecretKey;
    }


    // decrypt data using the generated shared key
    public String decrypt(byte[] sharedSecretKey, String encryptedDataBase64, String iv) throws NoSuchAlgorithmException,
            InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, UnsupportedEncodingException, NoSuchProviderException, InvalidParameterSpecException {

        byte[] newArray = new byte[32];
        System.arraycopy(sharedSecretKey, 0, newArray, 0, 32);

        byte[] encryptedBytes = Base64.getDecoder().decode(encryptedDataBase64);

        SecretKeySpec keySpec = new SecretKeySpec(newArray, "AES");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(Base64.getDecoder().decode(iv));

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivParameterSpec);

        byte[] decryptedData = cipher.doFinal(encryptedBytes);

        return new String(decryptedData);
    }


    // encrypt data using the generated shared key
    public String encrypt(byte[] sharedSecretKey, String imageData) throws InvalidAlgorithmParameterException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException {
        byte[] newArray = new byte[32];
        System.arraycopy(sharedSecretKey, 0, newArray, 0, 32);

        byte[] iv = new byte[16];
        SecureRandom random = new SecureRandom();
        random.nextBytes(iv);
//        System.out.println(iv.length);
        SecretKeySpec keySpec = new SecretKeySpec(newArray, "AES");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParameterSpec);

        byte[] decryptedData = cipher.doFinal(imageData.getBytes());

        String encryptedImageBase64 = Base64.getEncoder().encodeToString(decryptedData);
        String ivBase64 = Base64.getEncoder().encodeToString(iv);

        return encryptedImageBase64 + "------" + ivBase64;
    }

    public String readFile(String filename) throws IOException {
        Resource resource = resourceLoader.getResource("classpath:static/" + filename);
        InputStream inputStream = resource.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        String content = reader.readLine();
        reader.close();

        return content;
    }

}




