package com.sn.dataServer.api;


import com.sn.dataServer.model.User;
import com.sn.dataServer.payloads.LoginRequest;
import com.sn.dataServer.payloads.MessageResponse;
import com.sn.dataServer.payloads.SignupRequest;
import com.sn.dataServer.payloads.UserInfoResponse;
import com.sn.dataServer.repositories.UsersRepository;
import com.sn.dataServer.services.DiffieHellman;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.util.Objects;
import java.util.Optional;

import static ch.qos.logback.core.encoder.ByteArrayUtil.toHexString;



@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("api/users")
@RestController
public class UserAccountController {


    @Autowired
    private DiffieHellman diffieHellman;
    @Autowired
    UsersRepository userRepository;


    // Signup up API
    @PostMapping(path="/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupRequest request) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException, InvalidKeyException, NoSuchPaddingException,
            IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, NoSuchProviderException, InvalidParameterSpecException {

        byte[] secretKey = diffieHellman.generateSecretKey(request.getUserPublicKey());
        String dataString = diffieHellman.decrypt(secretKey, request.getEncryptedData(), request.getIv());

        System.out.println("requestdata :  " + dataString);
        String[] userdata = dataString.split("//");

        User user = new User(userdata[0], userdata[1], userdata[2], request.getUserPublicKey(), request.getRsaPublicKey());
        if(userRepository.existsById(userdata[0])) {
//            System.out.println(userRepository.findById(userdata[0]));
            System.out.println("email exists");
            return ResponseEntity.badRequest().body(new MessageResponse("email already exists !"));
        }

        try{
            userRepository.save(user);
            return ResponseEntity.ok().body(null);

        }catch (Exception e){
            e.printStackTrace();
            return (ResponseEntity<?>) ResponseEntity.internalServerError();
        }

    }


    // API to send server public key when requested
    @GetMapping(path="/getPDSKey")
    public ResponseEntity<?> sendPDSKey() throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
        return ResponseEntity.ok(new MessageResponse(diffieHellman.sendPDSKey()));
    }


    // Login API
    @PostMapping(path="/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException, InvalidKeyException, InvalidAlgorithmParameterException, NoSuchPaddingException,
            IllegalBlockSizeException, BadPaddingException, InvalidParameterSpecException, NoSuchProviderException {

        byte[] secretKey = diffieHellman.generateSecretKey(request.getUserPublicKey());
        String dataString = diffieHellman.decrypt(secretKey, request.getEncryptedData(), request.getIv());
        System.out.println("requestdata :  " + dataString);
        String[] userdata = dataString.split("//");
//        System.out.println(dataString);

        Optional<User> userOptional = userRepository.findById(userdata[0]);

        if(userOptional.isPresent()) {
            if (!Objects.equals(userOptional.get().getPassword(), userdata[1])) {
                return ResponseEntity.status(201).body(new MessageResponse("email/pass wrong"));
            }
        }
        else{
            return ResponseEntity.status(202).body(new MessageResponse("no such user"));
        }

        byte[] newArray = new byte[32];
        System.arraycopy(secretKey, 0, newArray, 0, 32);

        User login = userOptional.get();
        String sessionId = toHexString(secretKey);
        login.setSessionId(sessionId);
        userRepository.save(login);

        return ResponseEntity.ok().body(new UserInfoResponse(login.getFullName(), login.getEmail(), sessionId));
    }

}
