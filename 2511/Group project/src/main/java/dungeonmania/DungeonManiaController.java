package dungeonmania;

import dungeonmania.components.Location;
import dungeonmania.components.Stats;
import dungeonmania.components.state.MercenaryState;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.systems.AllySystem;
import dungeonmania.systems.BattleSystem;
import dungeonmania.systems.Build;
import dungeonmania.systems.GoalChecker;
import dungeonmania.systems.MoveSystem;
import dungeonmania.systems.Response;
import dungeonmania.systems.Tick;
import dungeonmania.systems.UseSystem;
import dungeonmania.util.Direction;
import dungeonmania.util.FileLoader;

import java.io.IOException;
import java.io.InvalidClassException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.json.JSONException;

public class DungeonManiaController {
    private Dungeon dungeon;


    public String getSkin() {
        return "default";
    }

    public String getLocalisation() {
        return "en_US";
    }

    /**
     * /dungeons
     */
    public static List<String> dungeons() {
        return FileLoader.listFileNamesInResourceDirectory("dungeons");
    }

    /**
     * /configs
     */
    public static List<String> configs() {
        return FileLoader.listFileNamesInResourceDirectory("configs");
    }

    /**
     * /game/new
     */
    public DungeonResponse newGame(String dungeonName, String configName) throws IllegalArgumentException {
        if (!dungeons().contains(dungeonName)) {
            throw new IllegalArgumentException("Dungeon Doesn't Exist");
        }
        if (!configs().contains(configName)) {
            throw new IllegalArgumentException("Config Doesn't Exist");
        }

        dungeon = new Dungeon(dungeonName, configName);
        return getDungeonResponseModel();
    }

    /**
     * /game/dungeonResponseModel
     */
    public DungeonResponse getDungeonResponseModel() {
        return Response.getDungeonResponse(dungeon);
    }

    /**
     * /game/tick/item
     */
    public DungeonResponse tick(String itemUsedId) throws IllegalArgumentException, InvalidActionException {
        Entity item = null;
        for (Entity entity : dungeon.getInventory().getInventory()) {
            if (entity.getId().equals(itemUsedId)) {
                item = entity;
                break;
            }
        }
        if (item == null) {
            throw new InvalidActionException("Item Not Found");
        }
        if (!(item.getType().equals("bomb") || item.getType().equals("invincibility_potion") || item.getType().equals("invisibility_potion"))) {
            throw new IllegalArgumentException("Item not Usable");
        }
        UseSystem uSystem = new UseSystem(dungeon, item);
        uSystem.Use();
        Tick tick = new Tick(dungeon);
        tick.Update();

        return getDungeonResponseModel();
    }

    /**
     * /game/tick/movement
     */
    public DungeonResponse tick(Direction movementDirection) {
        MoveSystem mSystem = new MoveSystem(dungeon);
        mSystem.movePlayer(movementDirection);

        Tick tick = new Tick(dungeon);
        tick.Update();

        return getDungeonResponseModel();
    }

    /**
     * /game/build
     */
    public DungeonResponse build(String buildable) throws IllegalArgumentException, InvalidActionException {
        Build build = new Build(dungeon);
        GoalChecker goals = new GoalChecker(dungeon);
        if (!buildable.equals("bow") && !buildable.equals("shield") && !buildable.equals("sceptre") && !buildable.equals("midnight_armour")) {
            throw new IllegalArgumentException("Not A Valid Item");
        }
        else if (!(build.update().contains(buildable))) {
            throw new InvalidActionException("Insufficient Items to Craft");
        }
        else if (buildable.equals("midnight_armour") && goals.isZombies()) {
            throw new InvalidActionException("Zombies in dungeon");
        }
        else {
            build.build(buildable);
        }
        return getDungeonResponseModel();

    }

    /**
     * /game/interact
     */
    public DungeonResponse interact(String entityId) throws IllegalArgumentException, InvalidActionException {
        
        Entity e = dungeon.findEntity(entityId);
        
        if (e == null) {
            throw new IllegalArgumentException("No entity found with matching ID");
        }
        else if (e.getType().equals("zombie_toast_spawner")) {
            Entity player = dungeon.getPlayer();
            int playerX = player.getComponent(Location.class).getX();
            int playerY = player.getComponent(Location.class).getY();
            int entityX = e.getComponent(Location.class).getX();
            int entityY = e.getComponent(Location.class).getY();
            if (!(playerX == entityX && (playerY == entityY - 1 || playerY == entityY + 1))) {
                if (!(playerY == entityY && (playerX == entityX - 1 || playerX == entityX + 1))) {
                    throw new InvalidActionException("player must be cardinally adjacent to spawner");
                }
            }
            
            Inventory inventory = dungeon.getInventory();
            if (inventory.countItem("bow") == 0 && inventory.countItem("sword") == 0) {
                throw new InvalidActionException("player must have weapon to destroy spawner");
            }
    
            dungeon.removeEntity(e);
        }
        else if (e.getType().equals("mercenary")) {
            AllySystem aS = new AllySystem(dungeon);
            aS.bribeMercenary(e);
        }
        else if (e.getType().equals("assassin")) {
            AllySystem aS = new AllySystem(dungeon);
            aS.bribeAssassin(e);
        }
        else {
            throw new IllegalArgumentException("Entity is not ZTS, mercenary or assassin");
        }

        return getDungeonResponseModel();
    }

    /**
     * /game/save
     */
    public DungeonResponse saveGame(String name) throws IllegalArgumentException {
        dungeon.saveGame();
        return null;
    }

    /**
     * /game/load
     */
    public DungeonResponse loadGame(String name) throws IllegalArgumentException {
        return null;
    }

    /**
     * /games/all
     */
    public List<String> allGames() {
        return new ArrayList<>();
    }

}
