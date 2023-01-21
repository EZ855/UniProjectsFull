package dungeonmania.systems;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import dungeonmania.Dungeon;
import dungeonmania.Entity;
import dungeonmania.components.Location;
import dungeonmania.components.Spider;
import dungeonmania.components.Stats;
import dungeonmania.components.state.MercenaryState;
import dungeonmania.components.Key;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class MoveSystem {

    private List<Entity> entities;
    Dungeon dungeon;
    // Collision collision;

    public MoveSystem(Dungeon dungeon) {
        this.entities = dungeon.getEntities();
        this.dungeon = dungeon;
        // this.collision = new Collision(entities);
    }

    
    public void move(Entity entity, Position position) {
        entity.getComponent(Location.class).move(position);
    }
   

    public void movePlayer(Direction direction) {
        // this.entities = entities;
        for (Entity entity : entities) {
            if (entity.getType().equals("player")) {
                Position newPosition = new Position(entity.getComponent(Location.class).getX()+direction.getOffset().getX(), entity.getComponent(Location.class).getY()+direction.getOffset().getY());
                Collision collision = new Collision(dungeon);
                Entity obstacle ;
                if ((obstacle = collision.isColision(entity, newPosition)) != null) {
                    collision.collisionAction(entity, direction, obstacle);
                }
                else {
                    move(entity, newPosition);
                }
            }
        }
    }

    public boolean moveEntity(Entity entity, Direction direction) {
        Position newPosition = new Position(entity.getComponent(Location.class).getX()+direction.getOffset().getX(), entity.getComponent(Location.class).getY()+direction.getOffset().getY());
        Collision collision = new Collision(dungeon);
        if (collision.isColision(entity, newPosition) != null) {
            return false;
        }
        else {
            move(entity, newPosition);
            return true;
        }
    }

    public void setEntities(List<Entity> entities) {
        this.entities = entities;
    }

    public void moveEnemy(Entity entity) {
        if (entity.getType().equals("spider")) {
            moveSpider(entity);
        } else if (entity.getType().equals("mercenary")) {
            moveMercenary(entity);
            entity.getComponent(MercenaryState.class).move();
        } else if (entity.getType().equals("assassin")) {
            moveAssassin(entity);
            entity.getComponent(MercenaryState.class).move();
        } else if (entity.getType().equals("zombie_toast")) {
            moveZombieOrHydra(entity);
        } else if (entity.getType().equals("hydra")) {
            moveZombieOrHydra(entity);
        }
    }

    public void moveSpider(Entity entity) {
        int prediction = entity.getComponent(Spider.class).prediction();
        Position newPosition = entity.getComponent(Spider.class).getSpawn().getAdjacentPositions().get(prediction);
        Collision collision = new Collision(dungeon);
        Entity obstacle ;
            if ((obstacle = collision.isColision(entity, newPosition)) != null) {
                if (obstacle.getTags().contains("Pushable")) {
                    entity.getComponent(Spider.class).setDirection(-entity.getComponent(Spider.class).getDirection());
                    //CHECK IF STUCK SO DONT CRASH
                    Position newPosition2 = entity.getComponent(Spider.class).getSpawn().getAdjacentPositions().get(entity.getComponent(Spider.class).prediction());
                    Entity obstacle2;
                    if ((obstacle2 = collision.isColision(entity, newPosition2)) != null) {
                        if (obstacle2.getTags().contains("Pushable")) {
                            return;
                        }
                    }

                    moveSpider(entity);
                    return;
                }
            }
        move(entity, newPosition);
        entity.getComponent(Spider.class).setArrayLocation(prediction);
    }

    public void moveZombieOrHydra(Entity entity) {
        Entity player = dungeon.getPlayer();
        if (player.getComponent(Stats.class).getPlayerState().equals("invincibility_potion")) {
            Position p = getMaxDistancePosition(entity);
            move(entity, p);
            return;
        }
        moveRandom(entity);
    }

    public void moveMercenary(Entity entity) {
        Entity player = dungeon.getPlayer();
        if (player.getComponent(Stats.class).getPlayerState().equals("invincibility_potion")) {
            Position p = getMaxDistancePosition(entity);
            move(entity, p);
            return;
        }

        if (player.getComponent(Stats.class).getPlayerState().equals("invisibility_potion")) {
            moveRandom(entity);
            return;
        }
        // initalise grid
        int radius = 15;
        ArrayList<Position> posList = new ArrayList<>();
        for(int x = entity.getComponent(Location.class).getX() - radius; x <= entity.getComponent(Location.class).getX() + radius; x++) {
            for(int y = entity.getComponent(Location.class).getY() - radius; y <= entity.getComponent(Location.class).getY() + radius; y++) {
                // check for obstacles
                boolean hasObstacle = false;
                for (Entity e : entities) {
                    if (e.getTags().contains("Collidable") &&
                    e.getComponent(Location.class).getX() == x &&
                    e.getComponent(Location.class).getY() == y) {
                        hasObstacle = true;
                        break;
                    }
                }
                if (!hasObstacle) {
                    posList.add(new Position(x, y));
                }
            }
        }
        
        Map<Position, Position> prev = dijkstra(posList, new Position(entity.getComponent(Location.class).getX(), entity.getComponent(Location.class).getY()));
        Position pos = null;
        for (Map.Entry<Position, Position> previousNode : prev.entrySet()) {
            if (previousNode.getKey().getX() == player.getComponent(Location.class).getX() && previousNode.getKey().getY() == player.getComponent(Location.class).getY()) {
                // find the next position the mercenary should take
                pos = getNextPosition(previousNode.getKey(), prev);
                if (pos == null) {
                    return;
                }
                if (entity.getComponent(Stats.class).isBribed()) {
                    if (entityAdjacentToPlayerPrevLocation(entity)) {
                        // move to player prev position
                        move(entity, new Position(player.getComponent(Location.class).getPreviousX(), player.getComponent(Location.class).getPreviousY()));
                    } else {
                        // keep following player 
                        move(entity, pos);
                    }
                } else {
                    // enemy is aggressive
                    move(entity, pos);
                }
            }
        }
    }

    public void moveAssassin(Entity entity) {
        Entity player = dungeon.getPlayer();
        if (player.getComponent(Stats.class).getPlayerState().equals("invincibility_potion")) {
            Position p = getMaxDistancePosition(entity);
            move(entity, p);
            return;
        }

        if (player.getComponent(Stats.class).getPlayerState().equals("invisibility_potion")) {
            if (!assassinInRangeOfPlayer(entity)) {
                moveRandom(entity);
                return;
            }
        }
        // initalise grid
        int radius = entity.getComponent(Stats.class).getRecon_radius();
        ArrayList<Position> posList = new ArrayList<>();
        for(int x = entity.getComponent(Location.class).getX() - radius; x <= entity.getComponent(Location.class).getX() + radius; x++) {
            for(int y = entity.getComponent(Location.class).getY() - radius; y <= entity.getComponent(Location.class).getY() + radius; y++) {
                // check for obstacles
                boolean hasObstacle = false;
                for (Entity e : entities) {
                    if (e.getTags().contains("Collidable") &&
                    e.getComponent(Location.class).getX() == x &&
                    e.getComponent(Location.class).getY() == y) {
                        hasObstacle = true;
                        break;
                    }
                }
                if (!hasObstacle) {
                    posList.add(new Position(x, y));
                }
            }
        }

        Map<Position, Position> prev = dijkstra(posList, new Position(entity.getComponent(Location.class).getX(), entity.getComponent(Location.class).getY()));
        Position pos = null;
        for (Map.Entry<Position, Position> previousNode : prev.entrySet()) {
            if (previousNode.getKey().getX() == player.getComponent(Location.class).getX() && previousNode.getKey().getY() == player.getComponent(Location.class).getY()) {
                // find the next position the assassin should take
                pos = getNextPosition(previousNode.getKey(), prev);
                if (pos == null) {
                    return;
                }
                if (entity.getComponent(Stats.class).isBribed()) {
                    if (entityAdjacentToPlayerPrevLocation(entity)) {
                        // move to player prev position
                        move(entity, new Position(player.getComponent(Location.class).getPreviousX(), player.getComponent(Location.class).getPreviousY()));
                    } else {
                        // keep following player 
                        move(entity, pos);
                    }
                } else {
                    // enemy is aggressive
                    move(entity, pos);
                }
            }
        }
    }

    public boolean assassinInRangeOfPlayer(Entity e) {
        int radius = e.getComponent(Stats.class).getRecon_radius();
        for(int x = e.getComponent(Location.class).getX() - radius; x <= e.getComponent(Location.class).getX() + radius; x++) {
            for(int y = e.getComponent(Location.class).getY() - radius; y <= e.getComponent(Location.class).getY() + radius; y++) {
                for (Entity ent : dungeon.getEntities()) {
                    if (ent.getType().equals("player")) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void moveRandom(Entity e) {
        Random random = new Random();
        if (random.nextInt(4) == 0) {
            Direction direction = Direction.LEFT;
            moveEntity(e, direction);
        } else if (random.nextInt(4) == 1) {
            Direction direction = Direction.RIGHT;
            moveEntity(e, direction);
        } else if (random.nextInt(4) == 2) {
            Direction direction = Direction.UP;
            moveEntity(e, direction);
        } else if (random.nextInt(4) == 3) {
            Direction direction = Direction.DOWN;
            moveEntity(e, direction);
        }
    }

    public Position getMaxDistancePosition(Entity e) {
        Map<Position, Double> maxDist = new HashMap<Position, Double>();
        maxDist.put(new Position(e.getComponent(Location.class).getX() + 1, e.getComponent(Location.class).getY()), null);
        maxDist.put(new Position(e.getComponent(Location.class).getX() - 1, e.getComponent(Location.class).getY()), null);
        maxDist.put(new Position(e.getComponent(Location.class).getX(), e.getComponent(Location.class).getY() + 1), null);
        maxDist.put(new Position(e.getComponent(Location.class).getX(), e.getComponent(Location.class).getY() - 1), null);
        maxDist.put(new Position(e.getComponent(Location.class).getX(), e.getComponent(Location.class).getY()), null);

        Map.Entry<Position, Double> max = null;
        Entity p = dungeon.getPlayer();
        for (Map.Entry<Position, Double> dist : maxDist.entrySet()) {
            boolean canMoveTo = true;
            for (Entity ent : entities) {
                if (ent.getTags().contains("Collidable") && ent.getComponent(Location.class).getX() == dist.getKey().getX() &&
                    ent.getComponent(Location.class).getY() == dist.getKey().getY()) {
                    canMoveTo = false;
                    break;
                }
            }
            if (canMoveTo) {
                double xDistance = Math.abs(p.getComponent(Location.class).getX() - dist.getKey().getX());
                double yDistance = Math.abs(p.getComponent(Location.class).getY() - dist.getKey().getY());
                maxDist.put(dist.getKey(), Math.sqrt(xDistance * xDistance + yDistance * yDistance));
                if ((max == null || max.getValue() < dist.getValue())) {
                    max = dist;
                }
            }
        }

        if (max == null) {
            return new Position(e.getComponent(Location.class).getX(), e.getComponent(Location.class).getY());
        }
        return max.getKey();
    }

    public boolean entityAdjacentToPlayerPrevLocation(Entity e) {
        Entity player = dungeon.getPlayer();
        if (e.getComponent(Location.class).getX() + 1 == player.getComponent(Location.class).getPreviousX()
        && e.getComponent(Location.class).getY() == player.getComponent(Location.class).getPreviousY()) {
            return true;
        } else if (e.getComponent(Location.class).getX() - 1 == player.getComponent(Location.class).getPreviousX()
        && e.getComponent(Location.class).getY() == player.getComponent(Location.class).getPreviousY()) {
            return true;
        } else if (e.getComponent(Location.class).getX() == player.getComponent(Location.class).getPreviousX()
        && e.getComponent(Location.class).getY() + 1 == player.getComponent(Location.class).getPreviousY()) {
            return true;
        } else if (e.getComponent(Location.class).getX() == player.getComponent(Location.class).getPreviousX()
        && e.getComponent(Location.class).getY() - 1 == player.getComponent(Location.class).getPreviousY()) {
            return true;
        } else if (e.getComponent(Location.class).getX() == player.getComponent(Location.class).getPreviousX()
        && e.getComponent(Location.class).getY() == player.getComponent(Location.class).getPreviousY()) {
            return true;
        } 
        return false;
    }   

    public Map<Position, Position> dijkstra(ArrayList<Position> grid, Position source)
    {
        // let dist be a Map<Position, Double>
        Map<Position, Double> dist = new HashMap<Position, Double>();
        //let prev be a Map<Position, Position>
        Map<Position, Position> prev = new HashMap<Position, Position>();

        /*for each Position p in grid:
            dist[p] := infinity
            previous[p] := null
        dist[source] := 0
        */
        for(Position p : grid) {
                dist.put(p, Double.MAX_VALUE);
                prev.put(p, null);
        }
        dist.put(source, 0.0);

        // let queue be a Queue<Position> of every position in grid
        ArrayList<Position> queue = new ArrayList<Position>();
        queue.add(source);
        for(Position p : grid) {
            if (p.getX() == source.getX() && p.getY() == source.getY()) {
                // skip
            } else {
                queue.add(p);
            }
        }

        /*while queue is not empty:
            u := next node in queue with the smallest dist
            for each cardinal neighbour v of u:
                if dist[u] + cost(u, v) < dist[v]:
                    dist[v] := dist[u] + cost(u, v)
                    previous[v] := u
        return previous*/
        while (queue.size() != 0) {
            // get node with min dist 
            Map.Entry<Position, Double> min = null;
            for (Map.Entry<Position, Double> minDist : dist.entrySet()) {
                if ((min == null || min.getValue() > minDist.getValue()) && queue.contains(minDist.getKey())) {
                    min = minDist;
                }
            }

            int queueIndex = queue.indexOf(min.getKey());
            Position node = queue.get(queueIndex);
            queue.remove(min.getKey());
            // get cardinal neighbours
            ArrayList<Position> neighbours = new ArrayList<>();
            for (Position p : grid) {
                if (node.getX() + 1 == p.getX() && node.getY() == p.getY()) {
                    neighbours.add(p);
                } else if (node.getX() - 1 == p.getX() && node.getY() == p.getY()) {
                    neighbours.add(p);
                } else if (node.getX() == p.getX() && node.getY() + 1 == p.getY()) {
                    neighbours.add(p);
                } else if (node.getX() == p.getX() && node.getY() - 1 == p.getY()) {
                    neighbours.add(p);
                }
            }

            for (Position adj : neighbours) {
                if (dist.get(node) + 1 < dist.get(adj)) {
                    dist.put(adj, dist.get(node) + 1);
                    prev.put(adj, node);
                }
            }
        }

        return prev;
    }

    public Position getNextPosition(Position prev, Map<Position, Position> prevMap) {
        Position v = prevMap.get(prev);

        if (v == null) {
            return null;
        }

        if (!checkIfPositionMerc(v, prevMap)) {
            return getNextPosition(v, prevMap);
        }

        return prev;
    }

    public boolean checkIfPositionMerc(Position p, Map<Position, Position> prevMap) {
        Position source = prevMap.get(p);
        if (source == null) {
            return true;
        }
        return false;
    }
}
