package dungeonmania;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import dungeonmania.components.Location;
import dungeonmania.response.models.BattleResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.systems.Response;
import dungeonmania.util.FileLoader;

public class Dungeon {

    public int Tick = 1;
    private String dungeonId;
    private String dungeonName;
    private Goals goals;
    private List<Entity> entities = new ArrayList<>();
    private Inventory inventory;
    private Config config;
    private Entity player;
    private List<Battle> battles = new ArrayList<>();

    Dungeon(String dungeonName, String configName) {
        try {
            config = new Config(JsonParser.parseString(FileLoader.loadResourceFile(("configs/" + configName + ".json"))).getAsJsonObject());
            JsonObject dungeon = JsonParser.parseString(FileLoader.loadResourceFile(("dungeons/" + dungeonName + ".json"))).getAsJsonObject();
            JsonArray entities = dungeon.getAsJsonArray("entities");
            for (JsonElement element : entities) {
                JsonObject e = element.getAsJsonObject();
                Factory factory = new Factory(config);
                if (e.get("type").getAsString().equals("door") || e.get("type").getAsString().equals("key")) {
                    this.entities.add(factory.createEntity(e.get("type").getAsString(), e.get("x").getAsInt(), e.get("y").getAsInt(),e.get("key").getAsInt()));
                }
                else if (e.get("type").getAsString().equals("portal")) {
                    this.entities.add(factory.createEntity(e.get("type").getAsString(), e.get("x").getAsInt(), e.get("y").getAsInt(),e.get("colour").getAsString()));
                }
                else {
                    this.entities.add(factory.createEntity(e.get("type").getAsString(), e.get("x").getAsInt(), e.get("y").getAsInt()));
                }

                ArrayList<String> g = new ArrayList<String>();
                Goals.FindGoal(dungeon.get("goal-condition").getAsJsonObject() , g);
                goals = new Goals(g);

                
            }

        } catch (Exception IOExcetion) {
            IOExcetion.printStackTrace();
            assert false : "Error Finding File";
        }

        for (Entity entity : entities) {
            if (entity.getType().equals("player")) {
                this.player = entity;
            }
        }


        inventory = new Inventory();
    }

    public Entity getPlayer() {
        return player;
    }

    public Config getConfig() {
        return config;
    }

    public String getDungeonId() {
        return dungeonId;
    }

    public String getDungeonName() {
        return dungeonName;
    }

    public String getGoals() {
        return goals.toString();
    }

    public Goals getGoalsRaw() {
        return goals;
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public List<Battle> getBattles() {
        return battles;
    }

    public void setPlayer(Entity player) {
        this.player = player;
    }

    public void addBattle(Battle battle) {
        battles.add(battle);
    }

    public void removeEntity(Entity entity) {
        if (entities.contains(entity)) {
            entities.remove(entity);
        }
    }

    public void addEntity(Entity entity) {
        entities.add(entity);
    }

    public JsonObject saveGame() {
        JsonObject save = new JsonObject();
        JsonArray entityArray = new JsonArray();
        for (Entity entity : entities) {
            JsonObject entityeObj = new JsonObject();
            entityeObj.addProperty("type", entity.getType());
            entityeObj.addProperty("x", entity.getComponent(Location.class).getX());
            entityeObj.addProperty("y", entity.getComponent(Location.class).getY());
            entityArray.add(entityeObj);
        }
        save.add("entities", entityArray);
        System.out.println(save.toString());
        return save;

    }
    /*
     * finds entity via entityId
     */
    public Entity findEntity(String entityId) {
        for (Entity entity: getEntities()) {
            if (entity.getId().equals(entityId)) {
                return entity;
            }
        }
        return null;
    }
}
