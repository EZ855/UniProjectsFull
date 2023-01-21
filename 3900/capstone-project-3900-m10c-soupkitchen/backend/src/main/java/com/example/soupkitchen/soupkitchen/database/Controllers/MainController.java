package com.example.soupkitchen.soupkitchen.database.Controllers;

import com.example.soupkitchen.soupkitchen.database.Repositories.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import javax.servlet.ServletContext;

public class MainController {
    @Autowired
    protected auth_tokensRepository auth_tokensRepository;

    @Autowired
    protected commentsRepository commentsRepository;

    @Autowired
    protected cookbook_recipesRepository cookbook_recipesRepository;

    @Autowired
    protected cookbook_subscribersRepository cookbook_subscribersRepository;

    @Autowired
    protected cookbooksRepository cookbooksRepository;

    @Autowired
    protected equipmentsRepository equipmentsRepository;

    @Autowired
    protected ingredientsRepository ingredientsRepository;

    @Autowired
    protected likesRepository likesRepository;

    @Autowired
    protected passwordsRepository passwordsRepository;

    @Autowired
    protected recipe_equipmentsRepository recipe_equipmentsRepository;

    @Autowired
    protected recipe_ingredientsRepository recipe_ingredientsRepository;

    @Autowired
    protected recipe_methodsRepository recipe_methodsRepository;

    @Autowired
    protected recipe_tagsRepository recipe_tagsRepository;

    @Autowired
    protected recipesRepository recipesRepository;

    @Autowired
    protected tagsRepository tagsRepository;

    @Autowired
    protected user_followersRepository user_followersRepository;

    @Autowired
    protected usersRepository userRepository;

    @Autowired
    ServletContext ServletContext;
    
    protected ResponseEntity<String> createResponseEntityOk(String message) {
        HttpHeaders responseHeader = new HttpHeaders();
        responseHeader.set("content-type", "application/json");
        return ResponseEntity.ok().headers(responseHeader).body(message);
    }

    protected ResponseEntity<String> createResponseEntityOk() {
        return createResponseEntityOk("{\"success\":true}");
    }

    protected ResponseEntity<String> createResponseEntityFail() {
        return createResponseEntityOk("{\"success\":false}");
    }

    protected int getUserFromToken(String token) {
        return auth_tokensRepository.findByToken(token).get(0).getUser();
    }
}
