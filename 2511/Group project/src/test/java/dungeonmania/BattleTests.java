package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static dungeonmania.TestUtils.getPlayer;
import static dungeonmania.TestUtils.getEntities;
import static dungeonmania.TestUtils.getInventory;
import static dungeonmania.TestUtils.getGoals;
import static dungeonmania.TestUtils.countEntityOfType;
import static dungeonmania.TestUtils.getValueFromConfigFile;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.BattleResponse;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.response.models.RoundResponse;
import dungeonmania.util.Direction;


public class BattleTests {
    private void assertBattleCalculations(String enemyType, BattleResponse battle, boolean enemyDies, String configFilePath) {
        List<RoundResponse> rounds = battle.getRounds();
        double playerHealth = Double.parseDouble(getValueFromConfigFile("player_health", configFilePath));
        double enemyHealth = Double.parseDouble(getValueFromConfigFile(enemyType + "_health", configFilePath));
        double playerAttack = Double.parseDouble(getValueFromConfigFile("player_attack", configFilePath));
        double enemyAttack = Double.parseDouble(getValueFromConfigFile(enemyType + "_attack", configFilePath));

        for (RoundResponse round : rounds) {
            assertEquals(round.getDeltaCharacterHealth(), -(enemyAttack / 10), 0.001);
            assertEquals(round.getDeltaEnemyHealth(), -(playerAttack / 5), 0.001);
            enemyHealth += round.getDeltaEnemyHealth();
            playerHealth += round.getDeltaCharacterHealth();
        }

        if (enemyDies) {
            assertTrue(enemyHealth <= 0);
        } else {
            assertTrue(playerHealth <= 0);
        }
    }

    // Simple battle interaction tests

    @Test
    @DisplayName("Test player battles spider and spider dies")
    public void testPlayerWinBattleSpider() {
       DungeonManiaController controller = new DungeonManiaController();
       controller.newGame("playerBattleTestSpider", "playerBattleWinsSpider");
       controller.tick(Direction.RIGHT);
       DungeonResponse postBattleResponse = controller.getDungeonResponseModel();
       BattleResponse battle = postBattleResponse.getBattles().get(0);
       assertBattleCalculations("spider", battle, true, "playerBattleWinsSpider");
    }

    @Test
    @DisplayName("Test player battles spider and player dies")
    public void testPlayerLosesBattleSpider() {
       DungeonManiaController controller = new DungeonManiaController();
       controller.newGame("playerBattleTestSpider", "playerBattleLosesSpider");
       controller.tick(Direction.RIGHT);
       DungeonResponse postBattleResponse = controller.getDungeonResponseModel();
       BattleResponse battle = postBattleResponse.getBattles().get(0);
       assertBattleCalculations("spider", battle, false, "playerBattleLosesSpider");
    }

    @Test
    @DisplayName("Test player battles mercenary and mercenary dies")
    public void testPlayerWinsBattleMercenary() {
       DungeonManiaController controller = new DungeonManiaController();
       controller.newGame("playerBattleTestMercenary", "playerBattleWinsMercenary");
       controller.tick(Direction.RIGHT);
       DungeonResponse postBattleResponse = controller.getDungeonResponseModel();
       BattleResponse battle = postBattleResponse.getBattles().get(0);
       assertBattleCalculations("mercenary", battle, true, "playerBattleWinsMercenary");
    }

    @Test
    @DisplayName("Test player battles mercenary and player dies")
    public void testPlayerLosesBattleMercenary() {
       DungeonManiaController controller = new DungeonManiaController();
       controller.newGame("playerBattleTestMercenary", "playerBattleLosesMercenary");
       controller.tick(Direction.RIGHT);
       DungeonResponse postBattleResponse = controller.getDungeonResponseModel();
       BattleResponse battle = postBattleResponse.getBattles().get(0);
       assertBattleCalculations("mercenary", battle, false, "playerBattleLosesMercenary");
    }

    @Test
    @DisplayName("Test player battles assassin and assassin dies")
    public void testPlayerWinsBattleAssassin() {
       DungeonManiaController controller = new DungeonManiaController();
       controller.newGame("playerBattleTestAssassin", "playerBattleWinsAssassin");
       controller.tick(Direction.RIGHT);
       DungeonResponse postBattleResponse = controller.getDungeonResponseModel();
       BattleResponse battle = postBattleResponse.getBattles().get(0);
       assertBattleCalculations("assassin", battle, true, "playerBattleWinsAssassin");
    }

    @Test
    @DisplayName("Test player battles assassin and player dies")
    public void testPlayerLosesBattleAssassin() {
       DungeonManiaController controller = new DungeonManiaController();
       controller.newGame("playerBattleTestAssassin", "playerBattleLosesAssassin");
       controller.tick(Direction.RIGHT);
       DungeonResponse postBattleResponse = controller.getDungeonResponseModel();
       BattleResponse battle = postBattleResponse.getBattles().get(0);
       assertBattleCalculations("assassin", battle, false, "playerBattleLosesAssassin");
    }

    // Battles with invincibility potion

    @Test
    @DisplayName("Test player battles spider while having invincibility")
    public void testPlayerBattleSpiderWhileInvincible() throws IllegalArgumentException, InvalidActionException {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("invinciblePlayerBattleTestSpider", "battleTest");
        controller.tick(Direction.UP);
        DungeonResponse dungeonResponse = controller.getDungeonResponseModel();
        String itemId = dungeonResponse.getInventory().get(0).getId();
        controller.tick(itemId);

        DungeonResponse postBattleResponse = controller.getDungeonResponseModel();
        BattleResponse battle = postBattleResponse.getBattles().get(0);
        List<RoundResponse> rounds = battle.getRounds();
        assertTrue(rounds.get(0).getDeltaEnemyHealth() == battle.getInitialEnemyHealth() * - 1.0);
        assertTrue(rounds.get(0).getDeltaCharacterHealth() == 0.0);
    }

    // Battles with items tests

    @Test
    @DisplayName("Player with sword - player dies")
    public void testPlayerWithSword() {
        /*
         *  [  ]   [  ]  [  ]  [  ]
         *  player sword [  ]  merc
         *  [  ]   [  ]  [  ]  [  ]
         */
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("playerBattleTestWithSword", "battleTest");
        controller.tick(Direction.RIGHT);
        controller.tick(Direction.RIGHT);
        // at this point, player is in same position as the mercenary, initiate battle
        DungeonResponse postBattleResponse = controller.getDungeonResponseModel();
        BattleResponse battle = postBattleResponse.getBattles().get(0);

        double sword_atk = Double.parseDouble(getValueFromConfigFile("sword_attack", "battleTest"));
        double player_atk = Double.parseDouble(getValueFromConfigFile("player_attack", "battleTest"));
        double enemy_atk = Double.parseDouble(getValueFromConfigFile("mercenary_attack", "battleTest"));
        List<RoundResponse> rounds = battle.getRounds();
        for (RoundResponse round : rounds) {
            assertEquals(round.getDeltaCharacterHealth(), -(enemy_atk / 10), 0.001);
            assertEquals(round.getDeltaEnemyHealth(), -((player_atk + sword_atk) / 5), 0.001);
        }

        // dungeon includes exit which counts as entity
        // so the remaining entities are the mercenary and the exit
        assertEquals(2, postBattleResponse.getEntities().size());
    }

    @Test
    @DisplayName("Player with bow - player dies")
    public void testPlayerWithBow() throws IllegalArgumentException, InvalidActionException {
         /*
         *  [  ]   [  ]  [  ]  [  ]  [  ]  [  ]  [  ]  [  ]  [  ]  [  ]
         *  player wood arrow arrow arrow  [  ]  [  ]  [  ]  [  ]  merc
         *  [  ]   [  ]  [  ]  [  ]  [  ]  [  ]  [  ]  [  ]  [  ]  [  ]
         */
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("playerBattleTestWithBow", "battleTest");
        controller.tick(Direction.RIGHT);
        controller.tick(Direction.RIGHT);
        controller.tick(Direction.RIGHT);
        controller.tick(Direction.RIGHT);
        controller.build("bow");
        controller.tick(Direction.RIGHT);
        // at this point, player is in same position as the mercenary, initiate battle
        DungeonResponse postBattleResponse = controller.getDungeonResponseModel();
        BattleResponse battle = postBattleResponse.getBattles().get(0);

        double player_atk = Double.parseDouble(getValueFromConfigFile("player_attack", "battleTest"));
        double enemy_atk = Double.parseDouble(getValueFromConfigFile("mercenary_attack", "battleTest"));
        List<RoundResponse> rounds = battle.getRounds();
        for (RoundResponse round : rounds) {
            assertEquals(round.getDeltaCharacterHealth(), -(enemy_atk / 10), 0.001);
            assertEquals(round.getDeltaEnemyHealth(), - 2 * (player_atk / 5), 0.001);
        }

        // dungeon includes exit which counts as entity
        // so the remaining entities are the mercenary and the exit
        assertEquals(2, postBattleResponse.getEntities().size());
    }

    @Test
    @DisplayName("Player with shield - player dies")
    public void testPlayerWithShield() throws IllegalArgumentException, InvalidActionException {
         /*
         *  [  ]   [  ]  [  ]  [  ]  [  ]  [  ]  [  ]  [  ]  [  ]  [  ]
         * player wood  wood treasure[  ]  [  ]  [  ]  merc  [  ]  [  ]
         *  [  ]   [  ]  [  ]  [  ]  [  ]  [  ]  [  ]  [  ]  [  ]  [  ]
         */
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("playerBattleTestWithShield", "battleTest");
        controller.tick(Direction.RIGHT);
        controller.tick(Direction.RIGHT);
        controller.tick(Direction.RIGHT);
        controller.build("shield");
        controller.tick(Direction.RIGHT);
        // at this point, player is in same position as the mercenary, initiate battle
        DungeonResponse postBattleResponse = controller.getDungeonResponseModel();
        BattleResponse battle = postBattleResponse.getBattles().get(0);

        double shield = Double.parseDouble(getValueFromConfigFile("shield_defence", "battleTest"));
        double player_atk = Double.parseDouble(getValueFromConfigFile("player_attack", "battleTest"));
        double enemy_atk = Double.parseDouble(getValueFromConfigFile("mercenary_attack", "battleTest"));
        List<RoundResponse> rounds = battle.getRounds();
        for (RoundResponse round : rounds) {
            assertEquals(round.getDeltaCharacterHealth(), -((enemy_atk - shield) / 10), 0.001);
            assertEquals(round.getDeltaEnemyHealth(), -(player_atk / 5), 0.001);
        }
       
        // dungeon includes exit which counts as entity
        // so the remaining entities are the mercenary and the exit
        assertEquals(2, postBattleResponse.getEntities().size());
    }
    
    @Test
    @DisplayName("Player with all items excluding potions - player dies")
    public void testPlayerWithAllItemsExcludePotions() throws IllegalArgumentException, InvalidActionException {
         /*
         *  [  ]   [  ]  [  ]  [  ]  [  ]  [  ]  [  ]  [  ]  [  ]  [  ]  [  ]  [  ]  [  ]  [  ]  [  ]  [  ]  [  ]  [  ]
         *  player wood arrow arrow arrow  wood  wood  key   sword [  ]  [  ]  [  ]  [  ]  [  ]  [  ]  [  ]  [  ]  merc 
         *  [  ]   [  ]  [  ]  [  ]  [  ]  [  ]  [  ]  [  ]  [  ]  [  ]  [  ]  [  ]  [  ]  [  ]  [  ]  [  ]  [  ]  [  ]
         */
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("playerBattleTestWithAllItemsExcludingPotions", "battleTest");
        controller.tick(Direction.RIGHT);
        controller.tick(Direction.RIGHT);
        controller.tick(Direction.RIGHT);
        controller.tick(Direction.RIGHT);
        controller.build("bow");
        controller.tick(Direction.RIGHT);
        controller.tick(Direction.RIGHT);
        controller.tick(Direction.RIGHT);
        controller.build("shield");
        controller.tick(Direction.RIGHT);
        controller.tick(Direction.RIGHT);
        // at this point, player is in same position as the mercenary, initiate battle
        DungeonResponse postBattleResponse = controller.getDungeonResponseModel();
        BattleResponse battle = postBattleResponse.getBattles().get(0);

        double shield = Double.parseDouble(getValueFromConfigFile("shield_defence", "battleTest"));
        double sword = Double.parseDouble(getValueFromConfigFile("sword_attack", "battleTest"));
        double player_atk = Double.parseDouble(getValueFromConfigFile("player_attack", "battleTest"));
        double enemy_atk = Double.parseDouble(getValueFromConfigFile("mercenary_attack", "battleTest"));
        List<RoundResponse> rounds = battle.getRounds();
        for (RoundResponse round : rounds) {
            assertEquals(round.getDeltaCharacterHealth(), -((enemy_atk - shield) / 10), 0.001);
            assertEquals(round.getDeltaEnemyHealth(), - 2 * ((player_atk + sword) / 5), 0.001);
        }
       
        // dungeon includes exit which counts as entity
        // so the remaining entities are the mercenary and the exit
        assertEquals(2, postBattleResponse.getEntities().size());
    }

    // Test battling multiple enemies consecutively
    @Test
    @DisplayName("Test player battling multiple enemies simultaneously and defeats them")
    public void testPlayerBattleWinsMultipleEnemiesSimultaneously() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("playerBattleTestMultipleEnemiesSimultaneously", "playerBattleWinsSimultaneouslyMultipleEnemies");
        controller.tick(Direction.RIGHT);
        DungeonResponse postBattleResponse = controller.getDungeonResponseModel();
        for (int i = 0; i < 3; i++) {
            BattleResponse battle = postBattleResponse.getBattles().get(i);
            assertBattleCalculations(battle.getEnemy(), battle, true, "playerBattleWinsSimultaneouslyMultipleEnemies");
        }
    }

    @Test
    @DisplayName("Test player battling multiple enemies simultaneously and player dies")
    public void testPlayerBattleLosesMultipleEnemiesSimultaneously() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("playerBattleTestMultipleEnemiesSimultaneously", "playerBattleLosesSimultaneouslyMultipleEnemies");
        controller.tick(Direction.RIGHT);
        DungeonResponse postBattleResponse = controller.getDungeonResponseModel();
        
        List<BattleResponse> battles = postBattleResponse.getBattles();
        assertEquals(1, battles.size());

        BattleResponse battle = postBattleResponse.getBattles().get(0);
        List<RoundResponse> rounds = battle.getRounds();
        assertEquals(2, rounds.size());
        assertTrue(rounds.get(1).getDeltaCharacterHealth() <= -1.0 * battle.getInitialPlayerHealth());
    }

    // Test item durability
    @Test
    @DisplayName("Test sword durability")
    public void testPlayerBattleSwordDurability() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("playerBattleTestSwordDurability", "playerBattleItemDurability");
        controller.tick(Direction.RIGHT);
        DungeonResponse postBattleResponse = controller.getDungeonResponseModel();

        // check sword no longer in inventory
        boolean isSwordConsumed = true;
        for (ItemResponse item : postBattleResponse.getInventory()) {
            if (item.getType().equals("sword")) {
                isSwordConsumed = false;
            }
        }
        assertEquals(true, isSwordConsumed);

    }

    @Test
    @DisplayName("Test bow durability")
    public void testPlayerBattleBowDurability() throws IllegalArgumentException, InvalidActionException {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("playerBattleTestBowDurability", "playerBattleItemDurability");
        controller.tick(Direction.RIGHT);
        controller.tick(Direction.RIGHT);
        controller.tick(Direction.RIGHT);
        controller.tick(Direction.RIGHT);
        controller.build("bow");
        controller.tick(Direction.RIGHT);
        DungeonResponse postBattleResponse = controller.getDungeonResponseModel();

        // check bow no longer in inventory
        boolean isBowConsumed = true;
        for (ItemResponse item : postBattleResponse.getInventory()) {
            if (item.getType().equals("bow")) {
                isBowConsumed = false;
            }
        }
        assertEquals(true, isBowConsumed);


    }

    @Test
    @DisplayName("Test shield durability")
    public void testPlayerBattleShieldDurability() throws IllegalArgumentException, InvalidActionException {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("playerBattleTestShieldDurability", "playerBattleItemDurability");
        controller.tick(Direction.RIGHT);
        controller.tick(Direction.RIGHT);
        controller.tick(Direction.RIGHT);
        controller.build("shield");
        controller.tick(Direction.RIGHT);
        DungeonResponse postBattleResponse = controller.getDungeonResponseModel();

        // check shield no longer in inventory
        boolean isShieldConsumed = true;
        for (ItemResponse item : postBattleResponse.getInventory()) {
            if (item.getType().equals("shield")) {
                isShieldConsumed = false;
            }
        }
        assertEquals(true, isShieldConsumed);
    }
}
