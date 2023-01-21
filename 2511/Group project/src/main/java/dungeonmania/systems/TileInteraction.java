package dungeonmania.systems;

import java.util.List;

import dungeonmania.Dungeon;
import dungeonmania.Entity;
import dungeonmania.Inventory;
import dungeonmania.components.Location;

public class TileInteraction {
    
    Dungeon dungeon;
    Entity entity;

    public TileInteraction(Dungeon dungeon, Entity entity) {
        this.dungeon = dungeon;
        this.entity = entity;
    }
    public void Check() {
        for (Entity entity2 : dungeon.getEntities()) {
            if (entity.getComponent(Location.class).getX() == entity2.getComponent(Location.class).getX() &&
            entity.getComponent(Location.class).getY() == entity2.getComponent(Location.class).getY()) {
                if (entity2.getTags().contains("Collectable")) {
                    if ((entity2.getType().equals("key") && dungeon.getInventory().containsType("key") != null)) {
                        
                    }
                    else {
                        dungeon.getInventory().addItem(entity2);
                    }
                }
            }
        }
    }
}
