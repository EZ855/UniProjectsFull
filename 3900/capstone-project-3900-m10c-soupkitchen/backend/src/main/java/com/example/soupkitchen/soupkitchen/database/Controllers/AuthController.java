package com.example.soupkitchen.soupkitchen.database.Controllers;

import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import com.example.soupkitchen.soupkitchen.database.Database.*;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@RestController
public class AuthController extends MainController {

    @CrossOrigin(origins = "*")
    @PostMapping(value = "/auth/register", consumes = {"application/json"})
    public ResponseEntity<String> authRegister(@RequestBody String body) {
        try {
            // get info from body
            JSONObject body_json = new JSONObject(body);
            String email = body_json.getString("email");
            String username = body_json.getString("username");
            String password = body_json.getString("password");

            List<String> reasons= new ArrayList<String>();        
            
            if (!isValidEmail(email)) {
                reasons.add("email");
            }

            if (!isValidUsername(username)) {
                reasons.add("username");
            }

            if (!isValidPassword(password)) {
                reasons.add("password");
            }

            List<users> temp_user2s = userRepository.findByEmail(email);

            if ( temp_user2s.size() != 0) {
                reasons.add("email taken");
            }

            List<users> temp_user3 = userRepository.findByUsername(username);

            if (temp_user3.size() != 0) {
                reasons.add("username taken");
            }
            
            if (reasons.size() > 0){
                String return_body = "{\"success\":false,";
                return_body += "\"reason\":";
                return_body += reasons.stream()
                    .map(reason -> "\"" + reason + "\"")
                    .collect(Collectors.joining(",", "[", "]"));
                return_body += "}";

                return createResponseEntityOk(return_body);
            }

            users temp_user = userRepository.save(
                new users(
                    username,
                    email,
                    new Date()
                )
            );

            passwordsRepository.save(
                new passwords(
                    temp_user.getId(),
                    password
                )
            );
            
            // create token
            String rand_token = UUID.randomUUID().toString();

            auth_tokens temp_token = auth_tokensRepository.save(
                new auth_tokens(
                    temp_user.getId(),
                    rand_token
                )
            );

            String return_body = String.format("{\"success\":true,\"token\":\"%s\"}", temp_token.getToken());
            return createResponseEntityOk(return_body);

        } catch (Exception e) {
            return createResponseEntityFail();
        }
    }

    @Transactional
    @CrossOrigin(origins = "*")
    @PostMapping(value = "/auth/login", consumes = {"application/json"})
    public ResponseEntity<String> authLogin(@RequestBody String body) {
        try {
            // get info from body
            JSONObject body_json = new JSONObject(body);
            String email = body_json.getString("email");
            String password = body_json.getString("password");

            // get user
            users temp_user = userRepository.findByEmail(email).get(0);

            // get password
            passwords temp_password = passwordsRepository.findByUser(temp_user.getId()).get(0);
            if (!password.equals(temp_password.getPassword_hashed())) {
                return createResponseEntityFail();
            }

            // create token
            String rand_token = UUID.randomUUID().toString();

            // delete token if exists
            if (auth_tokensRepository.existsByUser(temp_user.getId())){
                auth_tokensRepository.deleteByUser(temp_user.getId());
            }

            // save token
            auth_tokens temp_token = auth_tokensRepository.save(new auth_tokens(temp_user.getId(), rand_token));

            String return_body = String.format("{\"success\":true,\"token\":\"%s\"}", temp_token.getToken());

            return createResponseEntityOk(return_body);
        
        } catch (Exception e) {
            return createResponseEntityFail();
        }
    }

    @Transactional
    @CrossOrigin(origins = "*")
    @PostMapping(value = "/auth/logout", consumes = {"application/json"})
    public ResponseEntity<String> authLogout(@RequestBody String body){ 
        try {
            // get info from body
            JSONObject body_json = new JSONObject(body);
            String token = body_json.getString("token");

            // get token
            auth_tokens mainToken = auth_tokensRepository.findByToken(token).get(0);

            // delete token
            auth_tokensRepository.deleteByToken(mainToken.getToken());
            
            return createResponseEntityOk();

        } catch (Exception e) {
            return createResponseEntityFail();
        }
    }

    // Checks if the username is valid with a length less than 12 characters
    private static boolean isValidUsername(String username) {
        return username.length() > 0 && username.length() <= 12;
    }

    // Checks if the email is valid
    private static boolean isValidEmail(String email) {
        String regex = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$";
        return Pattern.matches(regex, email);
    }

    // Checks if a password is valid
    private static boolean isValidPassword(String password) {
        Pattern numeric = Pattern.compile(".*[0-9].*");
        Pattern lowerAlpha = Pattern.compile(".*[a-z].*");
        Pattern upperAlpha = Pattern.compile(".*[A-Z].*");
        Pattern special = Pattern.compile(".*[^0-9a-zA-Z].*");

        List<Pattern> pList = Arrays.asList(numeric, lowerAlpha, upperAlpha, special);
        if (password == null) {
            return false;
        }

        if (password.length() < 8) {
            return false;
        }

        Boolean res = true;
        for (int i = 0; i < pList.size(); i++){
            if (res && validatePattern(pList.get(i), password)){
                res = true;
            } else {
                res = false;
            }
        }
        return res;
    }

    private static boolean validatePattern(Pattern p, String password){
        Matcher m = p.matcher(password);
        return m.matches();
    }
}