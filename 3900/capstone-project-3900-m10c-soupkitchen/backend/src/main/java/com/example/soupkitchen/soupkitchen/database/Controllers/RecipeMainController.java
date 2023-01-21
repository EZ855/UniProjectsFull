package com.example.soupkitchen.soupkitchen.database.Controllers;

import com.example.soupkitchen.soupkitchen.database.Database.*;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

public class RecipeMainController extends MainController {
    protected static void saveFile(String dir, String filename, String image) throws IOException {
        Path uploadPath = Paths.get(dir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        try (InputStream inputStream = new ByteArrayInputStream(image.getBytes())) {
            Path filePath = uploadPath.resolve(filename);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ioe) {
            throw new IOException("Could not save image file: " + filename, ioe);
        }
    }
    
    protected void saveIngredients(JSONArray ingredients, int recipe_id){
        for (int i = 0;  i < ingredients.length(); i++) {            
            JSONObject ingred = ingredients.getJSONObject(i);
            String name = ingred.getString("name");
            // System.out.println(name);
            // add if required
            List<ingredients> temp_l = ingredientsRepository.findByName(name);
            int ingredient_id = -1;

            if (temp_l.size() == 0){
                ingredients res = ingredientsRepository.save(new ingredients(name));
                ingredient_id = res.getId();
            } else {
                ingredient_id = temp_l.get(0).getId();
            }
            
            recipe_ingredientsRepository.save(
                new recipe_ingredients(recipe_id,
                    ingredient_id,
                    ingred.getInt("quantity"),
                    ingred.getString("units")
                )
            );
            
        }
    }

    protected void saveEquipment(JSONArray equipment, int recipe_id){
        for (int i = 0;  i < equipment.length(); i++){
            String name = equipment.getString(i);

            // add if required
            List<equipments> temp_l = equipmentsRepository.findByName(name);
            int equipment_id = -1;

            if (temp_l.size() == 0){
                equipments res = equipmentsRepository.save(new equipments(name));
                equipment_id = res.getId();
            } else {
                equipment_id = temp_l.get(0).getId();
            }

            recipe_equipmentsRepository.save(
                new recipe_equipments(
                    recipe_id,
                    equipment_id)
            );
        }
    }

    protected void saveTag(JSONArray s_tag, int recipe_id){
        for (int i = 0;  i < s_tag.length(); i++){
            String name = s_tag.getString(i);

            // add if required
            int tag_id = -1;
            if (!tagsRepository.existsByName(name)){
                tags res = tagsRepository.save(new tags(name));
                tag_id = res.getId();
            } else {
                tag_id = tagsRepository.findByName(name).get(0).getId();
            }

            recipe_tagsRepository.save(
                new recipe_tags(
                    recipe_id,tag_id
                )
            );
        }
    }

    protected byte[] loadFile(String location) {
        try {
            Path file = Paths.get(location);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return Files.readAllBytes(file);
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
