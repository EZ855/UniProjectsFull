package dungeonmania;

import java.util.ArrayList;

import dungeonmania.components.Spider;
import dungeonmania.components.Stats;
import dungeonmania.response.models.BattleResponse;
import dungeonmania.response.models.RoundResponse;

public class Battle implements BattleComponent {
    private Config config;
    private Entity player;
    private double playerHealth;
    private double enemyHealth;
    private Entity enemy;
    private String enemyType;
    private Dungeon dungeon;
    ArrayList<BattleComponent> rounds = new ArrayList<BattleComponent>();

    public Battle(Entity player, Entity enemy, Dungeon dungeon) {
        this.dungeon = dungeon;
        this.player = player;
        this.playerHealth = player.getComponent(Stats.class).getHealth();
        this.enemy = enemy;
        this.enemyHealth = enemy.getComponent(Stats.class).getHealth();
        this.enemyType = enemy.getType();
        this.config = dungeon.getConfig();
    }

    @Override
    public double calculatePlayerDelta() {
        double delta = 0;
        for (BattleComponent round : rounds) {
            delta += round.calculatePlayerDelta();
        }
        return delta;
    }

    @Override
    public double calculateEnemyDelta() {
        double delta = 0;
        for (BattleComponent round : rounds) {
            delta += round.calculateEnemyDelta();
        }
        return delta;
    }

    public boolean addRound(Round round) {
        rounds.add(round);
        return true;
    }

    public boolean removeRound(Round round) {
        rounds.remove(round);
        return true;
    }

    public double getPlayerHealth() {
        return playerHealth;
    }

    public double getEnemyHealth() {
        return enemyHealth;
    }

    public String getEnemyType() {
        return enemyType;
    }  

    public BattleResponse getBattleResponse() {
        ArrayList<RoundResponse> roundResponseList = new ArrayList<RoundResponse>();
        for (BattleComponent r : rounds) {
            Round round = (Round) r;
            roundResponseList.add(round.getRoundResponse());
        }
        return new BattleResponse(enemyType, roundResponseList, playerHealth, enemyHealth);
    }

}
