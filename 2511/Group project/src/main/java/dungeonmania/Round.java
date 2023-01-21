package dungeonmania;

import java.util.ArrayList;
import java.util.Random;

import dungeonmania.components.ItemStat;
import dungeonmania.components.Stats;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.response.models.RoundResponse;

public class Round implements BattleComponent {
    private Entity player;
    private double playerHealth;
    private double playerDelta;
    private Entity enemy;
    private double enemyHealth;
    private double enemyDelta;
    private Dungeon dungeon;
    private ArrayList<Entity> itemsUsed = new ArrayList<Entity>();

    public Round(Entity player, Entity enemy, Dungeon dungeon) {
        this.dungeon = dungeon;
        this.player = player;
        this.playerHealth = player.getComponent(Stats.class).getHealth();
        this.enemy = enemy;
        this.enemyHealth = enemy.getComponent(Stats.class).getHealth();
        for (Entity item : dungeon.getInventory().getInventory()) {
            if (item.getType().equals("sword") || item.getType().equals("bow") || item.getType().equals("midnight_armour")) {
                boolean itemInItemsUsed = checkItemInItemsUsed(item.getType());
                if (!itemInItemsUsed) {
                    itemsUsed.add(item);
                }
            }
        }

        if (player.getComponent(Stats.class).getPlayerState().equals("invincibility_potion")) {
            itemsUsed.add(player.getComponent(Stats.class).getPotionInUse());
        }
    }

    @Override
    public double calculatePlayerDelta() {
        double enemyAttack = enemy.getComponent(Stats.class).getAttackDamage();
        double playerDefence = 0;
        Entity shield;
        if ((shield = (dungeon.getInventory().containsType("shield"))) != null) {
            playerDefence += shield.getComponent(ItemStat.class).getDefense();
        }
        Entity midnight_armour;
        if ((midnight_armour = (dungeon.getInventory().containsType("midnight_armour"))) != null) {
            playerDefence += midnight_armour.getComponent(ItemStat.class).getDefense();
        }

        for (Entity e : dungeon.getEntities()) {
            if (e.getType().equals("mercenary") && e.getComponent(Stats.class).isBribed()) {
                playerDefence += dungeon.getConfig().ally_defence;
            } else if (e.getType().equals("assassin") && e.getComponent(Stats.class).isBribed()) {
                playerDefence += dungeon.getConfig().ally_defence;
            }
        }

        double damageTaken = enemyAttack - playerDefence;
        if (damageTaken < 0.0) {
            damageTaken = 0.0;
        }
        double playerNewHealth = playerHealth - ((damageTaken)/10);
        double delta = playerNewHealth - playerHealth;
        if (playerNewHealth < 0.0) {
            playerNewHealth = 0.0;
        }
        player.getComponent(Stats.class).setHealth(playerNewHealth);
        return delta;
    }

    @Override
    public double calculateEnemyDelta() {
        double playerAttack = player.getComponent(Stats.class).getAttackDamage();
        double playerAttackMultiplier = 1.0;
        Entity sword;
        if ((sword = (dungeon.getInventory().containsType("sword"))) != null) {
            playerAttack += sword.getComponent(ItemStat.class).getAttack();
        }
        Entity midnight_armour;
        if ((midnight_armour = (dungeon.getInventory().containsType("midnight_armour"))) != null) {
            playerAttack += midnight_armour.getComponent(ItemStat.class).getAttack();
        }
        if (((dungeon.getInventory().containsType("bow"))) != null) {
            playerAttackMultiplier = 2.0;
        }

        for (Entity e : dungeon.getEntities()) {
            if (e.getType().equals("mercenary") && e.getComponent(Stats.class).isBribed()) {
                playerAttack += dungeon.getConfig().ally_attack;
            } else if (e.getType().equals("assassin") && e.getComponent(Stats.class).isBribed()) {
                playerAttack += dungeon.getConfig().ally_attack;
            }
        }

        if (enemy.getType().equals("hydra")) {
            Random increaseHealthChance = new Random();
            if (increaseHealthChance.nextDouble() < enemy.getComponent(Stats.class).getHealth_increase_rate()) {
                double enemyNewHealth = enemyHealth + enemy.getComponent(Stats.class).getHealth_increase_amount();
                double delta = enemyNewHealth - enemyHealth;
                enemy.getComponent(Stats.class).setHealth(enemyNewHealth);
                return delta;
            }
        }
        
        double enemyNewHealth = enemyHealth - ((playerAttackMultiplier * playerAttack) / 5);
        double delta = enemyNewHealth - enemyHealth;
        if (enemyNewHealth < 0.0) {
            enemyNewHealth = 0.0;
        }
        enemy.getComponent(Stats.class).setHealth(enemyNewHealth);
        return delta;
    }

    
    public double getPlayerHealth() {
        return playerHealth;
    }

    public void setPlayerHealth(double playerHealth) {
        this.playerHealth = playerHealth;
    }

    public double getPlayerDelta() {
        return playerDelta;
    }

    public void setPlayerDelta(double playerDelta) {
        this.playerDelta = playerDelta;
    }


    public double getEnemyHealth() {
        return enemyHealth;
    }

    public void setEnemyHealth(double enemyHealth) {
        this.enemyHealth = enemyHealth;
    }

    public double getEnemyDelta() {
        return enemyDelta;
    }

    public void setEnemyDelta(double enemyDelta) {
        this.enemyDelta = enemyDelta;
    }

    public ArrayList<Entity> getItemsUsed() {
        return itemsUsed;
    }

    public boolean checkItemInItemsUsed(String item) {
        for (Entity itemEntity : itemsUsed) {
            if (itemEntity.getType().equals(item)) {
                return true;
            }
        }
        return false;
    }

    public RoundResponse getRoundResponse() {
        ArrayList<ItemResponse> itemResponseList = new ArrayList<ItemResponse>();
        for (Entity item : itemsUsed) {
            ItemResponse itemResponse = new ItemResponse(item.getId(), item.getType());
            itemResponseList.add(itemResponse);
        }

        return new RoundResponse(playerDelta, enemyDelta, itemResponseList);
    }

}
