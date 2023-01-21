package dungeonmania;

import dungeonmania.components.Location;

public class Helper {
    public static boolean sharePos(Entity entity, Entity entity2) {
        if (entity.getComponent(Location.class).getX() == entity2.getComponent(Location.class).getX() && entity.getComponent(Location.class).getY() == entity2.getComponent(Location.class).getY()) {
            return true;
        }
        return false;
    }
}
