package dungeonmania.systems;

import dungeonmania.Dungeon;
import dungeonmania.Entity;
import dungeonmania.Helper;
import dungeonmania.components.Power;
import dungeonmania.response.models.BattleResponse;

public class GoalChecker {

    Dungeon dungeon;

    public GoalChecker(Dungeon dungeon) {
        this.dungeon = dungeon;
    }
    

    public boolean isZombies() {
        for (Entity entity : dungeon.getEntities()) {
            if (entity.getType().equals("zombie_toast")) {
                return true;
            }
        }
        return false;
    }

    public boolean treasureGoal() {
        int count = 0;
        for (Entity entity : dungeon.getInventory().getInventory()) {
            if (entity.getTags().contains("Treasure")) {
                count++;
            }
        }

        if (count >= dungeon.getConfig().treasure_goal) {
            return true;
        } else {
            return false;
        }
    }

    public boolean enemyGoal() {
        if (dungeon.getBattles().size() >= dungeon.getConfig().enemy_goal) {
            for (Entity entity : dungeon.getEntities()) {
                if (entity.getType().equals("zombie_toast_spawner")) {
                    return false;
                }
            } return true;
        } return false;
       
    }

    public boolean switchGoal() {
        for (Entity entity : dungeon.getEntities()) {
            if (entity.getType().equals("switch")) {
                if (entity.getComponent(Power.class).state() == false) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean exitGoal() {
        for (Entity entity : dungeon.getEntities()) {
            if (entity.getType().equals("exit")) {
                if (Helper.sharePos(dungeon.getPlayer(), entity)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void checkGoals() {
        if (treasureGoal()) {
            dungeon.getGoalsRaw().addGoal(":treasure");
        }
        if (enemyGoal()) {
            dungeon.getGoalsRaw().addGoal(":enemies");
        }
        if (switchGoal()) {
            dungeon.getGoalsRaw().addGoal(":boulders");
        }
        if (dungeon.getGoals().equals(":exit") && exitGoal()) {
            dungeon.getGoalsRaw().addGoal(":exit");
        }
    }
}
