package dungeonmania.systems;

import java.util.List;

import dungeonmania.Dungeon;
import dungeonmania.Entity;
import dungeonmania.Inventory;
import dungeonmania.components.Key;
import dungeonmania.components.Location;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class Collision {

    List<Entity> entities;
    Dungeon dungeon;
    MoveSystem move;
    
    public Collision(Dungeon dungeon) {
        this.entities = dungeon.getEntities();
        this.dungeon = dungeon;
        move = new MoveSystem(dungeon);
    }
    
    public Entity isColision(Entity entity, Position position) {
        for(Entity obstacle : entities) {
            if (obstacle.getTags().contains("Collidable")) {
                if (isSharePosition(position, obstacle)) {
                    return obstacle;
                }
            }
        }
        return null;
    }

    public static boolean isSharePosition(Position position, Entity entity2) {
        if ((position.getX() == entity2.getComponent(Location.class).getX()) &&
            (position.getY() == entity2.getComponent(Location.class).getY())) {
                return true;
            }
        return false;
    }

    public void collisionAction(Entity player, Direction direction, Entity entity) {
        //COLLISION WITH BOULDER
        if (entity.getTags().contains("Pushable")) {
            if (move.moveEntity(entity, direction)) {
                move.move(player, new Position(player.getComponent(Location.class).getX() + direction.getOffset().getX(), player.getComponent(Location.class).getY() + direction.getOffset().getY()));
                return;
            }
        }
        //COLLISION WITH DOOR
        if (entity.getTags().contains("Unlockable")) {
            if (unlockDoor(entity)) {
                move.movePlayer(direction);
            }
            return;
            
        }
        //COLLISION WITH PORTAL
        if (entity.getTags().contains("Portal")) {
            move.move(player, teleport(entity));
            move.movePlayer(direction);
            return;
        }
    }

    public boolean unlockDoor(Entity entity) {
        Entity toRemove = null;
        for (Entity item : dungeon.getInventory().getInventory()) {
            if (item.getComponent(Key.class) != null) {
                if (entity.getComponent(Key.class).getKey() == item.getComponent(Key.class).getKey()) {
                    toRemove = item;
                    entity.removeTag("Unlockable");
                    entity.removeTag("Collidable");
                    entity.setType("door_open");
                    
                    break;
                }
            }
        }
        if (toRemove == null) {
            return false;
        }
        else {
            dungeon.getInventory().removeItem(toRemove);
            return true;
        }
    }

    public Position teleport(Entity entity) {
        Entity To = entity;
        for (Entity portal: dungeon.getEntities()) {
            if (portal.getTags().contains("Portal")) {
                if (portal.getComponent(Key.class).getKeyString().equals(entity.getComponent(Key.class).getKeyString()) && portal != entity) {
                    To = portal;
                }
            }
        }
        return (new Position(To.getComponent(Location.class).getX(), To.getComponent(Location.class).getY()));
    }
    
}
