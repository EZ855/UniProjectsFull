package dungeonmania.systems;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.Dungeon;
import dungeonmania.Entity;
import dungeonmania.Factory;
import dungeonmania.Inventory;
import dungeonmania.components.Location;
import dungeonmania.components.Power;
import dungeonmania.components.Stats;
import dungeonmania.util.Position;

public class Tick {

    Dungeon dungeon;
    Entity player = null;

    public Tick(Dungeon dungeon) {
        this.dungeon = dungeon;

        for (Entity entity : dungeon.getEntities()) {
            if (entity.getType().equals("player")) {
                player = entity;
            }
        }
    }

    public void Update() {
        dungeon.getGoalsRaw().refreshCompleted();
        //INTERACT WITH OBJECTS PLAYER STANDING ON
        TileInteraction interaction = new TileInteraction(dungeon, player);
        interaction.Check();

        preUpdateTiles();
        postUpdateTiles();
        BattleSystem battleSystem = new BattleSystem(dungeon);
        battleSystem.initiateBattle();
        if (dungeon.getPlayer() == null) {
            return;
        }
        updateEnemeies();
        battleSystem.initiateBattle();
        if (dungeon.getPlayer() == null) {
            return;
        }
        battleSystem.updateCurrentPotionEffects();

        // Spawn Entities that Spawn
        spawn();

        //REMOVE ENTITIES FROM MAP IF IN INVENTORY
        for (Entity entity : dungeon.getInventory().getInventory()) {
            dungeon.removeEntity(entity);
        }
        GoalChecker goals = new GoalChecker(dungeon);
        goals.checkGoals();
        dungeon.Tick++;
    }

    public void updateEnemeies() {
        MoveSystem move = new MoveSystem(dungeon);
        for (Entity entity : dungeon.getEntities()) {
            if (entity.getTags().contains("Enemy")) {
                move.moveEnemy(entity);
            }
        }
    }

    public void preUpdateTiles() {
        for (Entity entity : dungeon.getEntities()) {
            if (entity.getType().equals("switch")) {
                Collision collision = new Collision(dungeon);
                Entity obstacle;
                if ((obstacle = collision.isColision(entity, entity.getComponent(Location.class).getPosition())) != null) {
                    if (obstacle.getType().equals("boulder")) {
                        entity.getComponent(Power.class).setState(true);
                    }
                    else {
                        entity.getComponent(Power.class).setState(false);
                    }
                }
                else {
                    entity.getComponent(Power.class).setState(false);
                }
            }
        }
    }

    public void postUpdateTiles() {
        List<Entity> explode = new ArrayList<>();
        for (Entity entity : dungeon.getEntities()) {
            if (entity.getTags().contains("Explosive")) {
                if (isPowered(entity)) {
                    explode.add(entity);
                }
            }
        }
        for (Entity entity : explode) {
            Explosion boom = new Explosion(entity, dungeon);
            boom.explode();
        }
        

    }

    public Boolean isPowered(Entity entity) {
        for (Entity entity2 : dungeon.getEntities()) {
            if (entity2.getComponent(Power.class) != null) {
                if (entity2.getComponent(Power.class).state()) {
                    if (Position.isAdjacent(entity.getComponent(Location.class).getPosition(), entity2.getComponent(Location.class).getPosition())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    public boolean avaliableEdges(Entity entity) {
        int count = 0;
        for (Position position : entity.getComponent(Location.class).getPosition().getAdjacentPositions()) {
            for (Entity e : dungeon.getEntities()) {
                if (e.getTags().contains("Collidable") && e.getComponent(Location.class).getPosition().getX() == position.getX() && e.getComponent(Location.class).getPosition().getY() == position.getY()){
                    count++;
                    break;
                }
            }
        }
        if (count >= 8) {
            return false;
        }
        else {
            return true;
        }
    }

    public void spawn() {
        if (dungeon.getConfig().spider_spawn_rate > 0) {
            if (dungeon.Tick % dungeon.getConfig().spider_spawn_rate == 0) {
                Factory factory = new Factory(dungeon.getConfig());
                dungeon.addEntity(factory.createEntity(
                    "spider", 
                    ((player.getComponent(Location.class).getX()-4) + (int)(Math.random() * (((player.getComponent(Location.class).getX()+4) - (player.getComponent(Location.class).getX()-4)) + 1))),
                    ((player.getComponent(Location.class).getY()-4) + (int)(Math.random() * (((player.getComponent(Location.class).getY()+4) - (player.getComponent(Location.class).getY()-4)) + 1)))
                    ));
            }
        }
        Position zombie = null;
        for (Entity entity : dungeon.getEntities()) {
            if (entity.getType().equals("zombie_toast_spawner")) {
                if (dungeon.getConfig().zombie_spawn_rate > 0) {
                    if (dungeon.Tick % dungeon.getConfig().zombie_spawn_rate == 0) {
                        List<Position> positions = entity.getComponent(Location.class).getPosition().getAdjacentPositions();
                        zombie = positions.get(((int)(Math.random()*positions.size())));
                        
                    }
                }
            }
        }
        if (zombie != null) {
            Factory factory = new Factory(dungeon.getConfig());
            dungeon.addEntity(factory.createEntity("zombie_toast", zombie.getX(), zombie.getY()));
        }
    }
}
