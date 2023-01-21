package dungeonmania.systems;

import java.util.Random;

import dungeonmania.Dungeon;
import dungeonmania.Entity;
import dungeonmania.components.Location;
import dungeonmania.components.Stats;
import dungeonmania.components.state.MercenaryState;
import dungeonmania.exceptions.InvalidActionException;

public class AllySystem {
    private Dungeon dungeon;

    public AllySystem(Dungeon dungeon) {
        this.dungeon = dungeon;
    }

    /*
     * Takes in any entity and check if it's in bribe range of the player variable in
     * the given dungeon.
     * Returns true, if within bribe range, else false.
     */
    private boolean playerInRadius(Entity e) {
        
        Entity player = dungeon.getPlayer();
        int playerX = player.getComponent(Location.class).getX();
        int playerY = player.getComponent(Location.class).getY();
        int entityX = e.getComponent(Location.class).getX();
        int entityY = e.getComponent(Location.class).getY();
        int bribeRadius = dungeon.getConfig().bribe_radius;

        // Within range for X
        if (entityX <= playerX + bribeRadius && entityX >= playerX - bribeRadius) {
            // Within range for Y
            if (entityY <= playerY + bribeRadius && entityY >= playerY - bribeRadius) {
                return true;
            }
        }
        return false;
    }
    /*
     * returns the bribe method via string
     */
    private String bribeMethod(int bribeAmount) {
        int numSceptre = dungeon.getInventory().countItem("sceptre");
        int numTreasure = dungeon.getInventory().countItem("treasure");
        if (numSceptre > 0) {
            return "sceptre";
        }
        if (numTreasure >= bribeAmount) {
            return "treasure";
        }
        return "invalid";
    }

    private void pay(int bribeAmount) {
        // finds and removes treasure from inventory bribeAmount of times
        for (int i = 0; i < bribeAmount; i++) {
            dungeon.getInventory().removeItem(dungeon.getInventory().containsType("treasure"));
        }
    }

    /*
     * bribes the mercenary
     */
    public void bribeMercenary(Entity e) throws InvalidActionException{
        int bribeAmount = dungeon.getConfig().bribe_amount;
        String method = bribeMethod(bribeAmount);
        if (method.equals("sceptre")) {
            e.getComponent(MercenaryState.class).bribeSceptre(dungeon.getConfig().mind_control_duration);
        }
        else if (method.equals("treasure")) {
            if (playerInRadius(e)) {
                if (e.getComponent(MercenaryState.class).bribeTreasure()) {
                    pay(bribeAmount);
                }
            }
            else {
                throw new InvalidActionException("Player not in bribing radius");
            }
        }
        else {
            throw new InvalidActionException("Not enough gold/no sceptre to bribe/mindcontrol");
        }
    }

    /*
     * bribes the assassin
     */
    public void bribeAssassin(Entity e) throws InvalidActionException{
        int bribeAmount = dungeon.getConfig().assassin_bribe_amount;
        String method = bribeMethod(bribeAmount);
        if (method.equals("sceptre")) {
            e.getComponent(MercenaryState.class).bribeSceptre(dungeon.getConfig().mind_control_duration);
        }
        else if (method.equals("treasure")) {
            if (playerInRadius(e)) {
                pay(bribeAmount);
                Random random = new Random(dungeon.Tick);// Not really random but it's this way for debugging/testing purposes
                if (random.nextDouble() > dungeon.getConfig().assassin_bribe_fail_rate) {
                    e.getComponent(MercenaryState.class).bribeTreasure();
                }
            }
            else {
                throw new InvalidActionException("Player not in bribing radius");
            }
        }
        else {
            throw new InvalidActionException("Not enough gold/no sceptre to bribe/mindcontrol");
        }
    }

}
