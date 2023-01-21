package com.example.soupkitchen.soupkitchen.database.Controllers;

import com.example.soupkitchen.soupkitchen.database.Database.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@RestController
public class dummyController extends RecipeMainController {

    @CrossOrigin(origins = "*")
    @PostMapping(value = "/recipes/addAll", consumes = {"application/json"})
    public ResponseEntity<String> recipeAddAll(@RequestBody String body) {
        try {
            // get info from body
            JSONObject body_json1 = new JSONObject(body);

            // get user
            List<String> final_ids = new ArrayList<String>();

            // int uid = auth_tokensRepository.findByToken(body_json1.getString("token")).get(0).getUser();
            int uid = 1;
            JSONArray recipes = body_json1.getJSONArray("recipes");
            for (int i = 0;  i < recipes.length(); i++) {
                JSONObject body_json = recipes.getJSONObject(i);
                // save recipe
                //Integer.parseInt(body.get("is_published")),
                recipes recipeMain = recipesRepository.save(
                    new recipes(
                    uid,
                    body_json.getString("name"),
                    body_json.getString("meal_type"),
                    body_json.getString("photo")
                    )
                );

                saveTag(body_json.getJSONArray("tags"), recipeMain.getId());
                saveIngredients(body_json.getJSONArray("ingredients"), recipeMain.getId());

                recipe_methodsRepository.save(
                    new recipe_methods(
                        recipeMain.getId(),
                        body_json.getString("methods")
                    )
                );

                //Add photo
                String userDirectory = Paths.get("").toAbsolutePath().toString();
                String uploadDir = userDirectory + "/recipe-photos/";
                saveFile(uploadDir, String.valueOf(recipeMain.getId()), body_json.getString("image"));
                
                final_ids.add(String.valueOf(recipeMain.getId()));
            }

            final_ids = final_ids.stream().map(id -> "\"" + id + "\"").toList();
            String return_body = String.format("{\"success\":true,\"recipe_ids\":%s}", "[" + String.join(",", final_ids) + "]");

            return createResponseEntityOk(return_body);

        } catch (Exception e) {
            return createResponseEntityFail();
        }
    }
}