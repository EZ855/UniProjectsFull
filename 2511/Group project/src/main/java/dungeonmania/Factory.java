package dungeonmania;

import dungeonmania.components.Location;
import dungeonmania.components.Power;
import dungeonmania.components.Spider;
import dungeonmania.components.Stats;
import dungeonmania.components.state.MercenaryStateEnemy;
import dungeonmania.util.Position;
import dungeonmania.components.ItemStat;
import dungeonmania.components.Key;

public class Factory {
    private Config config;

    public Factory(Config config) {
        this.config = config;
    }

    //ENTITIES WHICH ARE CRAFTED
    public Entity createEntity(String type) {
        Entity entity = new Entity(type);
        switch (type) {
            case "shield":
                entity.addComponent(new ItemStat(type, config));
                return entity;
            case "bow":
                entity.addComponent(new ItemStat(type, config));
                return entity;
            case "midnight_armour":
                entity.addComponent(new ItemStat(type, config));
                return entity;
            case "sceptre":
                entity.addComponent(new ItemStat(type, config));
                return entity;
            default:
                return entity;
        }
    }

    // ENTITIES
    public Entity createEntity(String type, int x, int y) {
        Entity entity = new Entity(type);
        switch (type) {
            case "player":
                entity.addComponent(new Location(x,y,3));
                entity.addComponent(new Stats(type, config));
                return entity;
            case "spider":
                entity.addComponent(new Location(x,y,2));
                entity.addComponent(new Stats(type, config));
                entity.addComponent(new Spider(new Position(x, y)));
                entity.addTags("Enemy");
                return entity;
            case "zombie_toast":
                entity.addComponent(new Location(x,y,2));
                entity.addComponent(new Stats(type, config));
                entity.addTags("Enemy");
                return entity;
            case "mercenary":
                entity.addComponent(new Location(x,y,2));
                entity.addComponent(new Stats(type, config));
                entity.addComponent(new MercenaryStateEnemy(entity));
                entity.addTags("Enemy", "Interactable");
                return entity;
            case "wall":
                entity.addComponent(new Location(x,y));
                entity.addTags("Collidable");
                return entity;
            case "boulder":
                entity.addComponent(new Location(x,y,1));
                entity.addTags("Collidable", "Pushable");
                return entity;
            case "switch":
                entity.addComponent(new Location(x,y,1));
                entity.addComponent(new Power());
                return entity;
            case "treasure":
                entity.addComponent(new Location(x, y));
                entity.addTags("Collectable", "Treasure");
                return entity;
            case "invincibility_potion":
                entity.addComponent(new Location(x, y));
                entity.addComponent(new ItemStat(type, config));
                entity.addTags("Collectable", "Potion");
                return entity;
            case "invisibility_potion":
                entity.addComponent(new Location(x, y));
                entity.addComponent(new ItemStat(type, config));
                entity.addTags("Collectable", "Potion");
                return entity;
            case "wood":
                entity.addComponent(new Location(x, y));
                entity.addTags("Collectable");
                return entity;  
            case "arrow":
                entity.addComponent(new Location(x, y));
                entity.addTags("Collectable");
                return entity;  
            case "bomb":
                entity.addComponent(new Location(x, y));
                entity.addTags("Collectable", "Explosive");
                return entity; 
            case "sword":
                entity.addComponent(new Location(x, y));
                entity.addComponent(new ItemStat(type, config));
                entity.addTags("Collectable");
                return entity;
            case "sun_stone":
                entity.addComponent(new Location(x, y));
                entity.addTags("Collectable", "Treasure");
                return entity;
            case "zombie_toast_spawner": 
                entity.addComponent(new Location(x, y));
                entity.addTags("Collidable", "Interactable");
                return entity;
            case "hydra":
                entity.addComponent(new Location(x, y, 2));
                entity.addComponent(new Stats(type, config));
                entity.addTags("Enemy");
                return entity;
            case "assassin":
                entity.addComponent(new Location(x, y, 2));
                entity.addComponent(new Stats(type, config));
                entity.addComponent(new MercenaryStateEnemy(entity));
                entity.addTags("Enemy", "Interactable");
                return entity;        
            default:
                entity.addComponent(new Location(x,y));
                return entity;

        }
    }

    // ENTITIES WITH KEY PAIRS
    public Entity createEntity(String type, int x, int y, int key) {
        Entity entity = new Entity(type);
        switch (type) {
            case "door":
                entity.addComponent(new Location(x, y));
                entity.addComponent(new Key(key));
                entity.addTags("Collidable", "Unlockable");
                return entity;
            case "key":
                entity.addComponent(new Location(x, y));
                entity.addComponent(new Key(key));
                entity.addTags("Collectable");
                return entity;
            default:    
                entity.addComponent(new Location(x,y));
                return entity;

        }
    }

    public Entity createEntity(String type, int x, int y, String key) {
        Entity entity = new Entity(type + "_" + key.toLowerCase());
        System.out.println(type + "_" + key.toLowerCase());
        switch (type) {
            case "portal":
                entity.addComponent(new Location(x,y));
                entity.addComponent(new Key(key));
                entity.addTags("Collidable", "Portal");
                return entity;
            default:    
                entity.addComponent(new Location(x,y));
                return entity;

        }
    }

    // public static Entity createEntity(String type) {
    //     Entity entity = new Entity(type);
        

    //     switch (type) {        
    //         default:
    //             return entity;

    //     }
    // }
}
