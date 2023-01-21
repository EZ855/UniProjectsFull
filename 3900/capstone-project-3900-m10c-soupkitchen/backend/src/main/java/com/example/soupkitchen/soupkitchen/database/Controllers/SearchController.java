package com.example.soupkitchen.soupkitchen.database.Controllers;

import org.json.JSONObject;
import org.springframework.data.util.Pair;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// import java.time.LocalDateTime;
import java.util.*;
// import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
public class SearchController extends MainController {

    /*
    * Every match gets the following number of points
    * Method : 1
    * Ingredients : 5
    * Equipment : 5
    * Tags : 3
    * Title : 10
    * Meal-type : 5
    *
    * */

    @CrossOrigin(origins = "*")
    @PostMapping(value = "/recipes/search", consumes = {"application/json"})
    public ResponseEntity<String> recipeSearch(@RequestBody String body) {
        try {
            // get info from body
            JSONObject body_json = new JSONObject(body);
            //Extract everything
            String query = body_json.getString("query");
            Boolean sTitle = body_json.getBoolean("title");
            Boolean sIngredients = body_json.getBoolean("ingredients");
            Boolean sMethod = body_json.getBoolean("method");
            Boolean sEquipment = body_json.getBoolean("equipment");
            Boolean sTags = body_json.getBoolean("tags");

            Boolean sMealType = body_json.getBoolean("mealtype");
            Boolean sUsers = body_json.getBoolean("user");


            //Query String processing
            List<String> words = Stream.of(query.split(" ")).toList();
            HashMap<Integer, Integer> recipeScores = new HashMap<Integer, Integer>();

            Boolean everything = false;
            if (!sMealType && !sEquipment && !sTitle && !sIngredients && !sMethod && !sTags){
                everything = true;
            }

            if (sMethod || everything){
                List<Pair<Integer, Integer>> methodScores = searchMethod(words);
                recipeScores = updateFinalScore(recipeScores, methodScores);
            }

            if (sTitle || everything){
                List<Pair<Integer, Integer>> titleScores = searchTitles(words);
                recipeScores = updateFinalScore(recipeScores, titleScores);
            }

            if (sTags || everything){
                List<Pair<Integer, Integer>> tagScores = searchTags(words);
                recipeScores = updateFinalScore(recipeScores, tagScores);
            }

            if (sIngredients || everything){
                List<Pair<Integer, Integer>> ingredScores = searchIngredients(words);
                recipeScores = updateFinalScore(recipeScores, ingredScores);
            }

            if (sEquipment || everything){
                List<Pair<Integer, Integer>> equipScores = searchEquipment(words);
                recipeScores = updateFinalScore(recipeScores, equipScores);
            }

            if (sUsers || everything){
                List<Pair<Integer, Integer>> userScores = searchUsers(words);
                recipeScores = updateFinalScore(recipeScores, userScores);
            }

            //Process recipeScores
            List<Pair<Integer, Integer>> scoreList = new ArrayList<Pair<Integer, Integer>>();
            scoreList.addAll(recipeScores.entrySet().stream().map(e -> Pair.of(e.getKey(), e.getValue())).toList());
            Collections.sort(scoreList, Comparator.comparing(p -> -p.getSecond() ));

            List<String> strList = scoreList.stream().map(s -> s.getFirst().toString()).toList();
            String final_str = String.join(",", strList);

            String bod = "{\"recipe_ids\" : [" + final_str + "]}";

            return createResponseEntityOk(bod);

        } catch (Exception e) {
            return createResponseEntityFail();
        }
    }

    private HashMap<Integer, Integer> updateFinalScore(HashMap<Integer, Integer> final_map, List<Pair<Integer, Integer>> searchRes){
        for (int i = 0; i < searchRes.size(); i++){
            Pair<Integer, Integer> p = searchRes.get(i);
            if (!final_map.containsKey(p.getFirst())){
                final_map.put(p.getFirst(), 0);
            }
            final_map.put(p.getFirst(), final_map.get(p.getFirst()) + p.getSecond());
        }
        return final_map;
    }

    private List<Pair<Integer, Integer>> searchIngredients(List<String> words){
        HashMap<Integer, Set<String>> final_list = new HashMap<Integer, Set<String>>();
        for (int i = 0; i < words.size(); i++){
            String word = words.get(i);
            List<Integer> ing_ids = ingredientsRepository.findByNameContainingIgnoreCase(word).stream().map(ing -> ing.getId()).toList();
            Set<Integer> recipeSet = new HashSet<Integer>();
            for (int j = 0; j < ing_ids.size(); j++){
                int ing_id = ing_ids.get(i);
                List<Integer> t_rec = recipe_ingredientsRepository.findByIngredient(ing_id).stream().map(r -> r.getRecipe()).toList();
                recipeSet.addAll(t_rec);
            }

            List<Integer> recipes = recipeSet.stream().toList();
            for (int j = 0; j < recipes.size(); j++){
                int rid = recipes.get(j);
                if (!final_list.containsKey(rid)){
                    final_list.put(rid, new HashSet<String>());
                }
                final_list.get(rid).add(word);
            }
        }
        List<Pair<Integer, Integer>> recipeMatches = new ArrayList<Pair<Integer, Integer>>();
        recipeMatches.addAll(final_list.entrySet().stream().map(r -> Pair.of(r.getKey(), r.getValue().size())).toList());
        Collections.sort(recipeMatches, Comparator.comparing(r -> -r.getSecond()));
        return recipeMatches;
    }

    private List<Pair<Integer, Integer>> searchMethod(List<String> words){
        HashMap<Integer, Set<String>> final_list = new HashMap<Integer, Set<String>>();
        for (int i = 0; i < words.size(); i++){
            String word = words.get(i);
            List<Integer> methods = recipe_methodsRepository.findByMethodContainingIgnoreCase(word).stream().map(r -> r.getRecipe()).toList();
            for (int j = 0; j < methods.size(); j++){
                int mid = methods.get(j);
                if (!final_list.containsKey(mid)){
                    final_list.put(mid, new HashSet<String>());
                }
                final_list.get(mid).add(word);
            }
        }
        List<Pair<Integer, Integer>> recipeMatches = new ArrayList<Pair<Integer, Integer>>();
        recipeMatches.addAll(final_list.entrySet().stream().map(r -> Pair.of(r.getKey(), r.getValue().size())).toList());
        Collections.sort(recipeMatches, Comparator.comparing(r -> -r.getSecond()));
        return recipeMatches;
    }

    private List<Pair<Integer, Integer>> searchTags(List<String> words){
        HashMap<Integer, Set<String>> final_list = new HashMap<Integer, Set<String>>();
        for (int i = 0; i < words.size(); i++){
            String word = words.get(i);
            List<Integer> tag_ids = tagsRepository.findByNameContainingIgnoreCase(word).stream().map(tag -> tag.getId()).toList();
            Set<Integer> recipeSet = new HashSet<Integer>();
            for (int j = 0; j < tag_ids.size(); j++){
                int tag_id = tag_ids.get(i);
                List<Integer> t_rec = recipe_tagsRepository.findByTag(tag_id).stream().map(r -> r.getRecipe()).toList();
                recipeSet.addAll(t_rec);
            }

            List<Integer> recipes = recipeSet.stream().toList();
            for (int j = 0; j < recipes.size(); j++){
                int rid = recipes.get(j);
                if (!final_list.containsKey(rid)){
                    final_list.put(rid, new HashSet<String>());
                }
                final_list.get(rid).add(word);
            }
        }
        List<Pair<Integer, Integer>> recipeMatches = new ArrayList<Pair<Integer, Integer>>();
        recipeMatches.addAll(final_list.entrySet().stream().map(r -> Pair.of(r.getKey(), r.getValue().size())).toList());
        Collections.sort(recipeMatches, Comparator.comparing(r -> -r.getSecond()));
        return recipeMatches;
    }

    private List<Pair<Integer, Integer>> searchEquipment(List<String> words){
        HashMap<Integer, Set<String>> final_list = new HashMap<Integer, Set<String>>();
        for (int i = 0; i < words.size(); i++){
            String word = words.get(i);
            List<Integer> equip_ids = equipmentsRepository.findByNameContainingIgnoreCase(word).stream().map(equip -> equip.getId()).toList();
            Set<Integer> recipeSet = new HashSet<Integer>();
            for (int j = 0; j < equip_ids.size(); j++){
                int tag_id = equip_ids.get(i);
                List<Integer> t_rec = recipe_equipmentsRepository.findByEquipment(tag_id).stream().map(r -> r.getRecipe()).toList();
                recipeSet.addAll(t_rec);
            }

            List<Integer> recipes = recipeSet.stream().toList();
            for (int j = 0; j < recipes.size(); j++){
                int rid = recipes.get(j);
                if (!final_list.containsKey(rid)){
                    final_list.put(rid, new HashSet<String>());
                }
                final_list.get(rid).add(word);
            }
        }
        List<Pair<Integer, Integer>> recipeMatches = new ArrayList<Pair<Integer, Integer>>();
        recipeMatches.addAll(final_list.entrySet().stream().map(r -> Pair.of(r.getKey(), r.getValue().size())).toList());
        Collections.sort(recipeMatches, Comparator.comparing(r -> -r.getSecond()));
        return recipeMatches;
    }

    private List<Pair<Integer, Integer>> searchUsers(List<String> words){
        HashMap<Integer, Set<String>> final_list = new HashMap<Integer, Set<String>>();
        for (int i = 0; i < words.size(); i++){
            String word = words.get(i);
            List<Integer> users = userRepository.findByUsernameContainingIgnoreCase(word).stream().map(u -> u.getId()).toList();
            Set<Integer> recipeSet = new HashSet<Integer>();
            for (int j = 0; j < users.size(); j++){
                int uid = users.get(i);
                List<Integer> t_rec = recipesRepository.findByAuthor(uid).stream().map(r -> r.getId()).toList();
                recipeSet.addAll(t_rec);
            }

            List<Integer> recipes = recipeSet.stream().toList();
            for (int j = 0; j < recipes.size(); j++){
                int mid = recipes.get(j);
                if (!final_list.containsKey(mid)){
                    final_list.put(mid, new HashSet<String>());
                }
                final_list.get(mid).add(word);
            }
        }
        List<Pair<Integer, Integer>> recipeMatches = new ArrayList<Pair<Integer, Integer>>();
        recipeMatches.addAll(final_list.entrySet().stream().map(r -> Pair.of(r.getKey(), r.getValue().size())).toList());
        Collections.sort(recipeMatches, Comparator.comparing(r -> -r.getSecond()));
        return recipeMatches;
    }


    private List<Pair<Integer, Integer>> searchTitles(List<String> words){
        HashMap<Integer, Set<String>> final_list = new HashMap<Integer, Set<String>>();
        for (int i = 0; i < words.size(); i++){
            String word = words.get(i);
            List<Integer> recipes = recipesRepository.findByNameContainingIgnoreCase(word).stream().map(r -> r.getId()).toList();

            for (int j = 0; j < recipes.size(); j++){
                int mid = recipes.get(j);
                if (!final_list.containsKey(mid)){
                    final_list.put(mid, new HashSet<String>());
                }
                final_list.get(mid).add(word);
            }
        }
        List<Pair<Integer, Integer>> recipeMatches = new ArrayList<Pair<Integer, Integer>>();
        recipeMatches.addAll(final_list.entrySet().stream().map(r -> Pair.of(r.getKey(), r.getValue().size())).toList());
        Collections.sort(recipeMatches, Comparator.comparing(r -> -r.getSecond()));
        return recipeMatches;
    }

    private List<Pair<Integer, Integer>> searchMealType(List<String> words){
        HashMap<Integer, Set<String>> final_list = new HashMap<Integer, Set<String>>();
        for (int i = 0; i < words.size(); i++){
            String word = words.get(i);
            List<Integer> recipes = recipesRepository.findByMealType(word).stream().map(r -> r.getId()).toList();

            for (int j = 0; j < recipes.size(); j++){
                int mid = recipes.get(j);
                if (!final_list.containsKey(mid)){
                    final_list.put(mid, new HashSet<String>());
                }
                final_list.get(mid).add(word);
            }
        }
        List<Pair<Integer, Integer>> recipeMatches = new ArrayList<Pair<Integer, Integer>>();
        recipeMatches.addAll(final_list.entrySet().stream().map(r -> Pair.of(r.getKey(), r.getValue().size())).toList());
        Collections.sort(recipeMatches, Comparator.comparing(r -> -r.getSecond()));
        return recipeMatches;
    }
}