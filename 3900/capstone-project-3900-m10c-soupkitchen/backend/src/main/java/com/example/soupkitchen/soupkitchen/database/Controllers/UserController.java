package com.example.soupkitchen.soupkitchen.database.Controllers;

import com.example.soupkitchen.soupkitchen.database.Database.*;
import com.example.soupkitchen.soupkitchen.database.Files.FileUploadUtil;

// import java.io.File;
// import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

// import org.apache.commons.io.FileCleaner;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.springframework.core.io.InputStreamResource;
// import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.StringUtils;


@RestController
public class UserController extends MainController {

    // private static final Logger logger = LoggerFactory.getLogger(FileCleaner.class);

    @CrossOrigin(origins = "*")
    @PostMapping(value = "/users/info", consumes = {"application/json"})
    public ResponseEntity<String> userInfo(@RequestBody String body) {
        try {
            // get info from body
            JSONObject body_json = new JSONObject(body);
            List<Integer> uids = new ArrayList<Integer>();
            if (body_json.has("user_ids")) {
                body_json.getJSONArray("user_ids").forEach(
                    uid -> uids.add(Integer.parseInt(uid.toString()))
                );
            }
            
            // get user
            if (body_json.has("token")) {
                int uid = auth_tokensRepository.findByToken(body_json.getString("token")).get(0).getUser();
                uids.add(uid);
            }

            String return_body = "{\"users\":[";
            
            for (int uid: uids) {
                users main = userRepository.findById(uid).get(0);
                List<recipes> rList = recipesRepository.findByAuthor(main.getId());
                List<cookbooks> cList = cookbooksRepository.findByCreator(main.getId());
                List<cookbook_subscribers> cList2 = cookbook_subscribersRepository.findByFollower(main.getId());
                List<user_followers> ufList = user_followersRepository.findByUser(main.getId());
                List<user_followers> fuList = user_followersRepository.findByFollower(main.getId());

                String recipes = rList.stream().map(r -> "\"" + String.valueOf(r.getId()) + "\"").collect(Collectors.joining(",", "[", "]"));
                String cookbooks = cList.stream().map(r -> "\"" + String.valueOf(r.getId()) + "\"").collect(Collectors.joining(",", "[", "]"));
                String cookbooks2 = cList2.stream().map(r -> "\"" + String.valueOf(r.getCookbook()) + "\"").collect(Collectors.joining(",", "[", "]"));
                String followers = ufList.stream().map(uf -> "\"" + String.valueOf(uf.getFollower()) + "\"").collect(Collectors.joining(",", "[", "]"));
                String following = fuList.stream().map(uf -> "\"" + String.valueOf(uf.getUser()) + "\"").collect(Collectors.joining(",", "[", "]"));

                return_body = return_body + String.format(
                    "{" +
                    "\"id\":%d," +
                    "\"username\":\"%s\"," +
                    "\"email\":\"%s\"," +
                    // "\"profile_base64\":\"%s\"," +
                    "\"recipe_ids\":%s," +
                    "\"cookbook_ids\":%s," +
                    "\"subscribed_ids\":%s," +
                    "\"follower_ids\":%s," +
                    "\"following_ids\":%s" +
                    "}",
                    main.getId(),
                    main.getUsername(),
                    main.getEmail(),
                    // main.getProfile_base64(),
                    recipes,
                    cookbooks,
                    cookbooks2,
                    followers,
                    following
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
    @PostMapping(value = "/users/follow", consumes = {"application/json"})
    public ResponseEntity<String> userFollow(@RequestBody String body) {
        try {
            // get info from body
            JSONObject body_json = new JSONObject(body);

            // get user
            int uid = auth_tokensRepository.findByToken(body_json.getString("token")).get(0).getUser();

            // save user_follower
            user_followersRepository.save(
                new user_followers(
                    body_json.getInt("user_id"),
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
    @PostMapping(value = "/users/unfollow", consumes = {"application/json"})
    public ResponseEntity<String> userUnfollow(@RequestBody String body) {
        try {
            // get info from body
            JSONObject body_json = new JSONObject(body);

            // get user
            int uid = auth_tokensRepository.findByToken(body_json.getString("token")).get(0).getUser();

            // save user_follower
            user_followersRepository.deleteByFollowerAndUser(
                body_json.getInt("user_id"),
                uid
            );
            
            return createResponseEntityOk();

        } catch (Exception e) {
            return createResponseEntityFail();
        }
    }

    @CrossOrigin(origins = "*")
    @GetMapping(value = "/users/image/get/{image_name}", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getImageWithMediaType(@PathVariable String image_name) throws IOException {
        try {
            InputStream in = getClass().getResourceAsStream("/com/example/soupkitchen/soupkitchen/photos/users/" + image_name + ".jpeg");
            InputStream in2 = getClass().getResourceAsStream("/com/example/soupkitchen/soupkitchen/photos/users/" + image_name + ".jpg");
            InputStream in3 = getClass().getResourceAsStream("/com/example/soupkitchen/soupkitchen/photos/users/" + image_name + ".png");
            
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
    @PostMapping("/users/image/add")
    public ResponseEntity<String> userImageAdd(@RequestParam("image") MultipartFile multipartFile, @RequestParam("token") String token) throws IOException {
        try {
            // get user
            int uid = auth_tokensRepository.findByToken(token).get(0).getUser();

            String fileType = StringUtils.cleanPath(multipartFile.getContentType());
            String uploadDir = "/com/example/soupkitchen/soupkitchen/photos/users/";
            FileUploadUtil.saveFile(uploadDir, Integer.toString(uid) + fileType, multipartFile);
            return createResponseEntityOk();

        } catch (Exception e) {
            return createResponseEntityFail();
        }
    }

    // @CrossOrigin(origins = "*")
    // @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    // public ResponseEntity uploadFile(@RequestParam MultipartFile[] files) {
    //     for (int i = 0; i < files.length; i++) {
    //         logger.info(String.format("File name '%s' uploaded successfully.", files[i].getOriginalFilename()));
    //     }
    //     return ResponseEntity.ok().build();
    // }

    // @CrossOrigin(origins = "*")
    // @RequestMapping("/download")
    // public ResponseEntity downloadFile1(@RequestParam String fileName) throws IOException {
        
    //     File file = new File(fileName);
    //     InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

    //     return ResponseEntity.ok()
    //         .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file.getName())
    //         .contentType(MediaType.APPLICATION_OCTET_STREAM)
    //         .contentLength(file.length()) 
    //         .body(resource);
    // }
}