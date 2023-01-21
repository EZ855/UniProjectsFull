package com.example.soupkitchen.soupkitchen.database.Controllers;

import com.example.soupkitchen.soupkitchen.database.Database.*;
import org.json.JSONObject;
import org.springframework.data.util.Pair;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// import javax.persistence.criteria.CriteriaBuilder;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
public class RecommendationController extends MainController {

    /* Finds drinks with the most similarity
    *
    * */
    @CrossOrigin(origins = "*")
    @PostMapping(value = "/recipes/drink", consumes = {"application/json"})
    public ResponseEntity<String> recipeDrink(@RequestBody String body) {
        try {
            // get info from body
            JSONObject body_json = new JSONObject(body);
            int rid = body_json.getInt("recipe_id");

            //Retrieve Set of similar tags to current recipe
            //Unidirectional - longer tags cannot match shorter tags without first creating an extensive index
            Set<Integer> tSet = new HashSet<Integer>();
            List<Integer> r_tags = recipe_tagsRepository.findByRecipe(rid).stream().map(r -> r.getTag()).toList();
            for (int i = 0; i < r_tags.size(); i++) {
                int tag = r_tags.get(i);
                //Search for similar tags
                List<String> tList = tagsRepository.findById(tag).stream().map(t -> t.getName()).toList();
                for (int j = 0; j < tList.size(); j++) {
                    //Stem here if possible
                    String tName = tList.get(j);
                    List<Integer> simTags = tagsRepository.findByNameContainingIgnoreCase(tName).stream().map(t -> t.getId()).toList();

                    tSet.addAll(simTags);
                }
            }

            //Find recipes with said tags
            List<Integer> tSetList = tSet.stream().toList();
            HashMap<Integer, Integer> recipeTagCount = new HashMap<Integer, Integer>();
            for (int i = 0; i < tSetList.size(); i++){
                int tag = tSetList.get(i);
                List<Integer> taggedRids = recipe_tagsRepository.findByTag(tag).stream().map(p -> p.getRecipe()).toList();
                //Filter for drinks only
                List<Integer> taggedDrinks = taggedRids.stream().filter(r -> recipesRepository.findById(r).get().getMealType().equals("drink") ).toList();

                for (int j = 0; j < taggedDrinks.size(); j++){
                    int tRid = taggedDrinks.get(j);

                    if (!recipeTagCount.containsKey(tRid)){
                        recipeTagCount.put(tRid, 0);
                    }

                    recipeTagCount.put(tRid, recipeTagCount.get(tRid) + 1);
                }
            }

            String bod = "{}";
            //If nothing get the most popular drinks
            if (recipeTagCount.entrySet().size() == 0){
                // System.out.println("here");
                List<Integer> popDrinks = recipesRepository.findByMealType("drink").stream().map(r -> r.getId()).filter(r -> r != rid).toList();

                List<Pair<Integer, Integer>>  countPopDrinks = new ArrayList<Pair<Integer, Integer>>();
                countPopDrinks.addAll(popDrinks.stream().map(r -> Pair.of(r, likesRepository.findByRecipe(r).size() + commentsRepository.findByRecipe(r).size())).toList());

                Collections.sort(countPopDrinks, Comparator.comparing(p -> -p.getSecond()));

                bod = "{\"recipe_ids\" : [" +
                        String.join(",", countPopDrinks.stream().map(p -> p.getFirst().toString()).toList()) +
                        "]}";

            } else {
                List<Pair<Integer, Integer>> convertMap = new ArrayList<Pair<Integer, Integer>>();
                convertMap.addAll(recipeTagCount.entrySet().stream().map(t -> Pair.of(t.getKey(), t.getValue())).toList());
                Collections.sort(convertMap, Comparator.comparing(p -> -p.getSecond()));

                bod = "{\"recipe_ids\" : [" +
                        String.join(",", convertMap.stream().map(p -> p.getFirst().toString()).toList()) +
                        "]}";
            }

            return createResponseEntityOk(bod);

        } catch (Exception e) {
            return createResponseEntityFail();
        }
    }

    @CrossOrigin(origins = "*")
    @PostMapping(value = "/recipes/similar", consumes = {"application/json"})
    public ResponseEntity<String> recipeSimilar(@RequestBody String body) {
        try {
            JSONObject body_json = new JSONObject(body);
            int rid = body_json.getInt("recipe_id");

            List<Pair<Integer, Double>> similarRecipes = new ArrayList<Pair<Integer, Double>>();
            recipes target = recipesRepository.findById(rid).get(0);

            List<recipe_ingredients> ingredients = recipe_ingredientsRepository.findByRecipe(rid);

            //Gets a set of recipes with at least one of the same ingredients
            Set<Integer> similarRecipeRids = new HashSet<Integer>();
            for (int i = 0; i <  ingredients.size(); i++){
                recipe_ingredients ingred = ingredients.get(i);
                List<recipe_ingredients> temp_recipes = recipe_ingredientsRepository.findByIngredient(ingred.getIngredient());
                List<Integer> rids = temp_recipes.stream().map(recipe -> recipe.getRecipe()).collect(Collectors.toList());
                similarRecipeRids.addAll(rids);
            }

            List<Integer> sim = similarRecipeRids.stream().toList();
            //Then calculate cosine value for each with quantity normalisation
            for (int i = 0; i < sim.size(); i++){
                List<recipe_ingredients> other = recipe_ingredientsRepository.findByRecipe(sim.get(i));
                Double cosine_value = calculateCosine(ingredients, other);
                similarRecipes.add(Pair.of(sim.get(i), cosine_value));
            }
            //Then sort via cosine calculation or second field
            Collections.sort(similarRecipes, Comparator.comparing(p -> -p.getSecond()));
            //Render into a string with expected output
            String ret = similarRecipes.stream().filter(p -> p.getFirst() != rid).map(p -> p.getFirst().toString()).collect(Collectors.joining(","));
            ret = "{\"similar_recipes\" : \"" + ret + "\"}";


            //Uses cosine similarity to find similar recipes

            //Need to hash out how far am I going to get similar recipes
            //Just ingredient ids? would look like (1, 0, 0, 1, 0) <- if its present or not in the recipe. Not very useful and we'd get marked down

            // Quantities? if so then is it just metric or are we accommodating imperial as well?
            // Can come up with proper conversions, but then we'd need to limit the number of option.
            // But we'd run into the issue of large egg vs small egg vs normal egg sizes and recipes that say large bunch
            // or pinch of salt. How do we account for this?

            return createResponseEntityOk(ret);

        } catch (Exception e) {
            return createResponseEntityFail();
        }
    }

    private Double calculateCosine(List<recipe_ingredients> origin, List<recipe_ingredients> comparison){
        //Normalise quantities

        //Only keep ingredients which exists in origin
        List <Integer> origin_ids = origin.stream().map(recipe -> recipe.getIngredient()).collect(Collectors.toList());
        List<Integer> comp = comparison.stream().
                                        filter( recipe -> origin_ids.contains(recipe.getIngredient())).
                                        map(recipe -> recipe.getIngredient()).
                                        collect(Collectors.toList());

        //Calculate
        //If not in list then 0 otherwise check quantities
        Double sum = 0.0;
        Double sumOrigin = 0.0;
        Double sumOther = 0.0;
        for (int i = 0; i < origin.size(); i++){
            recipe_ingredients r = origin.get(i);
            if (comp.contains(r.getIngredient())){
                recipe_ingredients other = comparison.stream().
                        filter(o -> o.getIngredient() == r.getIngredient()).
                        collect(Collectors.toList()).get(0);
                sum += r.getQuantity() * other.getQuantity();

                sumOther += other.getQuantity() * other.getQuantity();
            }
            sumOrigin += r.getQuantity() * r.getQuantity();
        }

        return sum / (Math.sqrt(sumOrigin) * Math.sqrt(sumOther));
    }

    @CrossOrigin(origins = "*")
    @PostMapping(value = "/recipes/feed")
    public ResponseEntity<String> recipeFeed(@RequestBody String body) {
        try {
            JSONObject body_json = new JSONObject(body);
            String token = body_json.getString("token");
            // get user info
            List<auth_tokens> authList = auth_tokensRepository.findByToken(token);

            List<Integer> final_list = new ArrayList<Integer>();
            List<Integer> diffIds = new ArrayList<Integer>();
            // Return normal recipes then other
            List<Integer> allUserIds = userRepository.findAll().stream().map(u -> u.getId()).collect(Collectors.toList());
            if (authList != null && authList.size() > 0) {
                int fid = authList.get(0).getUser();
                List<Integer> followed = user_followersRepository.findByFollower(fid).stream().map(n -> n.getUser()).collect(Collectors.toList());

                //Subscribed Users Within the last week
                List<Integer> followedRecipes = collectRecipesBeforeNDays(followed, 7);
                final_list.addAll(followedRecipes);

                diffIds.addAll(allUserIds.stream().filter(n -> !followed.contains(n)).collect(Collectors.toList()));

                //Non Subscribed User Recipes within the last three days
                List<Integer> diffRecipes = collectRecipesBeforeNDays(diffIds, 3);
                final_list.addAll(diffRecipes);
            }
            //Everything else sorted by most liked + commented
            List<Integer> restList = collectRecipesNotIn(final_list);

            final_list.addAll(restList);
            List<String> string_list = final_list.stream().map(r -> r.toString()).collect(Collectors.toSet()).stream().toList();

            String final_listStr = String.join(", ", string_list);

            // Take everyone else and return
            String bod = "{\"recipe_ids\": [" + final_listStr + "]}";
            return createResponseEntityOk(bod);

        } catch (Exception e) {
            return createResponseEntityFail();
        }
    }

    private List<Integer> collectRecipesBeforeNDays(List<Integer> userSet, int n){
        HashMap<Integer, List<Integer>> recipe_id_time = new HashMap<Integer, List<Integer>>();
        //Among followed users, sort users based on the total number of likes that the follower has for a given user's recipes.
        // Recipe Pair Times for followed account

        List<Pair<Integer, LocalDateTime>> final_list = new ArrayList<Pair<Integer, LocalDateTime>>();

        List<Pair<Integer, Integer>> userLikes = new ArrayList<Pair<Integer, Integer>>();
        for (int i = 0; i < userSet.size(); i++){
            int uid = userSet.get(i);
            int n_likes = 0;

            List<recipes> recipes = recipesRepository.findByAuthor(uid);
            for (int j = 0; j < recipes.size(); j++){
                // Add all recipes that are made in the last 5 days
                recipes r = recipes.get(0);
                if ( n != -1 && r.getTime().isAfter(LocalDateTime.now().minusDays(n))) {
                    final_list.add(Pair.of(r.getId(), r.getTime()));
                } else if (n == -1) {
                    final_list.add(Pair.of(r.getId(), r.getTime()));
                }
            }
        }

        Collections.sort(final_list, Comparator.comparing(p -> p.getSecond()));
        Collections.reverse(final_list);
        return final_list.stream().map(r -> r.getFirst()).collect(Collectors.toList());
    }

    private List<Integer> collectRecipesNotIn(List<Integer> rids){
        List<recipes> recipesList = recipesRepository.findAll().stream().filter(r -> !rids.contains(r.getId())).collect(Collectors.toList());
        Collections.sort(recipesList, Comparator.comparing(r -> r.getTime()));
        List<Pair<Integer, Integer>> recipeLikes = new ArrayList<Pair<Integer, Integer>>();
        for (int i = 0; i < recipesList.size(); i++){
            int rid = recipesList.get(i).getId();
            int likeComments = likesRepository.findByRecipe(rid).size() + commentsRepository.findByRecipe(rid).size();
            recipeLikes.add(Pair.of(rid, likeComments));
        }
        
        Collections.sort(recipeLikes, Comparator.comparing(r -> r.getSecond()));
        Collections.reverse(recipeLikes);
        return recipeLikes.stream().map(r -> r.getFirst()).collect(Collectors.toList());
    }
}