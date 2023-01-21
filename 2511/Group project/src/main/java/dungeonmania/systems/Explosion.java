package dungeonmania.systems;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.eclipse.jetty.server.LocalConnector;

import dungeonmania.Dungeon;
import dungeonmania.Entity;
import dungeonmania.Factory;
import dungeonmania.components.Location;
import dungeonmania.util.Position;

public class Explosion {
    
    Dungeon dungeon;
    int radius;
    Entity entity;
    public Explosion(Entity entity, Dungeon dungeon) {
        this.dungeon = dungeon;
        this.entity = entity;
        this.radius = dungeon.getConfig().bomb_radius;
    }

    public void explode() {
        int current = radius;
        current--;
        
        for (Position position : entity.getComponent(Location.class).getPosition().getAdjacentPositions()) {
            if ((current) > 0) {
                recursiveExplode(position, current);
            }
            removeEntity(position);
        }
        removeEntity(entity.getComponent(Location.class).getPosition());
    }

    public void recursiveExplode(Position center, int current) {
        current--;
        for (Position position : center.getAdjacentPositions()) {
            if ((current) > 0) {
                recursiveExplode(position, current);
            }
            removeEntity(position);
        }
    }


    public void removeEntity(Position position) {
        Iterator<Entity> itr = dungeon.getEntities().listIterator();
        while (itr.hasNext()) {
            Entity check = itr.next();
            if (check.getComponent(Location.class).getPosition().getX() == position.getX() && check.getComponent(Location.class).getPosition().getY() == position.getY() && !check.getType().equals("player")) {
                itr.remove();
            }
        }
    }
}
