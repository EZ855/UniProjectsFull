package com.example.soupkitchen.soupkitchen.database.Controllers;

import com.example.soupkitchen.soupkitchen.database.Database.*;
import com.example.soupkitchen.soupkitchen.database.Files.FileUploadUtil;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
// import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class RecipeController extends RecipeMainController {

    @CrossOrigin(origins = "*")
    @PostMapping(value = "/recipes/info", consumes = {"application/json"})
    public ResponseEntity<String> recipeInfo(@RequestBody String body) {
        try {
            // get info from body
            JSONObject body_json = new JSONObject(body);
            List<Integer> rids = new ArrayList<Integer>();
            body_json.getJSONArray("recipe_ids").forEach(
                rid -> rids.add(Integer.parseInt(rid.toString()))
            );

            String return_body = "{\"recipes\":[";

            for (int rid : rids) {
                // get recipe
                recipes main = recipesRepository.findById(rid).get(0);

                // get recipe_ingredients
                List<recipe_ingredients> ingreds_obj = recipe_ingredientsRepository.findByRecipe(rid);
                String ingreds = ingreds_obj.stream()
                    .map(ingred -> String.format(
                        "{"+
                        "\"name\":\"%s\"," + 
                        "\"quantity\":\"%s\"," +
                        "\"units\":\"%s\"" +
                        "}"
                        ,
                        ingredientsRepository.findById(ingred.getIngredient()).get(0).getName(),
                        ingred.getQuantity(),
                        ingred.getUnits()
                    ))
                    .collect(Collectors.joining(",", "[", "]"));
                
                // get recipe_methods
                String method = recipe_methodsRepository.findByRecipe(rid).get(0).getMethod();
                
                // get recipe_tags
                List<recipe_tags> tags_obj = recipe_tagsRepository.findByRecipe(rid);
                String tags = tags_obj.stream()
                    .map(tag -> tagsRepository.findById(tag.getTag()).get(0).getName())
                    .map(tag -> "\"" + tag + "\"")
                    .collect(Collectors.joining(",", "[", "]"));

                // get recipe_equipments
                List<recipe_equipments> equips_obj = recipe_equipmentsRepository.findByRecipe(rid);
                String equips = equips_obj.stream()
                    .map(equip -> equipmentsRepository.findById(equip.getEquipment()).get(0).getName())
                    .map(equip -> "\"" + equip + "\"")
                    .collect(Collectors.joining(",", "[", "]"));
                
                // get user
                users mainUser = userRepository.findById(main.getAuthor()).get(0);
                
                // get liked
                boolean liked = false;
                if (likesRepository.findByUserAndRecipe(mainUser.getId(), rid).size() > 0) {
                    liked = true;
                }

                //get likes
                String likes = likesRepository.findByRecipe(rid).stream()
                    .map(like -> Integer.toString(like.getUser()))
                    .collect(Collectors.joining(",", "[", "]"));

                //Photos
                /*
                * If photo doesnt exists or is a dummy file then it will render as an empty string supposedly lol
                */
                // String userDirectory = Paths.get("").toAbsolutePath().toString();
                // String dir = userDirectory + "/recipe-photos/";
                // String photo = loadFile(dir + main.getId()).toString();

                return_body = return_body + String.format(
                    "{" +
                    "\"id\":%s," +
                    "\"name\":\"%s\", " +
                    "\"meal_type\":\"%s\"," +
                    "\"tags\":%s," +
                    "\"equipments\":%s," +
                    "\"ingredients\":%s," +
                    "\"methods\":\"%s\"," +
                    "\"likes\":%s," +
                    // "\"photo_url\":\"%s\"," +
                    "\"author_id\":\"%s\"," +
                    "\"author_username\":\"%s\"," +
                    "\"liked\":%s" +
                    // "\"photo_64bytes\" : \"%s\"" +
                    "}",
                    rid,
                    main.getName(),
                    main.getMealType(),
                    tags,
                    equips,
                    ingreds,
                    method,
                    likes,
                    // main.getPhoto(),
                    main.getAuthor(),
                    mainUser.getUsername(),
                    liked
                    // photo
                );
                return_body += ",";
            }

            if (return_body.charAt(return_body.length() - 1) == ',') {
                return_body = return_body.substring(0, return_body.length() - 1);
            }
            
            return_body += "]";
            return_body = return_body + "}";

            return createResponseEntityOk(return_body);

        } catch (Exception e) {
            return createResponseEntityFail();
        }
    }

    @CrossOrigin(origins = "*")
    @PostMapping(value = "/recipes/add", consumes = {"application/json"})
    public ResponseEntity<String> recipeAdd(@RequestBody String body) {
        try {
            // get info from body
            JSONObject body_json = new JSONObject(body);

            // get user
            int uid = auth_tokensRepository.findByToken(body_json.getString("token")).get(0).getUser();
            
            // save recipe
            //Integer.parseInt(body.get("is_published")),
            recipes recipeMain = recipesRepository.save(
                new recipes(
                    uid,
                    body_json.getJSONObject("recipe").getString("name"),
                    body_json.getJSONObject("recipe").getString("meal_type"),
                    body_json.getJSONObject("recipe").getString("photo")
                )
            );

            saveTag(body_json.getJSONObject("recipe").getJSONArray("tags"), recipeMain.getId());
            // saveEquipment(body_json.getJSONObject("recipe").getJSONArray("equipments"), recipeMain.getId());
            saveIngredients(body_json.getJSONObject("recipe").getJSONArray("ingredients"), recipeMain.getId());

            recipe_methodsRepository.save(
                new recipe_methods(
                    recipeMain.getId(),
                    body_json.getJSONObject("recipe").getString("methods")
                )
            );

            //Add photo
            // String userDirectory = Paths.get("").toAbsolutePath().toString();
            // String uploadDir = userDirectory + "/recipe-photos/";
            // saveFile(uploadDir, String.valueOf(recipeMain.getId()), body_json.getString("image"));
            
            String return_body = String.format("{\"success\":true,\"recipe_id\":%s}", recipeMain.getId());
            return createResponseEntityOk(return_body);

        } catch (Exception e) {
            return createResponseEntityFail();
        }
    }
    
    @Transactional
    @CrossOrigin(origins = "*")
    @PostMapping(value = "/recipes/edit", consumes = {"application/json"})
    public ResponseEntity<String> recipeEdit(@RequestBody String body) {
        try {
            // get info from body
            JSONObject body_json = new JSONObject(body);

            // get user
            int uid = auth_tokensRepository.findByToken(body_json.getString("token")).get(0).getUser();
            
            // get recipe
            recipes recipeMain = recipesRepository.findById(body_json.getJSONObject("recipe").getInt("id")).get(0);

            // check if token matches original cookbook creator
            if (uid != recipeMain.getAuthor()) {
                return createResponseEntityFail();
            }

            //recipeMain.setIs_published(Integer.parseInt(body.get("is_published")));
            recipeMain.setName(body_json.getJSONObject("recipe").getString("name"));
            recipeMain.setMealType(body_json.getJSONObject("recipe").getString("meal_type"));
            recipeMain.setPhoto(body_json.getJSONObject("recipe").getString("photo"));
            
            recipesRepository.save(recipeMain);
            
            recipe_ingredientsRepository.deleteByRecipe(recipeMain.getId());
            saveIngredients(body_json.getJSONObject("recipe").getJSONArray("ingredients"), recipeMain.getId());
            
            recipe_methodsRepository.deleteByRecipe(recipeMain.getId());
            recipe_methodsRepository.save(
                new recipe_methods(
                    recipeMain.getId(),
                    body_json.getJSONObject("recipe").getString("methods")
                )
            );
            
            // recipe_equipmentsRepository.deleteAllByRecipe(recipeMain.getId());
            // saveEquipment(body_json.getJSONObject("recipe").getJSONArray("equipments"), recipeMain.getId());

            recipe_tagsRepository.deleteByRecipe(recipeMain.getId());
            saveTag(body_json.getJSONObject("recipe").getJSONArray("tags"), recipeMain.getId());

            //Add photo
            /*String userDirectory = Paths.get("")
                    .toAbsolutePath()
                    .toString();
            String uploadDir = userDirectory + "/recipe-photos/";

            //Will throw IOException upon failure
            try{
                saveFile(uploadDir, String.valueOf(recipeMain.getId()), body.get("image"));
            }
            catch (Exception e){
                bodyList.add("photo upload");
            }
            */
            
            return createResponseEntityOk();

        } catch (Exception e) {
            return createResponseEntityFail();
        }
    }


    // Request Param : recipe_id : just the number
    @Transactional
    @CrossOrigin(origins = "*")
    @PostMapping(value = "/recipes/delete", consumes = {"application/json"})
    public ResponseEntity<String> deleteRecipe(@RequestBody String body) {
        try {
            // get info from body
            JSONObject body_json = new JSONObject(body);

            // get user
            int uid = auth_tokensRepository.findByToken(body_json.getString("token")).get(0).getUser();

            // get recipe
            recipes recipeMain = recipesRepository.findById(body_json.getInt("recipe_id")).get(0);

            // check if token matches original cookbook creator
            if (uid != recipeMain.getAuthor()) {
                return createResponseEntityFail();
            }

            //First photos
            /*String userDirectory = Paths.get("")
                    .toAbsolutePath()
                    .toString();
            String photo_location = userDirectory + "/recipe-photos/" + rid;

            Path loc = Paths.get(photo_location);
            Files.deleteIfExists(loc);
            */

            recipe_methodsRepository.deleteByRecipe(recipeMain.getId());
            recipe_tagsRepository.deleteByRecipe(recipeMain.getId());
            recipe_ingredientsRepository.deleteByRecipe(recipeMain.getId());
            recipe_equipmentsRepository.deleteByRecipe(recipeMain.getId());
            recipesRepository.deleteById(recipeMain.getId());

            likesRepository.deleteByRecipe(recipeMain.getId());
            commentsRepository.deleteByRecipe(recipeMain.getId());
            cookbook_recipesRepository.deleteByRecipe(recipeMain.getId());
            
            return createResponseEntityOk();

        } catch (Exception e) {
            return createResponseEntityFail();
        }
    }

    @CrossOrigin(origins = "*")
    @PostMapping(value = "/recipes/like", consumes = {"application/json"})
    public ResponseEntity<String> recipeLike(@RequestBody String body) {
        try {
            // get info from body
            JSONObject body_json = new JSONObject(body);

            // get user
            int uid = auth_tokensRepository.findByToken(body_json.getString("token")).get(0).getUser();

            // add like
            likesRepository.save(
                new likes(
                    uid,
                    body_json.getInt("recipe_id"), 
                    body_json.getBoolean("liked")
                )
            );

            return createResponseEntityOk();

        } catch (Exception e) {
            return createResponseEntityFail();
        }
    }

    @CrossOrigin(origins = "*")
    @PostMapping(value = "/recipes/comments/get", consumes = {"application/json"})
    public ResponseEntity<String> recipeCommentsGet(@RequestBody String body) {
        try {
            // get info from body
            JSONObject body_json = new JSONObject(body);
            int rid = body_json.getInt("recipe_id");

            // get recipe_comments
            List<comments> mainComment = commentsRepository.findByRecipe(rid);
            
            String return_body = "{\"comments\" : [\n";

            for (comments comment : mainComment) {
                users commentUser = userRepository.findById(comment.getCommenter()).get(0);
                return_body = return_body + String.format(
                    "{" +
                    "\"id\":%s," +
                    "\"author_id\":%s," +
                    "\"comment\":\"%s\"," +
                    "\"author_username\":\"%s\"" +
                    "}",
                    comment.getId(),
                    comment.getCommenter(),
                    comment.getComment(),
                    commentUser.getUsername()
                );
                return_body += ",";
            }

            if (return_body.charAt(return_body.length() - 1) == ',') {
                return_body = return_body.substring(0, return_body.length() - 1);
            }

            return_body += "]";
            return_body = return_body + "}";

            return createResponseEntityOk(return_body);

        } catch (Exception e) {
            return createResponseEntityFail();
        }
    }

    @CrossOrigin(origins = "*")
    @PostMapping(value = "/recipes/comments/add", consumes = {"application/json"})
    public ResponseEntity<String> commentAdd(@RequestBody String body) {
        try {
            // get info from body
            JSONObject body_json = new JSONObject(body);

            // get user
            int uid = auth_tokensRepository.findByToken(body_json.getString("token")).get(0).getUser();

            // add comment
            commentsRepository.save(
                new comments(
                    uid,
                    body_json.getJSONObject("comment").getInt("recipe"), 
                    body_json.getJSONObject("comment").getString("comment")
                )
            );

            return createResponseEntityOk();
            
        } catch (Exception e) {
            return createResponseEntityFail();
        }
    }

    @CrossOrigin(origins = "*")
    @GetMapping(value = "/recipes/image/get/{image_name}", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getImageWithMediaType(@PathVariable String image_name) throws IOException {
        try {
            InputStream in = getClass().getResourceAsStream("/com/example/soupkitchen/soupkitchen/photos/recipes/" + image_name + ".jpeg");
            InputStream in2 = getClass().getResourceAsStream("/com/example/soupkitchen/soupkitchen/photos/recipes/" + image_name + ".jpg");
            InputStream in3 = getClass().getResourceAsStream("/com/example/soupkitchen/soupkitchen/photos/recipes/" + image_name + ".png");
            
            if (in == null) {
                in = in2;
                if (in == null) {
                    in = in3;
                }
            }
            return IOUtils.toByteArray(in);
        } catch (Exception e) {
            InputStream in = getClass().getResourceAsStream("/com/example/soupkitchen/soupkitchen/photos/recipes/image.jpeg");
            return IOUtils.toByteArray(in);
        }
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/recipes/image/add")
    public ResponseEntity<String> userImageAdd(@RequestParam("image") MultipartFile multipartFile, @RequestParam("token") String token, @RequestParam("recipe_id") String recipe_id) throws IOException {
        try {            
            // get user
            int uid = auth_tokensRepository.findByToken(token).get(0).getUser();

            // get recipe
            int rid = Integer.parseInt(recipe_id);
            recipes recipeMain = recipesRepository.findById(rid).get(0);

            if (recipeMain.getAuthor() != uid) {
                return createResponseEntityFail();
            }
            
            String fileType = StringUtils.cleanPath(multipartFile.getContentType());
            String uploadDir = "/com/example/soupkitchen/soupkitchen/photos/recipes/";
            FileUploadUtil.saveFile(uploadDir, Integer.toString(rid) + fileType, multipartFile);
            return createResponseEntityOk();

        } catch (Exception e) {
            return createResponseEntityFail();
        }
    }
}
