package dungeonmania.systems;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.Battle;
import dungeonmania.Dungeon;
import dungeonmania.Entity;
import dungeonmania.components.Location;
import dungeonmania.response.models.BattleResponse;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Position;

public class Response {

    public static EntityResponse getEntityInfoResponse(Entity entity) {
        EntityResponse response = new EntityResponse(
            entity.getId(), 
            entity.getType(), 
            new Position(entity.getComponent(Location.class).getX(), entity.getComponent(Location.class).getY(), entity.getComponent(Location.class).getZ()), 
            (entity.getTags().contains("Interactable")));
        return response;
    }

    public static List<EntityResponse> getEntityResponses(List<Entity> entities) {
        List<EntityResponse> response = new ArrayList<>();
        for (Entity entity : entities) {
            response.add(Response.getEntityInfoResponse(entity));
        }
        return response;
    }

    public static ItemResponse getItemInfoResponse(Entity entity) {
        ItemResponse response = new ItemResponse(
            entity.getId(), 
            entity.getType());
        return response;
    }

    public static List<ItemResponse> getItemResponses(List<Entity> entities) {
        List<ItemResponse> response = new ArrayList<>();
        for (Entity entity : entities) {
            response.add(Response.getItemInfoResponse(entity));
        }
        return response;
    }

    public static List<BattleResponse> getBattleResponse(Dungeon dungeon) {
        List<BattleResponse> response = new ArrayList<>();
        for (Battle battle : dungeon.getBattles()) {
            response.add(battle.getBattleResponse());
        }
        return response;
    }


    public static DungeonResponse getDungeonResponse(Dungeon dungeon) {
        DungeonResponse response = new DungeonResponse(
            dungeon.getDungeonId(), 
            dungeon.getDungeonName(),  
            getEntityResponses(dungeon.getEntities()), 
            getItemResponses(dungeon.getInventory().getInventory()),
            getBattleResponse(dungeon),
            new Build(dungeon).update(), //ADD BUILDABLE RESPONSE
            dungeon.getGoals());
        return response;
    }

    

    
}
