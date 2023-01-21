package com.example.soupkitchen.soupkitchen.database.Controllers;

import com.example.soupkitchen.soupkitchen.database.Database.*;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class CookbookController extends MainController {

    @CrossOrigin(origins = "*")
    @PostMapping(value = "/cookbooks/info", consumes = {"application/json"})
    public ResponseEntity<String> cookbookInfo(@RequestBody String body) {
        try {
            // get info from body
            JSONObject body_json = new JSONObject(body);
            List<Integer> cids = new ArrayList<Integer>();
            body_json.getJSONArray("cookbook_ids").forEach(
                cid -> cids.add(Integer.parseInt(cid.toString()))
            );
            
            String return_body = "{\"cookbooks\":[";
            
            for (int cid : cids) {
                // find cookbook
                cookbooks main = cookbooksRepository.findById(cid).get(0);
                
                // find cookbook_recipes
                List<cookbook_recipes> recipes_obj = cookbook_recipesRepository.findByCookbook(cid);
                String recipes = recipes_obj.stream()
                    .map(r -> Integer.toString(r.getRecipe()))
                    .collect(Collectors.joining(",", "[", "]"));

                users mainUser = userRepository.findById(main.getCreator()).get(0);
                
                boolean subscribed = false;
                if (cookbook_subscribersRepository.findByCookbookAndFollower(cid, mainUser.getId()).size() > 0) {
                    subscribed = true;
                }

                return_body = return_body + String.format(
                    "{" +
                    "\"id\":\"%d\"," +
                    "\"name\":\"%s\"," +
                    "\"creator\":\"%s\"," +
                    "\"recipe_ids\":%s," +
                    "\"subscribed\":%s," +
                    "\"creator_username\":\"%s\"" +
                    "}",
                    main.getId(),
                    main.getName(),
                    main.getCreator(),
                    recipes,
                    subscribed,
                    mainUser.getUsername()
                );
                return_body += ",";
            }

            if (return_body.charAt(return_body.length() - 1) == ',') {
                return_body = return_body.substring(0, return_body.length() - 1);
            }
            
            return_body += "]";
            return_body += "}";

            return createResponseEntityOk(return_body);
        
        } catch (Exception e) {
            return createResponseEntityFail();
        }
    }

    @CrossOrigin(origins = "*")
    @PostMapping(value = "/cookbooks/add", consumes = {"application/json"})
    public ResponseEntity<String> cookbookAdd(@RequestBody String body) {
        try {
            JSONObject body_json = new JSONObject(body);
            
            // get user info
            int uid = auth_tokensRepository.findByToken(body_json.getString("token")).get(0).getUser();

            // save new cookbook
            cookbooks cookbookMain = cookbooksRepository.save(
                new cookbooks(
                    body_json.getJSONObject("cookbook").getString("name"),
                    uid
                )
            );
            
            // save cookbook_recipes
            JSONArray recipes_array = body_json.getJSONObject("cookbook").getJSONArray("recipe_ids");
            for (int i = 0; i < recipes_array.length(); i++) {
                cookbook_recipesRepository.save(
                    new cookbook_recipes(
                        i,
                        recipes_array.getInt(i),
                        cookbookMain.getId()
                    )
                );
            }

            String return_body = String.format("{\"success\":true,\"cookbook_id\":%s}", cookbookMain.getId());
            return createResponseEntityOk(return_body);

        } catch (Exception e) {
            return createResponseEntityFail();
        }
    }
    
    @Transactional
    @CrossOrigin(origins = "*")
    @PostMapping(value = "/cookbooks/edit", consumes = {"application/json"})
    public ResponseEntity<String> cookbookEdit(@RequestBody String body) {
        try {
            // get info from body
            JSONObject body_json = new JSONObject(body);

            // get user and cookbook info
            int uid = auth_tokensRepository.findByToken(body_json.getString("token")).get(0).getUser();
            cookbooks cookbookMain = cookbooksRepository.findById(body_json.getJSONObject("cookbook").getInt("id")).get(0);

            // check if user matches cookbook creator
            if (uid != cookbookMain.getCreator()) {
                return createResponseEntityFail();
            }

            // edit cookbook
            cookbookMain.setName(body_json.getJSONObject("cookbook").getString("name"));
            cookbooksRepository.save(cookbookMain);

            // edit cookbook_recipes
            cookbook_recipesRepository.deleteByCookbook(body_json.getJSONObject("cookbook").getInt("id"));

            JSONArray recipes_array = body_json.getJSONObject("cookbook").getJSONArray("recipe_ids");
            for (int i = 0; i < recipes_array.length(); i++) {
                cookbook_recipesRepository.save(
                    new cookbook_recipes(
                        i,
                        recipes_array.getInt(i),
                        cookbookMain.getId()
                    )
                );
            }
            
            return createResponseEntityOk();

        } catch (Exception e) {
            return createResponseEntityFail();
        }
    }

    @Transactional
    @CrossOrigin(origins = "*")
    @PostMapping(value = "/cookbooks/delete", consumes = {"application/json"})
    public ResponseEntity<String> cookbookDelete(@RequestBody String body) {
        try {
            // get info from body
            JSONObject body_json = new JSONObject(body);

            // get user
            int uid = auth_tokensRepository.findByToken(body_json.getString("token")).get(0).getUser();
            
            // get cookbook
            cookbooks cookbookMain = cookbooksRepository.findById(body_json.getInt("cookbook_id")).get(0);

            // check if user matches cookbook creator
            if (uid != cookbookMain.getCreator()) {
                return createResponseEntityFail();
            }

            cookbook_recipesRepository.deleteByCookbook(body_json.getInt("cookbook_id"));
            cookbook_subscribersRepository.deleteByCookbook(body_json.getInt("cookbook_id"));
            cookbooksRepository.deleteById(body_json.getInt("cookbook_id"));
            
            return createResponseEntityOk();

        } catch (Exception e) {
            return createResponseEntityFail();
        }
    }

    @CrossOrigin(origins = "*")
    @PostMapping(value = "/cookbooks/subscribe", consumes = {"application/json"})
    public ResponseEntity<String> cookbookSubscribe(@RequestBody String body) {
        try {
            // get info from body
            JSONObject body_json = new JSONObject(body);

            // get user info
            int uid = auth_tokensRepository.findByToken(body_json.getString("token")).get(0).getUser();

            // save to cookbook_subscribers
            cookbook_subscribersRepository.save(
                new cookbook_subscribers(
                    body_json.getInt("cookbook_id"),
                    uid
                )
            );
            
            return createResponseEntityOk();

        } catch (Exception e) {
            return createResponseEntityFail();
        }
    }

    @Transactional
    @CrossOrigin(origins = "*")
    @PostMapping(value = "/cookbooks/unsubscribe", consumes = {"application/json"})
    public ResponseEntity<String> cookbookUnubscribe(@RequestBody String body) {
        try {
            // get info from body
            JSONObject body_json = new JSONObject(body);

            // get user info
            int uid = auth_tokensRepository.findByToken(body_json.getString("token")).get(0).getUser();

            // delete from cookbook_subscribers
            cookbook_subscribersRepository.deleteByCookbookAndFollower(
                body_json.getInt("cookbook_id"),
                uid
            );
            
            return createResponseEntityOk();

        } catch (Exception e) {
            return createResponseEntityFail();
        }
    }
}